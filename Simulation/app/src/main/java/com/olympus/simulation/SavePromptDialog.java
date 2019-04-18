package com.olympus.simulation;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatDialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

public class SavePromptDialog extends AppCompatDialogFragment {
    private EditText editTextFileName;
    private SavePromptDialogListener listener;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog_saveprompt, null);

        builder.setView(view)
                .setTitle("Enter File Name")
                .setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                })
                .setPositiveButton("ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        String fileName = editTextFileName.getText().toString();
                        listener.submitFileName(fileName);
                    }
                });

        editTextFileName = view.findViewById(R.id.edit_filename);

        return builder.create();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        try {
            listener = (SavePromptDialogListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString() +
                    "must implement SavePromptDialogListener");
        }
    }

    public interface SavePromptDialogListener {
        void submitFileName(String fileName);
    }
}
