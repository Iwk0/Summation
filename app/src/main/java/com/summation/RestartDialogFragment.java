package com.summation;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

public class RestartDialogFragment extends DialogFragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_status, container);

        getDialog().setTitle(getActivity().getResources().getString(R.string.score));

        final EditText userName = (EditText) view.findViewById(R.id.user_name);
        final TextView scoreView = (TextView) view.findViewById(R.id.score_view);

        final Bundle bundle = getArguments();
        scoreView.setText(bundle.getString(Constants.TIME));

        view.findViewById(R.id.restart).setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                ((MainActivity) getActivity()).selectItem(1);
                dismiss();
            }
        });

        view.findViewById(R.id.save).setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                ((MainActivity) getActivity()).selectItem(0);
                Database database = new Database(getActivity());
                Score score = new Score();
                score.time = (String) scoreView.getText();
                score.name = userName.getText().toString();
                score.countSuccessfulSummation = bundle.getInt(Constants.SUCCESSFUL_SUMMATION);
                database.insertScore(score);
                dismiss();
            }
        });

        view.findViewById(R.id.main_menu).setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                ((MainActivity) getActivity()).selectItem(0);
                dismiss();
            }
        });

        return view;
    }

    @Override
    public void onCancel(DialogInterface dialog) {
        ((MainActivity) getActivity()).selectItem(0);
        super.onCancel(dialog);
    }
}