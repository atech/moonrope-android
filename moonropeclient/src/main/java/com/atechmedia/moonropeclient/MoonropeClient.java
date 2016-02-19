package com.atechmedia.moonropeclient;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

// have a quick look at the imports (coding convention)

/**
 * Created by jonathan on 16/02/16.
 */
public class MoonropeClient {

    String httpHost;
    String httpProtocol;
    int apiVersion;
    Map<String, String> headers;

    // Initialize by providing the host and protocol
    public MoonropeClient(String httpHost, String httpProtocol, int apiVersion) {
        this.httpHost = httpHost;
        this.httpProtocol = httpProtocol;
        this.apiVersion = apiVersion;
        this.headers = new HashMap<>();
    }

    public MoonropeClient(String httpHost, String httpProtocol) {
        this(httpHost, httpProtocol, 1);
    }

    // Add an HTTP header which will be sent with all requests for this client
    public void addHeader(String name, String value) {
        headers.put(name, value);
    }

    // Convenience method for making a request without params
    public MoonropeResponse makeRequest(String path) throws IOException {
        return makeRequest(path, new HashMap<String, Object>());
    }

    // Make a request to the remote server with the given params and execute the completion
    // handler when complete. The handler will be called with a MoonropeResponse enum containing
    // the appropriate response information.
    public MoonropeResponse makeRequest(String path, Map<String, Object> params) throws IOException {
        HttpURLConnection httpConnection = this.createRequest(path, params);

        InputStream errorStream = httpConnection.getErrorStream();
        if (errorStream == null) {
            int statusCode = httpConnection.getResponseCode();
            String stringData = streamToString(httpConnection.getInputStream());

            if (statusCode == 200) {
                try {
                    Map<String, Object> jsonData = jsonObjectToMap(new JSONObject(stringData));
                    String moonropeStatus = (String)jsonData.get("status");
                    @SuppressWarnings("unchecked")
                    Map<String, Object> moonropeFlags = (Map<String, Object>)jsonData.get("flags");

                    if (moonropeStatus.equals("success")) {
                        return new MoonropeResponseSuccess(jsonData.get("data"), moonropeFlags);
                    } else {
                        return new MoonropeResponseError(moonropeStatus, jsonData.get("data"));
                    }
                } catch (Exception e)
                {
                    return new MoonropeResponseFailure("Couldn't decode the JSON");
                }
            } else {
                // Something is afoot. This is a failure of the API because all moonrope requests
                // should have a 200 status.
                System.out.println("Something went wrong with the API: " + stringData + " (code: " + statusCode + ")");
                return new MoonropeResponseFailure("Moonrope Internal Error");
            }
        } else {
            return new MoonropeResponseFailure(errorStream.toString());
        }
    }

    // Create a new request object for the given path & set of parameters
    HttpURLConnection createRequest(String path, Map<String, Object> params) throws IOException {
        String fullURL = this.httpProtocol + "://" + this.httpHost + "/api/v" + this.apiVersion + "/" + path;
        HttpURLConnection httpConnection = (HttpURLConnection)new URL(fullURL).openConnection();
        httpConnection.setDoOutput(true); // for POST
        httpConnection.setRequestProperty("Content-Type", "application/json");
        httpConnection.setRequestProperty("User-Agent", "Moonrope Java Client");
        for (Map.Entry<String, String> headerEntry : headers.entrySet()) {
            httpConnection.setRequestProperty(headerEntry.getKey(), headerEntry.getValue());
        }

        // Create some JSON data to send to the remote server from the given dictionary of params
        JSONObject jsonData = new JSONObject(params);
        if (jsonData.length() == 0) {
            System.out.println("Couldn't successfully create a JSON payload. Sending no params.");
        }

        // Set the request body
        OutputStream stream = httpConnection.getOutputStream();
        stream.write(jsonData.toString().getBytes("UTF-8"));
        stream.flush();
        stream.close();

        return httpConnection;
    }

    String streamToString(InputStream stream) throws IOException {
        BufferedReader streamReader = new BufferedReader(new InputStreamReader(stream));
        StringBuilder stringBuilder = new StringBuilder();

        String inputString;
        while ((inputString = streamReader.readLine()) != null)
        {
            stringBuilder.append(inputString);
        }

        return stringBuilder.toString();
    }

    Map<String, Object> jsonObjectToMap(JSONObject object) throws JSONException {
        Map<String, Object> map = new HashMap<>();

        Iterator keys = object.keys();
        while (keys.hasNext()) {
            String key = (String)keys.next();
            map.put(key, jsonRecursive(object.get(key)));
        }

        return map;
    }

    @SuppressWarnings("unchecked")
    List jsonArrayToList(JSONArray array) throws JSONException {
        List list = new ArrayList();

        for (int i = 0; i < array.length(); i++) {
            list.add(jsonRecursive(array.get(i)));
        }

        return list;
    }

    Object jsonRecursive(Object json) throws JSONException {
        if (json == JSONObject.NULL) {
            return null;
        } else if (json instanceof JSONObject) {
            return jsonObjectToMap((JSONObject) json);
        } else if (json instanceof JSONArray) {
            return jsonArrayToList((JSONArray) json);
        } else {
            return json;
        }
    }
}
