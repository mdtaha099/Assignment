package com.example.assignment;

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
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class SignUpActivity extends AppCompatActivity {

    TextView Welcome_tv, Signin_tv;
    EditText username_et;

    EditText Email_et, Password_et;

    Button goToSignin_btn, Signup_btn;

    //Firebase authentication
    private FirebaseAuth firebaseAuth;
    private DatabaseReference db;

    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        firebaseAuth = FirebaseAuth.getInstance();
        db = FirebaseDatabase.getInstance().getReference();

        Welcome_tv = findViewById(R.id.welcome_tv);
        Signin_tv = findViewById(R.id.signin_tv);

        Email_et = findViewById(R.id.emailET);
        Password_et = findViewById(R.id.passwordET);
        username_et = findViewById(R.id.usernameET);


        progressDialog = new ProgressDialog(this);


        
        Signup_btn = findViewById(R.id.sign_up_btn);
        Signup_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CreateNewAcc();
            }
        });
        

        goToSignin_btn = findViewById(R.id.sign_in_btn);
        goToSignin_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendUserToLogin();
            }
        });


    }

    private void sendUserToLogin() {
        Intent intent = new Intent(SignUpActivity.this, LoginActivity.class);
        startActivity(intent);
    }

    private void sendUserToMain() {
        Intent i = new Intent(SignUpActivity.this, MainActivity.class);
        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK );
        startActivity(i);
        finish();
    }


    private void CreateNewAcc(){
        String email = Email_et.getText().toString();
        String pass = Password_et.getText().toString();
        String username = username_et.getText().toString();

        if(TextUtils.isEmpty(username)){
            Toast.makeText(this, "Please enter username", Toast.LENGTH_SHORT).show();
        }


        if(TextUtils.isEmpty(email)){
            Toast.makeText(this, "Please enter email address", Toast.LENGTH_SHORT).show();
        }
        if(TextUtils.isEmpty(pass)){
            Toast.makeText(this, "Please enter password", Toast.LENGTH_SHORT).show();
        }
        else{

            progressDialog.setTitle("Creating new account.");
            progressDialog.setMessage("Please wait.");
            progressDialog.setCanceledOnTouchOutside(true);
            progressDialog.show();

            firebaseAuth.createUserWithEmailAndPassword(email, pass)
                    .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if(task.isSuccessful()){
                                String UID = firebaseAuth.getCurrentUser().getUid();
                                Map<String, String> map = new HashMap<>();
                                map.put("UID", UID);
                                map.put("username", username);
                                map.put("email", email);
                                db.child("Users").child(UID).setValue(map);

                                sendUserToMain();
                                Toast.makeText(SignUpActivity.this, "Account Created!!", Toast.LENGTH_SHORT).show();
                                progressDialog.dismiss();
                            }
                            else{
                                String e = task.getException().toString();
                                Toast.makeText(SignUpActivity.this, ""+e, Toast.LENGTH_SHORT).show();
                                progressDialog.dismiss();
                            }
                        }
                    });

        }
    }
}