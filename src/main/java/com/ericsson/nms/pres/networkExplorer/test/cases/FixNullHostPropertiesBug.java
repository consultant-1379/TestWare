package com.ericsson.nms.pres.networkExplorer.test.cases;

import com.ericsson.cifwk.taf.TestCase;
import com.ericsson.cifwk.taf.TorTestCaseHelper;
import com.ericsson.cifwk.taf.annotations.VUsers;
import com.ericsson.cifwk.taf.data.Host;
import com.ericsson.nms.pres.networkExplorer.test.data.UserInputData;
import org.testng.annotations.Test;

/**
 * This testcase needs to be appended at the top of the test suite in order to avoid broken properties bug.
 */
public class FixNullHostPropertiesBug extends TorTestCaseHelper implements TestCase{

    @VUsers(vusers = {1})
    @Test(groups={"Acceptance"}, dataProvider = "NetwExplorUserData", dataProviderClass = UserInputData.class)
    public void fix(Host restServer, String fdn, String moName) {
        assertEquals(true, true);
    }
}
