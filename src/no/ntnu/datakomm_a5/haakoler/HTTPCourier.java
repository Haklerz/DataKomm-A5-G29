package no.ntnu.datakomm_a5.haakoler;

import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Scanner;

import org.json.JSONObject;

/**
 * HTTPCourier
 */
public class HTTPCourier {
    private static final int OK_RESPONSE_CODE = 200;
    private final String URL_BASE;

    /**
     * Creates a HTTPCourier with given a host and a port.
     * 
     * @param host The host
     * @param port The port
     */
    public HTTPCourier(String host, int port) {
        URL_BASE = "http://" + host + ":" + port + "/";
    }

    /**
     * Creates a HTTPCourier with a given host and a default port 80.
     * 
     * @param host The host
     */
    public HTTPCourier(String host) {
        URL_BASE = "http://" + host + ":80/";
    }

    /**
     * Sends a <code>HTTP GET</code> to a given path and returns the response.
     * 
     * @param path The given path
     * @return The response
     */
    public JSONObject get(String path) {
        JSONObject response = null;
        try {
            URL url = new URL(URL_BASE + path);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            if (connection.getResponseCode() == OK_RESPONSE_CODE)
                response = new JSONObject(readStream(connection.getInputStream()));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return response;
    }

    /**
     * <code>HTTP POST</code>'s a given <code>JSONObject</code> to a given path and
     * returns the response.
     * 
     * @param json The given <code>JSONObject</code>
     * @param path The given path
     * @return The response
     */
    public JSONObject post(JSONObject json, String path) {
        JSONObject response = null;
        try {
            URL url = new URL(URL_BASE + path);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Content-Type", "application/json");
            connection.setDoOutput(true);
            new PrintWriter(connection.getOutputStream(), true).println(json.toString());
            if (connection.getResponseCode() == OK_RESPONSE_CODE)
                response = new JSONObject(readStream(connection.getInputStream()));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return response;
    }

    /**
     * Reads a <code>InputStream</code> and returns what was read as a
     * <code>String</code>.
     * 
     * @param inputStream The <code>InputStream</code> to be read
     * @return The <code>String</code> that was read
     */
    private static String readStream(InputStream inputStream) {
        StringBuilder string = new StringBuilder();
        Scanner in = new Scanner(inputStream);
        while (in.hasNextLine())
            string.append(in.nextLine());
        in.close();
        return string.toString();
    }
}