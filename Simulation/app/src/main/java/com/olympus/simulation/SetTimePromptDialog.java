package com.olympus.simulation;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatDialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TimePicker;

public class SetTimePromptDialog extends AppCompatDialogFragment {
    private int start;
    private int end;
    private TimePicker startTime;
    private TimePicker endTime;
    private SetTimePromptDialogListener listener;

    public SetTimePromptDialog() {
        super();
        this.start = 0;
        this.end = Time.convertToInt("23:59");
    }

    @SuppressLint("ValidFragment")
    public SetTimePromptDialog(int start, int end) {
        super();
        this.start = start;
        this.end = end;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog_settimeprompt, null);

        startTime = view.findViewById(R.id.startTimePicker);
        startTime.setIs24HourView(true);
        startTime.setCurrentHour(start/60);
        startTime.setCurrentMinute(start%60);
        endTime = view.findViewById(R.id.endTimePicker);
        endTime.setIs24HourView(true);
        endTime.setCurrentHour(end/60);
        endTime.setCurrentMinute(end%60);

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        builder.setView(view)
                .setTitle("Set Start/End Time")
                .setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        listener.cancelTime();
                    }
                })
                .setPositiveButton("ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        start = (startTime.getCurrentHour() * 60) + startTime.getCurrentMinute();
                        end = (endTime.getCurrentHour() * 60) + endTime.getCurrentMinute();
                        listener.setStartEndTime(start, end);
                    }
                });

        return builder.create();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        try {
            listener = (SetTimePromptDialogListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString() +
                    "must implement SetTimePromptDialogListener");
        }
    }

    public interface SetTimePromptDialogListener {
        void setStartEndTime(int start, int end);
        void cancelTime();
    }
}
