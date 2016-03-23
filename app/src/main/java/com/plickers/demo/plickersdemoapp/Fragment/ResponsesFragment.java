package com.plickers.demo.plickersdemoapp.Fragment;

import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.plickers.demo.plickersdemoapp.Objects.Choice;
import com.plickers.demo.plickersdemoapp.Objects.Question;
import com.plickers.demo.plickersdemoapp.Objects.Response;
import com.plickers.demo.plickersdemoapp.R;

import org.json.JSONException;

import java.util.Arrays;

/**
 * Displays the class details pertaining to a question.
 * A simple {@link Fragment} subclass.
 * Use the {@link ResponsesFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ResponsesFragment extends Fragment {
    private static final String ARG_SELECTED_QUESTION = "selectedQuestion";
    private static final String ARG_CHOICES = "choices";

    private Context mContext;
    private Question mSelectedQuestion;
    private Choice[] mChoices;

    public ResponsesFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param selectedQuestion The question selected by the user
     * @param choices          The list of choices for the question
     * @return A new instance of fragment ResponsesFragment.
     */
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
            mSelectedQuestion = getArguments().getParcelable(ARG_SELECTED_QUESTION);
            Parcelable[] parcelableArray = getArguments().getParcelableArray(ARG_CHOICES);
            mChoices = Arrays.copyOf(parcelableArray, parcelableArray.length, Choice[].class);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_responses, container, false);

        LinearLayout linearLayout = addQuestion(view); //Add question to top of view
        addResponses(view, linearLayout); //add the responses and stats

        return view;
    }

    /**
     * Adds the question and the answers to the layout so the user has easy reference
     *
     * @param view
     * @return LinearLayout that is added to the beginning of the fragment_responses
     */
    @NonNull
    private LinearLayout addQuestion(View view) {
        LinearLayout linearLayout = (LinearLayout) view.findViewById(R.id.responsesLinearLayout);
        QuestionFragment questionFragment = new QuestionFragment().newInstance
                (mSelectedQuestion, mChoices);
        /* Display the question in responses tab to avoid having to refer back */
        LinearLayout fragmentLayout = new LinearLayout(mContext);
        fragmentLayout.setId(View.generateViewId());
        getFragmentManager().beginTransaction().add(fragmentLayout.getId(),
                questionFragment, "question").commit();
        linearLayout.addView(fragmentLayout, 0); //Add to top of layout
        return linearLayout;
    }

    /**
     * Finds the correct answer out of all the choices
     *
     * @return A Choice object that is the correct choice
     */
    private Choice findAnswer() {
        /*
            Assign null if no correct value is given
        */
        for (Choice c : mChoices) {
            if (c.getCorrect() == null) {
                return c;
            } else if (c.getCorrect()) {
                return c;
            }
        }
        return null;
    }

    /**
     * Calculates all the statistics of the class' students and adds them to the layout
     *
     * @param view         The view that contains TextViews we append the stats to
     * @param linearLayout The LinearLayout that we append new textViews to
     */
    private void addResponses(View view, LinearLayout linearLayout) {
        Response[] responses = null;
        try {
            responses = Response.parseResponses(mSelectedQuestion);
        } catch (JSONException je) {
            Log.e("JSON", je.toString());
        }


        Choice answer = findAnswer(); //Get the answer

        int correctResponses = 0;
        int aResponses = 0;
        int bResponses = 0;
        int cResponses = 0;
        int dResponses = 0;

        for (Response x : responses) {
            TextView textView = new TextView(mContext);
            final char studentAnswer = x.getAnswer();
            //Count the number of each answer
            switch (studentAnswer) {
                case 'A':
                    aResponses++;
                    break;
                case 'B':
                    bResponses++;
                    break;
                case 'C':
                    cResponses++;
                    break;
                case 'D':
                    dResponses++;
                    break;
                default:
                    break;
            }

            //Set the color of the answers; no color if there is no answer
            if (answer.getCorrect() == null) {
            } else {
                if (studentAnswer == answer.getLetter()) {
                    textView.setBackgroundColor(ContextCompat.getColor
                            (mContext, R.color.colorLightGreen));
                    correctResponses++;
                } else {
                    textView.setBackgroundColor(ContextCompat.getColor
                            (mContext, R.color.colorLightRed));
                }
            }

            textView.setTextSize(15);
            textView.setPadding(10, 10, 10, 10);
            final String studentName = x.getStudent();
            final int studentCard = x.getCard();
            String studentResponseDetails = studentName + " - " + studentAnswer;
            textView.setText(studentResponseDetails);
            textView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    initiatePopupWindow(studentName, studentAnswer, studentCard);
                }
            });

            linearLayout.addView(textView);
        }

        setClassStats(view, responses, answer, correctResponses, aResponses,
                bResponses, cResponses, dResponses);

    }

    /**
     * Calculates and sets the class stats in the view
     *
     * @param view             The view that contains the TextViews we append data to
     * @param responses        The student responses
     * @param answer           The correct answer
     * @param correctResponses The number of correct answers
     * @param aResponses       The number of a's
     * @param bResponses       The number of b's
     * @param cResponses       The number of c's
     * @param dResponses       The number of d's
     */
    private void setClassStats(View view, Response[] responses, Choice answer,
                               int correctResponses, int aResponses, int bResponses,
                               int cResponses, int dResponses) {
        TextView numStudents = (TextView) view.findViewById(R.id.numStudents);
        numStudents.append(String.valueOf(responses.length));

        if (answer.getCorrect() == null) {
        } else {
            TextView numCorrect = (TextView) view.findViewById(R.id.numCorrect);
            numCorrect.append(String.valueOf(correctResponses));

            TextView numWrong = (TextView) view.findViewById(R.id.numWrong);
            numWrong.append(String.valueOf(responses.length - correctResponses));

            //Calculate the percentage
            TextView percentageRight = (TextView) view.findViewById(R.id.percentRight);
            double tempPercentageRight=(double) correctResponses / (double) responses.length * 100;
            percentageRight.append(String.valueOf(Math.round(tempPercentageRight)) + "%");

            TextView numA = (TextView) view.findViewById(R.id.numA);
            numA.append(String.valueOf(aResponses));

            TextView numB = (TextView) view.findViewById(R.id.numB);
            numB.append(String.valueOf(bResponses));

            TextView numC = (TextView) view.findViewById(R.id.numC);
            numC.append(String.valueOf(cResponses));

            TextView numD = (TextView) view.findViewById(R.id.numD);
            numD.append(String.valueOf(dResponses));
        }
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
    Create popup for Student Details
    Source: http://mobilemancer.com/2011/01/08/popup-window-in-android/
     */
    private PopupWindow mPopUpWindow;

    public void initiatePopupWindow(String studentName, char studentAnswer, int studentCard) {
        try {
            //We need to get the instance of the LayoutInflater, use the context of this activity
            LayoutInflater inflater = (LayoutInflater) mContext.getSystemService
                    (Context.LAYOUT_INFLATER_SERVICE);
            //Inflate the view from a predefined XML layout
            View layout = inflater.inflate(R.layout.activity_student_response_details,
                    (ViewGroup) getView().findViewById(R.id.popup_element));
            // create a 300px width and 470px height PopupWindow
            mPopUpWindow = new PopupWindow(layout);
            mPopUpWindow.setBackgroundDrawable(new BitmapDrawable(null, ""));
            mPopUpWindow.setOutsideTouchable(true);

            TextView nameTextView = (TextView) layout.findViewById(R.id.studentPopUp);
            nameTextView.append(studentName);

            TextView answerTextView = (TextView) layout.findViewById(R.id.answerPopUp);
            String curText = answerTextView.getText().toString();
            answerTextView.setText(curText + studentAnswer);

            TextView cardTextView = (TextView) layout.findViewById(R.id.cardPopUp);
            cardTextView.append(String.valueOf(studentCard));


            mPopUpWindow.setHeight(WindowManager.LayoutParams.WRAP_CONTENT);
            mPopUpWindow.setWidth(WindowManager.LayoutParams.WRAP_CONTENT);
            // display the popup in the center
            mPopUpWindow.showAtLocation(layout, Gravity.CENTER, 0, 0);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}
