package com.plickers.demo.plickersdemoapp.Helper;

import android.util.Log;

import org.json.JSONArray;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by Admin on 3/19/2016.
 */
public class JSONParser {

    public JSONParser(){

    }

    public JSONArray parseArray(String jsonURL){
        InputStream inputStream = null;
        String result = null;
        HttpURLConnection urlConnection;
        try {
            URL url = new URL(jsonURL);
            urlConnection = (HttpURLConnection) url.openConnection();
            inputStream = new BufferedInputStream(urlConnection.getInputStream());
            // json is UTF-8 by default
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, "UTF-8"), 8);
            StringBuilder sb = new StringBuilder();

            String line = null;
            while ((line = reader.readLine()) != null) {
                sb.append(line + "\n");
            }
            result = sb.toString();
            urlConnection.disconnect();

        } catch (Exception e) {
            Log.e("URL ERROR", "getWebInfo: ", e);
        } finally {
            try {
                if (inputStream != null) inputStream.close();
            } catch (Exception squish) {
            }
        }
        try {
            JSONArray jObject = new JSONArray(result);
            return jObject;
        } catch (Exception e) {
            Log.e("ERROR", "getJsonInfo: ", e);
            e.printStackTrace();
        }
        return null;
    }

}
