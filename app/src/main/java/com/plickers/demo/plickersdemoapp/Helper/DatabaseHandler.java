package com.plickers.demo.plickersdemoapp.Helper;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.plickers.demo.plickersdemoapp.Objects.Question;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Admin on 3/18/2016.
 */
public class DatabaseHandler extends SQLiteOpenHelper {

    // Database Version
    private static final int DATABASE_VERSION = 1;
    // Database Name
    private static final String DATABASE_NAME = "classroom_data";
    // Question table name
    private static final String TABLE_QUESTION = "question";
    // Responses table name
    private static final String TABLE_RESPONSE = "response";

    // Question Table Columns names
    private static final String QUESTION_SECTION_ID = "section_id";
    private static final String QUESTION_QUESTION_ID = "question_id";
    private static final String QUESTION_LAST_MODIFIED = "last_modified";
    private static final String QUESTION_BODY = "body";
    private static final String QUESTION_CHOICES = "choices";
    private static final String QUESTION_IMAGE = "image";
    // Response Table Columns names
    private static final String RESPONSE_SECTION_ID = "section_id";
    private static final String RESPONSE_QUESTION_ID = "question_id";
    private static final String RESPONSE_STUDENT = "student";
    private static final String RESPONSE_ANSWER = "answer";
    private static final String RESPONSE_CARD = "card";

    public DatabaseHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    // Creating Tables
    @Override
    public void onCreate(SQLiteDatabase db) {
        //Create the question table
        String CREATE_QUESTION_TABLE = "CREATE TABLE IF NOT EXISTS question" +
                "( section_id TEXT , question_id TEXT , last_modified TEXT ," +
                " body TEXT , choices TEXT , image TEXT );";
        db.execSQL(CREATE_QUESTION_TABLE);
        //Create the response table
        String CREATE_RESPONSE_TABLE = "CREATE TABLE IF NOT EXISTS response" +
                "( section_id TEXT , question_id TEXT , student TEXT , answer TEXT , card TEXT );";
        db.execSQL(CREATE_RESPONSE_TABLE);
    }

    // Upgrading database
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_QUESTION);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_RESPONSE);
        // Create tables again
        onCreate(db);
    }


    public void addQuestion(Question question){
        SQLiteDatabase schoolDatabase = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(QUESTION_SECTION_ID, question.getSection_id());
        values.put(QUESTION_QUESTION_ID, question.getQuestion_id());
        values.put(QUESTION_LAST_MODIFIED, question.getLast_modified());
        values.put(QUESTION_BODY, question.getBody());
        values.put(QUESTION_CHOICES, question.getChoices());
        values.put(QUESTION_IMAGE, question.getImage());

        schoolDatabase.insert("question", null, values);
        schoolDatabase.close();
    }

    public Question getQuestion(String question_id){
        SQLiteDatabase schoolDatabase = this.getReadableDatabase();

        Cursor cursor = schoolDatabase.query(TABLE_QUESTION, new String[] { QUESTION_SECTION_ID,
                        QUESTION_QUESTION_ID, QUESTION_LAST_MODIFIED, QUESTION_BODY,
                        QUESTION_CHOICES, QUESTION_IMAGE }, QUESTION_QUESTION_ID + "=?",
                new String[] { question_id }, null, null, null, null);
        if (cursor != null)
            cursor.moveToFirst();

        Question question = new Question(cursor.getString(0), cursor.getString(1),
                cursor.getString(2), cursor.getString(3), cursor.getString(4), cursor.getString(5));

        return question;
    }

    public List<Question> getAllQuestions(){
        List<Question> questionList = new ArrayList<Question>();
        // Select All Query
        String selectQuery = "SELECT  * FROM " + TABLE_QUESTION;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                Question question = new Question();
                question.setSection_id(cursor.getString(0));
                question.setQuestion_id(cursor.getString(1));
                question.setLast_modified(cursor.getString(2));
                question.setBody(cursor.getString(3));
                question.setChoices(cursor.getString(4));
                question.setImage(cursor.getString(5));
                questionList.add(question);
            } while (cursor.moveToNext());
        }

        return questionList;
    }

    public int getQuestionCount(){
        String countQuery = "SELECT  * FROM " + TABLE_QUESTION;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        cursor.close();
        // return count
        return cursor.getCount();
    }

    public int updateQuestion(Question question){
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(QUESTION_SECTION_ID, question.getSection_id());
        values.put(QUESTION_QUESTION_ID, question.getQuestion_id());
        values.put(QUESTION_LAST_MODIFIED, question.getLast_modified());
        values.put(QUESTION_BODY, question.getBody());
        values.put(QUESTION_CHOICES, question.getChoices());
        values.put(QUESTION_IMAGE, question.getImage());

        return db.update(TABLE_QUESTION, values, QUESTION_QUESTION_ID + " = ?",
                new String[] { question.getQuestion_id() });
    }

    public void deleteQuestion(Question question){
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_QUESTION, QUESTION_QUESTION_ID + " = ?",
                new String[] { question.getQuestion_id() });
        db.close();
    }

}
