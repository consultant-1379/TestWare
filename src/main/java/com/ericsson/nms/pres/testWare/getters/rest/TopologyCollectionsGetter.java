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

public class TopologyCollectionsGetter implements RestGetter {

    public static final String RESOURCE_URI = "topologyCollections/staticCollections/";
    public static final String SAVE_URI = "topologyCollections/staticCollections/";
    public static final String SAVED_SEARCHES_URI = "topologyCollections/savedSearches/";
    public static final RestResponseCode SUCCESSFUL_CALL_CODE = RestResponseCode.CREATED;

    public static String getSavePath() {
        return SAVE_URI;
    }

    public static String getCollectionsUri() {
        return RESOURCE_URI;
    }

    public static String getCollectionByPoidUri(final String poid) {
        return RESOURCE_URI + poid;
    }

    public static String getSaveSearchesURI() {
        return SAVED_SEARCHES_URI;
    }

    public static String getCollectionWithAttributesByPoIdURI(final String poid, final String attributes) {
        return RESOURCE_URI + poid + "/" + attributes;
    }
}
