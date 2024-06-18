package com.example.cw_hikermanagementapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class Login extends AppCompatActivity {
    private CheckBox cbRe;
    private Button btnLogin;
    private TextView txtUser, txtPass;
    SharedPreferences sharedPreferences;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        txtUser = (EditText) findViewById(R.id.edtUser);
        txtPass = (EditText) findViewById(R.id.edtPass);
        cbRe = (CheckBox) findViewById(R.id.cbRemember);
        btnLogin = (Button) findViewById(R.id.btnLogin);

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String usr = txtUser.getText().toString();
                String pas = txtPass.getText().toString();

                if (usr.equals("User1") && pas.equals("123456")) {
                    Toast.makeText(Login.this, "Login successfully!", Toast.LENGTH_SHORT).show();
                    if (cbRe.isChecked()) {
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.putString("username", usr);
                        editor.putString("pass", pas);
                        editor.putBoolean("remember", true);
                        editor.commit();
                    } else {
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.putString("username", "");
                        editor.putString("pass", "");
                        editor.putBoolean("remember", false);
                        editor.commit();
                    }
                    MainActivity.isLoginIntentProcessed = true;
                    finish();
                } else {
                    Toast.makeText(Login.this, "Login fail!", Toast.LENGTH_SHORT).show();
                }

            }
        });

        sharedPreferences = getSharedPreferences("dataLogin", MODE_PRIVATE);
        String userName = sharedPreferences.getString("username", "");
        String edtPass = sharedPreferences.getString("pass", "");
        boolean remember = sharedPreferences.getBoolean("remember", false);

        txtUser.setText(userName);
        txtPass.setText(edtPass);
        cbRe.setChecked(remember);
    }
}