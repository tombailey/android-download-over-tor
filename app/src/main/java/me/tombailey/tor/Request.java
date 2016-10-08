package me.tombailey.tor;

import com.msopentech.thali.toronionproxy.OnionProxyManager;
import com.msopentech.thali.toronionproxy.Utilities;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.Writer;
import java.net.Socket;
import java.net.URL;
import java.util.List;

import javax.net.ssl.SSLSocketFactory;

/**
 * Created by Tom on 25/09/2016.
 */

public class Request {

    private static final int HTTP = 80;
    private static final int HTTPS = 443;


    private Method mMethod;
    private String mHost;
    private String mFile;
    private int mPort;
    private List<Header> mHeaders;

    public enum Method {
        GET, POST;
    }

    public Request(Method method, URL url, List<Header> headers) {
        mMethod = method;
        mHost = url.getHost();
        mFile = url.getFile();
        mPort = url.getProtocol().equalsIgnoreCase("https") ? HTTPS : HTTP;
        mHeaders = headers;
    }

    public Response request(OnionProxyManager onionProxyManager) throws IOException {
        Socket socket = null;
        try {
            socket = Utilities.socks4aSocketConnection(mHost, mPort, "127.0.0.1", onionProxyManager.getIPv4LocalHostSocksPort());
            if (mPort == HTTPS) {
                socket = ((SSLSocketFactory) SSLSocketFactory.getDefault()).createSocket(socket, mHost, mPort, true);
            }

            Writer writer;
            if (mMethod == Method.GET) {
                writer = get(socket, mHost, mFile, mHeaders);
            } else {
                writer = post(socket, mHost, mFile, mHeaders);
            }

           Response response = new Response.Builder()
                   .fromInputStream(socket.getInputStream())
                   .build();

            writer.close();
            socket.close();

            return response;
        } finally {
            if (socket != null) {
                try {
                    socket.close();
                } catch (IOException ioe) {
                    //ignored
                }
            }
        }
    }

    private Writer get(Socket socket, String host, String file, List<Header> headers) throws IOException {
        PrintWriter pw = new PrintWriter(socket.getOutputStream());
        pw.println("GET " + (file.length() > 0 ? file : "/") + " HTTP/1.1");
        pw.println("Host: " + host);
        //TODO: headers
        pw.println("");
        pw.flush();
        return pw;
    }

    private Writer post(Socket socket, String host, String file, List<Header> headers) {
        //TODO: impl
        return null;
    }

}
