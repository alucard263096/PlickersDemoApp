package com.plickers.demo.plickersdemoapp.Objects;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Ron on 3/18/2016.
 *
 * The purpose of this class Choices to have all the details and data related to the individual question
 * in one object
 *
 */
public class Choices implements Parcelable {

    private String body;
    private Boolean correct;

    public Choices(){

    }

    public Choices(String body, Boolean correct){
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


    protected Choices(Parcel in) {
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
    public static final Parcelable.Creator<Choices> CREATOR = new Parcelable.Creator<Choices>() {
        @Override
        public Choices createFromParcel(Parcel in) {
            return new Choices(in);
        }

        @Override
        public Choices[] newArray(int size) {
            return new Choices[size];
        }
    };
}