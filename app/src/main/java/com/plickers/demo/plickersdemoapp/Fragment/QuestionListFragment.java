package com.plickers.demo.plickersdemoapp.Fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.plickers.demo.plickersdemoapp.Activity.QuestionDetails;
import com.plickers.demo.plickersdemoapp.Adapters.QuestionAdapter;
import com.plickers.demo.plickersdemoapp.Objects.Question;
import com.plickers.demo.plickersdemoapp.R;

import java.util.ArrayList;
import java.util.Collections;

/**
 * Displays a list of questions to the user which can be sorted by body or by date
 * A simple {@link Fragment} subclass.
 * Use the {@link QuestionListFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class QuestionListFragment extends Fragment {

    private static final String QUESTION_LIST = "questionlist";
    private static final String ARG_SELECTED_QUESTION = "selectedQuestion";

    private ArrayList<Question> mQuestionList;
    private QuestionAdapter mQuestionAdapter;
    private ListView mListView;
    private Context mContext;

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

        setHasOptionsMenu(true); //Set the options menu

        if (getArguments() != null) {
            mQuestionList = getArguments().getParcelableArrayList(QUESTION_LIST);
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_main, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_datesort:
                sortbyDate(mQuestionList);
                return true;
            case R.id.action_bodysort:
                sortbyBody(mQuestionList);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_question_list, container, false);
        //Add the questions
        addQuestionList(view);
        return view;
    }

    /**
     * Adds the list of questions to the layout so that the user can see the question bodies and
     * click on one of the questions to go into more detail
     *
     * @param view The view that has the questionListView
     */
    private void addQuestionList(View view) {
        mListView = (ListView) view.findViewById(R.id.questionListView);
        mQuestionAdapter = new QuestionAdapter(mContext, R.layout.question_list_item,
                mQuestionList);
        mListView.setAdapter(mQuestionAdapter);

        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Question selectedQuestion = (Question) mListView.getItemAtPosition(position);
                Intent intent = new Intent(mContext, QuestionDetails.class);
                intent.putExtra(ARG_SELECTED_QUESTION, selectedQuestion);
                startActivity(intent);
            }
        });
    }

    /**
     * Sorts the questions by date
     *
     * @param questions The list of questions
     */
    private void sortbyDate(ArrayList<Question> questions) {
        Collections.sort(questions, Question.questionDateComparator);
        mQuestionAdapter.sortData(questions);
    }

    /**
     * Sorts the questions by Body
     *
     * @param questions The list of questions
     */
    private void sortbyBody(ArrayList<Question> questions) {
        Collections.sort(questions, Question.questionBodyComparator);
        mQuestionAdapter.sortData(questions);
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


}
