package org.apache.torque.mojo;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.cxf.common.util.StringUtils;
import org.apache.maven.plugin.MojoExecutionException;
import org.kuali.core.db.torque.KualiTorqueSchemaDumpTask;

/**
 * Export a description of a database to XML. This generates information about the metadata ie the tables, primary keys,
 * foreign keys, indexes, sequences, and views. It does not export any data.
 * 
 * @goal exportschema
 * @phase generate-sources
 */
public class ExportSchemaMojo extends AntTaskMojo {

	/**
	 * Comment that gets placed into the generated XML document
	 * 
	 * @parameter expression="${comment}"
	 *            default-value="Automatically generated by the maven-impex-plugin version ${project.version}"
	 */
	private String comment;

	/**
	 * Flag indicating whether or not tables will be processed. Default is true
	 * 
	 * @parameter expression="${processTables}" default-value="true"
	 */
	private boolean processTables;

	/**
	 * Flag indicating whether or not views will be processed. Default is true
	 * 
	 * @parameter expression="${processViews}" default-value="true"
	 */
	private boolean processViews;

	/**
	 * Flag indicating whether or not sequences will be processed. Default is true
	 * 
	 * @parameter expression="${processSequences}" default-value="true"
	 */
	private boolean processSequences;

	/**
	 * Database type (oracle, mysql etc)
	 * 
	 * @parameter expression="${targetDatabase}"
	 */
	private String targetDatabase;

	/**
	 * The directory where the schema XML file will be written
	 * 
	 * @parameter expression="${schemaXMLFile}" default-value="${basedir}/src/main/impex/${project.artifactId}.xml"
	 * @required
	 */
	private File schemaXMLFile;

	/**
	 * The XML file contains a name attribute for the schema being exported. This value is what will end up there
	 * 
	 * @parameter expression="${schemaXMLName}" default-value="${project.artifactId}"
	 * @required
	 */
	private String schemaXMLName;

	/**
	 * The physical name of the schema inside the database we will be exporting. This typically needs some database
	 * specific conversion applied to it. Oracle needs everything to be upper case. For MySQL it is better if it is all
	 * lower case
	 * 
	 * @parameter expression="${schema}"
	 * @required
	 */
	private String schema;

	/**
	 * The fully qualified class name of the database driver.
	 * 
	 * @parameter expression="${driver}"
	 * @required
	 */
	private String driver;

	/**
	 * The connect URL of the database.
	 * 
	 * @parameter expression="${url}"
	 * @required
	 */
	private String url;

	/**
	 * The user name to connect to the database.
	 * 
	 * @parameter expression="${username}"
	 */
	private String username;

	/**
	 * The password for the database user.
	 * 
	 * @parameter expression="${password}"
	 */
	private String password;

	/**
	 * Comma separated list of regular expressions for tables to include in the export
	 * 
	 * @parameter expression="${includePatterns}"
	 */
	private String includePatterns;

	/**
	 * Comma separated list of regular expressions for tables to exclude from the export
	 * 
	 * @parameter expression="${excludePatterns}"
	 */
	private String excludePatterns;

	/**
	 * Creates a new SQLMojo object.
	 */
	public ExportSchemaMojo() {
		super(new KualiTorqueSchemaDumpTask());
	}

	/**
	 * Configure the Ant task
	 */
	protected void configureTask() throws MojoExecutionException {
		super.configureTask();
		KualiTorqueSchemaDumpTask task = (KualiTorqueSchemaDumpTask) super.getAntTask();
		try {
			BeanUtils.copyProperties(task, this);
		} catch (Exception e) {
			throw new MojoExecutionException("Error copying properties", e);
		}
		task.setIncludePatterns(getList(getIncludePatterns()));
		task.setExcludePatterns(getList(getExcludePatterns()));
	}

	protected static List<String> getList(String csv) {
		if (StringUtils.isEmpty(csv)) {
			return new ArrayList<String>();
		}
		String[] tokens = csv.split(",");
		List<String> list = new ArrayList<String>();
		for (String token : tokens) {
			list.add(token.trim());
		}
		return list;
	}

	public boolean isProcessTables() {
		return processTables;
	}

	public void setProcessTables(boolean processTables) {
		this.processTables = processTables;
	}

	public boolean isProcessViews() {
		return processViews;
	}

	public void setProcessViews(boolean processViews) {
		this.processViews = processViews;
	}

	public boolean isProcessSequences() {
		return processSequences;
	}

	public void setProcessSequences(boolean processSequences) {
		this.processSequences = processSequences;
	}

	public String getTargetDatabase() {
		return targetDatabase;
	}

	public void setTargetDatabase(String targetDatabase) {
		this.targetDatabase = targetDatabase;
	}

	public File getSchemaXMLFile() {
		return schemaXMLFile;
	}

	public void setSchemaXMLFile(File schemaXMLFile) {
		this.schemaXMLFile = schemaXMLFile;
	}

	public String getSchemaXMLName() {
		return schemaXMLName;
	}

	public void setSchemaXMLName(String schemaXMLName) {
		this.schemaXMLName = schemaXMLName;
	}

	public String getSchema() {
		return schema;
	}

	public void setSchema(String schema) {
		this.schema = schema;
	}

	public String getDriver() {
		return driver;
	}

	public void setDriver(String driver) {
		this.driver = driver;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getIncludePatterns() {
		return includePatterns;
	}

	public void setIncludePatterns(String includePatterns) {
		this.includePatterns = includePatterns;
	}

	public String getExcludePatterns() {
		return excludePatterns;
	}

	public void setExcludePatterns(String excludePatterns) {
		this.excludePatterns = excludePatterns;
	}

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

}
