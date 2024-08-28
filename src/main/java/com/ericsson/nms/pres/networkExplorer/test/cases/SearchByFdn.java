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

import com.ericsson.cifwk.taf.TestCase;
import com.ericsson.cifwk.taf.TorTestCaseHelper;
import com.ericsson.cifwk.taf.annotations.VUsers;
import com.ericsson.cifwk.taf.data.Host;
import com.ericsson.nms.pres.networkExplorer.test.data.UserInputData;
import com.ericsson.nms.pres.testWare.operators.rest.NetworkExplorerRestOperator;
import com.ericsson.nms.pres.testWare.util.ManagedObjectHelper;
import org.json.simple.JSONArray;
import org.json.simple.JSONValue;
import org.testng.annotations.Test;
import org.apache.log4j.Logger;

import java.util.HashMap;
import java.util.List;

/*
 * Test Case content should be mainly generated from AVS service!
 * Added part should be as minimal as possible and should contain:
 * - interaction between test cases
 * - usage of test data
 * - verification and reporting logic
 * 
 *  Shape of this class determines output on test report.
 * 
 * Please note superclass is TorTestCaseHelper that provides reporting utilities
 * and it is marked with TestCase interface used as a marker only  
 */
public class SearchByFdn extends TorTestCaseHelper implements TestCase{
	/*
	 * Operator is the point of contact to "operate" - execute business actions on the tested functionality
	 * It also provides expected results
	 */
    NetworkExplorerRestOperator networkExplorerOperator = new NetworkExplorerRestOperator();
    private final static Logger logger = Logger.getLogger(SearchByFdn.class);
    public final static String VALID_FDN = "MeContext=ERBS1,ManagedElement=1";
    public final static String VALID_NAME = "1";
	
	/*
	 * Please note that both VUsers and Context are accepting arrays. It means that test case will be executed in all combinations of all
	 * values. Exception is UI context as it can be used with 1 parallel user only 
	 * Groups is a powerful mechanism allowing to tag test cases for reuse and isolated execution
	 * Test Case below will be part of smoke tests, but also used in R&V workflow and GAT executions
	 * 
	 * The test method should be short and simple - usually below 4 lines of non-reporting code (with optimum of one comparison line)
	 */

    //Search by FDN returns the MO's name in UI.
    @VUsers(vusers = {1})
    @Test(groups={"Acceptance"},dataProvider = "NetwExplorUserData", dataProviderClass = UserInputData.class)
    public void searchFdnReturnName(Host restServer, String fdn, String moName) {    // take as many params as data provider will try to pass
		/*
		 *Good practice is to improve default reporting by adding the test data parameters to setTestCase description.
		 */
        setTestCase("TORF-2277_Func_1", "Search by FDN returns the MO's name in UI. On " + restServer + " using fdn: " + fdn + " and expecting name: " + moName );
        setTestStep("EXECUTE: User provides a valid FDN.");

        //TODO: swap constant VALID_FDN with fdn provided from dataProvider.
        List<String> responseContent = networkExplorerOperator.executeRestCall(restServer  , VALID_FDN);

        Object obj=JSONValue.parse(responseContent.get(0));
        JSONArray array=(JSONArray)obj;
        HashMap responseMap;
        String receivedNameFromFDN = null;

        try {
            responseMap = (HashMap)array.get(0);
            receivedNameFromFDN = (String)responseMap.get("name");
        }
        catch (NullPointerException npe) {
            // suppress NullPointerException to allow a clean error report.
            logger.info("No data received from rest");   // TODO: Verify if possible to log extra report info (look at log4j.properties).
        }
        finally {
            setTestStep("VERIFY: Name of the MO matching the FDN is displayed.");
            // order of objects asserted is relevant to reporting making a logical sense.
            assertEquals(VALID_NAME, receivedNameFromFDN);       // eg: name will be = "Cell1"
        }
    }


    @VUsers(vusers = {1})
	@Test(groups={"Acceptance"},dataProvider = "NetwExplorUserData", dataProviderClass = UserInputData.class)
	public void validMoReturned(Host restServer, String fdn, String moName) {

		setTestCase("TORF-2277_Func_2", "Single user accesses REST link representing \"search by FDN\" query. On " + restServer + " using fdn: " + fdn + " .");

        setTestStep("Execute: get Managed Object by FDN");
        List<String> responseContent = networkExplorerOperator.executeRestCall(restServer, VALID_FDN);

        setTestStep("Verify: valid Managed Object is returned");

        String reportHeader = "Valid MO Returned = ";

        // TODO: Make your expected values based on Valid flag from CSV to be able to reuse testcase for negative scenarios.
        assertEquals(reportHeader + true, reportHeader + ManagedObjectHelper.isValidMO(responseContent.get(0)));
	}
}