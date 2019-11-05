package no.ntnu.a5dk.haakoler;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import org.json.JSONArray;
import org.json.JSONObject;

/**
 * Solution for task A5.
 */
public class A5 {
    private static final HTTPCourier server = new HTTPCourier("datakomm.work");
    private static int sessionId;

    public static final void main(String[] args) {
        authorize("haakoler@stud.ntnu.no", "94825606");
        hello();
        echo();
        multiply();
        crackPinCode();
        secret();
        showResults();
    }

    /**
     * Authorizes and gets a sessionId.
     */
    private static void authorize(String email, String phone) {
        JSONObject credentials = new JSONObject();
        credentials.put("email", email);
        credentials.put("phone", phone);
        JSONObject response = server.post(credentials, "dkrest/auth");
        if (response.has("sessionId")) {
            sessionId = response.getInt("sessionId");
        }
    }

    /**
     * Says hello to the server.
     */
    private static void hello() {
        JSONObject task = getTask(1);
        printJSONObject("Task 1", task);

        JSONObject solution = new JSONObject();
        solution.put("msg", "Hello");
        printJSONObject("Solution", solution);

        JSONObject result = solveTask(solution);
        printJSONObject("Result", result);
    }

    /**
     * Echoes back to the server.
     */
    private static void echo() {
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

    /**
     * Multiplies n numbers together.
     */
    private static void multiply() {
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

    /**
     * Cracks the MD5 hashed 4-digit pin code.
     */
    private static void crackPinCode() {
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

    /**
     * Solves the secret task.
     */
    private static void secret() {
        JSONObject task = getTask(2016);
        printJSONObject("Secret Task", task);

        JSONArray arguments = task.getJSONArray("arguments");
        String[] networkIp = arguments.getString(0).split("\\.");
        String[] subnetMask = arguments.getString(1).split("\\.");

        String ip = (Integer.parseInt(networkIp[0]) & Integer.parseInt(subnetMask[0])) + "." +
            (Integer.parseInt(networkIp[1]) & Integer.parseInt(subnetMask[1])) + "." +
            (Integer.parseInt(networkIp[2]) & Integer.parseInt(subnetMask[2])) + ".1";

        JSONObject solution = new JSONObject();
        solution.put("ip", ip);
        printJSONObject("Solution", solution);

        JSONObject result = solveTask(solution);
        printJSONObject("Result", result);
    }

    /**
     * Gets and prints the results of the tests.
     */
    private static void showResults() {
        printJSONObject("Results", server.get("dkrest/results/" + sessionId));
    }

    /**
     * Posts a soluiton to a task ot the server.
     * 
     * @param solution The solution
     * @return The result
     */
    private static JSONObject solveTask(JSONObject solution) {
        return server.post(solution.put("sessionId", sessionId), "dkrest/solve");
    }

    /**
     * Gets a task from the server.
     * 
     * @param taskNr The number of the task to get
     * @return The task
     */
    private static JSONObject getTask(int taskNr) {
        return server.get("dkrest/gettask/" + taskNr + "?sessionId=" + sessionId);
    }

    /**
     * Prints a <code>JSONObject</code> to the terminal.
     * 
     * @param name The name of the object
     * @param json The <code>JSONObject</code> to be printed
     */
    private static void printJSONObject(String name, JSONObject json) {
        System.out.println(name + ": {");
        for (String key : json.keySet()) {
            System.out.println("    \"" + key + "\" : " + json.get(key));
        }
        System.out.println("}");
    }

    /**
     * MD5 hash algorithm. Stolen from
     * https://www.geeksforgeeks.org/md5-hash-in-java
     */
    public static String md5(String input) {
        String hashtext = null;
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            byte[] messageDigest = md.digest(input.getBytes());
            BigInteger no = new BigInteger(1, messageDigest);
            hashtext = no.toString(16);
            while (hashtext.length() < 32) {
                hashtext = "0" + hashtext;
            }
        }
        catch (NoSuchAlgorithmException e) {
        }
        return hashtext;
    }
}