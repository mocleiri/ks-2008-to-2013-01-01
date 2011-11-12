package org.kuali.maven.mojo.s3;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugin.descriptor.PluginDescriptor;
import org.apache.maven.project.MavenProject;
import org.apache.maven.wagon.TransferFailedException;
import org.kuali.maven.common.UrlBuilder;
import org.kuali.maven.mojo.s3.threads.ElementHandler;
import org.kuali.maven.mojo.s3.threads.ListIteratorContext;
import org.kuali.maven.mojo.s3.threads.ListIteratorThread;
import org.kuali.maven.mojo.s3.threads.ListObjectsContextHandler;
import org.kuali.maven.mojo.s3.threads.ThreadHandler;
import org.kuali.maven.mojo.s3.threads.ThreadHandlerFactory;
import org.kuali.maven.mojo.s3.threads.UpdateDirectoryThread;
import org.kuali.maven.mojo.s3.threads.UpdateDirectoryThreadContext;

import com.amazonaws.AmazonServiceException;
import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.CopyObjectRequest;
import com.amazonaws.services.s3.model.ListObjectsRequest;
import com.amazonaws.services.s3.model.ObjectListing;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.services.s3.model.S3Object;
import com.amazonaws.services.s3.model.S3ObjectSummary;

/**
 * <p>
 * This mojo updates a bucket serving as an origin for a Cloud Front distribution. It generates an html directory
 * listing for each "directory" in the bucket and stores the html under a key in the bucket such that a regular http
 * request for a directory returns html. What would happen otherwise is XML for "object does not exist" would be
 * returned by Amazon.
 * </p>
 * <p>
 * For example: The url "http://www.mybucket.com/foo/bar" will return an html page containing a listing of all the files
 * and directories under "foo/bar" in the bucket.
 * </p>
 * <p>
 * If a directory contains an object with a key that is the same as the default object, the plugin copies the object to
 * a key representing the directory structure.
 * </p>
 *
 * <p>
 * For example, the url "http://www.mybucket.com/foo/bar/index.html" represents an object in an S3 bucket under the key
 * "foo/bar/index.html". This plugin will copy the object from the key "foo/bar/index.html" to the key "foo/bar/". This
 * causes the url "http://www.mybucket.com/foo/bar/" to return the same content as the url
 * "http://www.mybucket.com/foo/bar/index.html"
 * </p>
 * <p>
 * It also generates an html directory listing at the root of the bucket hierarchy and places that html into the bucket
 * as the default object, unless a default object already exists.
 * </p>
 *
 * @goal updateoriginbucket
 */
public class UpdateOriginBucketMojo extends S3Mojo implements BucketUpdater {
    UrlBuilder builder = new UrlBuilder();
    SimpleFormatter formatter = new SimpleFormatter();

    private static final String S3_INDEX_METADATA_KEY = "maven-cloudfront-plugin-index";
    private static final String S3_INDEX_CONTENT_TYPE = "text/html";
    CloudFrontHtmlGenerator generator;
    S3DataConverter converter;

    private static final Timer TIMER = new Timer();

    /**
     * The number of threads to use when updating indexes
     *
     * @parameter expression="${cloudfront.threads}" default-value="3"
     */
    private int threads;

    /**
     * The portion of the groupId that is implied by the hostname where content is published.
     *
     * For example, all of the groupId's for Kuali Foundation begin with "org.kuali". The hostname where all Maven
     * generated site content gets published is "site.kuali.org".
     *
     * The base url for accessing the Maven generated web content for a Kuali Foundation project is the hostname plus
     * the non-redundant portion of the groupId.
     *
     * For example, the Kuali Rice project has the groupId "org.kuali.rice". Content for the Kuali Rice project is
     * published under "site.kuali.org/rice". The "org.kuali" portion of the groupId is implied from the hostname
     * "site.kuali.org" and is thus removed when calculating the url in order to keep things a little more compact.
     *
     * If this parameter is not supplied, the complete groupId is used.
     *
     * @parameter expression="${cloudfront.organizationGroupId}"
     */
    private String organizationGroupId;

    /**
     * This controls the caching behavior for CloudFront. By default, CloudFront edge locations cache content from an S3
     * bucket for 24 hours. That interval is shortened to 1 hour for the html indexes generated by this plugin.
     *
     * @parameter expression="${cloudfront.cacheControl}" default-value="max-age=3600, must-revalidate"
     */
    private String cacheControl;

    /**
     * If true, the hierarchy underneath <code>prefix</code> will be recursively updated. If false, only the directory
     * corresponding to the prefix will be updated along with the path back to the root of the bucket
     *
     * @parameter expression="${cloudfront.recurse}" default-value="true"
     */
    private boolean recurse;

    /**
     * If true, "foo/bar/index.html" will get copied to "foo/bar/"
     *
     * @parameter expression="${cloudfront.copyDefaultObjectWithDelimiter}" default-value="true"
     */
    private boolean copyDefaultObjectWithDelimiter;

    /**
     * If true, "foo/bar/index.html" will get copied to "foo/bar". This is defaulted to false because the relative
     * pathing in the html generated by the maven-site-plugin does not render correctly from a url without the trailing
     * slash.
     *
     * @parameter expression="${cloudfront.copyDefaultObjectWithoutDelimiter}" default-value="false"
     */
    private boolean copyDefaultObjectWithoutDelimiter;

    /**
     * The stylesheet to use for the directory listing
     *
     * @parameter expression="${cloudfront.css}" default-value="http://s3browse.ks.kuali.org/css/style.css"
     */
    private String css;

    /**
     * Image representing a file
     *
     * @parameter expression="${cloudfront.fileImage}"
     *            default-value="http://s3browse.ks.kuali.org/images/page_white.png"
     */
    private String fileImage;

    /**
     * Image representing a directory
     *
     * @parameter expression="${cloudfront.directoryImage}"
     *            default-value="http://s3browse.ks.kuali.org/images/folder.png"
     */
    private String directoryImage;

    /**
     * When displaying the last modified timestamp, use this timezone
     *
     * @parameter expression="${cloudfront.timezone}" default-value="UTC"
     */
    private String timezone;

    /**
     * When displaying the last modified timestamp use this format
     *
     * @parameter expression="${cloudfront.dateFormat}" default-value="EEE, dd MMM yyyy HH:mm:ss z"
     */
    private String dateFormat;

    /**
     * The key containing the default object for the Cloud Front distribution. If this object already exists, the plugin
     * will not modify it. If it does not exist, this plugin will generate an html directory listing and place it into
     * the bucket under this key.
     *
     * @parameter expression="${cloudfront.defaultObjectKey}" default-value="index.html"
     */
    private String defaultObjectKey;

    /**
     * The html for browsing a directory will be created under this key
     *
     * @parameter expression="${cloudfront.browseKey}" default-value="browse.html"
     */
    private String browseKey;

    /**
     * The maximum depth of nested directories to update relative to the current project's directory
     *
     * @parameter expression="${cloudfront.maxDepth}" default-value="1"
     */
    private int maxDepth;

    /**
     * The permissions for S3 objects created by this plugin
     *
     * @parameter expression="${cloudfront.acl}" default-value="PublicRead"
     * @required
     */
    private CannedAccessControlList acl;

    /**
     * The maximum number of keys to list in one request.
     *
     * @parameter expression="${cloudfront.maxKeys}" default-value="1000"
     * @required
     */
    private Integer maxKeys;

    protected List<String> getPrefixes(ObjectListing listing, String prefix, String delimiter) {
        List<String> commonPrefixes = listing.getCommonPrefixes();
        List<String> pathPrefixes = getPathPrefixes(delimiter, prefix);
        List<String> prefixes = new ArrayList<String>();
        prefixes.addAll(commonPrefixes);
        prefixes.addAll(pathPrefixes);
        Collections.sort(prefixes);
        return prefixes;

    }

    protected ObjectListing getObjectListing(S3BucketContext context, ListObjectsRequest request) {
        String prefix = getPrefix();
        String bucket = context.getBucket();
        String delimiter = context.getDelimiter();
        Integer maxKeys = context.getMaxKeys();

        // This is the file system equivalent of typing "ls" in a directory
        long start = System.currentTimeMillis();
        ObjectListing listing = context.getClient().listObjects(request);
        long millis = System.currentTimeMillis() - start;
        TIMER.addMillis(millis);
        getLog().info(getListMsg(millis));

        // If we have more than 1000 files/directories in the current directory we have an issue
        if (listing.isTruncated()) {
            throw new AmazonServiceException("The listing for " + bucket + delimiter + prefix + " exceeded " + maxKeys);
        }
        return listing;
    }

    protected List<ListObjectsContext> getListObjectsContexts(S3BucketContext bucketContext, List<String> prefixes) {
        List<ListObjectsContext> contexts = new ArrayList<ListObjectsContext>();
        for (String prefix : prefixes) {
            ListObjectsRequest request = getListObjectsRequest(bucketContext, prefix);
            ListObjectsContext context = new ListObjectsContext();
            context.setRequest(request);
            context.setBucketContext(bucketContext);
            contexts.add(context);
        }
        return contexts;
    }

    protected ListObjectsRequest getListObjectsRequest(S3BucketContext context, String prefix) {
        String bucket = context.getBucket();
        String delimiter = context.getDelimiter();
        Integer maxKeys = context.getMaxKeys();
        ListObjectsRequest request = new ListObjectsRequest(bucket, prefix, null, delimiter, maxKeys);
        return request;
    }

    protected String getListMsg(long millis) {
        long total = TIMER.getMillis();
        long count = TIMER.getCount();
        StringBuilder sb = new StringBuilder();
        sb.append("Listing Request:" + formatter.getTime(millis));
        sb.append(" Total:" + formatter.getTime(total));
        sb.append(" Count:" + count);
        sb.append(" Avg:" + formatter.getTime(total / count));
        return sb.toString();
    }

    @Override
    public void executeMojo() throws MojoExecutionException, MojoFailureException {
        try {
            getLog().info("Updating S3 bucket - " + getBucket());
            updateMojoState();
            S3BucketContext context = getS3BucketContext();
            generator = new CloudFrontHtmlGenerator(context);
            converter = new S3DataConverter(context);
            converter.setBrowseKey(getBrowseKey());
            getLog().info("Re-indexing - " + getPrefix());
            String prefix = getPrefix();

            // Get the object listing for the current directory
            ListObjectsRequest request = getListObjectsRequest(context, prefix);
            ObjectListing listing = getObjectListing(context, request);

            // Get a list of prefixes representing other directories in this directory AND the path of directories
            // leading back to and including the root directory
            List<String> prefixes = getPrefixes(listing, prefix, context.getDelimiter());
            List<ListObjectsContext> contexts = getListObjectsContexts(context, prefixes);

            // Start some threads for listing the bucket contents
            ThreadHandlerFactory factory = new ThreadHandlerFactory();
            ListObjectsContextHandler elementHandler = new ListObjectsContextHandler();
            ThreadHandler handler = factory.getThreadHandler(threads, contexts, elementHandler);

            show("Prefixes:", prefixes);

        } catch (Exception e) {
            throw new MojoExecutionException("Unexpected error: ", e);
        }
    }

    protected List<ObjectListing> getObjectListings(List<ListObjectsContext> contexts) {
        List<ObjectListing> objectListings = new ArrayList<ObjectListing>();
        return objectListings;
    }

    protected String getUploadCompleteMsg(long millis, int count) {
        String time = formatter.getTime(millis);
        StringBuilder sb = new StringBuilder();
        sb.append("Updates: " + count);
        sb.append("  Time: " + time);
        return sb.toString();
    }

    protected String getUploadStartMsg(int updates, int threadCount, int updatesPerThread) {
        StringBuilder sb = new StringBuilder();
        sb.append("Updates: " + updates);
        sb.append("  Threads: " + threadCount);
        sb.append("  Updates Per Thread: " + updatesPerThread);
        return sb.toString();
    }

    protected int getElementsPerThread(int threads, int elements) {
        int requestsPerThread = elements / threads;
        while (requestsPerThread * threads < elements) {
            requestsPerThread++;
        }
        return requestsPerThread;
    }

    protected <T> ThreadHandler getThreadHandler2(List<T> list, ElementHandler<T> elementHandler) {
        int elementCount = list.size();
        int threadCount = threads > elementCount ? elementCount : threads;
        int elementsPerThread = getElementsPerThread(threadCount, list.size());
        ThreadHandler handler = new ThreadHandler();
        handler.setThreadCount(threadCount);
        handler.setElementsPerThread(elementsPerThread);
        ProgressTracker tracker = new PercentCompleteTracker();
        tracker.setTotal(list.size());
        handler.setTracker(tracker);
        ThreadGroup group = new ThreadGroup("S3 Index Updaters");
        group.setDaemon(true);
        handler.setGroup(group);
        Thread[] threads = getListThreads(handler, list, elementHandler);
        handler.setThreads(threads);
        return handler;
    }

    protected ThreadHandler getThreadHandler(List<UpdateDirectoryContext> contexts) {
        int updateCounts = contexts.size();
        int actualThreadCount = threads > updateCounts ? updateCounts : threads;
        int requestsPerThread = getElementsPerThread(actualThreadCount, contexts.size());
        ThreadHandler handler = new ThreadHandler();
        handler.setThreadCount(actualThreadCount);
        handler.setElementsPerThread(requestsPerThread);
        ProgressTracker tracker = new PercentCompleteTracker();
        tracker.setTotal(contexts.size());
        handler.setTracker(tracker);
        ThreadGroup group = new ThreadGroup("S3 Index Updaters");
        group.setDaemon(true);
        handler.setGroup(group);
        Thread[] threads = getThreads(handler, contexts);
        handler.setThreads(threads);
        return handler;
    }

    protected Thread[] getListObjectThreads(ThreadHandler handler, List<ListObjectsContext> contexts) {
        Thread[] threads = new Thread[handler.getThreadCount()];
        for (int i = 0; i < threads.length; i++) {
            int offset = i * handler.getElementsPerThread();
            int length = handler.getElementsPerThread();
            if (offset + length > contexts.size()) {
                length = contexts.size() - offset;
            }
            ListIteratorContext<ListObjectsContext> context = new ListIteratorContext<ListObjectsContext>();
            context.setList(contexts);
            context.setTracker(handler.getTracker());
            context.setOffset(offset);
            context.setLength(length);
            context.setThreadHandler(handler);
            int id = i + 1;
            context.setId(id);
            ListIteratorThread<ListObjectsContext> thread = new ListIteratorThread<ListObjectsContext>();
            thread.setContext(context);

            threads[i] = new Thread(handler.getGroup(), thread, "S3-" + id);
            threads[i].setUncaughtExceptionHandler(handler);
            threads[i].setDaemon(true);
        }
        return threads;
    }

    protected <T> Thread[] getListThreads(ThreadHandler threadHandler, List<T> list, ElementHandler<T> elementHandler) {
        Thread[] threads = new Thread[threadHandler.getThreadCount()];
        for (int i = 0; i < threads.length; i++) {
            int offset = i * threadHandler.getElementsPerThread();
            int length = threadHandler.getElementsPerThread();
            if (offset + length > list.size()) {
                length = list.size() - offset;
            }
            ListIteratorContext<T> context = new ListIteratorContext<T>();
            context.setList(list);
            context.setTracker(threadHandler.getTracker());
            context.setOffset(offset);
            context.setLength(length);
            context.setThreadHandler(threadHandler);
            context.setElementHandler(elementHandler);
            int id = i + 1;
            context.setId(id);
            ListIteratorThread<T> thread = new ListIteratorThread<T>();
            thread.setContext(context);

            threads[i] = new Thread(threadHandler.getGroup(), thread, "ListIterator-" + id);
            threads[i].setUncaughtExceptionHandler(threadHandler);
            threads[i].setDaemon(true);
        }
        return threads;
    }

    protected Thread[] getThreads(ThreadHandler handler, List<UpdateDirectoryContext> contexts) {
        Thread[] threads = new Thread[handler.getThreadCount()];
        for (int i = 0; i < threads.length; i++) {
            int offset = i * handler.getElementsPerThread();
            int length = handler.getElementsPerThread();
            if (offset + length > contexts.size()) {
                length = contexts.size() - offset;
            }
            UpdateDirectoryThreadContext context = new UpdateDirectoryThreadContext();
            context.setContexts(contexts);
            context.setTracker(handler.getTracker());
            context.setOffset(offset);
            context.setLength(length);
            context.setUpdater(this);
            context.setHandler(handler);
            int id = i + 1;
            context.setId(id);
            Runnable runnable = new UpdateDirectoryThread(context);
            threads[i] = new Thread(handler.getGroup(), runnable, "S3-" + id);
            threads[i].setUncaughtExceptionHandler(handler);
            threads[i].setDaemon(true);
        }
        return threads;
    }

    protected List<UpdateDirectoryContext> getUpdateDirContexts(List<S3PrefixContext> contexts) {
        List<UpdateDirectoryContext> list = new ArrayList<UpdateDirectoryContext>();
        for (S3PrefixContext context : contexts) {

            if (context.isRoot()) {
                continue;
            }

            String delimiter = context.getBucketContext().getDelimiter();
            String trimmedPrefix = converter.getTrimmedPrefix(context.getPrefix(), delimiter);

            UpdateDirectoryContext udc1 = new UpdateDirectoryContext();
            udc1.setContext(context);
            udc1.setCopyIfExists(isCopyDefaultObjectWithDelimiter());
            udc1.setCopyToKey(context.getPrefix());

            UpdateDirectoryContext udc2 = new UpdateDirectoryContext();
            udc2.setContext(context);
            udc2.setCopyIfExists(isCopyDefaultObjectWithoutDelimiter());
            udc2.setCopyToKey(trimmedPrefix);

            list.add(udc1);
            list.add(udc2);

        }
        return list;
    }

    protected List<String> getPathPrefixes(String delimiter, String startingPrefix) {
        List<String> list = new ArrayList<String>();

        list.add(delimiter);

        String[] prefixes = StringUtils.splitByWholeSeparator(startingPrefix, delimiter);
        String newPrefix = "";
        for (int i = 0; i < prefixes.length - 2; i++) {
            newPrefix += prefixes[i] + delimiter;
            list.add(newPrefix);
        }
        return list;
    }

    protected List<S3PrefixContext> getContextsGoingUp(S3BucketContext context, String startingPrefix)
            throws IOException {
        List<S3PrefixContext> list = new ArrayList<S3PrefixContext>();

        if (StringUtils.isEmpty(startingPrefix)) {
            return list;
        }

        String[] prefixes = StringUtils.splitByWholeSeparator(startingPrefix, context.getDelimiter());
        if (prefixes.length == 1) {
            return list;
        }
        String newPrefix = "";
        for (int i = 0; i < prefixes.length - 2; i++) {
            newPrefix += prefixes[i] + context.getDelimiter();
            list.add(getS3PrefixContext(context, newPrefix));
        }
        return list;
    }

    protected String getDefaultPrefix(MavenProject project, String groupId) {

        if (builder.isBaseCase(project, groupId)) {
            return builder.getSitePath(project, groupId) + "/" + project.getVersion();
        } else {
            return getDefaultPrefix(project.getParent(), groupId) + "/" + project.getArtifactId();
        }
    }

    protected void updatePrefix() {
        String s = getPrefix();
        if (StringUtils.isEmpty(s)) {
            s = getDefaultPrefix(getProject(), getOrganizationGroupId());
        }
        if (!s.endsWith(getDelimiter())) {
            s = s + getDelimiter();
        }
        setPrefix(s);
    }

    protected void updateMojoState() throws MojoExecutionException {
        updateCredentials();
        validateCredentials();
        updatePrefix();
    }

    protected S3BucketContext getS3BucketContext() throws MojoExecutionException {
        AWSCredentials credentials = getCredentials();
        AmazonS3Client client = new AmazonS3Client(credentials);
        S3BucketContext context = new S3BucketContext();
        try {
            BeanUtils.copyProperties(context, this);
        } catch (Exception e) {
            throw new MojoExecutionException("Error copying properties", e);
        }
        context.setClient(client);
        context.setLastModifiedDateFormatter(getLastModifiedDateFormatter());
        context.setAbout(getAbout());
        return context;
    }

    /**
     * Create a PutObjectRequest for some html generated by this mojo. The PutObjectRequest sets the content type to
     * S3_INDEX_CONTENT_TYPE, sets the ACL to PublicRead, and adds some custom metadata so we can positively identify it
     * as an object created by this plugin
     */
    protected PutObjectRequest getPutIndexObjectRequest(String html, String key) {
        InputStream in = new ByteArrayInputStream(html.getBytes());
        ObjectMetadata om = new ObjectMetadata();
        om.setCacheControl(getCacheControl());
        String contentType = S3_INDEX_CONTENT_TYPE;
        om.setContentType(contentType);
        om.setContentLength(html.length());
        om.addUserMetadata(S3_INDEX_METADATA_KEY, "true");
        PutObjectRequest request = new PutObjectRequest(getBucket(), key, in, om);
        request.setCannedAcl(getAcl());
        return request;
    }

    /**
     * Return a SimpleDateFormat object initialized with the date format and timezone supplied to the mojo
     */
    protected SimpleDateFormat getLastModifiedDateFormatter() {
        SimpleDateFormat sdf = new SimpleDateFormat(getDateFormat());
        sdf.setTimeZone(TimeZone.getTimeZone(getTimezone()));
        return sdf;
    }

    /**
     * Return true if the Collection is null or contains no entries, false otherwise
     */
    protected boolean isEmpty(Collection<?> c) {
        return c == null || c.size() == 0;
    }

    /**
     * Show some text about this plugin
     */
    protected String getAbout() {
        String date = getLastModifiedDateFormatter().format(new Date());
        PluginDescriptor descriptor = (PluginDescriptor) this.getPluginContext().get("pluginDescriptor");
        if (descriptor == null) {
            // Maven 2.2.1 is returning a null descriptor
            return "Listing generated by the maven-cloudfront-plugin on " + date;
        } else {
            String name = descriptor.getArtifactId();
            String version = descriptor.getVersion();
            return "Listing generated by the " + name + " v" + version + " on " + date;
        }
    }

    @Override
    public void updateDirectory(UpdateDirectoryContext context) throws IOException {
        updateDirectory(context.getContext(), context.isCopyIfExists(), context.getCopyToKey());
    }

    /**
     * Create an object in the bucket under a key that lets a normal http request function correctly with CloudFront /
     * S3.<br>
     * Either use the client's object or upload some html created by this plugin<br>
     */
    protected void updateDirectory(S3PrefixContext context, boolean isCopyIfExists, String copyToKey)
            throws IOException {
        S3BucketContext bucketContext = context.getBucketContext();
        AmazonS3Client client = context.getBucketContext().getClient();
        String bucket = bucketContext.getBucket();

        boolean containsDefaultObject = isExistingObject(context.getObjectListing(), context.getDefaultObjectKey());
        if (containsDefaultObject && isCopyIfExists) {
            // Copy the contents of the clients default object
            String sourceKey = context.getDefaultObjectKey();
            String destKey = copyToKey;
            CopyObjectRequest request = getCopyObjectRequest(bucket, sourceKey, destKey);
            getLog().debug("Copy: " + sourceKey + " to " + destKey);
            client.copyObject(request);
        } else {
            // Upload our custom content
            PutObjectRequest request = getPutIndexObjectRequest(context.getHtml(), copyToKey);
            getLog().debug("Put: " + copyToKey);
            client.putObject(request);
        }
    }

    /**
     * Update this S3 "directory".
     */
    protected void updateDirectory(final S3PrefixContext context) throws IOException {
        String trimmedPrefix = converter.getTrimmedPrefix(context.getPrefix(), context.getBucketContext()
                .getDelimiter());

        // Handle "http://www.mybucket.com/foo/bar/"
        updateDirectory(context, isCopyDefaultObjectWithDelimiter(), context.getPrefix());

        // Handle "http://www.mybucket.com/foo/bar"
        updateDirectory(context, isCopyDefaultObjectWithoutDelimiter(), trimmedPrefix);

        // Handle "http://www.mybucket.com/foo/bar/browse.html"
        // context.getBucketContext().getClient().putObject(getPutIndexObjectRequest(context.getHtml(),
        // context.getBrowseHtmlKey()));
    }

    /**
     * If this is the root of the bucket and the default object either does not exist or was created by this plugin,
     * overwrite the default object with newly generated html. Otherwise, do nothing.
     */
    protected void updateRoot(S3PrefixContext context) throws IOException {
        AmazonS3Client client = context.getBucketContext().getClient();

        // Handle "http://www.mybucket.com/browse.html"
        PutObjectRequest request1 = getPutIndexObjectRequest(context.getHtml(), context.getBrowseHtmlKey());
        StringBuilder sb = new StringBuilder();
        sb.append(context.getBrowseHtmlKey());
        client.putObject(request1);

        boolean isCreateOrUpdateDefaultObject = isCreateOrUpdateDefaultObject(context);
        if (!isCreateOrUpdateDefaultObject) {
            getLog().info("Put: " + sb.toString());
            return;
        }

        // Update the default object
        PutObjectRequest request2 = getPutIndexObjectRequest(context.getHtml(), context.getDefaultObjectKey());
        getLog().info("Put: " + sb.toString() + ", " + context.getDefaultObjectKey());
        client.putObject(request2);
    }

    protected S3PrefixContext getS3PrefixContext(S3BucketContext context, String prefix) {
        getLog().info("Listing objects for " + prefix);
        ListObjectsRequest request = new ListObjectsRequest(context.getBucket(), prefix, null, context.getDelimiter(),
                1000);
        ObjectListing objectListing = context.getClient().listObjects(request);
        List<String[]> data = converter.getData(objectListing, prefix, context.getDelimiter());
        String html = generator.getHtml(data, prefix, context.getDelimiter());
        String defaultObjectKey = StringUtils.isEmpty(prefix) ? getDefaultObjectKey() : prefix + getDefaultObjectKey();
        String browseHtmlKey = StringUtils.isEmpty(prefix) ? getBrowseKey() : prefix + getBrowseKey();
        // Is this the root of the bucket?
        boolean isRoot = StringUtils.isEmpty(prefix);

        S3PrefixContext prefixContext = new S3PrefixContext();
        prefixContext.setObjectListing(objectListing);
        prefixContext.setHtml(html);
        prefixContext.setRoot(isRoot);
        prefixContext.setDefaultObjectKey(defaultObjectKey);
        prefixContext.setPrefix(prefix);
        prefixContext.setBucketContext(context);
        prefixContext.setBrowseHtmlKey(browseHtmlKey);
        return prefixContext;
    }

    /**
     * Recurse the hierarchy of a bucket starting at "prefix" and S3PrefixContext objects corresponding to the directory
     * structure of the hierarchy
     */
    protected List<S3PrefixContext> getS3PrefixContexts(S3BucketContext context, String prefix, Depth depth) {

        List<S3PrefixContext> list = new ArrayList<S3PrefixContext>();

        S3PrefixContext prefixContext = getS3PrefixContext(context, prefix);
        list.add(prefixContext);

        if (depth.getValue() == maxDepth || !isRecurse()) {
            return list;
        }

        // Recurse down the hierarchy
        List<String> commonPrefixes = prefixContext.getObjectListing().getCommonPrefixes();
        depth.increment();
        for (String commonPrefix : commonPrefixes) {
            // System.out.print(".");
            getLog().debug(commonPrefix + " @ " + depth.getValue());
            list.addAll(getS3PrefixContexts(context, commonPrefix, depth));
        }
        depth.decrement();
        return list;
    }

    protected boolean isChildModule(String commonPrefix) {
        @SuppressWarnings("unchecked")
        List<String> modules = getProject().getModules();
        for (String module : modules) {
            if (commonPrefix.endsWith(module + "/")) {
                return true;
            }
        }
        return false;
    }

    /**
     * Return true if the ObjectListing contains an object under "key"
     */
    protected boolean isExistingObject(final ObjectListing objectListing, final String key) {
        List<S3ObjectSummary> summaries = objectListing.getObjectSummaries();
        for (S3ObjectSummary summary : summaries) {
            if (key.equals(summary.getKey())) {
                return true;
            }
        }
        return false;
    }

    /**
     * Return true if there is no object in the ObjectListing under defaultObjectKey.<br>
     * Return true if the object in the ObjectListing was created by this plugin.<br>
     * Return false otherwise.<br>
     */
    protected boolean isCreateOrUpdateDefaultObject(final S3PrefixContext context) {
        if (!isExistingObject(context.getObjectListing(), context.getDefaultObjectKey())) {
            // There is no default object, we are free to create one
            return true;
        }
        S3BucketContext s3Context = context.getBucketContext();
        // There is a default object, but if it was created by this plugin, we
        // still need to update it
        S3Object s3Object = s3Context.getClient().getObject(s3Context.getBucket(), context.getDefaultObjectKey());
        boolean isOurDefaultObject = isOurObject(s3Object);
        IOUtils.closeQuietly(s3Object.getObjectContent());
        if (isOurDefaultObject) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * Return true if this S3Object was created by this plugin. This is is done by checking the metadata attached to
     * this object for the presence of a custom value.
     */
    protected boolean isOurObject(final S3Object s3Object) {
        ObjectMetadata metadata = s3Object.getObjectMetadata();
        Map<String, String> userMetadata = metadata.getUserMetadata();
        String value = userMetadata.get(S3_INDEX_METADATA_KEY);
        boolean isOurObject = "true".equals(value);
        return isOurObject;
    }

    /**
     * Create a CopyObjectRequest with an ACL set to PublicRead
     */
    protected CopyObjectRequest getCopyObjectRequest(final String bucket, final String sourceKey, final String destKey) {
        CopyObjectRequest request = new CopyObjectRequest(bucket, sourceKey, bucket, destKey);
        request.setCannedAccessControlList(getAcl());
        return request;
    }

    public String getTimezone() {
        return timezone;
    }

    public void setTimezone(final String timezone) {
        this.timezone = timezone;
    }

    public String getDateFormat() {
        return dateFormat;
    }

    public void setDateFormat(final String dateFormat) {
        this.dateFormat = dateFormat;
    }

    public String getDefaultObjectKey() {
        return defaultObjectKey;
    }

    public void setDefaultObjectKey(final String defaultCloudFrontObject) {
        this.defaultObjectKey = defaultCloudFrontObject;
    }

    public String getFileImage() {
        return fileImage;
    }

    public void setFileImage(final String fileImage) {
        this.fileImage = fileImage;
    }

    public String getDirectoryImage() {
        return directoryImage;
    }

    public void setDirectoryImage(final String directoryImage) {
        this.directoryImage = directoryImage;
    }

    public String getCss() {
        return css;
    }

    public void setCss(final String css) {
        this.css = css;
    }

    public boolean isCopyDefaultObjectWithDelimiter() {
        return copyDefaultObjectWithDelimiter;
    }

    public void setCopyDefaultObjectWithDelimiter(final boolean copyDefaultObjectWithDelimiter) {
        this.copyDefaultObjectWithDelimiter = copyDefaultObjectWithDelimiter;
    }

    public boolean isCopyDefaultObjectWithoutDelimiter() {
        return copyDefaultObjectWithoutDelimiter;
    }

    public void setCopyDefaultObjectWithoutDelimiter(final boolean copyDefaultObjectWithoutDelimiter) {
        this.copyDefaultObjectWithoutDelimiter = copyDefaultObjectWithoutDelimiter;
    }

    public String getCacheControl() {
        return cacheControl;
    }

    public void setCacheControl(final String cacheControl) {
        this.cacheControl = cacheControl;
    }

    public String getBrowseKey() {
        return browseKey;
    }

    public void setBrowseKey(final String browseHtml) {
        this.browseKey = browseHtml;
    }

    /**
     * @return the recurse
     */
    public boolean isRecurse() {
        return recurse;
    }

    /**
     * @param recurse
     *            the recurse to set
     */
    public void setRecurse(final boolean recurse) {
        this.recurse = recurse;
    }

    /**
     * @return the organizationGroupId
     */
    public String getOrganizationGroupId() {
        return organizationGroupId;
    }

    /**
     * @param organizationGroupId
     *            the organizationGroupId to set
     */
    public void setOrganizationGroupId(final String organizationGroupId) {
        this.organizationGroupId = organizationGroupId;
    }

    public int getThreads() {
        return threads;
    }

    public void setThreads(int threadCount) {
        this.threads = threadCount;
    }

    public int getMaxDepth() {
        return maxDepth;
    }

    public void setMaxDepth(int maxUpdateDepth) {
        this.maxDepth = maxUpdateDepth;
    }

    public CannedAccessControlList getAcl() {
        return acl;
    }

    public void setAcl(CannedAccessControlList acl) {
        this.acl = acl;
    }

    @Override
    public Integer getMaxKeys() {
        return maxKeys;
    }

    @Override
    public void setMaxKeys(Integer maxKeys) {
        this.maxKeys = maxKeys;
    }

    public void executeMojo2() throws MojoExecutionException, MojoFailureException {
        try {
            getLog().info("Updating S3 bucket - " + getBucket());
            updateMojoState();
            S3BucketContext context = getS3BucketContext();
            generator = new CloudFrontHtmlGenerator(context);
            converter = new S3DataConverter(context);
            converter.setBrowseKey(getBrowseKey());
            getLog().info("Re-indexing - " + getPrefix());

            // List<String> directories = getPrefixes(context);

            S3PrefixContext projectContext = getS3PrefixContext(context, getPrefix());

            // System.out.print("[INFO] Examining directory structure ");
            long startTime = System.currentTimeMillis();
            List<S3PrefixContext> contexts = getS3PrefixContexts(context, getPrefix(), new Depth());
            // System.out.println();
            contexts.addAll(getContextsGoingUp(context, getPrefix()));
            // removeChildModules(contexts);
            List<UpdateDirectoryContext> udcs = getUpdateDirContexts(contexts);
            ThreadHandler handler = getThreadHandler(udcs);
            getLog().info(getUploadStartMsg(udcs.size(), handler.getThreadCount(), handler.getElementsPerThread()));
            long start = System.currentTimeMillis();
            handler.executeThreads();
            long millis = System.currentTimeMillis() - start;

            // One (or more) of the threads had an issue
            if (handler.getException() != null) {
                throw new TransferFailedException("Unexpected error", handler.getException());
            }

            // Show some stats
            getLog().info(getUploadCompleteMsg(millis, handler.getTracker().getCount()));
            getLog().info("Total time: " + formatter.getTime(System.currentTimeMillis() - startTime));
            updateRoot(getS3PrefixContext(context, null));
        } catch (Exception e) {
            throw new MojoExecutionException("Unexpected error: ", e);
        }
    }

    protected void show(String msg, List<String> strings) {
        for (String s : strings) {
            getLog().info(msg + s);
        }
    }

}
