package com.summation;

import android.app.Activity;
import android.content.res.Resources;
import android.os.Bundle;
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
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;

//TODO add functionality for successful summation
public class PlaygroundFragment extends Fragment {

    private Timer counter;
    private int oldSum, newSum, countSuccessfulSummation, complexity = 10;
    private byte attemptsCount = 0;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_playground, container, false);
        final Activity activity = getActivity();
        final Resources resources = activity.getResources();
        final GridView playground = (GridView) view.findViewById(R.id.playground);
        final Random random = new Random();
        final TextView sum = (TextView) view.findViewById(R.id.sum);
        final TextView timer = (TextView) view.findViewById(R.id.timer);
        final TextView current = (TextView) view.findViewById(R.id.current_sum);
        final TextView attempts = (TextView) view.findViewById(R.id.attempts);
        final List<Integer> numbers = new ArrayList<>();
        final ArrayAdapter<Integer> adapter = new ArrayAdapter<>(activity,
                android.R.layout.simple_dropdown_item_1line, numbers);

        oldSum = restartGame(numbers, complexity);
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
                        countSuccessfulSummation++;

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
                            oldSum = calculateNextSum(numbers, position);

                            /*Set new sum*/
                            sum.setText(resources.getString(R.string.sum) + " " + oldSum);
                        } else {
                            complexity += 5;
                            oldSum = restartGame(numbers, complexity);
                            sum.setText(resources.getString(R.string.sum) + " " + oldSum);

                            Log.i("Numbers size", String.valueOf(numbers.size()));
                            adapter.notifyDataSetChanged();
                        }

                        enableAllViews(parent);
                    } else if (newSum > oldSum) {
                        newSum = 0;
                        current.setBackgroundResource(R.color.holo_red_light);
                        enableAllViews(parent);

                        attempts.setText(resources.getString(R.string.attempts) + " " + (++attemptsCount));
                        if (attemptsCount == 3) {
                            openDialog((String) timer.getText(), countSuccessfulSummation);
                        }
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

    @Override
    public void onDetach() {
        if (counter != null) {
            counter.cancel();
        }
        super.onDetach();
    }

    private int restartGame(List<Integer> numbers, int complexity) {
        Random random = new Random();
        int min = complexity - (complexity - 1);
        for (int i = 1; i <= 15; i++) {
            numbers.add(random.nextInt((complexity - min) + 1) + min);
        }

        return calculateNextSum(numbers, -1);
    }

    private void enableAllViews(AdapterView<?> parent) {
        final int size = parent.getChildCount();
        for (int i = 0; i < size; i++) {
            parent.getChildAt(i).setEnabled(true);
        }
    }

    private void timer(final Resources resources, final TextView timer) {
        TimerTask countdownTask = new TimerTask() {

            private int counter = 0;

            @Override
            public void run() {
                getActivity().runOnUiThread(new Runnable() {

                    @Override
                    public void run() {
                        counter += 1000;

                        String formattedTime = String.format("%02d:%02d:%02d", TimeUnit.MILLISECONDS.toHours(counter),
                                TimeUnit.MILLISECONDS.toMinutes(counter) - TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(counter)),
                                TimeUnit.MILLISECONDS.toSeconds(counter) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(counter)));

                        timer.setText(resources.getString(R.string.time) + " " + formattedTime);
                    }
                });
            }
        };

        counter = new Timer();
        counter.schedule(countdownTask, 0, 1000);
    }

    private void openDialog(String time, int countSuccessfulSummation) {
        FragmentTransaction ft = getFragmentManager().beginTransaction();
        Fragment prev = getFragmentManager().findFragmentByTag("dialog");
        if (prev != null) {
            ft.remove(prev);
        }
        ft.addToBackStack(null);

        Bundle bundle = new Bundle();
        bundle.putString("time", time);
        bundle.putInt("successful_summation", countSuccessfulSummation);

        RestartDialogFragment restartDialogFragment = new RestartDialogFragment();
        restartDialogFragment.setArguments(bundle);
        restartDialogFragment.show(ft, "dialog");
    }

    private int calculateNextSum(List<Integer> numbers, int currentPosition) {
        Random random = new Random();
        int sum = 0;
        int size = numbers.size() > 1 ? 2 : 1;

        for (int i = 0; i < size; i++) {
            int randomNumber;

            do {
                randomNumber = random.nextInt(numbers.size());
            } while (currentPosition == randomNumber && size != 1);

            sum += numbers.get(randomNumber);
            currentPosition = randomNumber;
        }

        return sum;
    }
}