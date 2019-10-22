package no.ntnu.datakomm_a5.haakoler;

import org.json.JSONObject;

/**
 * HTTPCourier
 */
public class HTTPCourier {
    private final String URL_BASE;

    public HTTPCourier(String host) {
        URL_BASE = "http://" + host + "/";
    }

    public JSONObject get(String path) {
        return null;
    }

    private String getString(String path) {
        return null;
    }

    public void post(JSONObject json, String path) {

    }
}