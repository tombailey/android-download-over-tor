package me.tombailey.tor;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Tom on 08/10/2016.
 */

public class Headers {

    private Header[] mHeaders;
    private Map<String, String> mHeadersKeyToValue;

    public Headers(List<Header> headers) {
        mHeaders = headers.toArray(new Header[headers.size()]);

        mHeadersKeyToValue = new HashMap<String, String>(headers.size());
        for (Header header : mHeaders) {
            //TODO: should we make getKey() lowercase for case-insensitive keys?
            mHeadersKeyToValue.put(header.getKey(), header.getValue());
        }
    }

    public String getString(String name) {
        return mHeadersKeyToValue.get(name);
    }

    public int getInt(String name) throws NumberFormatException {
        String value = getString(name);
        return value != null ? Integer.parseInt(value) : null;
    }

    public Header[] getHeaders() {
        return mHeaders;
    }

}
