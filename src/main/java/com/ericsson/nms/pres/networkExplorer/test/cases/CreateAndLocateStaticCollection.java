package com.ericsson.nms.pres.networkExplorer.test.cases;

import java.math.BigInteger;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
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

//TODO: use of data provided from DataProviders instead of using constants!
// TODO: once DELETE REST endpoint becomes available, create static name and cleanup method isntead of random name.
public class CreateAndLocateStaticCollection extends TorTestCaseHelper implements TestCase {

    NetworkExplorerRestOperator neworkExplorerOper = new NetworkExplorerRestOperator();

    @VUsers(vusers = { 1 })
    @Context(context = { Context.REST })
    @Test(groups = { "Acceptance" }, dataProvider = "NetwExplorUserData", dataProviderClass = UserInputData.class)
    public void saveCollection(final Host restServer, final String query, final String expected) {

        setTestCase("TORF-4730_Func_1", "Create static collection");
        setTestStep("EXECUTE: User sends POST request containing collection represented in JSON format.");
        // construct JSON DATA to POST.
        final ArrayList<Long> poidList = new ArrayList<>();
        poidList.add(Long.decode("281474976966687"));
        poidList.add(Long.decode("281474976966693"));

        final JSONObject obj = new JSONObject();

        // TODO: once DELETE REST endpoint becomes available, create static name and cleanup method isntead of random name.
        final String randomName = new BigInteger(130, new java.security.SecureRandom()).toString(32);
        obj.put("name", "test" + randomName);
        obj.put("moList", poidList);

        final RestTool restTool = neworkExplorerOper.saveStaticCollection(restServer, obj.toJSONString(), "usr1");

        setTestStep("VERIFY: Response has been returned with a 201 status code.");
        assertEquals(201, restTool.getLastResponseCodes().get(0).toInt());

        setTestStep("VERIFY: Response headers contain location of resource that has been created.");
        final HashMap<String, String> headersMap = RestToolHelper.getResponseHeadersFromTool(restTool);
        assertEquals(true, headersMap.get("Location") != null);

        setTestCase("TORF-724_Func_1", "Locate static collection");
        setTestStep("VERIFY: New resource representing saved collection is accessible via REST endpoint.");

        try {
            final URL resourceLocation = new URL(headersMap.get("Location"));

            String path = resourceLocation.getPath();
            path = path.replaceAll("//", "/"); // bug in TAF REST TOOL requires to remove doubled slashes from path.
            path = path.replaceFirst("/", ""); // format path to conform to TAF REST TOOL
            final List<String> response = restTool.get(path);

            final Object respObj = JSONValue.parse(response.get(0));
            final HashMap<String, HashMap> jsonObject = (HashMap) respObj;
            final HashMap<String, String> attributes = (HashMap) jsonObject.get("details").get("attributes");
            final String returnedName = attributes.get("name");

            setTestStep("VERIFY: Name of the collection returned from rest matches the name of saved collection.");
            assertEquals(returnedName, "test" + randomName);

        } catch (final MalformedURLException e) {
            e.printStackTrace();
        }
    }

    @VUsers(vusers = { 1 })
    @Context(context = { Context.REST })
    @Test(groups = { "Acceptance" }, dataProvider = "NetwExplorUserData", dataProviderClass = UserInputData.class)
    public void getSavedCollections(final Host restServer, final String query, final String expected) {

        // ensure at least one collection is persisted.
        // construct JSON DATA to POST.
        final ArrayList<Long> poidList = new ArrayList<>();
        poidList.add(Long.decode("281474976966687"));
        poidList.add(Long.decode("281474976966693"));
        final JSONObject obj = new JSONObject();
        String randomName = new BigInteger(130, new java.security.SecureRandom()).toString(32);
        obj.put("name", "test" + randomName);
        obj.put("moList", poidList);
        neworkExplorerOper.saveStaticCollection(restServer, obj.toJSONString(), "usr2");

        setTestCase("TORF-724_Func_2", "Locate static collection");

        setTestStep("PRE: There is at least one collection persisted.");
        final int numOfPeristenCollections = neworkExplorerOper.getStaticCollectionsCount(restServer, "usr2");
        assertEquals(true, (numOfPeristenCollections > 0));

        setTestStep("EXECUTE: User saves static collection.");
        randomName = new BigInteger(130, new java.security.SecureRandom()).toString(32);
        obj.put("name", "test" + randomName);
        obj.put("moList", poidList);
        neworkExplorerOper.saveStaticCollection(restServer, obj.toJSONString(), "usr2");

        setTestStep("VERIFY: Collections count has increased by 1.");
        final int currentNumberOfCollections = neworkExplorerOper.getStaticCollectionsCount(restServer, "usr2");
        assertEquals(true, ((currentNumberOfCollections - 1) == numOfPeristenCollections));
    }

    @VUsers(vusers = { 1 })
    @Context(context = { Context.REST })
    @Test(groups = { "Acceptance" }, dataProvider = "NetwExplorUserData", dataProviderClass = UserInputData.class)
    public void getSingleCollection(final Host restServer, final String query, final String expected) {

        //ARRANGE
        // ensure at least one collection is persisted.
        // construct JSON DATA to POST.
        final ArrayList<Long> poidList = new ArrayList<>();
        poidList.add(Long.decode("281474976966687"));
        poidList.add(Long.decode("281474976966693"));
        final JSONObject obj = new JSONObject();
        final String randomName = new BigInteger(130, new java.security.SecureRandom()).toString(32);
        obj.put("name", "test" + randomName);
        obj.put("moList", poidList);
        // Save static collection first.
        neworkExplorerOper.saveStaticCollection(restServer, obj.toJSONString(), "usr2");

        setTestCase("TORF-724_Func_2", "Locate static collection");

        setTestStep("PRE: There is at least one collection persisted.");
        final int numOfPeristenCollections = neworkExplorerOper.getStaticCollectionsCount(restServer, "usr2");
        assertEquals(true, (numOfPeristenCollections > 0));

        setTestStep("VERIFY: User can access collection by specifying it's ID (poid).");

        // Check what poid was given to the collection using existing REST endpoint.
        final List<String> responses = neworkExplorerOper.getAllStaticCollections(restServer, "usr2");
        final Object respObj = JSONValue.parse(responses.get(0));
        final JSONArray jsonObject = (JSONArray) respObj; // JSONArray will be returned only if Root of JSON begins with a collection.
        final Map<String, String> collectionsMap = (Map) jsonObject.get(0);
        final String poId = collectionsMap.get("poId");

        // ACT
        final List<String> singleCollectionResponse = neworkExplorerOper.getCollectionByPoid(restServer, "usr2", poId);

        // ASSERT
        final Object singleCollRespObject = JSONValue.parse(singleCollectionResponse.get(0));
        // final JSONArray singleCollJsonObject = (JSONArray) singleCollRespObject; // JSONArray will be returned only if Root of JSON begins with a collection.
        //        final Map<String, String> savedSearchesMap = (Map) singleCollJsonObject.get(0);
        // TODO: implement assertion when possible to run test.
        final Map<String, String> collection = (Map) singleCollRespObject;
        assertNotNull(collection.get("details"));
    }

}
