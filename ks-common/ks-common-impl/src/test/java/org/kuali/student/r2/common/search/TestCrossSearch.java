package org.kuali.student.r2.common.search;

import org.junit.Test;
import org.kuali.student.r2.common.dto.ContextInfo;
import org.kuali.student.r2.common.exceptions.MissingParameterException;
import org.kuali.student.r2.common.exceptions.OperationFailedException;
import org.kuali.student.r2.common.exceptions.PermissionDeniedException;
import org.kuali.student.r2.common.search.dto.SearchRequestInfo;
import org.kuali.student.r2.common.search.dto.SearchResultInfo;
import org.kuali.student.r2.common.util.ContextUtils;

import static org.junit.Assert.assertNotNull;

public class TestCrossSearch {

	@Test
	public void testCrossSearchUnion() throws MissingParameterException, PermissionDeniedException, OperationFailedException {
		MockSearch search = new MockSearch();
		SearchRequestInfo searchRequest = new SearchRequestInfo();
		searchRequest.setSearchKey("test.crossSearch");
		SearchResultInfo result = search.search(searchRequest, new ContextInfo());
		assertNotNull(result);
	}
}
