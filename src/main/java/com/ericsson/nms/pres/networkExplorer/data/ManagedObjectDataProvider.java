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
package com.ericsson.nms.pres.networkExplorer.data;

import java.util.*;

import com.ericsson.cifwk.taf.DataProvider;
import com.ericsson.cifwk.taf.data.*;
import com.ericsson.cifwk.taf.utils.csv.CsvReader;
/*
 * Data Provider contains logic required to fetch data for test purposes.
 * It is containing all necessary sources,e.g. reading files from disk, databases
 * or Data Providers delivered by other teams
 * 
 *  Data Provider is used by Test Data class for providing data entries to test methods
 *  It is also contacted by Generic Operator to calculate expected results
 */
public class ManagedObjectDataProvider implements DataProvider{

	public static final String DATA_FILE = "network_explorer_user_data.csv";
	public static final String HOST_LOCAL = "local";
	public static final String HOST_2229_SC2 = "2229-sc2";
	public static final String HOST_UI_MUTI_NODE_1_S1 = "ui-multi-node1-sc1";
	public static final String HOST_UI_MUTI_NODE_1_S2 = "002-sc2";
	public static final String RV_JBOSS_1 = "host.rv-sc1.node.jboss";
	public static final String VALID_USER_MARK = "TRUE";
	public static final int QUERY_COLUMN = 0;
	public static final int EXPECTED_VALUE_COLUMN = 1;
	public static final int VALID_COLUMN = 2;

	/*
	 * Host is fetched using generic mechanism, so it can be changed for all entries at once if necessary.
	 */

    public static Host getHost() throws InterruptedException {

        Host jbossNode = DataHandler.getHostByName("atmws40-jboss");
        return jbossNode;
    }



	/*
	 * Test Data is fetched from a file that is an output of test analysis, but it may be a combination
	 * of test analysis data and dynamic reconciliation of data
	 */

    // Returned List of List will represent tabular data from CSV.
	public static List<List<String>> getUserNamePassword(){
		String fdn;
		String moName;
        // TODO: Implement passing of the VALID value to support asserted expectation in the test case.

		List<List<String>> result = new ArrayList<List<String>>();
		/*
		 * DataHandler should be the main entity accessed from Data Provider
		 */
		CsvReader testItems = DataHandler.readCsv(DATA_FILE);
		for (int i=1; i< testItems.getRowCount();i++){
			fdn = testItems.getCell(QUERY_COLUMN, i);
			moName = testItems.getCell(EXPECTED_VALUE_COLUMN, i);
			result.add(Arrays.asList(fdn,moName));
		}
		return result;
	}

	/*
	 * Data Provider may contains logic required to categorize the data. Eg it could be checking
	 * if data is correct based on entry in file
	 */
	public boolean isFdnValid(String fdn, String moName) {
		CsvReader testItems = DataHandler.readCsv(DATA_FILE);

        // iterate over csv files, to get multiple entries if present.
		for (int i=1; i< testItems.getRowCount();i++){
			if(fdn.equals((testItems).getCell(QUERY_COLUMN,i)) && moName.equals((testItems).getCell(EXPECTED_VALUE_COLUMN, i))){
				String isValidString =  (testItems.getCell(VALID_COLUMN,i)).trim();
				if(isValidString.equalsIgnoreCase(VALID_USER_MARK))
					return true;
			}
		}
		return false;
	}

}
