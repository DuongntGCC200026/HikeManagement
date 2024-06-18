package com.example.cw_hikermanagementapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.DialogFragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;

public class UpdateHikeActivity extends AppCompatActivity {
    Button btnUpdate, btnCancel;
    EditText txtUpName, txtUpLocation, txtUpDateOfHike, txtUpLength, txtUpLevel, txtUpDes, txtUpTStart, txtUpTEnd;
    RadioButton rbYes, rbNo;
    String contentWarning = "";
    Hike hike;
    int idHike;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_hike);

        Toolbar mytoolbar = findViewById(R.id.toolbarUpdate);
        setSupportActionBar(mytoolbar);

        btnUpdate = (Button) findViewById(R.id.btnUpdate);
        btnCancel = (Button) findViewById(R.id.btnUpCancel);
        txtUpName = (EditText) findViewById(R.id.editUpName);
        txtUpLocation = (EditText) findViewById(R.id.editUpLocation);
        txtUpDateOfHike = (EditText) findViewById(R.id.editUpDateOfHike);
        rbYes = (RadioButton) findViewById(R.id.radioUp_yes);
        rbNo = (RadioButton) findViewById(R.id.radioUp_no);
        txtUpLength = (EditText) findViewById(R.id.editUpLength);
        txtUpLevel = (EditText) findViewById(R.id.editUpLevel);
        txtUpDes = (EditText) findViewById(R.id.editUpDescription);
        txtUpTStart = (EditText) findViewById(R.id.editUpTimeStart);
        txtUpTEnd = (EditText) findViewById(R.id.editUpTimeEnd);

        Intent i = getIntent();
        hike = (Hike) i.getSerializableExtra("hikeObj");
        idHike = hike.getId();
        if (i.getSerializableExtra("hikeObj") != null) {
            txtUpName.setText(hike.getName());
            txtUpLocation.setText(hike.getLocation());
            txtUpDateOfHike.setText(hike.getDateOfTheHike());
            rbYes.setChecked(hike.isParking() == 1);
            rbNo.setChecked(!rbYes.isChecked());
            String length = String.valueOf(hike.getLength());
            txtUpLength.setText(length);
            String level = String.valueOf(hike.getLevel());
            txtUpLevel.setText(level);
            txtUpDes.setText(hike.getDescription());
            txtUpTStart.setText(hike.getTimeStart());
            txtUpTEnd.setText(hike.getTimeStop());
        }

        txtUpDateOfHike.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DialogFragment newFragment = new DatePickerFragment(txtUpDateOfHike, "dd/MM/yyyy");
                newFragment.show(getSupportFragmentManager(), "datePicker");
            }
        });

        btnUpdate.setOnClickListener(view -> {
            if (txtUpName.getText().toString().isEmpty() || txtUpName.getText().toString().length() <= 3) {
                contentWarning = "The Name of the Hike must have more than 3 characters!\n";
            }
            if (txtUpLocation.getText().toString().isEmpty() || txtUpLocation.getText().toString().length() <= 3) {
                contentWarning += "Location of the Hike must have more than 3 characters!\n";
            }
            if (txtUpDateOfHike.getText().toString().isEmpty()) {
                contentWarning += "Please select a valid date!\n";
            }
            if (txtUpLength.getText().toString().isEmpty() || !isNumeric(txtUpLength.getText().toString())) {
                contentWarning += "The Length of the Hike must be numeric!\n";
            }
            if (txtUpLevel.getText().toString().isEmpty() || !isNumeric(txtUpLevel.getText().toString())) {
                contentWarning += "The Level of the Hike must be numeric!\n";
            }
            if (contentWarning.equals("")) {

                String name = txtUpName.getText().toString();
                String location = txtUpLocation.getText().toString();
                String date = txtUpDateOfHike.getText().toString();
                int parking = Integer.parseInt((rbYes.isChecked()) ? "1" : "0");
                double length = Double.parseDouble(txtUpLength.getText().toString());
                int level = Integer.parseInt(txtUpLevel.getText().toString());
                String des = txtUpDes.getText().toString();
                String start = txtUpTStart.getText().toString();
                String stop = txtUpTEnd.getText().toString();

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
                        UpdateHikeActivity.this,
                        (dialog, which) -> HandleUpdateHike()
                );
            } else {
                OpenDialog();
                contentWarning = "";
            }
        });

        btnCancel.setOnClickListener(view -> {
            Intent returnBtn = new Intent(UpdateHikeActivity.this, MainActivity.class);
            startActivity(returnBtn);
        });
    }

    private void HandleUpdateHike() {
        int park = Integer.parseInt((rbYes.isChecked() ? "1" : "0"));
        double length = Double.parseDouble(txtUpLength.getText().toString());
        int level = Integer.parseInt(txtUpLevel.getText().toString());

        Intent intent = new Intent();
        Hike putHike = new Hike(idHike, txtUpName.getText().toString(),
                txtUpLocation.getText().toString(),
                txtUpDateOfHike.getText().toString(),
                park,
                length,
                level,
                txtUpDes.getText().toString(),
                txtUpTStart.getText().toString(),
                txtUpTEnd.getText().toString(), 0);

        intent.putExtra("hikeObj", putHike);
        setResult(RESULT_OK, intent);
        finish();
    }

    public static boolean isNumeric(String s) {
        try {
            double value = Double.parseDouble(s);
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