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

import java.util.ArrayList;
import java.util.List;

import org.testng.annotations.DataProvider;

import com.ericsson.cifwk.taf.data.DataHandler;
import com.ericsson.cifwk.taf.utils.csv.CsvReader;
import com.ericsson.nms.pres.networkExplorer.data.ManagedObjectDataProvider;

public class SearchMOByNameTestData {
    public final static String TYPE_QUERY = "types/MeContext/name/ERBS001";
    public final static String TYPE_QUERY_WITH_WRONG_NAME = "types/MeContext/name/WRONG_NODE_NAME01";
    public final static String TYPE_QUERY_WITH_WRONG_TYPE = "types/WRONG_MO_TYPE/name/ERBS001";
    public static final String CSV_FILE_NAME = "Search_MO_by_Name_TestData.csv";

    private static ManagedObjectDataProvider managedObjectDataProvider = new ManagedObjectDataProvider();

    @DataProvider(name = "searchByName")
    public static Object[][] provideSearchQueries() {
        final List searchQueriesData = new ArrayList();
        final int idx = 0;
        DataHandler.readCsv(CSV_FILE_NAME);
        final Object[][] result = new Object[5][];
        try {
            result[0] = new Object[] { managedObjectDataProvider.getHost(), "types/MeContext/name/ERBS001", "1" };
            result[1] = new Object[] { managedObjectDataProvider.getHost(), "types/MeContext/name/ERBS00*", "6" };
            result[2] = new Object[] { managedObjectDataProvider.getHost(), "types/MeContext/name/*RBS00*", "6" };
            result[3] = new Object[] { managedObjectDataProvider.getHost(), "types/MeContext/name/*RBS001", "1" };
            result[4] = new Object[] { managedObjectDataProvider.getHost(), "types/MeContext/name/ERBS0*01", "0" };
        } catch (final InterruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return result;

    }

    @DataProvider(name = "searchByName_csv")
    public static Object[][] provideSearchQueriesFromCSV() {

        final CsvReader testItems = DataHandler.readCsv(CSV_FILE_NAME);
        final Object[][] result = new Object[testItems.getRowCount()][];
        try {
            for (int rowCount = 0; rowCount < testItems.getRowCount(); rowCount++) {

                result[rowCount] = new Object[] { managedObjectDataProvider.getHost(), testItems.getCell(0, rowCount),
                        testItems.getCell(1, rowCount), testItems.getCell(2, rowCount) };

            }
        } catch (final InterruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return result;
    }
}
