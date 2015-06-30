package com.summation;

import android.app.Activity;
import android.content.res.Resources;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
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

    private CountDownTimer countDownTimer;
    private int oldSum, newSum, lastNumberIndex;

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

        for (int i = 1; i <= 16; i++) {
            numbers.add(random.nextInt(10) + 1);
        }

        oldSum =  calculateNextSum(random, numbers);
        sum.setText(resources.getString(R.string.sum) + " " + oldSum);

        playground.setAdapter(adapter);
        playground.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
                if (v.isEnabled()) {
                    newSum += (Integer) parent.getItemAtPosition(position);
                    current.setText(resources.getString(R.string.current) + " " + newSum);
                    current.setBackgroundResource(android.R.color.transparent);
                    v.setEnabled(false);

                    if (newSum == oldSum) {
                        newSum = 0;

                        /*Remove all selected views*/
                        int size = parent.getChildCount();
                        for (int i = size - 1; i >= 0; i--) {
                            View child = parent.getChildAt(i);
                            if (!child.isEnabled()) {
                                numbers.remove(i);
                                adapter.notifyDataSetChanged();
                            }
                        }

                        if (!numbers.isEmpty()) {
                            /*Restarting current sum to 0*/
                            current.setText(resources.getString(R.string.current) + " " + 0);

                            /*Calculate new sum*/
                            oldSum = calculateNextSum(random, numbers);

                            /*Set new sum*/
                            sum.setText(resources.getString(R.string.sum) + " " + oldSum);
                        } else {
                            if (countDownTimer != null) {
                                countDownTimer.cancel();
                                openDialog(R.string.won);
                            }
                        }

                        enableAllViews(parent);
                    } else if (newSum > oldSum) {
                        newSum = 0;
                        current.setBackgroundResource(R.color.holo_red_light);
                        enableAllViews(parent);
                    } else {
                        Log.i("NewSum", String.valueOf(newSum));
                    }
                }
            }
        });

         /*Count down timer*/
        timer(resources, timer);

        return view;
    }

    private void enableAllViews(AdapterView<?> parent) {
        final int size = parent.getChildCount();
        for (int i = 0; i < size; i++) {
            parent.getChildAt(i).setEnabled(true);
        }
    }

    private void timer(final Resources resources, final TextView timer) {
        countDownTimer = new CountDownTimer(30000, 1000) {

            private String labelTimer = resources.getString(R.string.timer) + " ";

            @Override
            public void onTick(long millisUntilFinished) {
                timer.setText(labelTimer + millisUntilFinished / 1000);
            }

            @Override
            public void onFinish() {
                openDialog(R.string.game_over);
            }
        };
        countDownTimer.start();
    }

    private void openDialog(int textId) {
        FragmentTransaction ft = getFragmentManager().beginTransaction();
        Fragment prev = getFragmentManager().findFragmentByTag("dialog");
        if (prev != null) {
            ft.remove(prev);
        }
        ft.addToBackStack(null);

        RestartDialogFragment restartDialogFragment = new RestartDialogFragment(textId);
        restartDialogFragment.show(ft, "dialog");
    }

    private int calculateNextSum(Random random, List<Integer> numbers) {
        int sum = 0;
        int size = numbers.size() >= 2 ? 2 : 1;

        for (int i = 0; i < size; i++) {
            int randomNumber = random.nextInt(numbers.size());

            while (lastNumberIndex == randomNumber) {
                randomNumber = random.nextInt(numbers.size());
                Log.i("Index", String.valueOf(randomNumber));
            }

            sum += numbers.get(randomNumber);
            lastNumberIndex = randomNumber;
        }

        return sum;
    }
}