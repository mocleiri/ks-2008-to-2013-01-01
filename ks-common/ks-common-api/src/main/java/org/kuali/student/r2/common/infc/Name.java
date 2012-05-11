package org.kuali.student.r2.common.infc;

/**
 * A small object to hold a name with the locale.
 *
 * @author Kuali Student Team
 *
 */
public interface Name {

    /**
     * Locale of the name
     *
     * @name locale
     * @required
     */
    public String getLocale();
    

    /**
     * Name
     *
     * @name name
     */
    public String getName();
    
}
