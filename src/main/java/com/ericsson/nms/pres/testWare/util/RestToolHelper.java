package com.ericsson.nms.pres.testWare.util;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.ericsson.cifwk.taf.tools.RestTool;

public class RestToolHelper {

    /**
     * 
     */
    private static final String SLASH = "/";
    /**
     * 
     */
    private static final String LOCATION = "Location";

    public static HashMap<String, String> getResponseHeadersFromTool(final RestTool restTool) {

        final List<String> responseHeaders = restTool.getLastResponseHeaders().get(0);

        final HashMap headersMap = new HashMap();

        for (final String header : responseHeaders) {
            final String[] headerKeyValuePair = header.split(":", 2);
            if (header == responseHeaders.get(0)) {
                continue; // exclude first header as it does not follow key:value pattern
            }
            headersMap.put(headerKeyValuePair[0], headerKeyValuePair[1]);
        }

        return headersMap;
    }

    public static String getPoIdFromLocationHeader(final RestTool restTool) {
        String poId;
        final Map<String, String> headers = getResponseHeadersFromTool(restTool);
        final String[] splittedArray = headers.get(LOCATION).split(SLASH);
        poId = splittedArray[splittedArray.length - 1];
        return poId;
    }
}
