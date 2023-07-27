package com.example.recipefy;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import java.util.regex.Pattern;

public class RegistrationActivity extends AppCompatActivity {

    private EditText emailEditText, passwordEditText, confirmPasswordEditText;
    private Button registerButton;
    private TextView loginTextView;
    private TextView passwordStrengthTextView;
    private TextView uppercaseRequirementTextView;
    private TextView lowercaseRequirementTextView;
    private TextView specialCharRequirementTextView;


    private FirebaseAuth firebaseAuth;

    private void updatePasswordRequirements(String password) {
        boolean hasUppercase = Pattern.compile("[A-Z]").matcher(password).find();
        boolean hasLowercase = Pattern.compile("[a-z]{5,}").matcher(password).find();
        boolean hasSpecialChar = Pattern.compile("[!@#$%^&+=]").matcher(password).find();

        int greenColor = getResources().getColor(R.color.green);
        int redColor = getResources().getColor(R.color.dark_orange);

        if (hasUppercase) {
            uppercaseRequirementTextView.setTextColor(greenColor);
        } else {
            uppercaseRequirementTextView.setTextColor(redColor);
        }

        if (hasLowercase) {
            lowercaseRequirementTextView.setTextColor(greenColor);
        } else {
            lowercaseRequirementTextView.setTextColor(redColor);
        }

        if (hasSpecialChar) {
            specialCharRequirementTextView.setTextColor(greenColor);
        } else {
            specialCharRequirementTextView.setTextColor(redColor);
        }
    }

    private void updatePasswordStrengthTextView(String password) {
        TextView passwordStrengthTextView = findViewById(R.id.passwordStrengthTextView);
        boolean isPasswordValid = isPasswordValid(password);

        if (isPasswordValid) {
            passwordStrengthTextView.setText("Password strength: Strong");
            passwordStrengthTextView.setTextColor(getResources().getColor(R.color.green));
        } else {
            passwordStrengthTextView.setText("Password strength: Weak");
            passwordStrengthTextView.setTextColor(getResources().getColor(R.color.dark_orange));
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        firebaseAuth = FirebaseAuth.getInstance();

        emailEditText = findViewById(R.id.registerEmailEditText);
        passwordEditText = findViewById(R.id.registerPasswordEditText);
        confirmPasswordEditText = findViewById(R.id.confirmPasswordEditText);
        registerButton = findViewById(R.id.registerButton);
        loginTextView = findViewById(R.id.loginTextView);

        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = emailEditText.getText().toString().trim();
                String password = passwordEditText.getText().toString().trim();
                String confirmPassword = confirmPasswordEditText.getText().toString().trim();

                if (!isEmailValid(email)) {
                    emailEditText.setError("Invalid email");
                    return;
                }

                if (!isPasswordValid(password)) {
                    passwordEditText.setError("Password must have at least 5 lowercase letters, one uppercase letter, and one special character");
                    return;
                }

                if (!password.equals(confirmPassword)) {
                    confirmPasswordEditText.setError("Passwords do not match");
                    return;
                }

                registerUser(email, password);
            }
        });

        passwordStrengthTextView = findViewById(R.id.passwordStrengthTextView);
        uppercaseRequirementTextView = findViewById(R.id.uppercaseRequirementTextView);
        lowercaseRequirementTextView = findViewById(R.id.lowercaseRequirementTextView);
        specialCharRequirementTextView = findViewById(R.id.specialCharRequirementTextView);

        // Update password requirements when the user enters a password
        passwordEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String password = s.toString();
                updatePasswordStrengthTextView(password);
                updatePasswordRequirements(password);
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });



        loginTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(RegistrationActivity.this, LoginActivity.class));
            }
        });
    }

    private boolean isEmailValid(String email) {
        return Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    private boolean isPasswordValid(String password) {
        // Password must have at least 5 lowercase letters, one uppercase letter, and one special character
        String passwordPattern = "^(?=.*[a-z]{5,})(?=.*[A-Z])(?=.*[@#$%^&+=!.<>?:;_]).*$";
        return Pattern.matches(passwordPattern, password);
    }

    private void registerUser(String email, String password) {
        firebaseAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            startActivity(new Intent(RegistrationActivity.this, HomeActivity.class));
                            finish();
                        } else {
                            Toast.makeText(RegistrationActivity.this, "Registration failed. Please try again.", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
}
