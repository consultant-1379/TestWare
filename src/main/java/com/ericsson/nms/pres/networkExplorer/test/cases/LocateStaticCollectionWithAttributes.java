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
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

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

public class LocateStaticCollectionWithAttributes extends TorTestCaseHelper implements TestCase {

    NetworkExplorerRestOperator neworkExplorerOper = new NetworkExplorerRestOperator();

    @VUsers(vusers = { 1 })
    @Context(context = { Context.REST })
    @Test(groups = { "Acceptance" }, dataProvider = "serverDetails", dataProviderClass = UserInputData.class)
    public void getCollectionWithAttributes(final Host restServer) {

        //ARRANGE
        // ensure at least one collection is persisted.
        // construct JSON DATA to POST.
        final ArrayList<Long> poidList = new ArrayList<>();
        poidList.add(Long.decode("1688849861364248"));
        poidList.add(Long.decode("1688849861364255"));
        final JSONObject obj = new JSONObject();
        final String randomName = new BigInteger(130, new java.security.SecureRandom()).toString(32);
        obj.put("name", "test" + randomName);
        obj.put("moList", poidList);
        // Save static collection first.
        final RestTool restTool = neworkExplorerOper.saveStaticCollection(restServer, obj.toJSONString(), "usr2");
        final String poId = RestToolHelper.getPoIdFromLocationHeader(restTool);
        setTestCase("TORF-724_Func_2", "Locate static collection");

        // ACT
        setTestStep("EXECUTE: Collection with attributes- mimInfo:mimVersion and same user who has created the collection is accessible via REST endpoint.");
        final List<String> attributes1 = neworkExplorerOper.getCollectionWithAttributesByPoId(restServer, "usr2", poId, "mimInfo:mimVersion");

        setTestStep("EXECUTE: Collection with attributes- mimInfo and same user who has created the collection is accessible via REST endpoint.");
        final List<String> attributes2 = neworkExplorerOper.getCollectionWithAttributesByPoId(restServer, "usr2", poId, "mimInfo");

        setTestStep("EXECUTE: Collection with attributes- mimInfo:mimVersion is accessible via REST endpoint. In this case, users who has created the collection is not same as user who is trying to access it");
        final List<String> attributes3 = neworkExplorerOper.getCollectionWithAttributesByPoId(restServer, "usr1", poId, "mimInfo:mimVersion");

        setTestStep("VERIFY: Collection with attributes- mimInfo:mimVersion and same user who has created the collection is accessible via REST endpoint.");
        final Object respObj1 = JSONValue.parse(attributes1.get(0));
        final List<JSONObject> jsonObjects1 = (List) respObj1;
        //ASSERT 1st condition
        for (final JSONObject jsonObject : jsonObjects1) {
            assertNotNull(jsonObject.get("nodePoId"));
            assertNotNull(jsonObject.get("fdnName"));
            assertNotNull(jsonObject.get("mimInfo"));

        }

        setTestStep("VERIFY: Collection with attributes- mimInfoand same user who has created the collection is accessible via REST endpoint.");
        final Object respObj2 = JSONValue.parse(attributes2.get(0));
        final List<JSONObject> jsonObjects2 = (List) respObj2;

        //ASSERT 2nd condition
        for (final JSONObject jsonObject : jsonObjects2) {
            assertNotNull(jsonObject.get("nodePoId"));
            assertNotNull(jsonObject.get("fdnName"));
            assertNotNull(jsonObject.get("mimInfo"));

            final Map<String, String> mimInfoMap = (Map) jsonObject.get("mimInfo");
            assertNotNull(mimInfoMap.get("mimVersion"));
            assertNotNull(mimInfoMap.get("mimName"));
            assertNotNull(mimInfoMap.get("mimRelease"));
        }

        setTestStep("VERIFY: Collection with attributes- mimInfo:mimVersion is accessible via REST endpoint. In this case, users who has created the collection is not same as user who is trying to access it.");
        // ASSERT 3rd condition
        final Object respObj3 = JSONValue.parse(attributes3.get(0));
        final List<JSONObject> jsonObjects3 = (List) respObj3;
        assertEquals(0, jsonObjects3.size());

    }
}
