package com.plickers.demo.plickersdemoapp.Fragment;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.plickers.demo.plickersdemoapp.Activity.ClassDetails;
import com.plickers.demo.plickersdemoapp.Helper.JSONParser;
import com.plickers.demo.plickersdemoapp.Helper.JSONReader;
import com.plickers.demo.plickersdemoapp.Objects.Question;
import com.plickers.demo.plickersdemoapp.R;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;


/**
 * Displays a list of classes to the user for him/her to select and decide which course
 * to explore. For the sake of this demo, only one class "Demo 101" was added.
 * Components of this class remains untouched from the original Android template.
 * Any changes have been commentated.
 * A simple {@link Fragment} subclass.
 * Use the {@link ClassSelectionFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ClassSelectionFragment extends Fragment {
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_CLASSNAME = "className";
    private static final String ARG_QUESTIONS = "questions";

    private String mClassName;
    private ListView mListView;

    private Context mContext;

    public ClassSelectionFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment ClassSelectionFragment.
     */
    public static ClassSelectionFragment newInstance() {
        ClassSelectionFragment fragment = new ClassSelectionFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_class_selection, container, false);
        addDemoClass(view); //Add the hard coded data
        return view;
    }

    /*
    Adds the hard coded Demo 101 class to the list and sets the listView onClickListener
    to launch into the appropriate activity
    @param view View that has the classListView list view
     */
    private void addDemoClass(View view) {
        mListView = (ListView) view.findViewById(R.id.classListView);

        //Add only one class Demo 101 to the list of class
        ArrayList<String> classList = new ArrayList<>();
        classList.add("Demo 101");

        //Set adapter to custom adapter
        ArrayAdapter<String> classArrayAdapter = new ArrayAdapter<>(
                mContext, android.R.layout.simple_list_item_1, classList);
        mListView.setAdapter(classArrayAdapter);

        /*
        Potential logic concerning which class we would want to go into more depth about would be
        done here.
         */
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                mClassName = mListView.getItemAtPosition(position).toString();
                String linkToClassroom = "http://plickers-interview.herokuapp.com/polls";
                new retrieveClassroomJSON().execute(linkToClassroom);
            }
        });
    }

    /*
    Launches into the ClassDetails activity where the user can interact with the data
    I'm using bundle instead of intent.putExtra() to simulate a real world application.
    We might be potentially passing more information than the class name and questions.
    @param questionArrayList    Arraylist that has all of the questions retrieved
    */
    private void launchDetailsActivity(ArrayList<Question> questionArrayList) {
        Intent intent = new Intent(mContext, ClassDetails.class);
        Bundle bundle = new Bundle();
        bundle.putString(ARG_CLASSNAME, this.mClassName);
        bundle.putParcelableArrayList(ARG_QUESTIONS, questionArrayList);
        intent.putExtras(bundle);
        startActivity(intent);
    }

    @Override
    public void onAttach(Context context) {
        this.mContext = context;
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }


    /*
    Retrieves the JSON by running a task in the background
    */
    private class retrieveClassroomJSON extends AsyncTask<String, Void, JSONArray> {
        private ProgressDialog mProgressDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            //Stop User Interaction while loading
            mProgressDialog = new ProgressDialog(mContext);
            mProgressDialog.setMessage("Getting Data ...");
            mProgressDialog.setIndeterminate(false);
            mProgressDialog.setCancelable(true);
            mProgressDialog.show();

        }

        protected JSONArray doInBackground(String... urls) {
            //Read the json
            JSONReader jsonReader = new JSONReader();
            return jsonReader.readArray(urls[0]);
        }

        protected void onPostExecute(JSONArray jsonArray) {
            try {
                //Parse the json
                JSONParser jsonParser = new JSONParser();
                ArrayList<Question> questionArrayList = jsonParser.parseJSONfile(jsonArray);

                //Go to the next activity with the received questions
                mProgressDialog.dismiss(); //Remove the loading screen
                launchDetailsActivity(questionArrayList);
            } catch (JSONException je) {
                Log.e("ERROR", "getJsonInfo: ", je);
            }
        }
    }

}
