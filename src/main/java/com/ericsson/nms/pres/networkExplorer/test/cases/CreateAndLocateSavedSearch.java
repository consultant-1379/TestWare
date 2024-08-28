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
package com.ericsson.nms.pres.networkExplorer.test.cases;

import java.math.BigInteger;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;
import org.testng.annotations.Test;

import com.ericsson.cifwk.taf.TestCase;
import com.ericsson.cifwk.taf.TorTestCaseHelper;
import com.ericsson.cifwk.taf.annotations.Context;
import com.ericsson.cifwk.taf.annotations.VUsers;
import com.ericsson.cifwk.taf.data.Host;
import com.ericsson.cifwk.taf.tools.RestTool;
import com.ericsson.nms.pres.networkExplorer.test.data.UserInputData;
import com.ericsson.nms.pres.testWare.operators.rest.NetworkExplorerRestOperator;
import com.ericsson.nms.pres.testWare.util.RestToolHelper;

public class CreateAndLocateSavedSearch extends TorTestCaseHelper implements TestCase {

    NetworkExplorerRestOperator neworkExplorerOper = new NetworkExplorerRestOperator();

    @VUsers(vusers = { 1 })
    @Context(context = { Context.REST })
    @Test(groups = { "Acceptance" }, dataProvider = "NetwExplorUserData", dataProviderClass = UserInputData.class)
    public void saveSearch(final Host restServer, final String query, final String expected) {
        setTestCase("TORF-720_Func_1", "Create saved search");
        setTestStep("EXECUTE: User sends POST request containing saved search represented in JSON format.");

        // construct JSON DATA to POST.
        final JSONObject obj = new JSONObject();

        // TODO: once DELETE REST endpoint becomes available, create static name
        // and cleanup method isntead of random name.
        final String randomName = new BigInteger(130, new java.security.SecureRandom()).toString(32);
        obj.put("name", "SavedSearch_Test" + randomName);
        obj.put("searchQuery", "types/MeContext");
        final RestTool restTool = neworkExplorerOper.saveSearch(restServer, obj.toJSONString(), "usr1");

        setTestStep("VERIFY: Response has been returned with a 201 status code.");
        assertEquals(201, restTool.getLastResponseCodes().get(0).toInt());

        setTestStep("VERIFY: Response headers contain location of resource that has been created.");
        final HashMap<String, String> headersMap = RestToolHelper.getResponseHeadersFromTool(restTool);
        assertEquals(true, headersMap.get("Location") != null);
    }

    @VUsers(vusers = { 1 })
    @Context(context = { Context.REST })
    @Test(groups = { "Acceptance" }, dataProvider = "NetwExplorUserData", dataProviderClass = UserInputData.class)
    public void locateSavedSearches(final Host restServer, final String query, final String expected) {
        setTestCase("TORF-4732_Func_1", "Locate saved search");
        setTestStep("EXECUTE: User sends GET request containing user Id represented in JSON format.");

        // ARRANGE
        // construct JSON DATA to POST.
        final JSONObject obj = new JSONObject();
        final String randomName = new BigInteger(130, new java.security.SecureRandom()).toString(32);
        obj.put("name", "SavedSearch_Test" + randomName);
        obj.put("searchQuery", "types/MeContext");
        neworkExplorerOper.saveSearch(restServer, obj.toJSONString(), "maverick");

        setTestStep("PRE: There is at least one saved search persisted.");

        // ACT
        final List<String> responses = neworkExplorerOper.getSavedSearches(restServer, "eramkoh");

        // ASSERT
        final Object respObj = JSONValue.parse(responses.get(0));
        final JSONArray jsonObject = (JSONArray) respObj;
        final int noOfExistingSavedSearches = jsonObject.size();
        assertEquals(true, (noOfExistingSavedSearches > 0));

    }

    @VUsers(vusers = { 1 })
    @Context(context = { Context.REST })
    @Test(groups = { "Acceptance" }, dataProvider = "NetwExplorUserData", dataProviderClass = UserInputData.class)
    public void locateSavedSearch(final Host restServer, final String query, final String expected) {
        setTestCase("TORF-4732_Func_1", "Locate saved search");
        setTestStep("EXECUTE: User sends GET request containing saved search Id represented in JSON format.");

        // ARRANGE
        // construct JSON DATA to POST.
        final JSONObject obj = new JSONObject();
        final String randomName = new BigInteger(130, new java.security.SecureRandom()).toString(32);
        obj.put("name", "SavedSearch_Test" + randomName);
        obj.put("searchQuery", "types/MeContext");
        neworkExplorerOper.saveSearch(restServer, obj.toJSONString(), "maverick1");
        // ACT
        final List<String> responses = neworkExplorerOper.getSavedSearches(restServer, "maverick1");

        final Object respObj = JSONValue.parse(responses.get(0));
        final JSONArray jsonObject = (JSONArray) respObj; // JSONArray will be returned only if Root of JSON begins with a collection.
        final Map<String, String> savedSearchesMap = (Map) jsonObject.get(0);
        final String poId = savedSearchesMap.get("id");

        List<String> response = neworkExplorerOper.getSavedSearch(restServer, "usr2", poId);
        Object object = JSONValue.parse(response.get(0));
        Map<String, Map> jsonObj = (HashMap) object;
        Map<String, String> savedSearchMap = jsonObj.get("details");
        assertNull(savedSearchMap);

        response = neworkExplorerOper.getSavedSearch(restServer, "maverick1", poId);
        object = JSONValue.parse(response.get(0));
        jsonObj = (HashMap) object;
        savedSearchMap = jsonObj.get("details");
        final String returnedName = savedSearchMap.get("name");
        assertNotNull(returnedName);

    }
}
