package no.ntnu.datakomm_a5.haakoler;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;

import org.json.JSONObject;

/**
 * A HTTP Courier can exchange JSON Objects with an assigned HTTP server.
 */
public class HTTPCourier {

    private static final int HTTP_OK_CODE = 200;
    private final String URL_BASE;

    public HTTPCourier(String host, int port) {
        URL_BASE = "http://" + host + ":" + port + "/";
    }

    public HTTPCourier(String host) {
        URL_BASE = "http://" + host + ":80/";
    }

    public JSONObject get(String path) {
        JSONObject response = null;

        try {

            URL url = new URL(URL_BASE + path);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");

            if (connection.getResponseCode() == HTTP_OK_CODE) {

            }
            else {

            }
        }
        catch (IOException e) {

            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return response;
    }

    public void post(String path, JSONObject json) {

    }
}