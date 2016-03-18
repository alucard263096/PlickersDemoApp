package com.plickers.demo.plickersdemoapp.Fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.plickers.demo.plickersdemoapp.Activity.ClassDetails;
import com.plickers.demo.plickersdemoapp.R;

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
    private ListView listView;

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
        addDemoClass(view);
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
                Intent intent = new Intent(context, ClassDetails.class);
                Bundle bundle = new Bundle();
                /*
                I'm using bundle instead of intent.putExtra() to simulate a real world application.
                We might be potentially passing more information than the class name.
                 */
                bundle.putString("className", listView.getItemAtPosition(position).toString());
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });

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

}
