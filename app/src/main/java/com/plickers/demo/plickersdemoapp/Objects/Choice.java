package com.plickers.demo.plickersdemoapp.Objects;

import android.os.Parcel;
import android.os.Parcelable;

import org.json.JSONArray;
import org.json.JSONException;

/**
 * Created by Ron on 3/18/2016.
 *
 * The purpose of this class Choice to have all the details and data related to the individual question
 * in one object
 *
 */
public class Choice implements Parcelable {

    private String body;
    private Boolean correct;
    private char letter;

    public Choice(){

    }

    public Choice(String body, Boolean correct){
        this.body = body;
        this.correct = correct;
    }

    public String getBody(){
        return this.body;
    }

    public void setBody(String body){
        this.body = body;
    }

    public Boolean getCorrect(){
        return this.correct;
    }

    public void setCorrect(Boolean correct){
        this.correct = correct;
    }

    public char getLetter(){
        return this.letter;
    }

    public void setLetter(char letter){
        this.letter = letter;
    }

    public static Choice[] parseChoices(Question selectedQuestion) throws JSONException {
        JSONArray choices = new JSONArray(selectedQuestion.getChoices());
        Choice[] choicesArray = new Choice[choices.length()];
        for(int i = 0; i<choices.length(); i++){
            String body = choices.getJSONObject(i).getString("body");
            Boolean answer = choices.getJSONObject(i).getBoolean("correct");
            choicesArray[i] = new Choice(body,answer);
        }
        for(int i = 0; i<choicesArray.length; i++){
            int choice = 'A' + i;
            choicesArray[i].setLetter((char)choice);
        }
        return choicesArray;
    }

    protected Choice(Parcel in) {
        body = in.readString();
        byte correctVal = in.readByte();
        correct = correctVal == 0x02 ? null : correctVal != 0x00;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(body);
        if (correct == null) {
            dest.writeByte((byte) (0x02));
        } else {
            dest.writeByte((byte) (correct ? 0x01 : 0x00));
        }
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<Choice> CREATOR = new Parcelable.Creator<Choice>() {
        @Override
        public Choice createFromParcel(Parcel in) {
            return new Choice(in);
        }

        @Override
        public Choice[] newArray(int size) {
            return new Choice[size];
        }
    };
}