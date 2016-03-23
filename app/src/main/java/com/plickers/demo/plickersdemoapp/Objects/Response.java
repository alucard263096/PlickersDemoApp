package com.plickers.demo.plickersdemoapp.Objects;

import android.os.Parcel;
import android.os.Parcelable;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;

/**
 * Has all the details related to a question response in one object
 */
public class Response implements Parcelable {

    private String student;
    private char answer;
    private int card;

    public Response() {

    }

    public Response(String student, char answer, int card) {
        this.student = student;
        this.answer = answer;
        this.card = card;
    }

    public String getStudent() {
        return this.student;
    }

    public void setStudent(String student) {
        this.student = student;
    }

    public char getAnswer() {
        return this.answer;
    }

    public void setAnswer(char answer) {
        this.answer = answer;
    }

    public int getCard() {
        return this.card;
    }

    public void setCard(int card) {
        this.card = card;
    }

    /**
     * Parses the JSON file and returns back an array of the responses
     *
     * @param selectedQuestion  The question the user had selected
     * @return Array of the Responses parsed from JSON
     * @throws JSONException
     */
    public static Response[] parseResponses(Question selectedQuestion) throws JSONException {
        JSONArray responses = new JSONArray(selectedQuestion.getResponses());
        ArrayList<Response> responseArrayList = new ArrayList<>();
        for (int i = 0; i < responses.length(); i++) {
            //Get the Student name(email)
            String student = responses.getJSONObject(i).getString("student");

            //Get the answer recorded
            String answer = responses.getJSONObject(i).getString("answer");
            char answerchar = ' '; //No answer was recorded
            if (answer.length() == 1) {
                answerchar = answer.charAt(0);
            }

            //Get the card used
            int card = responses.getJSONObject(i).getInt("card");
            responseArrayList.add(new Response(student, answerchar, card));
        }
        return responseArrayList.toArray(new Response[responseArrayList.size()]);
    }

    protected Response(Parcel in) {
        student = in.readString();
        answer = (char) in.readValue(char.class.getClassLoader());
        card = in.readInt();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(student);
        dest.writeValue(answer);
        dest.writeInt(card);
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<Response> CREATOR = new Parcelable.Creator<Response>() {
        @Override
        public Response createFromParcel(Parcel in) {
            return new Response(in);
        }

        @Override
        public Response[] newArray(int size) {
            return new Response[size];
        }
    };
}