package no.ntnu.datakomm_a5.haakoler;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.Scanner;

import org.json.JSONObject;

public class HTTPGetter {

    private final String BASE_URL;

    /**
     * Creates an HTTPGetter.
     * 
     * @param host The host
     * @param port The port
     */
    public HTTPGetter(String host, int port) {
        BASE_URL = "http://" + host + ":" + port + "/";
    }

    /**
     * Creates a HTTPGetter.
     * 
     * @param host The host
     */
    public HTTPGetter(String host) {
        BASE_URL = "http://" + host + "/";
    }

    public static void main(String[] args) {
        HTTPGetter a = new HTTPGetter("datakomm.work");
        System.out.println(a.getString("dkrest/test/get"));
        System.out.println(a.getString("dkrest/test/get2"));
        JSONObject b = a.getJSON("dkrest/test/get2");
        System.out.println("a : " + b.getInt("a"));
        System.out.println("b : " + b.getInt("b"));
        System.out.println("c : " + b.getInt("c"));
    }

    public JSONObject getJSON(String path) {
        return new JSONObject(getString(path));
    }

    /**
     * Gets a response from a requested path.
     * 
     * @param path The requested path
     * @return The response
     */
    public String getString(String path) {
        String response = null;

        try {

            URL url = new URL(BASE_URL + path);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");

            if (connection.getResponseCode() == 200) {

                response = readResponse(connection);
            }
        }
        catch (IOException e) {

            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return response;
    }

    /**
     * Reads a response from a HttpURLConnection.
     * 
     * @param connection the connection
     * @return The response
     */
    private String readResponse(HttpURLConnection connection) {
        StringBuilder response = new StringBuilder();

        try {

            Scanner in = new Scanner(connection.getInputStream());

            while (in.hasNextLine()) {

                response.append(in.nextLine());
                // response.append("\n");
            }
            in.close();
        }
        catch (IOException e) {

            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return response.toString();
    }
}