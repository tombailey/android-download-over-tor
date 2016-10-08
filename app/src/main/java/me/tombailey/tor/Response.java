package me.tombailey.tor;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Tom on 25/09/2016.
 */

public class Response {

    private static final String CONTENT_TYPE = "Content-Type";

    private static final String ASCII = "ISO-8859-1";
    private static final String UTF_8 = "UTF-8";


    private String mHttpVersion;
    private int mStatusCode;
    private String mStatusText;

    private Headers mHeaders;
    private byte[] mBody;

    public Response(String httpVersion, int statusCode, String statusText, Headers headers, byte[] body) {
        mHttpVersion = httpVersion;
        mStatusCode = statusCode;
        mStatusText = statusText;

        mHeaders = headers;
        mBody = body;
    }

    public String getHttpVersion() {
        return mHttpVersion;
    }

    public int getStatusCode() {
        return mStatusCode;
    }

    public String statusText() {
        return mStatusText;
    }

    public Header[] getHeaders() {
        return mHeaders.getHeaders();
    }

    public String getBodyAsString() throws UnsupportedEncodingException {
        //TODO: take character set directly from Content-Type and use ASCII if it is not set

        String characterSet;
        String contentType = mHeaders.getString(CONTENT_TYPE);
        if (contentType != null && contentType.toLowerCase().contains(UTF_8)) {
            characterSet = UTF_8;
        } else {
            characterSet = ASCII;
        }

        return new String(mBody, characterSet);
    }

    public byte[] getBodyAsBytes() {
        return mBody;
    }


    public static class Builder {

        private InputStream mInputStream;

        public Builder() {

        }

        public Builder fromInputStream(InputStream inputStream) {
            mInputStream = inputStream;
            return this;
        }

        public Response build() throws IOException {
            //READ ENTIRE RESPONSE
            BufferedInputStream bufferedInputStream = new BufferedInputStream(mInputStream);
            byte[] buffer = new byte[4096]; //4kb buffer

            //presume about 1MB response
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream(1000 * 1024);
            int bytesRead = bufferedInputStream.read(buffer);
            while (bytesRead != -1) {
                byteArrayOutputStream.write(buffer, 0, bytesRead);
                bytesRead = bufferedInputStream.read(buffer);
            }



            byte[] response = byteArrayOutputStream.toByteArray();

            //FIGURE OUT WHICH PARTS ARE STATUS, HEADERS and BODY
            String statusLine = null;
            List<Header> headers = new ArrayList<Header>(16);
            byte[] body;

            boolean isHeaders = false;
            int lineStarts = 0;
            int bodyStarts = 0;

            for (int index = 0; index < response.length; index++) {
                //TODO: watch out for index outside of bounds
                //we have found a CRLF so split
                if (response[index] == 0x0D && response[index+1] == 0x0A) {
                    if (isHeaders) {
                        String[] headerParts = new String(response, lineStarts, index-1, ASCII).split(": ");
                        headers.add(new Header(headerParts[0], headerParts[1]));
                    } else {
                        statusLine = new String(response, lineStarts, index-1, ASCII);
                        isHeaders = true;
                    }
                    lineStarts = index + 2;

                    //TODO: watch out for index outside of bounds
                    if (buffer[index+2] == 0x0D && buffer[index+3] == 0x0A) {
                        bodyStarts = index + 4;
                        break;
                    }
                }
            }

            body = new byte[response.length - bodyStarts];
            System.arraycopy(response, bodyStarts, body, 0, response.length - bodyStarts);

            String[] statusLineParts = statusLine.split(" ");
            return new Response(statusLineParts[0], Integer.parseInt(statusLineParts[1]), statusLineParts[2], new Headers(headers), body);
        }

    }

}
