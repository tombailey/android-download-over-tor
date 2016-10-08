package me.tombailey.tor;

/**
 * Created by Tom on 25/09/2016.
 */

public class Header {

    private String mKey;
    private String mValue;

    public Header(String key, String value) {
        mKey = key;
        mValue = value;
    }

    public String getKey() {
        return mKey;
    }

    public String getValue() {
        return mValue;
    }
}
