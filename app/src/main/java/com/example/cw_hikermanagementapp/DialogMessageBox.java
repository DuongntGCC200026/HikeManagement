package com.example.cw_hikermanagementapp;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDialogFragment;

public class DialogMessageBox extends AppCompatDialogFragment {
    private String type;
    private String content;

    public DialogMessageBox(String type, String content) {
        this.type = type;
        this.content = content;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(type)
                .setMessage(content)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                });
        return builder.create();
    }

    public void showConfirmationDialog(Context context, DialogInterface.OnClickListener yesClickListener) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setIcon(R.drawable.baseline_warning_24);
        builder.setTitle(this.type);
        builder.setMessage(this.content);

        builder.setPositiveButton("Yes", yesClickListener);

        builder.setNegativeButton("No", (dialog, which) -> {
        });

        AlertDialog dialog = builder.create();
        dialog.show();
    }
}
