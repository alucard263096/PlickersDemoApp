package com.plickers.demo.plickersdemoapp.Fragment;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import com.plickers.demo.plickersdemoapp.Adapters.QuestionAdapter;
import com.plickers.demo.plickersdemoapp.Objects.Question;
import com.plickers.demo.plickersdemoapp.R;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link QuestionListFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link QuestionListFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class QuestionListFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String QUESTION_LIST = "questionlist";


    // TODO: Rename and change types of parameters
    private ArrayList<Question> questionList;
    private QuestionAdapter questionAdapter;
    private ListView listView;

    private Context context;

    private OnFragmentInteractionListener mListener;

    public QuestionListFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param questionArrayList questionList
     * @return A new instance of fragment QuestionListFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static QuestionListFragment newInstance(ArrayList<Question> questionArrayList) {
        QuestionListFragment fragment = new QuestionListFragment();
        Bundle args = new Bundle();
        args.putParcelableArrayList(QUESTION_LIST, questionArrayList);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            questionList = getArguments().getParcelableArrayList(QUESTION_LIST);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_question_list, container, false);
        addQuestionList(view);
        return view;
    }

    private void addQuestionList(View view){
        listView = (ListView) view.findViewById(R.id.questionListView);
        questionAdapter = new QuestionAdapter(context, R.layout.list_item, questionList);
        listView.setAdapter(questionAdapter);
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

    public void refreshData(ArrayList<Question> data) {
        questionList = data;
        questionAdapter.notifyDataSetChanged();
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
}
