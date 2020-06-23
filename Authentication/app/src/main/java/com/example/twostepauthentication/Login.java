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
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class Login extends AppCompatActivity {
    private EditText edit_email,edit_password;
    private Button login_Button;

    private FirebaseAuth mAuth;
    private ProgressDialog loadingBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mAuth = FirebaseAuth.getInstance();

        edit_email =findViewById(R.id.editemail);
        edit_password = findViewById(R.id.editpassword);
        login_Button = findViewById(R.id.loginbutton);
        //logging the user in
        loadingBar =new ProgressDialog(this);
        login_Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                AllowUserToLogin();
                FirebaseUser currentUser = mAuth.getCurrentUser();
                if (currentUser !=null)
                {
                    SendUserToMainActivity();
                }

            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();

    }

    private void AllowUserToLogin()
    {
        String email = edit_email.getText().toString().trim();
        String password = edit_password.getText().toString().trim();

        if (TextUtils.isEmpty(email))
        {
            Toast.makeText(this, "Please write your email...", Toast.LENGTH_SHORT).show();
        }
        else if(TextUtils.isEmpty(password)){
            Toast.makeText(this, "Please write your password", Toast.LENGTH_SHORT).show();
        }
        else
        {
            //progress bar
            loadingBar.setTitle("Log In");
            loadingBar.setMessage("please wait...");
            loadingBar.show();
            loadingBar.setCanceledOnTouchOutside(true);

           mAuth.signInWithEmailAndPassword(email,password)
           .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
               @Override
               public void onComplete(@NonNull Task<AuthResult> task) {

                   if (task.isSuccessful()){
                       SendUserToMainActivity();
                       Toast.makeText(Login.this, "You are logged in successful", Toast.LENGTH_SHORT).show();
                       loadingBar.dismiss();
                   }
                   else
                   {
                       String message = task.getException().getMessage();
                       Toast.makeText(Login.this, "error occurred" +message, Toast.LENGTH_SHORT).show();
                       loadingBar.dismiss();
                   }

               }
           });
    }
    }

    private void SendUserToMainActivity()
    {
        Intent mainIntent = new Intent(Login.this,MainActivity.class);
        mainIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(mainIntent);
        finish();
    }

    //if user has no account will be directed to register
    public void RegisteActivity(View view)
    {
        Intent registerIntent = new Intent(Login.this,Register.class);
        startActivity(registerIntent);
    }
}
