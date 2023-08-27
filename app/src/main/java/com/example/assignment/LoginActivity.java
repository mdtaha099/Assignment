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
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

public class LoginActivity extends AppCompatActivity {

    TextView Welcome_tv, Signin_tv;

    EditText Email_et, Password_et, ConfirmPass_et;

    Button Signin_btn, Signup_btn;

    private ProgressDialog progressDialog;

    //Firebase authentication
    private FirebaseAuth firebaseAuth;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        firebaseAuth = FirebaseAuth.getInstance();


        progressDialog = new ProgressDialog(this);

        Welcome_tv = findViewById(R.id.welcome_tv);
        Signin_tv = findViewById(R.id.signin_tv);

        Email_et = findViewById(R.id.emailET);
        Password_et = findViewById(R.id.passwordET);


        Signin_btn = findViewById(R.id.loginBtn);
        Signup_btn = findViewById(R.id.sign_up_btn);

        Signin_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SignInUser();
            }
        });

        Signup_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //Go To SignUp Page
                Intent intent = new Intent(getApplicationContext(), SignUpActivity.class);
                startActivity(intent);
            }
        });

    }

    private void sendUserToMain() {
        Intent i = new Intent(getApplicationContext(), MainActivity.class);
        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK );
        startActivity(i);
        finish();
    }

    private void SignInUser() {
        String email = Email_et.getText().toString();
        String pass = Password_et.getText().toString();

        if(TextUtils.isEmpty(email)){
            Toast.makeText(this, "Please enter email address", Toast.LENGTH_SHORT).show();
        }
        if(TextUtils.isEmpty(pass)){
            Toast.makeText(this, "Please enter password", Toast.LENGTH_SHORT).show();
        }
        else{
            progressDialog.setTitle("Please wait...");
            progressDialog.setCanceledOnTouchOutside(true);
            progressDialog.show();

            firebaseAuth.signInWithEmailAndPassword(email, pass)
                    .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if(task.isSuccessful()){
                                Toast.makeText(LoginActivity.this, "Welcome", Toast.LENGTH_SHORT).show();
                                progressDialog.dismiss();
                                //Send User to main activity
                                sendUserToMain();
                            }
                            else{
                                progressDialog.dismiss();
                                String e = task.getException().toString();
                                Toast.makeText(LoginActivity.this, ""+e, Toast.LENGTH_SHORT).show();
                            }

                        }
                    });
        }


    }


}