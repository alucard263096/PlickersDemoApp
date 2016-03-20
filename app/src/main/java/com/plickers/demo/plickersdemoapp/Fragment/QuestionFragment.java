package com.plickers.demo.plickersdemoapp.Fragment;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.plickers.demo.plickersdemoapp.Objects.Choices;
import com.plickers.demo.plickersdemoapp.Objects.Question;
import com.plickers.demo.plickersdemoapp.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Console;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link QuestionFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link QuestionFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class QuestionFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_SELECTED_QUESTION = "selectedQuestion";

    // TODO: Rename and change types of parameters
    private Context context;
    private Question selectedQuestion;
    private TextView questionData;
    private ImageView questionImage;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public QuestionFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment QuestionFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static QuestionFragment newInstance(Question selectedQuestion) {
        QuestionFragment fragment = new QuestionFragment();
        Bundle args = new Bundle();
        args.putParcelable(ARG_SELECTED_QUESTION, selectedQuestion);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            selectedQuestion = getArguments().getParcelable(ARG_SELECTED_QUESTION);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_question, container, false);
        //Set the question Body
        questionData = (TextView) view.findViewById(R.id.questionTextView);
        questionData.setText(selectedQuestion.getBody());
        //Set the question Image
        questionImage = (ImageView) view.findViewById(R.id.questionImageView);
        new DownloadImageTask(questionImage)
                .execute(selectedQuestion.getImage());
        Choices[] answerArray = null;
        try{
            answerArray = parseChoices();
        }
        catch (JSONException je){
            Log.e("LOG", je.toString());
        }
        LinearLayout linearLayout = (LinearLayout) view.findViewById(R.id.linearLayout);

        for( int i = 0; i < answerArray.length; i++ )
        {
            TextView textView = new TextView(context);
            if(answerArray[i].getCorrect()){
                textView.setBackgroundColor(ContextCompat.getColor(context, R.color.colorLightGreen));
            }
            else{
                textView.setBackgroundColor(ContextCompat.getColor(context,R.color.colorLightRed));
            }
            textView.setTextSize(30);
            textView.setPadding(10,10,10,10);
            textView.setText(answerArray[i].getBody());
            linearLayout.addView(textView);
        }




        return view;
    }


    private Choices[] parseChoices() throws JSONException{
        JSONArray choices = new JSONArray(selectedQuestion.getChoices());
        ArrayList<Choices> answerChoices = new ArrayList<>();
        for(int i = 0; i<choices.length(); i++){
            String body = choices.getJSONObject(i).getString("body");
            Boolean answer = choices.getJSONObject(i).getBoolean("correct");
            answerChoices.add(new Choices(body,answer));
        }
        return answerChoices.toArray(new Choices[answerChoices.size()]);
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
    Class to download the question images
    Source: http://stackoverflow.com/questions/2471935/how-to-load-an-imageview-by-url-in-android
     */
    private class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {
        ImageView bmImage;

        public DownloadImageTask(ImageView bmImage) {
            this.bmImage = bmImage;
        }

        protected Bitmap doInBackground(String... urls) {
            String urldisplay = urls[0];
            Bitmap mIcon11 = null;
            try {
                InputStream in = new java.net.URL(urldisplay).openStream();
                mIcon11 = BitmapFactory.decodeStream(in);
            } catch (Exception e) {
                Log.e("Error", e.getMessage());
                e.printStackTrace();
            }
            return mIcon11;
        }

        protected void onPostExecute(Bitmap result) {
            bmImage.setImageBitmap(result);
        }
    }
}
