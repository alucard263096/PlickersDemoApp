package com.plickers.demo.plickersdemoapp.Fragment;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.felipecsl.gifimageview.library.GifImageView;
import com.plickers.demo.plickersdemoapp.Objects.Choice;
import com.plickers.demo.plickersdemoapp.Objects.Question;
import com.plickers.demo.plickersdemoapp.R;


import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;

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
    private static final String ARG_CHOICES = "choices";

    // TODO: Rename and change types of parameters
    private Context context;
    private Question selectedQuestion;
    private Choice[] choices;
    private TextView questionData;
    private GifImageView questionImage;

    public QuestionFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param selectedQuestion The question that was selected by the user.
     * @param choices A list of possible answer choices.
     * @return A new instance of fragment QuestionFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static QuestionFragment newInstance(Question selectedQuestion, Choice[] choices) {
        QuestionFragment fragment = new QuestionFragment();
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
        View view = inflater.inflate(R.layout.fragment_question, container, false);

        //Set the question Body
        questionData = (TextView) view.findViewById(R.id.questionTextView);
        questionData.setText(selectedQuestion.getBody());

        //Set the question Image
        questionImage = (GifImageView) view.findViewById(R.id.questionImageView);
        new DownloadImageTask(questionImage)
                .execute(selectedQuestion.getImage());

        questionImage.startAnimation();
        //Add textviews for each of the choices
        LinearLayout linearLayout = (LinearLayout) view.findViewById(R.id.questionLinearLayout);
        addChoices(linearLayout);

        return view;
    }

    /*
    Adds text views for each of the choices that are potential answers to the question
    @param linearLayout The layout which the textviews are appended to
     */
    private void addChoices(LinearLayout linearLayout){
        for( int i = 0; i < choices.length; i++ )
        {
            TextView textView = new TextView(context);

            //Set the color of the textview
            if(choices[i].getCorrect() == null) {} //No color if no answer
            else if(choices[i].getCorrect()){
                //Green if right
                textView.setBackgroundColor(ContextCompat.getColor
                        (context, R.color.colorLightGreen));
            }
            else{
                //Red if wrong
                textView.setBackgroundColor(ContextCompat.getColor(context,R.color.colorLightRed));
            }

            textView.setTextSize(15);
            textView.setPadding(10, 10, 10, 10);
            String answer = choices[i].getLetter() + ": " + choices[i].getBody(); //Ex) A: Answer
            textView.setText(answer);

            linearLayout.addView(textView);
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
    }



    /*
    Class to download the question images
    Sources: http://stackoverflow.com/questions/2471935/how-to-load-an-imageview-by-url-in-android
    https://github.com/felipecsl/GifImageView
     */
    private class DownloadImageTask extends AsyncTask<String, Void, byte[]> {
        GifImageView bmImage;


        private byte[] readBytes(InputStream inputStream) throws IOException {
            // this dynamically extends to take the bytes you read
            ByteArrayOutputStream byteBuffer = new ByteArrayOutputStream();

            // this is storage overwritten on each iteration with bytes
            int bufferSize = 1024;
            byte[] buffer = new byte[bufferSize];

            // we need to know how may bytes were read to write them to the byteBuffer
            int len = 0;
            while ((len = inputStream.read(buffer)) != -1) {
                byteBuffer.write(buffer, 0, len);
            }

            // and then we can return your byte array.
            return byteBuffer.toByteArray();
        }

        public DownloadImageTask(GifImageView bmImage) {
            this.bmImage = bmImage;
        }

        protected byte[] doInBackground(String... urls) {
            String urldisplay = urls[0];
            byte[] bytes = null;
            try {
                InputStream in = new java.net.URL(urldisplay).openStream();
                bytes = readBytes(in);
            } catch (Exception e) {
                Log.e("Error", e.getMessage());
                e.printStackTrace();
            }
            return bytes;
        }

        protected void onPostExecute(byte[] result) {
            try{//Try to set the gif
                bmImage.setBytes(result);
                bmImage.startAnimation();
            }
            catch (ArithmeticException ae){ //If it is not a gif
                Bitmap bitmap = BitmapFactory.decodeByteArray(result , 0, result.length);
                bmImage.setImageBitmap(bitmap);
            }
            catch ( NullPointerException npe ){ //No byte data means no image
                bmImage.setImageBitmap(null);
            }

        }
    }
}
