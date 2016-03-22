package com.plickers.demo.plickersdemoapp.Helper;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by Admin on 3/19/2016.
 * This class is used to retrieve the JSON data from an URL. It works by retrieving the JSON data
 * in a buffered reader and passing the output to a StringBuilder. The StringBuilder then passing
 * that string into a JSONArray (or JSONObject) to create the returning object.
 */
public class JSONReader {

    //Empty constructor
    public JSONReader(){

    }

    /*
    Returns a JSONArray object based on the data found at jsonURL.

    @param jsonURL  The url of the JSON
    @return         The JSONArray located at specified url
     */
    public JSONArray readArray(String jsonURL){
        InputStream inputStream = null;
        String result = null;
        HttpURLConnection urlConnection;
        try {
            //Form a url from the string passed in
            URL url = new URL(jsonURL);
            //Get the text input from the url
            urlConnection = (HttpURLConnection) url.openConnection();
            inputStream = new BufferedInputStream(urlConnection.getInputStream());
            // read the json data that is UTF-8 by default
            BufferedReader reader = new BufferedReader(
                    new InputStreamReader(inputStream, "UTF-8"), 8);
            //Store the result into a string
            StringBuilder sb = new StringBuilder();
            String line = null;
            while ((line = reader.readLine()) != null) {
                sb.append(line + "\n");
            }
            result = sb.toString();
            //Disconnect the connection
            urlConnection.disconnect();

        } catch (IOException ioe) {
            Log.e("URL ERROR", "getWebInfo: ", ioe);
        } finally { //Close the inputStream
            try {
                if (inputStream != null) inputStream.close();
            } catch (IOException ioe) {
                Log.e("inputStream ERROR", "getWebInfo: ", ioe);
            }
        }
        try {
            //Return the JSONArray generated from the string retrieved
            JSONArray jObject = new JSONArray(result);
            return jObject;
        } catch (JSONException je) {
            Log.e("ERROR", "getJsonInfo: ", je);
            je.printStackTrace();
        }
        return null;
    }

}
