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
 * Used when fragment is displaying a list of the questions to the user.
 */
public class QuestionAdapter extends ArrayAdapter<Question> {

    private final Context mContext;
    private ArrayList<Question> mData;
    private final int mLayoutResourceId;

    public QuestionAdapter(Context context, int layoutResourceId, ArrayList<Question> data) {
        super(context, layoutResourceId, data);
        this.mContext = context;
        this.mData = data;
        this.mLayoutResourceId = layoutResourceId;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View row = convertView;
        ViewHolder holder = null;

        if (row == null) {
            LayoutInflater inflater = ((Activity) mContext).getLayoutInflater();
            row = inflater.inflate(mLayoutResourceId, parent, false);

            holder = new ViewHolder();
            holder.textView1 = (TextView) row.findViewById(R.id.body);
            holder.textView2 = (TextView) row.findViewById(R.id.modified);
            row.setTag(holder);
        } else {
            holder = (ViewHolder) row.getTag();
        }

        Question question = mData.get(position);

        holder.textView1.setText(question.getBody());
        holder.textView2.setText(question.getLast_modified().
                substring(0, question.getLast_modified().indexOf('T')));

        return row;
    }

    static class ViewHolder {
        TextView textView1;
        TextView textView2;
    }

    public void sortData(ArrayList<Question> data) {
        this.mData = data;
        notifyDataSetChanged();
    }

}
