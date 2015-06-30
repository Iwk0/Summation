package com.summation;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by Iwk0 on 28/06/2015.
 */
public class StartFragment extends Fragment {

    private Activity parent;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_start, container, false);
        parent = getActivity();

        view.findViewById(R.id.start_game).setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                ((MainActivity) parent).selectItem(1);
            }
        });

        view.findViewById(R.id.score).setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                ((MainActivity) parent).selectItem(2);
            }
        });

        view.findViewById(R.id.exit).setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                parent.finish();
            }
        });

        return view;
    }
}