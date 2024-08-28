/*------------------------------------------------------------------------------
 *******************************************************************************
 * COPYRIGHT Ericsson 2012
 *
 * The copyright to the computer program(s) herein is the property of
 * Ericsson Inc. The programs may be used and/or copied only with written
 * permission from Ericsson Inc. or in accordance with the terms and
 * conditions stipulated in the agreement/contract under which the
 * program(s) have been supplied.
 *******************************************************************************
 *----------------------------------------------------------------------------*/
package com.ericsson.nms.pres.testWare.getters.rest;

import com.ericsson.cifwk.taf.RestGetter;
import com.ericsson.cifwk.taf.tal.rest.RestResponseCode;
/*
 * Getter class is containing a mapping of point exposed by SUT (in REST case URI and it parts) and way test cases
 * are accessing it
 * 
 * Getter class suppose to be as minimal as possible in logic - it should just isolate these points so in case of change maintenance
 * is trivial and can even be automated
 * 
 *  This class contains only properties of SUT, not test data and is accessed only by Operators
 */
public class ManagedObjectServiceGetter implements RestGetter{

	public static final String URI = "managedObjects/";
	public static final RestResponseCode SUCCESSFUL_CALL_CODE = RestResponseCode.OK;

	/*
	 * This is minimal logic combining static fields in the class, so it is still easy to maintain
	 */
	public static String getResourcePath() {
		return URI;
	}


	
}
