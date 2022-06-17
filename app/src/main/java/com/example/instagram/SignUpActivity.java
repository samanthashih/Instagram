package com.example.instagram;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.SignUpCallback;

public class SignUpActivity extends AppCompatActivity {
    private final static String TAG = "SignUpActivity";
    private EditText etUsername;
    private EditText etPassword;
    private Button btnSignUp;
    private ImageView ivInstagram;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.i(TAG, "sign up");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        etUsername = findViewById(R.id.etUsername);
        etPassword = findViewById(R.id.etPassword);
        btnSignUp = findViewById(R.id.btnSignUp);
        ivInstagram = findViewById(R.id.ivInstagram);
        Glide.with(this)
                .load(R.drawable.ic_instagram_logo)
                .into(ivInstagram);

        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ParseUser user = new ParseUser();
                String username = etUsername.getText().toString();
                String password = etPassword.getText().toString();
                user.setUsername(username);
                user.setPassword(password);
                user.signUpInBackground(new SignUpCallback() {
                    @Override
                    public void done(ParseException e) {
                        Intent intent = new Intent(SignUpActivity.this, MainActivity.class);
                        startActivity(intent);
                        finish();
                    }
                });
            }
        });
    }
}