package com.plickers.demo.plickersdemoapp.Adapters;

import android.app.Activity;
import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.plickers.demo.plickersdemoapp.Objects.Choice;
import com.plickers.demo.plickersdemoapp.R;

import java.util.ArrayList;

/**
 * Class was discarded - ListView is not well supported in ScrollView
 */
public class ChoicesAdapter extends ArrayAdapter<Choice> {

    private final Context mContext;
    private ArrayList<Choice> mData;
    private final int mLayoutResourceId;


    public ChoicesAdapter(Context context, int layoutResourceId, ArrayList<Choice> data) {
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
            holder.textView1 = (TextView) row.findViewById(R.id.questionChoice);
            row.setTag(holder);
        } else {
            holder = (ViewHolder) row.getTag();
        }


        Choice choice = mData.get(position);

        holder.textView1.setText(choice.getLetter() + " : " + choice.getBody());


        if (choice.getCorrect() == null) {
        } //No color if no answer
        else if (choice.getCorrect()) {
            //Green if right
            holder.textView1.setBackgroundColor(ContextCompat.getColor
                    (mContext, R.color.colorLightGreen));
        } else {
            //Red if wrong
            holder.textView1.setBackgroundColor(ContextCompat.getColor
                    (mContext, R.color.colorLightRed));
        }


        return row;
    }

    static class ViewHolder {
        TextView textView1;
    }


}
