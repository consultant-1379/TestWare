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
package com.ericsson.nms.pres.networkExplorer.test.data;

import java.util.List;

import org.testng.annotations.DataProvider;

import com.ericsson.cifwk.taf.TestData;
import com.ericsson.cifwk.taf.data.Host;
import com.ericsson.nms.pres.networkExplorer.data.ManagedObjectDataProvider;

/*
 * Goal of TestData class is to fetch data from Data Provider class and prepare for test method
 * It contains methods annotated with @DataProvider and name used in @Test annotation on test method
 */
public class UserInputData implements TestData {

    /*
     * This test data method is providing data for test method requiring 3
     * arguments: - Host that contains deployed service - userName credential
     * for authenticated call - password credential for authenticated call The
     * data provider class is giving host, but credentials are delivered as
     * List<String> so changing the format is required Changing the format is
     * the only reason for this class, as it is not shared and any additional
     * logic would be lost
     * 
     * @DataProvider annotation is bit misguiding. In TAF nomenclature this
     * method is Test Data, while Data Provider is lower level that is actually
     * fetching data
     */
    @DataProvider(name = "NetwExplorUserData")
    public static Object[][] authorisedCallArguments() {
        /*
         * All data are fetched from Data Provider. Data Provider is containing
         * logic on how and from where to get the data
         */
        Host host = null;
        try {
            host = ManagedObjectDataProvider.getHost();
        } catch (final InterruptedException e) {
            e.printStackTrace();
        }
        final List<List<String>> userDataList = ManagedObjectDataProvider.getUserNamePassword();

        /*
         * This method need to return Object[][] where first dimension is amount
         * of calls of test method and data combinations and second dimension is
         * amount of arguments required by test method
         */

        // TODO: strip out auth credentials and modify for FDN and valid column.
        final Object[][] result = new Object[userDataList.size()][];
        int idx = 0;
        for (final List item : userDataList) {
            final Object[] testMethodArguments = new Object[3];
            testMethodArguments[0] = host;
            testMethodArguments[1] = item.get(ManagedObjectDataProvider.QUERY_COLUMN);
            testMethodArguments[2] = item.get(ManagedObjectDataProvider.EXPECTED_VALUE_COLUMN);
            result[idx] = testMethodArguments;
            idx++;
        }
        return result;
    }

    @DataProvider(name = "serverDetails")
    public static Object[][] getNetworkExplorerServer() {
        /*
         * All data are fetched from Data Provider. Data Provider is containing
         * logic on how and from where to get the data
         */
        Host host = null;
        try {
            host = ManagedObjectDataProvider.getHost();
        } catch (final InterruptedException e) {
            e.printStackTrace();
        }

        final Object[][] result = new Object[1][];
        final Object[] testMethodArguments = new Object[1];
        testMethodArguments[0] = host;
        result[0] = testMethodArguments;

        return result;
    }
}
