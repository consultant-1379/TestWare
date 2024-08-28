package com.ericsson.nms.pres.networkExplorer.test.cases;

import java.util.HashMap;
import java.util.List;

import com.ericsson.cifwk.taf.data.Host;
import com.ericsson.nms.pres.networkExplorer.test.data.UserInputData;
import com.ericsson.nms.pres.testWare.operators.rest.NetworkExplorerRestOperator;
import org.json.simple.JSONArray;
import org.json.simple.JSONValue;
import org.testng.annotations.Test;

import se.ericsson.jcat.fw.annotations.Setup;
import com.ericsson.cifwk.taf.annotations.Context;

import com.ericsson.cifwk.taf.TestCase;
import com.ericsson.cifwk.taf.TorTestCaseHelper;
import com.ericsson.cifwk.taf.annotations.VUsers;

public class SearchByMOType extends TorTestCaseHelper implements TestCase {

    NetworkExplorerRestOperator networkExpRestOperator = new NetworkExplorerRestOperator();
    public final static String TYPE_QUERY = "types/MeContext";

	@VUsers(vusers = { 1 })
	@Context(context = { Context.REST })
	@Test(groups = { "Acceptance" }, dataProvider = "NetwExplorUserData", dataProviderClass = UserInputData.class)
	public void getMoType(Host restServer, String query, String expected) {
        //TODO: make use of data provided from DataProviders instead of using constants!

		setTestCase("TORF-2278_Func_1", "Search by MoType returns list of Mos");
		setTestStep("EXECUTE: User accesses REST resource representing all MO's of certain type.");

        List<String> responseContent = networkExpRestOperator.executeRestCall(restServer, TYPE_QUERY);

        HashMap<String,Object> responseMap = new HashMap<>();
		Object obj = JSONValue.parse(responseContent.get(0));
		JSONArray array = (JSONArray) obj;
		if(array.size() >0) {
            responseMap = (HashMap) array.get(0);
        }

		setTestStep("VERIFY: Name of the MO matching the FDN is displayed.");
		assertEquals(responseMap.get("namespace"), "OSS_TOP");
	}

}
