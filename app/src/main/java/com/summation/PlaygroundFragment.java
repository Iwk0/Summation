package com.summation;

import android.app.Activity;
import android.content.res.Resources;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;

public class PlaygroundFragment extends Fragment {

    private Timer mCounter;

    private int mOldSum, mNewSum, mCountSuccessfulSummation, mComplexity = 10;
    private byte mAttemptsCount = 3;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_playground, container, false);
        Activity activity = getActivity();
        final Resources resources = activity.getResources();

        /*View initialization*/
        GridView playground = (GridView) view.findViewById(R.id.playground);
        final TextView sum = (TextView) view.findViewById(R.id.sum);
        final TextView timer = (TextView) view.findViewById(R.id.timer);
        final TextView current = (TextView) view.findViewById(R.id.current_sum);
        final TextView attempts = (TextView) view.findViewById(R.id.attempts);

        /*GridView initialization*/
        final List<Number> numbers = new ArrayList<>();
        final ArrayAdapter<Number> adapter = new ArrayAdapter<>(activity,
                android.R.layout.simple_dropdown_item_1line, numbers);

        final String attemptsString = resources.getString(R.string.attempts);
        attempts.setText(resources.getString(R.string.template, attemptsString, "3"));

        final String currentString = resources.getString(R.string.current);
        current.setText(resources.getString(R.string.template, currentString, "0"));

        mOldSum = restartGame(numbers, mComplexity);
        final String sumLabel = resources.getString(R.string.sum);
        sum.setText(resources.getString(R.string.template, sumLabel, mOldSum));

        playground.setAdapter(adapter);
        playground.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
                if (v.isEnabled()) {
                    v.setEnabled(false);

                    numbers.get(position).index = position;
                    mNewSum += numbers.get(position).value;

                    current.setText(resources.getString(R.string.template, currentString, mNewSum));
                    current.setTextColor(Color.WHITE);

                    if (mNewSum == mOldSum) {
                        mNewSum = 0;
                        mCountSuccessfulSummation++;

                        removeDisabledViews(numbers, parent);

                        adapter.notifyDataSetChanged();

                        if (!numbers.isEmpty()) {
                            /*Calculate new sum*/
                            mOldSum = calculateNextSum(numbers, position);

                            /*Set new sum*/
                            sum.setText(resources.getString(R.string.template, sumLabel, mOldSum));

                            /*Restarting current sum to 0*/
                            current.setText(resources.getString(R.string.template, currentString, "0"));
                        } else {
                            mComplexity += 5;
                            mOldSum = restartGame(numbers, mComplexity);

                            sum.setText(resources.getString(R.string.template, sumLabel, mOldSum));
                            current.setText(resources.getString(R.string.template, currentString, "0"));

                            adapter.notifyDataSetChanged();
                        }
                    } else if (mNewSum > mOldSum) {
                        mNewSum = 0;
                        current.setTextColor(Color.RED);

                        /*Enable all disabled views*/
                        for (Number number : numbers) {
                            if (number.index != -1) {
                                parent.getChildAt(number.index).setEnabled(true);
                                number.index = -1;
                            }
                        }

                        /*At a wrong calculation decrement attempts*/
                        attempts.setText(resources.getString(R.string.template,
                                attemptsString, (--mAttemptsCount)));

                        if (mAttemptsCount == 0) {
                            String text = timer.getText().toString();
                            openDialog((text).substring(text.indexOf(":") + 1),
                                    mCountSuccessfulSummation);
                        }
                    }
                }
            }
        });

         /*Count down timer*/
        timer(timer, resources);

        return view;
    }

    @Override
    public void onDetach() {
        if (mCounter != null) {
            mCounter.cancel();
        }
        super.onDetach();
    }

    private void removeDisabledViews(List<Number> numbers, AdapterView<?> parent) {
        for (Iterator<Number> iterator = numbers.iterator(); iterator.hasNext();) {
            Number number = iterator.next();
            if (number.index != -1) {
                parent.getChildAt(number.index).setEnabled(true);
                iterator.remove();
            }
        }
    }

    private void timer(final TextView timer, final Resources resources) {
        final Handler handler = new Handler();

        TimerTask countdownTask = new TimerTask() {

            private int counter = 0;

            @Override
            public void run() {
                handler.post(new Runnable() {

                    @Override
                    public void run() {
                        counter += 1000;

                        String formattedTime = String.format("%02d:%02d:%02d",
                                TimeUnit.MILLISECONDS.toHours(counter),
                                TimeUnit.MILLISECONDS.toMinutes(counter) -
                                        TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(counter)),
                                TimeUnit.MILLISECONDS.toSeconds(counter) -
                                        TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(counter)));

                        timer.setText(resources.getString(R.string.template,
                                resources.getString(R.string.time), formattedTime));

                        if (formattedTime.equals("99:99:99")) {
                            openDialog((String) timer.getText(), mCountSuccessfulSummation);
                        }
                    }
                });
            }
        };

        mCounter = new Timer();
        mCounter.schedule(countdownTask, 0, 1000);
    }

    private void openDialog(String time, int countSuccessfulSummation) {
        FragmentTransaction ft = getFragmentManager().beginTransaction();
        Fragment prev = getFragmentManager().findFragmentByTag(Constants.DIALOG);
        if (prev != null) {
            ft.remove(prev);
        }
        ft.addToBackStack(null);

        Bundle bundle = new Bundle();
        bundle.putString(Constants.TIME, time);
        bundle.putInt(Constants.SUCCESSFUL_SUMMATION, countSuccessfulSummation);

        RestartDialogFragment restartDialogFragment = new RestartDialogFragment();
        restartDialogFragment.setArguments(bundle);
        restartDialogFragment.show(ft, Constants.DIALOG);
    }

    private int calculateNextSum(List<Number> numbers, int currentPosition) {
        Random random = new Random();
        int sum = 0;
        int size = numbers.size() > 1 ? 2 : 1;

        for (int i = 0; i < size; i++) {
            int randomNumber;

            do {
                randomNumber = random.nextInt(numbers.size());
            } while (currentPosition == randomNumber && size != 1);

            sum += numbers.get(randomNumber).value;
            currentPosition = randomNumber;
        }

        return sum;
    }

    private int restartGame(List<Number> numbers, int complexity) {
        Random random = new Random();
        int min = complexity - (complexity - 1);
        for (int i = 1; i <= 15; i++) {
            Number number = new Number();
            number.value = random.nextInt((complexity - min) + 1) + min;
            numbers.add(number);
        }

        return calculateNextSum(numbers, -1);
    }
}