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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;
import org.testng.annotations.Test;

import com.ericsson.cifwk.taf.TestCase;
import com.ericsson.cifwk.taf.TorTestCaseHelper;
import com.ericsson.cifwk.taf.annotations.Context;
import com.ericsson.cifwk.taf.annotations.VUsers;
import com.ericsson.cifwk.taf.data.Host;
import com.ericsson.nms.pres.networkExplorer.test.data.SearchMOByNameTestData;
import com.ericsson.nms.pres.testWare.operators.rest.NetworkExplorerRestOperator;

public class SearchMOByName extends TorTestCaseHelper implements TestCase {
    NetworkExplorerRestOperator networkExpRestOperator = new NetworkExplorerRestOperator();

    @VUsers(vusers = { 1 })
    @Context(context = { Context.REST })
    @Test(groups = { "Acceptance" }, dataProvider = "searchByName_csv", dataProviderClass = SearchMOByNameTestData.class)
    public void getMoName_CSV(final Host restServer, final String query, final String expectedResultCount, final String expectedMoNameList) {
        //TODO: make use of data provided from DataProviders instead of using constants!
        final List<String> actualMoNameList = new ArrayList<>();
        setTestCase("TORF-4624_Func_1", "Search MO by name(exact match and wildcard) returns list of Mos");
        setTestStep("EXECUTE: User accesses REST resource representing MO's of certain type and name.");

        final List<String> responseContent = networkExpRestOperator.executeRestCall(restServer, query);

        final Object obj = JSONValue.parse(responseContent.get(0));
        final JSONArray array = (JSONArray) obj;
        for (final Object object : array) {
            final JSONObject jsonObject = (JSONObject) object;
            actualMoNameList.add(jsonObject.get("name").toString());

        }
        setTestStep("VERIFY: Check matching no. of MOs are returned");
        assertEquals(array.size(), Integer.parseInt(expectedResultCount));
        if (expectedMoNameList != null) {
            assertEquals(actualMoNameList, Arrays.asList(expectedMoNameList.split(";")));
        }

    }
}
