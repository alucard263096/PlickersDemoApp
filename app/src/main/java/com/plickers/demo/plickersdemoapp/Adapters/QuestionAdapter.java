package com.plickers.demo.plickersdemoapp.Adapters;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.plickers.demo.plickersdemoapp.Objects.Question;
import com.plickers.demo.plickersdemoapp.R;

import java.util.ArrayList;

/**
 * Created by Admin on 3/19/2016.
 */
public class QuestionAdapter extends ArrayAdapter<Question> {

    private final Context context;
    private final ArrayList<Question> data;
    private final int layoutResourceId;

    public QuestionAdapter(Context context, int layoutResourceId, ArrayList<Question> data) {
        super(context, layoutResourceId, data);
        this.context = context;
        this.data = data;
        this.layoutResourceId = layoutResourceId;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View row = convertView;
        ViewHolder holder = null;

        if(row == null)
        {
            LayoutInflater inflater = ((Activity)context).getLayoutInflater();
            row = inflater.inflate(layoutResourceId, parent, false);

            holder = new ViewHolder();
            holder.textView1 = (TextView)row.findViewById(R.id.body);
            holder.textView2 = (TextView)row.findViewById(R.id.modified);
            row.setTag(holder);
        }
        else
        {
            holder = (ViewHolder)row.getTag();
        }

        Question question = data.get(position);

        holder.textView1.setText(question.getBody());
        holder.textView2.setText(question.getLast_modified());

        return row;
    }

    static class ViewHolder
    {
        TextView textView1;
        TextView textView2;
    }

}
