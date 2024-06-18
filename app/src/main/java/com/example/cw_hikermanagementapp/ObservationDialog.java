package com.example.cw_hikermanagementapp;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatDialogFragment;
import androidx.fragment.app.DialogFragment;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Objects;

public class ObservationDialog extends AppCompatDialogFragment {
    Hike hike;
    Observation observation;
    String type;
    private EditText edtNameObser;
    private EditText edtDateObser;
    private EditText edtAdditionObser;
    private AddObserDialogListener addObserDialogListener;

    public ObservationDialog(Hike hike, String type) {
        this.hike = hike;
        this.type = type;
    }

    public ObservationDialog(Observation observation, String type) {
        this.observation = observation;
        this.type = type;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        if (type.equals("add")) {
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

            LayoutInflater inflater = getActivity().getLayoutInflater();
            View view = inflater.inflate(R.layout.layout_add_observation, null);
            builder.setView(view).
                    setTitle("Add New Observation").
                    setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {

                        }
                    }).
                    setPositiveButton("Add", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            String name = edtNameObser.getText().toString();
                            String date = edtDateObser.getText().toString();
                            String addition = edtAdditionObser.getText().toString();
                            addObserDialogListener.sendData(name,date,addition, hike);
                        }
                    });

            edtNameObser = (EditText) view.findViewById(R.id.NameObservation);
            edtDateObser = (EditText) view.findViewById(R.id.TimeObservation);
            edtAdditionObser = (EditText) view.findViewById(R.id.AdditionalObservation);

            String pattern = "yyyy-MM-dd hh:mm:ss";
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
            String date = simpleDateFormat.format(new Date());
            edtDateObser.setText(date);
            edtDateObser.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    DialogFragment newFragment = new DatePickerFragment(edtDateObser, "dd/MM/yyyy");
                    newFragment.show(requireActivity().getSupportFragmentManager(), "datePicker");
                }
            });

            return builder.create();

        } else if (type.equals("update")) {
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

            LayoutInflater inflater = getActivity().getLayoutInflater();
            View view = inflater.inflate(R.layout.layout_add_observation, null);
            builder.setView(view).
                    setTitle("Update Observation").
                    setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {

                        }
                    }).
                    setPositiveButton("Update", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            String name = edtNameObser.getText().toString();
                            String date = edtDateObser.getText().toString();
                            String addition = edtAdditionObser.getText().toString();
                            addObserDialogListener.sendData(name,date,addition, observation);
                        }
                    });

            edtNameObser = (EditText) view.findViewById(R.id.NameObservation);
            edtDateObser = (EditText) view.findViewById(R.id.TimeObservation);
            edtAdditionObser = (EditText) view.findViewById(R.id.AdditionalObservation);

            edtNameObser.setText(observation.getNameOfObservation());
            edtDateObser.setText(observation.getDateAndTime());
            edtAdditionObser.setText(observation.getAdditionalComments());

            edtDateObser.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    DialogFragment newFragment = new DatePickerFragment(edtDateObser, "dd/MM/yyyy");
                    newFragment.show(requireActivity().getSupportFragmentManager(), "datePicker");
                }
            });
            return builder.create();
        }
        return null;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        try {
            addObserDialogListener = (AddObserDialogListener) context;
        } catch (ClassCastException e){
            throw new ClassCastException(context.toString() + "must implement AddObservationDialogListener");
        }
    }

    public interface AddObserDialogListener <T>{
        void sendData(String name, String dateAndTime, String addition, T data);
    }
}
