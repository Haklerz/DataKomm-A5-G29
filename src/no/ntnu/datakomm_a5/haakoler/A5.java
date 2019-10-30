package no.ntnu.datakomm_a5.haakoler;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import org.json.JSONArray;
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
        a5.echo();
        a5.multiply();
        a5.crackPinCode();
        a5.secret();
        a5.results();
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
        System.out.println();
        JSONObject task = getTask(1);
        printJSONObject("Task 1", task);

        JSONObject solution = new JSONObject();
        solution.put("msg", "Hello");
        printJSONObject("Solution", solution);

        JSONObject result = solveTask(solution);
        printJSONObject("Result", result);
    }

    private void echo() {
        System.out.println();
        JSONObject task = getTask(2);
        printJSONObject("Task 2", task);

        JSONArray arguments = task.getJSONArray("arguments");
        String receivedMessage = arguments.getString(0);

        JSONObject solution = new JSONObject();
        solution.put("msg", receivedMessage);
        printJSONObject("Solution", solution);

        JSONObject result = solveTask(solution);
        printJSONObject("Result", result);
    }

    private void multiply() {
        System.out.println();
        JSONObject task = getTask(3);
        printJSONObject("Task 3", task);

        JSONArray arguments = task.getJSONArray("arguments");
        int product = 1;
        for (int i = 0; i < arguments.length(); i++) {
            product *= arguments.getInt(i);
        }

        JSONObject solution = new JSONObject();
        solution.put("result", product);
        printJSONObject("Solution", solution);

        JSONObject result = solveTask(solution);
        printJSONObject("Result", result);
    }

    private void crackPinCode() {
        System.out.println();
        JSONObject task = getTask(4);
        printJSONObject("Task 4", task);

        JSONArray arguments = task.getJSONArray("arguments");
        String targetHash = arguments.getString(0);
        String pin = null;
        for (int i = 0; i < 10000; i++) {
            String testPin = String.format("%04d", i);
            if (targetHash.equals(md5(testPin))) {
                pin = testPin;
                break;
            }
        }

        JSONObject solution = new JSONObject();
        solution.put("pin", pin);
        printJSONObject("Solution", solution);

        JSONObject result = solveTask(solution);
        printJSONObject("Result", result);
    }

    private void secret() {
        System.out.println();
        JSONObject task = getTask(2016);
        printJSONObject("Secret Task", task);

        JSONArray arguments = task.getJSONArray("arguments");
        long networkIp = Long.parseLong(arguments.getString(0).replace(".", ""));
        long subnetMask = Long.parseLong(arguments.getString(1).replace(".", ""));
        System.out.println("nwIP:" + networkIp);
        System.out.println("mask:" + subnetMask);
        long ipAddress = networkIp & subnetMask;

        String ipString = (ipAddress & 0xFF000000) + "." + (ipAddress & 0x00FF0000) + "." + (ipAddress & 0x0000FF00) + ".0";

        JSONObject solution = new JSONObject();
        solution.put("ip", ipString);
        printJSONObject("Solution", solution);

        JSONObject result = solveTask(solution);
        printJSONObject("Result", result);
    }

    private void results() {
        System.out.println();
        printJSONObject("Results", server.get("dkrest/results/" + sessionId));
    }

    private JSONObject solveTask(JSONObject solution) {
        return server.post(solution.put("sessionId", sessionId), "dkrest/solve");
    }

    private JSONObject getTask(int taskNr) {
        return server.get("dkrest/gettask/" + taskNr + "?sessionId=" + sessionId);
    }

    private void printJSONObject(String name, JSONObject json) {
        System.out.println(name + ": {");
        for (String key : json.keySet()) {
            System.out.println("    \"" + key + "\" : " + json.get(key));
        }
        System.out.println("}");
    }

    public static String md5(String input) {
        String hashtext = null;
        try {

            // Static getInstance method is called with hashing MD5
            MessageDigest md = MessageDigest.getInstance("MD5");

            // digest() method is called to calculate message digest
            // of an input digest() return array of byte
            byte[] messageDigest = md.digest(input.getBytes());

            // Convert byte array into signum representation
            BigInteger no = new BigInteger(1, messageDigest);

            // Convert message digest into hex value
            hashtext = no.toString(16);
            while (hashtext.length() < 32) {
                hashtext = "0" + hashtext;
            }
        }
        catch (NoSuchAlgorithmException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return hashtext;
    }
}