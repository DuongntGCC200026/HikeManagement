package com.example.cw_hikermanagementapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;

public class AddHikeActivity extends AppCompatActivity {
    Button btnAdd, btnCancel;
    EditText txtName, txtLocation, txtDateOfHike, txtLength, txtLevel, txtDes, txtTStart, txtTEnd;
    RadioGroup radioGroup;
    RadioButton rbYes;
    String contentWarning = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_hike);

        btnAdd = (Button) findViewById(R.id.btnAdd);
        btnCancel = (Button) findViewById(R.id.btnUpCancel);
        txtName = (EditText) findViewById(R.id.editUpName);
        txtLocation = (EditText) findViewById(R.id.editUpLocation);
        txtDateOfHike = (EditText) findViewById(R.id.editUpDateOfHike);
        radioGroup = (RadioGroup) findViewById(R.id.rgUpParking);
        rbYes = (RadioButton) findViewById(R.id.radioUp_yes);
        txtLength = (EditText) findViewById(R.id.editUpLength);
        txtLevel = (EditText) findViewById(R.id.editUpLevel);
        txtDes = (EditText) findViewById(R.id.editUpDescription);
        txtTStart = (EditText) findViewById(R.id.editUpTimeStart);
        txtTEnd = (EditText) findViewById(R.id.editUpTimeEnd);

        txtDateOfHike.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DialogFragment newFragment = new DatePickerFragment(txtDateOfHike, "dd/MM/yyyy");
                newFragment.show(getSupportFragmentManager(), "datePicker");
            }
        });

        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (txtName.getText().toString().isEmpty() || txtName.getText().toString().length() <= 3) {
                    contentWarning = "The Name of the Hike must have more than 3 characters!\n";
                }
                if (txtLocation.getText().toString().isEmpty() || txtLocation.getText().toString().length() <= 3) {
                    contentWarning += "Location of the Hike must have more than 3 characters!\n";
                }
                if (txtDateOfHike.getText().toString().isEmpty()) {
                    contentWarning += "Please select a valid date!\n";
                }
                if (txtLength.getText().toString().isEmpty() || !isNumeric(txtLength.getText().toString()) || Double.parseDouble(txtLength.getText().toString()) < 1) {
                    contentWarning += "The Length of the Hike must be numeric more than 0!\n";
                }
                if (txtLevel.getText().toString().isEmpty() || !isNumeric(txtLevel.getText().toString()) || Integer.parseInt(txtLevel.getText().toString()) < 1 || Integer.parseInt(txtLevel.getText().toString()) > 5 ) {
                    contentWarning += "The Level of the Hike must be numeric from 1 to 5!\n";
                }
                if (contentWarning.equals("")) {
                    String name = txtName.getText().toString();
                    String location = txtLocation.getText().toString();
                    String date = txtDateOfHike.getText().toString();
                    int parking = Integer.parseInt((rbYes.isChecked()) ? "1" : "0");
                    double length = Double.parseDouble(txtLength.getText().toString());
                    int level = Integer.parseInt(txtLevel.getText().toString());
                    String des = txtDes.getText().toString();
                    String start = txtTStart.getText().toString();
                    String stop = txtTEnd.getText().toString();

                    String mess = "Check Input Data!\n";
                    mess += "Name: " + name + "\n";
                    mess += "Location: " + location + "\n";
                    mess += "Date: " + date + "\n";
                    if (parking == 1) {
                        mess += "Parking: Available\n";
                    } else {
                        mess += "Parking: Unavailable\n";
                    }
                    mess += "Length: " + length + " KM\n";
                    mess += "Level: " + level + "\n";
                    mess += "Description: " + des + "\n";
                    mess += "Start: " + start + "\n";
                    mess += "Stop: " + stop + "\n";

                    DialogMessageBox confirmationDialog = new DialogMessageBox("Warning", mess);
                    confirmationDialog.showConfirmationDialog(
                            AddHikeActivity.this,
                            (dialog, which) -> HandleAddHike()
                    );

                } else {
                    OpenDialog();
                    contentWarning = "";
                }
            }
        });

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent returnBtn = new Intent(AddHikeActivity.this, MainActivity.class);
                startActivity(returnBtn);
            }
        });
    }

    private void HandleAddHike() {
        Intent i = new Intent();
        i.putExtra("name", txtName.getText().toString());
        i.putExtra("location", txtLocation.getText().toString());
        i.putExtra("date", txtDateOfHike.getText().toString());
        i.putExtra("parking", (rbYes.isChecked()) ? "1" : "0");
        i.putExtra("length", txtLength.getText().toString());
        i.putExtra("level", txtLevel.getText().toString());
        i.putExtra("des", txtDes.getText().toString());
        i.putExtra("timeS", txtTStart.getText().toString());
        i.putExtra("timeE", txtTEnd.getText().toString());
        setResult(RESULT_OK, i);
        finish();
    }

    public boolean isNumeric(String s) {
        try {
            double value = Double.parseDouble(s);
            int value1 = Integer.parseInt(s);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    public void OpenDialog() {
        DialogMessageBox dialogMessageBox = new DialogMessageBox("Please Fill In Valid Information!", contentWarning);
        dialogMessageBox.show(getSupportFragmentManager(), "Message Box");
    }
}