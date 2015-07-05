package com.summation;

import android.content.Context;
import android.content.res.Resources;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

/**
 * Created by Iwk0 on 05/07/2015.
 */
public class ScoreAdapter extends ArrayAdapter<Score> {

    private List<Score> scores;
    private Context context;

    public ScoreAdapter(Context context, int resource, List<Score> scores) {
        super(context, resource);
        this.scores = scores;
        this.context = context;
    }


    public static class ViewHolder {
        TextView number;
        TextView time;
        TextView name;
    }

    @Override
    public int getCount() {
        return scores.size();
    }

    @Override
    public Score getItem(int position) {
        return scores.get(position);
    }

    @Override
    public long getItemId(int position) {
        return scores.get(position).id;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;

        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.item, parent, false);

            viewHolder = new ViewHolder();
            viewHolder.number = (TextView) convertView.findViewById(R.id.number);
            viewHolder.time = (TextView) convertView.findViewById(R.id.time);
            viewHolder.name = (TextView) convertView.findViewById(R.id.name);

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        if (position % 2 == 0) {
            convertView.setBackgroundResource(R.color.Blue);
        } else {
            convertView.setBackgroundResource(R.color.LightBlue);
        }

        Resources resources = context.getResources();
        Score score = scores.get(position);
        viewHolder.number.setText(resources.getString(R.string.number) + " " + (position + 1));
        viewHolder.time.setText(score.time);
        viewHolder.name.setText(resources.getString(R.string.name) + " " + score.name);

        return convertView;
    }
}