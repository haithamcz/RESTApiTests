package org.haitham.demoswagger;

import com.google.gson.*;
import com.squareup.okhttp.*;

import java.io.IOException;

/**
 * @Author Haitham Jassim
 */
public class TestRestPoint {
    private String restPoint;
    private MediaType mediaType = MediaType.parse("application/json; charset=utf-8");
    private OkHttpClient client = new OkHttpClient();
    private Request lastRequest;
    private Response lastResponse;
    private String lastResponseBody;

    public TestRestPoint(String restPoint) {
        this.restPoint = restPoint;
    }

    /**
     * @param jsonBodyString
     * @return http response
     * @throws IOException
     */
    public Response postReguest(String jsonBodyString) throws IOException {
        RequestBody body = RequestBody.create(mediaType, jsonBodyString);
        this.lastRequest = new Request.Builder()
                .url(this.restPoint)
                .post(body)
                .build();
        lastResponse = client.newCall(lastRequest).execute();
        lastResponseBody = lastResponse.body().string(); // get body json
        return lastResponse;
    }

    public int getResponseStatusCode() {
        return this.lastResponse.code();
    }

    public String getResponseBody() {
        return this.lastResponseBody;
    }

    public Boolean validateResponseStatusCode(int expectedReturnCode) {
        return this.lastResponse.code() == expectedReturnCode;
    }

    /**
     *
     * @param expectedStatus
     * @return true if response body contains expected status, otherwise false
     */
    public Boolean validateResponseStatus(String expectedStatus) {
        JsonObject jsonObj = parseJsonObj();
        // check if the response received
        if (jsonObj.get("status") != null) {
            return jsonObj.get("status").toString().replace("\"", "").equals(expectedStatus);
        }
        // return false if no response
        else {
            return false;
        }
    }

    /**
     * @return json object
     */
    public JsonObject parseJsonObj() {
        JsonParser parser = new JsonParser();
        JsonObject jsonObj;
        try {
            jsonObj = parser.parse(this.lastResponseBody).getAsJsonObject();
            return jsonObj;
        } catch (Exception e) {
            System.out.println("could not parse json response  " + e.getMessage());
        }
        return null;
    }

    /**
     * @return json element
     */
    public JsonElement parseJson() {
        JsonParser parser = new JsonParser();
        JsonElement jsonElement;
        try {
            jsonElement = parser.parse(this.lastResponseBody);
            return jsonElement;
        } catch (Exception e) {
            System.out.println("could not parse json response  " + e.getMessage());
        }
        return null;
    }

    /**
     * @param expectedStatus
     * @return true if response body contains expected status, otherwise false
     */
    public Boolean validateBatchResponseStatus(String expectedStatus) {
        JsonElement jsonElement = parseJson();
        if (jsonElement != null) {
            return jsonElement.toString().contains(expectedStatus);
        } else {
            return false;
        }
    }

    /**
     * @param expectedMessage
     * @return true if response body contains expected message, otherwise false
     */
    public Boolean validateResponseMessage(String expectedMessage) {
        JsonObject jsonObj = parseJsonObj();
        if (jsonObj.get("messages") != null) {
            return jsonObj.get("messages").toString().contains(expectedMessage);
        }
        // return false if no response
        else {
            return false;
        }

    }

}
