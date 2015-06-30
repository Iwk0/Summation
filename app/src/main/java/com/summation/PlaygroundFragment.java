package com.summation;

import android.app.Activity;
import android.content.res.Resources;
import android.graphics.Color;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class PlaygroundFragment extends Fragment {

    private int oldSum, newSum;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_playground, container, false);
        final Activity activity = getActivity();
        final Resources resources = activity.getResources();
        final GridView playground = (GridView) view.findViewById(R.id.playground);
        final Random random = new Random();
        final List<Integer> numbers = new ArrayList<>();
        final TextView sum = (TextView) view.findViewById(R.id.sum);
        final TextView timer = (TextView) view.findViewById(R.id.timer);
        final TextView current = (TextView) view.findViewById(R.id.current_sum);
        final ArrayAdapter<Integer> adapter = new ArrayAdapter<>(activity,
                android.R.layout.simple_dropdown_item_1line, numbers);

        for (int i = 1; i <= 18; i++) {
            numbers.add(random.nextInt(10) + 1);
        }

        oldSum =  getNextSum(random, numbers);
        sum.setText(resources.getString(R.string.sum) + " " + oldSum);

        playground.setAdapter(adapter);
        playground.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
                if (v.isEnabled()) {
                    newSum += (Integer) parent.getItemAtPosition(position);
                    current.setText(resources.getString(R.string.current) + newSum);
                    v.setEnabled(false);

                    if (oldSum == newSum) {
                        newSum = 0;

                        /*Calculate new sum*/
                        oldSum =  getNextSum(random, numbers);

                        /*Restarting current sum to 0*/
                        current.setText(resources.getString(R.string.current) + " " + 0);

                        /*Set new sum*/
                        sum.setText(resources.getString(R.string.sum) + " " + oldSum);

                        /*Remove all selected views*/
                        int size = parent.getChildCount();
                        for (int i = 0; i < size; i++) {
                            if (!parent.getChildAt(i).isEnabled()) {
                                numbers.remove(i);
                                adapter.notifyDataSetChanged();
                            }
                        }

                        enableAllViews(parent);
                    } else if (newSum > oldSum) {
                        newSum = 0;
                        current.setBackgroundColor(Color.RED);

                        enableAllViews(parent);
                    } else {
                        current.setBackgroundColor(Color.WHITE);
                    }
                }
            }
        });

        /*Count down timer*/
        new CountDownTimer(30000, 1000) {

            private String labelTimer = resources.getString(R.string.timer) + " ";
            private String gameOver = resources.getString(R.string.game_over);

            @Override
            public void onTick(long millisUntilFinished) {
                timer.setText(labelTimer + millisUntilFinished / 1000);
            }

            @Override
            public void onFinish() {
                timer.setText(gameOver);
            }
        }.start();

        return view;
    }

    private void enableAllViews(AdapterView<?> parent) {
        final int size = parent.getChildCount();
        for (int i = 0; i < size; i++) {
            parent.getChildAt(i).setEnabled(true);
        }
    }

    private int getNextSum(Random random, List<Integer> numbers) {
        int sum = 0;
        for (int i = 1; i <= 2; i++) {
            sum += numbers.get(random.nextInt(numbers.size()));
        }
        return sum;
    }
}