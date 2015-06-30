package com.summation;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;

/**
 * Created by imishev on 30.6.2015 г..
 */
public class RestartDialogFragment extends DialogFragment {

    private int textId;

    public RestartDialogFragment(int textId) {
        this.textId = textId;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage(textId)
                .setPositiveButton(R.string.restart, new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        ((MainActivity) getActivity()).selectItem(1);
                    }
                })
                .setNegativeButton(R.string.main, new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        ((MainActivity) getActivity()).selectItem(0);
                    }
                });

        return builder.create();
    }

    @Override
    public void onCancel(DialogInterface dialog) {
        getActivity().finish();
        super.onCancel(dialog);
    }
}