package com.ericsson.nms.pres.testWare.operators;

import com.ericsson.cifwk.taf.data.Host;

import java.util.List;

/*
 * This interface is used for making the access to all contexts unified.
 */
public interface NetworkExplorerOperatorActions {

    List<String> executeRestCall(Host restServer, String query);

	/*
	 * It is assumed that calling a services with credential will be available in all contexts, otherwise there would be no point to put it on interface  
	 */
	List<String> executeAuthorisedRestCall(Host restServer, String userName, String password, String query);
}
