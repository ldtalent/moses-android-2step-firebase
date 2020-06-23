package com.example.twostepauthentication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class Register extends AppCompatActivity {

    private EditText edit_email,edit_password,edit_confirmPassword;
    private Button register;
    private FirebaseAuth mAuth;
    //adding a progress dialog box
    private ProgressDialog loadingBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        mAuth = FirebaseAuth.getInstance();

        edit_email = findViewById(R.id.editemail);
        edit_password =findViewById(R.id.editPassword);
        edit_confirmPassword = findViewById(R.id.editPasswordConfirm);

        loadingBar = new ProgressDialog(this);
        register = findViewById(R.id.register_button);

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                //creating account for users
             CreateNewAccount();
            }


        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser !=null)
        {
            SendUserToMainActivity();
        }

    }

    private void SendUserToMainActivity()
    {
        Intent mainIntent = new Intent(Register.this,MainActivity.class);
        mainIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(mainIntent);
        finish();
    }

    private void CreateNewAccount()
    {
            //getting the values from the input fields
            String Email = edit_email.getText().toString();
            String Password = edit_password.getText().toString();
            String PasswordConfirm = edit_confirmPassword.getText().toString();

            //adding validation if user left any of the fields empty
            if (TextUtils.isEmpty(Email)){
                Toast.makeText(Register.this, "Please enter your email...", Toast.LENGTH_SHORT).show();
            }
            else if(TextUtils.isEmpty(Password)){
                Toast.makeText(Register.this, "Please enter your password...", Toast.LENGTH_SHORT).show();
            }
            else if (TextUtils.isEmpty(PasswordConfirm)){
                Toast.makeText(Register.this, "PLease confirm your password...", Toast.LENGTH_SHORT).show();
            }
            else if(!Password.equals(PasswordConfirm)){
                Toast.makeText(Register.this, "Password do no match...", Toast.LENGTH_SHORT).show();

            }
            else{
                //progress bar
                loadingBar.setTitle("Creating new Account");
                loadingBar.setMessage("please wait...");
                loadingBar.show();
                loadingBar.setCanceledOnTouchOutside(true);
                //creating account for user
                mAuth.createUserWithEmailAndPassword(Email,Password)
                        .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                //checking if the task is sucessful
                                if (task.isSuccessful()){
                                    SenduserToSTepTwoAuthentication();
                                    Toast.makeText(Register.this, "You have passed the first step authentication...", Toast.LENGTH_SHORT).show();
                                    loadingBar.dismiss();
                                }
                                else{
                                    String message = task.getException().getMessage();
                                    Toast.makeText(Register.this, "error occured" + message, Toast.LENGTH_SHORT).show();                                   }
                                loadingBar.dismiss();

                            }
                        });
            }


    }

    private void SenduserToSTepTwoAuthentication()
    {
        Intent setupIntent = new Intent(Register.this,StepTwoAuthentication.class);
        setupIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(setupIntent);
        finish();

    }


    {

    }
}
