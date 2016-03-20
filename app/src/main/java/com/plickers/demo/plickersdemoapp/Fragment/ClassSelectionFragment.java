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


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ClassSelectionFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ClassSelectionFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private String className;
    private ListView listView;
    private ArrayList<Question> questionArrayList;

    // TODO: Rename and change types of parameters
    private Context context;
    private String mParam1;
    private String mParam2;

    public ClassSelectionFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment ClassSelectionFragment.
     */
    // TODO: Rename and change types and number of parameters
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
    Method: addDemoClass(View view)
    Description: Adds the hard coded Demo 101 class to the list and sets the listView onClickListener
    to launch into the appropriate activity
     */
    private void addDemoClass(View view){
        listView = (ListView) view.findViewById(R.id.classListView);
        ArrayList<String> classList = new ArrayList<>();
        classList.add("Demo 101");
        ArrayAdapter<String> classArrayAdapter = new ArrayAdapter<>(
                context, android.R.layout.simple_list_item_1, classList);
        listView.setAdapter(classArrayAdapter);
        /*
        Logic concerning which class we would want to go into more depth about would be done here
         */
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                className = listView.getItemAtPosition(position).toString();
                String linkToClassroom = "http://plickers-interview.herokuapp.com/polls";
                new retrieveClassroomJSON().execute(linkToClassroom);
            }
        });
    }

    /*
    Method: launchDetailsActivity()
    Description: Launches into the ClassDetails activity where the user can interact with the data
    I'm using bundle instead of intent.putExtra() to simulate a real world application.
    We might be potentially passing more information than the class name and questions.
    */
    private void launchDetailsActivity(){
        Intent intent = new Intent(context, ClassDetails.class);
        Bundle bundle = new Bundle();
        bundle.putString("className", className);
        bundle.putParcelableArrayList("questions", questionArrayList);
        intent.putExtras(bundle);
        startActivity(intent);
    }

    @Override
    public void onAttach(Context context) {
        this.context = context;
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        super.onDetach();
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
            pDialog  = new ProgressDialog(context);
            pDialog.setMessage("Getting Data ...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.show();

        }
        protected JSONArray doInBackground(String... urls) {
            JSONParser jsonParser = new JSONParser();
            return jsonParser.parseArray(urls[0]);
        }

        protected void onPostExecute(JSONArray jsonArray) {
            try {
                questionArrayList = new ArrayList<>();
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
                    Question question = new Question(sectionId, questionId, modified, body,
                            choices.toString(), image);
                    questionArrayList.add(question);
                }
                pDialog.dismiss();
                launchDetailsActivity();
            } catch (Exception e) {
                Log.e("ERROR", "getJsonInfo: ", e);
            }
        }
    }

}
