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
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class StepTwoAuthentication extends AppCompatActivity {

    private EditText edit_firstname, edit_othernames,edit_phonenumber;
    private Button save;
    private FirebaseAuth mAuth;
    private DatabaseReference userRef;
    private ProgressDialog loadingBar;

    String currentUserID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_step_two_authentication);

        mAuth = FirebaseAuth.getInstance();
        currentUserID = mAuth.getCurrentUser().getUid();
        userRef = FirebaseDatabase.getInstance().getReference().child("Users").child(currentUserID);

        edit_firstname = findViewById(R.id.editfirstname);
        edit_othernames = findViewById(R.id.editothernames);
        edit_phonenumber = findViewById(R.id.editphonenumber);
        save = findViewById(R.id.save_button);

        loadingBar = new ProgressDialog(this);

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SaveInformation();
            }
        });

    }

    private void SaveInformation()
    {
        String firstname = edit_firstname.getText().toString().trim();
        String othernames = edit_othernames.getText().toString().trim();
        String phonenumber = edit_phonenumber.getText().toString().trim();

        if (TextUtils.isEmpty(firstname))
        {
            Toast.makeText(this, "plese write your first name", Toast.LENGTH_SHORT).show();
        }
        if (TextUtils.isEmpty(othernames))
        {
            Toast.makeText(this, "plese write your other name", Toast.LENGTH_SHORT).show();
        }
        if (TextUtils.isEmpty(phonenumber))
        {
            Toast.makeText(this, "plese write your phone number", Toast.LENGTH_SHORT).show();
        }
        else {

            loadingBar.setTitle("Saving Information");
            loadingBar.setMessage("please wait...");
            loadingBar.show();
            loadingBar.setCanceledOnTouchOutside(true);

            //saving information on the databse
            HashMap userMap = new HashMap();
            userMap.put("FirstName",firstname);
            userMap.put("OtherName",othernames);
            userMap.put("Phone",phonenumber);

            //store
            userRef.updateChildren(userMap).addOnCompleteListener(new OnCompleteListener() {
                @Override
                public void onComplete(@NonNull Task task) {

                    if (task.isSuccessful()){
                        SendUserToMainActivity();
                        Toast.makeText(StepTwoAuthentication.this, "Your account is created successfully", Toast.LENGTH_LONG).show();
                        loadingBar.dismiss();
                    }
                    else {
                        String message = task.getException().getMessage();
                        Toast.makeText(StepTwoAuthentication.this, "error corred" + message, Toast.LENGTH_SHORT).show();
                        loadingBar.dismiss();
                    }
                }
            });

        }
    }

    private void SendUserToMainActivity()
    {
        Intent mainIntent = new Intent(StepTwoAuthentication.this,MainActivity.class);
        mainIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(mainIntent);
        finish();
    }
}
