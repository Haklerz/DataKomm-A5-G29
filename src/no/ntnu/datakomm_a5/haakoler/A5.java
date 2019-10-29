package no.ntnu.datakomm_a5.haakoler;

import org.json.JSONObject;

public class A5 {
    private final HTTPCourier server;
    private final String EMAIL;
    private final String PHONE;
    private int sessionId;

    public A5(String host, String email, String phone) {
        server = new HTTPCourier(host);
        EMAIL = email;
        PHONE = phone;
    }

    public static void main(String[] args) {
        A5 a5 = new A5("datakomm.work", "haakoler@stud.ntnu.no", "94825606");

        a5.authorize();
        a5.hello();
    }

    private void authorize() {
        JSONObject credentials = new JSONObject();
        credentials.put("email", EMAIL);
        credentials.put("phone", PHONE);
        JSONObject response = server.post(credentials, "dkrest/auth");
        if (response.has("sessionId")) {
            sessionId = response.getInt("sessionId");
        }
    }

    private void hello() {
        printJSONObject(getTask(6));
    }

    private JSONObject getTask(int taskNr) {
        return server.get("dkrest/gettask/" + taskNr + "?sessionId=" + sessionId);
    }

    private void printJSONObject(JSONObject json) {
        for (String key : json.keySet()) {
            System.out.println(key + " : " + json.get(key));
        }
    }
}