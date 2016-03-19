package com.plickers.demo.plickersdemoapp.Objects;

import android.content.ContentValues;
import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Ron on 3/18/2016.
 *
 * The purpose of this class is to have all the details and data related to the individual question
 * in one object
 *
 */
public class Question implements Parcelable {

    private String section_id;
    private String question_id;
    private String last_modified;
    private String body;
    private String choices;
    private String image;

    public Question(){

    }

    public Question(String section_id, String question_id, String last_modified, String body,
                    String choices, String image){
        this.section_id = section_id;
        this.question_id = question_id;
        this.last_modified = last_modified;
        this.body = body;
        this.choices = choices;
        this.image = image;
    }

    public String getSection_id(){
        return this.section_id;
    }

    public void setSection_id(String section_id){
        this.section_id = section_id;
    }

    public String getQuestion_id(){
        return this.question_id;
    }

    public void setQuestion_id(String question_id){
        this.question_id = question_id;
    }

    public String getLast_modified(){
        return this.last_modified;
    }

    public void setLast_modified(String last_modified){
        this.last_modified = last_modified;
    }

    public String getBody(){
        return this.body;
    }

    public void setBody(String body){
        this.body = body;
    }

    public String getChoices(){
        return this.choices;
    }

    public void setChoices(String choices){
        this.choices = choices;
    }

    public String getImage(){
        return this.image;
    }

    public void setImage(String image){
        this.image = image;
    }


    protected Question(Parcel in) {
        section_id = in.readString();
        question_id = in.readString();
        last_modified = in.readString();
        body = in.readString();
        choices = in.readString();
        image = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(section_id);
        dest.writeString(question_id);
        dest.writeString(last_modified);
        dest.writeString(body);
        dest.writeString(choices);
        dest.writeString(image);
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<Question> CREATOR = new Parcelable.Creator<Question>() {
        @Override
        public Question createFromParcel(Parcel in) {
            return new Question(in);
        }

        @Override
        public Question[] newArray(int size) {
            return new Question[size];
        }
    };
}