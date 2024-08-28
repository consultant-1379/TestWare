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
package com.ericsson.nms.pres.testWare.operators.rest;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.json.simple.JSONArray;
import org.json.simple.JSONValue;

import com.ericsson.cifwk.taf.data.Host;
import com.ericsson.cifwk.taf.tal.rest.RestResponseCode;
import com.ericsson.cifwk.taf.tools.RestTool;
import com.ericsson.nms.pres.testWare.getters.rest.ManagedObjectServiceGetter;
import com.ericsson.nms.pres.testWare.getters.rest.TopologyCollectionsGetter;
import com.ericsson.nms.pres.testWare.operators.NetworkExplorerOperatorActions;

/*
 * This class is "operating" on SUT in REST context. To do it it uses RestTool
 */
public class NetworkExplorerRestOperator implements NetworkExplorerOperatorActions, com.ericsson.cifwk.taf.RestOperator {

    private final static Logger logger = Logger.getLogger(NetworkExplorerRestOperator.class);

    private RestTool restTool;

    @Override
    public List<String> executeRestCall(final Host restServer, final String query) {

        restTool = new RestTool(restServer);
        /*
         * URI is externalised and isolated in GETTER class, so when it changes,
         * it will required effort in one place only
         */
        final List<String> calResponseContent = restTool.get(ManagedObjectServiceGetter.getResourcePath() + query);
        printResponseTimes();
        return calResponseContent;
    }

    /*
     * This is implementation of executeAuthorisedRestCall method in REST
     * context
     */
    @Override
    public List<String> executeAuthorisedRestCall(final Host restServer, final String userName, final String password, final String query) {
        restTool = new RestTool(restServer);
        restTool.setAuthenticationCredentails(userName, password);
        final List<String> calResponseContent = restTool.get(ManagedObjectServiceGetter.getResourcePath() + query);
        printResponseTimes();
        return calResponseContent;
    }

    public RestTool saveStaticCollection(final Host server, final String jsonData, final String username) {
        restTool = new RestTool(server);
        setTorUserHeader(restTool, username);
        restTool.postJson(TopologyCollectionsGetter.getSavePath(), jsonData, false);
        return restTool;
    }

    public List<String> getAllStaticCollections(final Host server, final String username) {
        restTool = new RestTool(server);
        setTorUserHeader(restTool, username);
        final List<String> responses = restTool.get(TopologyCollectionsGetter.getCollectionsUri());
        final Object respObj = JSONValue.parse(responses.get(0));
        final JSONArray jsonObject = (JSONArray) respObj;
        return responses;
    }

    public List<String> getCollectionByPoid(final Host server, final String username, final String poId) {
        restTool = new RestTool(server);
        setTorUserHeader(restTool, username);
        final String uri = TopologyCollectionsGetter.getCollectionByPoidUri(poId);
        final List<String> responses = restTool.get(uri);
        return responses;
    }

    public RestTool saveSearch(final Host server, final String jsonData, final String username) {
        restTool = new RestTool(server);
        setTorUserHeader(restTool, username);
        restTool.postJson(TopologyCollectionsGetter.getSaveSearchesURI(), jsonData, false);
        return restTool;
    }

    public List<String> getSavedSearches(final Host server, final String username) {
        restTool = new RestTool(server);
        setTorUserHeader(restTool, username);
        final List<String> responses = restTool.get(TopologyCollectionsGetter.getSaveSearchesURI());

        return responses;
    }

    public List<String> getSavedSearch(final Host server, final String username, final String poId) {
        restTool = new RestTool(server);
        setTorUserHeader(restTool, username);
        final String uri = TopologyCollectionsGetter.getSaveSearchesURI() + poId;
        final List<String> responses = restTool.get(uri);
        return responses;
    }

    public int getStaticCollectionsCount(final Host server, final String username) {
        restTool = new RestTool(server);
        setTorUserHeader(restTool, username);
        final List<String> responses = restTool.get(TopologyCollectionsGetter.getCollectionsUri());
        final Object respObj = JSONValue.parse(responses.get(0));
        final JSONArray jsonObject = (JSONArray) respObj; // JSONArray will be returned only if Root of JSON begins with a collection.
        return jsonObject.size();
    }

    public List<String> getCollectionWithAttributesByPoId(final Host server, final String username, final String poId, final String attributes) {
        restTool = new RestTool(server);
        setTorUserHeader(restTool, username);
        final String uri = TopologyCollectionsGetter.getCollectionWithAttributesByPoIdURI(poId, attributes);
        final List<String> responses = restTool.get(uri);
        return responses;
    }

    /*
     * This method is implementing verification of response codes
     */
    private boolean verifyResponseCodes(final List<RestResponseCode> responseCodes) {
        boolean result = true;
        for (final RestResponseCode callResponseCode : responseCodes) {
            /*
             * Putting debug information is very helpful when test case is
             * failing
             */
            logger.debug("Processing response code " + callResponseCode);
            /*
             * Expected content is externalised and isolated in GETTER class for
             * maintenance purposes
             */
            result = result && (callResponseCode == ManagedObjectServiceGetter.SUCCESSFUL_CALL_CODE);
        }
        /*
         * Because method is returning boolean, it is good practice to put
         * actual result on the test report
         */
        logger.info("Result of response code verification: " + result);
        return result;
    }

    /*
     * Optionally verification of content could be done in this operator
     */

    /*
     * This method is operating on response times programatically, The average
     * response time is being logged by Tool Monitor if enabled
     */
    private void printResponseTimes() {
        final List<Long> callResponseTimes = restTool.getLastExecutionTimes();
        for (final Long time : callResponseTimes) {
            logger.debug("Response time: " + time);
        }
        logger.debug("Average response time: " + restTool.getAverageExecutionTime());
    }

    public void setTorUserHeader(final RestTool rTool, final String username) {
        final List<String> headers = new ArrayList<>();
        final String userHeader = "X-Tor-UserID:" + username;
        headers.add(userHeader);
        rTool.setHttpHeaders(headers);
    }

}