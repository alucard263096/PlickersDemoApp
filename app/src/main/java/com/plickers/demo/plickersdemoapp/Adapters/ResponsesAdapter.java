package com.plickers.demo.plickersdemoapp.Adapters;

import android.app.Activity;
import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.plickers.demo.plickersdemoapp.Objects.Response;
import com.plickers.demo.plickersdemoapp.R;

import java.util.ArrayList;

/**
 * Class was discarded - ListView is not well supported in ScrollView
 */
public class ResponsesAdapter extends ArrayAdapter<Response> {

    private final Context mContext;
    private ArrayList<Response> mData;
    private final int mLayoutResourceId;
    private final char mCorrectAnswer;

    public ResponsesAdapter(Context context, int layoutResourceId, ArrayList<Response> data,
                            char correctAnswer) {
        super(context, layoutResourceId, data);
        this.mContext = context;
        this.mData = data;
        this.mLayoutResourceId = layoutResourceId;
        this.mCorrectAnswer = correctAnswer;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View row = convertView;
        ViewHolder holder = null;

        if (row == null) {
            LayoutInflater inflater = ((Activity) mContext).getLayoutInflater();
            row = inflater.inflate(mLayoutResourceId, parent, false);

            holder = new ViewHolder();
            holder.textView1 = (TextView) row.findViewById(R.id.studentResponse);
            row.setTag(holder);
        } else {
            holder = (ViewHolder) row.getTag();
        }


        Response response = mData.get(position);

        holder.textView1.setText(response.getStudent() + " - " + response.getAnswer());

        if (response.getAnswer() == mCorrectAnswer) {
            holder.textView1.setBackgroundColor(ContextCompat.getColor
                    (mContext, R.color.colorLightGreen));
        } else {
            holder.textView1.setBackgroundColor(ContextCompat.getColor
                    (mContext, R.color.colorLightRed));
        }

        return row;
    }

    static class ViewHolder {
        TextView textView1;
    }


}
