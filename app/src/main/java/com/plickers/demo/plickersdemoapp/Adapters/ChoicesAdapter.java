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
 * Created by Admin on 3/19/2016.
 */
public class ChoicesAdapter extends ArrayAdapter<Choice> {

    private final Context context;
    private ArrayList<Choice> data;
    private final int layoutResourceId;


    public ChoicesAdapter(Context context, int layoutResourceId, ArrayList<Choice> data) {
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
            holder.textView1 = (TextView)row.findViewById(R.id.questionChoice);
            row.setTag(holder);
        }
        else
        {
            holder = (ViewHolder)row.getTag();
        }


        Choice choice = data.get(position);

        holder.textView1.setText(choice.getLetter() + " : " + choice.getBody());


        if(choice.getCorrect() == null) {} //No color if no answer
        else if(choice.getCorrect()){
            //Green if right
            holder.textView1.setBackgroundColor(ContextCompat.getColor
                    (context, R.color.colorLightGreen));
        }
        else{
            //Red if wrong
            holder.textView1.setBackgroundColor(ContextCompat.getColor(context,R.color.colorLightRed));
        }


        return row;
    }

    static class ViewHolder
    {
        TextView textView1;
        TextView textView2;
    }


}
