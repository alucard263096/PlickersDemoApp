package com.plickers.demo.plickersdemoapp.Activity;

import android.app.ProgressDialog;
import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.support.design.widget.TabLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.TextView;

import com.plickers.demo.plickersdemoapp.Fragment.QuestionListFragment;
import com.plickers.demo.plickersdemoapp.Helper.DatabaseHandler;
import com.plickers.demo.plickersdemoapp.Objects.Question;
import com.plickers.demo.plickersdemoapp.R;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/*
The majority of this class remains untouched from the original Android template. Any changes
have been commentated.
 */

public class ClassDetails extends AppCompatActivity {


    //private DatabaseHandler databaseHandler;
    private ArrayList<Question> questions;

    /**
     * The {@link android.support.v4.view.PagerAdapter} that will provide
     * fragments for each of the sections. We use a
     * {@link FragmentPagerAdapter} derivative, which will keep every
     * loaded fragment in memory. If this becomes too memory intensive, it
     * may be best to switch to a
     * {@link android.support.v4.app.FragmentStatePagerAdapter}.
     */
    private SectionsPagerAdapter mSectionsPagerAdapter;

    /**
     * The {@link ViewPager} that will host the section contents.
     */
    private ViewPager mViewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_class_details);

        questions = new ArrayList<>();
        /*
        Receive data from class list selection
         */
        Bundle bundle = getIntent().getExtras();
        String className = bundle.getString("className");

        //Set the toolbar to the class name
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        TextView toolbarTitle = (TextView) findViewById(R.id.toolbar_title); //Toolbar title
        toolbarTitle.setText(className);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false); //Hide the default toolbar title

        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(mViewPager);

        retrieveClassroomData();
    }

    private void retrieveClassroomData(){
        /*Potentially have a parameter for CourseID(section) and retrieve the JSON that corresponds
        to the correct CourseID(section)
         */
        //this.deleteDatabase("classroom_data");
        //databaseHandler = new DatabaseHandler(this);

        String linkToClassroom = "http://plickers-interview.herokuapp.com/polls";
        new retrieveClassroomJSON().execute(linkToClassroom);

    }

    /*
    JSON retrieval task that is run in the background
     */
    private class retrieveClassroomJSON extends AsyncTask<String, Void, JSONArray> {
        private Exception exception;
        private ProgressDialog pDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(ClassDetails.this);
            pDialog.setMessage("Getting Data ...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.show();

        }

        /*
        Instead of creating an independent JSON Parser, I did the parsing in the doInBackground
        and in onPostExecute methods so it is easier to see how the logic is taking place
         */
        protected JSONArray doInBackground(String... urls) {
            InputStream inputStream = null;
            String result = null;
            HttpURLConnection urlConnection;
            try {
                URL url = new URL(urls[0]);
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

        protected void onPostExecute(JSONArray jsonArray) {
            try {
                JSONArray jsonData = jsonArray;
                for (int i = 0; i < jsonData.length(); i++) {
                    String sectionId = jsonData.getJSONObject(i).getString("section");
                    String questionId = jsonData.getJSONObject(i).getJSONObject("question").getString("id");
                    String modified;
                    //If there is no modified, the last touched date is created
                    try {
                        modified = jsonData.getJSONObject(i).getJSONObject("question").
                                getString("modified");//get modified if it exists
                    } catch (JSONException je) {
                        modified = jsonData.getJSONObject(i).getJSONObject("question").
                                getString("created");//if it wasn't modified get created
                    }
                    String body = jsonData.getJSONObject(i).getJSONObject("question").getString("body");
                    JSONArray choices = jsonData.getJSONObject(i).getJSONObject("question").getJSONArray("choices"); //Will have to convert to String
                    String image = "none";
                    //If there is a image, store the link, else store "none"
                    try {
                        image = jsonData.getJSONObject(i).getJSONObject("question").getString("image");
                    } catch (JSONException je) {
                        image = "none";
                    }

                    questions.add(new Question(sectionId, questionId, modified, body,
                            choices.toString(), image));

                    //databaseHandler.addQuestion(new Question
                    //      (sectionId, questionId, modified, body, choices.toString(), image));

                }

                pDialog.dismiss();

                QuestionListFragment questionListFragment = (QuestionListFragment) getSupportFragmentManager()
                        .findFragmentByTag("QuestionListFragment");
                if (questionListFragment != null) {
                    System.out.println("ITS NOT NULL");
                    questionListFragment.refreshData(questions);
                }

                // Reading all contacts
                //Log.d("Reading: ", "Reading all contacts..");
                //List<Question> questions = databaseHandler.getAllQuestions();

                //for (Question question : questions) {
                //String log = "Id: "+question.getQuestion_id()+" ,Body: " + question.getBody();
                //Log.d("QUESTION: ", log);
                // }


            } catch (Exception e) {
                     Log.e("ERROR", "getJsonInfo: ", e);
                }
        }
    }


    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment {
        /**
         * The fragment argument representing the section number for this
         * fragment.
         */
        private static final String ARG_SECTION_NUMBER = "section_number";

        public PlaceholderFragment() {
        }

        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
        public static PlaceholderFragment newInstance(int sectionNumber) {
            PlaceholderFragment fragment = new PlaceholderFragment();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            fragment.setArguments(args);
            return fragment;
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_class_details, container, false);
            TextView textView = (TextView) rootView.findViewById(R.id.section_label);
            textView.setText(getString(R.string.section_format, getArguments().getInt(ARG_SECTION_NUMBER)));
            return rootView;
        }
    }

    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            // getItem is called to instantiate the fragment for the given page.
            // Return a PlaceholderFragment (defined as a static inner class below).
            if(position == 0){
                return QuestionListFragment.newInstance(questions);
            }
            return PlaceholderFragment.newInstance(position + 1);
        }

        @Override
        public int getCount() {
            // Show 3 total pages.
            return 3;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                    return "SECTION 1";
                case 1:
                    return "SECTION 2";
                case 2:
                    return "SECTION 3";
            }
            return null;
        }
    }
}
