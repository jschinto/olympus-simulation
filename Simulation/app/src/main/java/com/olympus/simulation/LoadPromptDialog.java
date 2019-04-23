package com.olympus.simulation;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatDialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;

public class LoadPromptDialog extends AppCompatDialogFragment {
    private Spinner spinnerFileName;
    private LoadPromptDialogListener listener;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog_loadprompt, null);

        spinnerFileName = view.findViewById(R.id.spinner_filename);

        String[] items = (new FileHelper()).getFileNames(getContext());
        ArrayAdapter<String> adapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_dropdown_item, items);
        spinnerFileName.setAdapter(adapter);
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        builder.setView(view)
                .setTitle("Select File to Load")
                .setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        listener.cancelLoad();
                    }
                })
                .setPositiveButton("ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        String fileName = spinnerFileName.getSelectedItem().toString();
                        listener.loadFileName(fileName);
                    }
                });

        return builder.create();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        try {
            listener = (LoadPromptDialogListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString() +
                    "must implement LoadPromptDialogListener");
        }
    }

    public interface LoadPromptDialogListener {
        void loadFileName(String fileName);
        void cancelLoad();
    }
}
