package com.plickers.demo.plickersdemoapp.Fragment;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.plickers.demo.plickersdemoapp.Activity.QuestionDetails;
import com.plickers.demo.plickersdemoapp.Activity.StudentResponseDetails;
import com.plickers.demo.plickersdemoapp.Objects.Choice;
import com.plickers.demo.plickersdemoapp.Objects.Question;
import com.plickers.demo.plickersdemoapp.Objects.Response;
import com.plickers.demo.plickersdemoapp.R;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link ResponsesFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link ResponsesFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ResponsesFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_SELECTED_QUESTION = "selectedQuestion";
    private static final String ARG_CHOICES = "choices";


    // TODO: Rename and change types of parameters
    private Context context;
    private Question selectedQuestion;
    private Choice[] choices;

    private OnFragmentInteractionListener mListener;

    public ResponsesFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param Question selectedQuestion
     * @param Choice[] choices
     * @return A new instance of fragment ResponsesFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ResponsesFragment newInstance(Question selectedQuestion, Choice[] choices) {
        ResponsesFragment fragment = new ResponsesFragment();
        Bundle args = new Bundle();
        args.putParcelable(ARG_SELECTED_QUESTION, selectedQuestion);
        args.putParcelableArray(ARG_CHOICES, choices);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            selectedQuestion = getArguments().getParcelable(ARG_SELECTED_QUESTION);
            Parcelable[] parcelableArray = getArguments().getParcelableArray(ARG_CHOICES);
            choices = Arrays.copyOf(parcelableArray, parcelableArray.length, Choice[].class);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =inflater.inflate(R.layout.fragment_responses, container, false);

        LinearLayout linearLayout = addQuestion(view); //Add question to top of view
        addResponses(view, linearLayout); //add the responses and stats

        return view;
    }

    @NonNull
    private LinearLayout addQuestion(View view) {
        LinearLayout linearLayout = (LinearLayout) view.findViewById(R.id.responsesLinearLayout);
        QuestionFragment questionFragment = new QuestionFragment().newInstance(selectedQuestion, choices);
        /* Display the question in responses tab to avoid having to refer back */
        LinearLayout fragmentLayout = new LinearLayout(context);
        fragmentLayout.setId(View.generateViewId());
        getFragmentManager().beginTransaction().add(fragmentLayout.getId(), questionFragment, "question").commit();
        linearLayout.addView(fragmentLayout, 0); //Add to top of layout
        return linearLayout;
    }

    private void addResponses(View view, LinearLayout linearLayout) {
        Response[] responses = null;
        try{
            responses = Response.parseResponses(selectedQuestion);
        }
        catch (JSONException je){
            Log.e("JSON", je.toString());
        }
        Choice answer = null;
        for(Choice c: choices){
            if(c.getCorrect()){
                answer = c;
            }
        }

        int correctResponses = 0;

        for(Response x: responses){

            TextView textView = new TextView(context);
            if(x.getAnswer() == answer.getLetter()){
                textView.setBackgroundColor(ContextCompat.getColor(context, R.color.colorLightGreen));
                correctResponses++;
            }
            else{
                textView.setBackgroundColor(ContextCompat.getColor(context,R.color.colorLightRed));
            }
            textView.setTextSize(15);
            textView.setPadding(10, 10, 10, 10);
            String studentResponseDetails = x.getStudent() + " - " + x.getAnswer() +
                    " - Card Used: " + x.getCard();
            textView.setText(studentResponseDetails);

            /*
            textView.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    //initiatePopupWindow();
                }
            });
            */
            linearLayout.addView(textView);
        }

        TextView numStudents = (TextView) view.findViewById(R.id.numStudents);
        numStudents.append(String.valueOf(responses.length));

        TextView numCorrect = (TextView) view.findViewById(R.id.numCorrect);
        numCorrect.append(String.valueOf(correctResponses));

        TextView numWrong = (TextView) view.findViewById(R.id.numWrong);
        numWrong.append(String.valueOf(responses.length - correctResponses));
    }


    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        this.context = context;
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }

    /*
    Create popup for Student Details
    Source: http://mobilemancer.com/2011/01/08/popup-window-in-android/
     */
    private PopupWindow pw;
    public void initiatePopupWindow() {
        try {
            //We need to get the instance of the LayoutInflater, use the context of this activity
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            //Inflate the view from a predefined XML layout
            View layout = inflater.inflate(R.layout.activity_student_response_details,
                    (ViewGroup) getView().findViewById(R.id.popup_element));
            // create a 300px width and 470px height PopupWindow
            pw = new PopupWindow(layout);
            pw.setHeight(WindowManager.LayoutParams.WRAP_CONTENT);
            pw.setWidth(WindowManager.LayoutParams.WRAP_CONTENT);
            // display the popup in the center
            pw.showAtLocation(layout, Gravity.CENTER, 0, 0);
            TextView mResultText = (TextView) layout.findViewById(R.id.server_status_text);



        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private View.OnClickListener cancel_button_click_listener = new View.OnClickListener() {
        public void onClick(View v) {
            pw.dismiss();
        }
    };

}
