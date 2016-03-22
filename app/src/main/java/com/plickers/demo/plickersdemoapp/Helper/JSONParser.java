package com.plickers.demo.plickersdemoapp.Helper;

import com.plickers.demo.plickersdemoapp.Objects.Question;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;

/**
 * Parses the JSON file to create the Quesitons and return them back in an ArrayList
 *
 */
public class JSONParser {

    //Empty constructor
    public JSONParser() {

    }

    public ArrayList<Question> parseJSONfile(JSONArray jsonArray) throws JSONException{
        ArrayList<Question> questionArrayList = new ArrayList<>();
        JSONArray jsonData = jsonArray;
                /*
                Loop through the array and store the appropriate data per question
                 */
        for (int i = 0; i < jsonData.length(); i++) {
            String sectionId = jsonData.getJSONObject(i).getString("section");
            String questionId = jsonData.getJSONObject(i).getJSONObject("question").
                    getString("id");
            String modified;
            //If there is no modified, the last touched date is created
            try {
                modified = jsonData.getJSONObject(i).getJSONObject("question").
                        getString("modified");//get modified if it exists
            } catch (JSONException je) {
                modified = jsonData.getJSONObject(i).getJSONObject("question").
                        getString("created");//if it wasn't modified get created
            }
            String body = jsonData.getJSONObject(i).getJSONObject("question").
                    getString("body");
            JSONArray choices = jsonData.getJSONObject(i).getJSONObject("question").
                    getJSONArray("choices"); //Will have to convert to String
            JSONArray responses = jsonData.getJSONObject(i).getJSONArray("responses");
            String image = "none";
            //If there is a image, store the link, else store "none"
            try {
                image = jsonData.getJSONObject(i).getJSONObject("question").getString("image");
            } catch (JSONException je) {
                image = "none";
            }
            Question question = new Question(sectionId, questionId, modified, body,
                    choices.toString(), image, responses.toString());
            questionArrayList.add(question); //Add the question created to the list
        }
        return questionArrayList;
    }
}
