/**
 *
 */
package org.kuali.rice.student.lookup.keyvalues;

import java.util.List;

import org.kuali.rice.kns.web.ui.KeyLabelPair;

/**
 * @author lindholm
 *
 */
public class DepartmentCocValuesFinder extends CocValuesFiinder {

	public List<KeyLabelPair> getKeyValues() {
		return findCocOrgs("kuali.org.Department");
	}
}
