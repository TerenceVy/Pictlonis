package com.example.pictionis.user;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.pictionis.MainActivity;
import com.example.pictionis.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class Register extends AppCompatActivity {
    EditText pFullName, pEmail, pPassword;
    Button pRegisterBtn;
    TextView pLogin, pCreateBtn;
    FirebaseAuth fAuth;
    ProgressBar progressBar;
    FirebaseUser fUser = FirebaseAuth.getInstance().getCurrentUser();
    FirebaseFirestore fStore;
    String userId;
    public static final String TAG = "TAG";



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        pFullName = findViewById(R.id.fullName);
        pEmail = findViewById(R.id.email);
        pPassword= findViewById(R.id.password);
        pRegisterBtn = findViewById(R.id.loginBtn);
        pLogin = findViewById(R.id.createBtn);
        fAuth = FirebaseAuth.getInstance();
        progressBar = findViewById(R.id.progressBar);
        fStore = FirebaseFirestore.getInstance();


        if(fUser != null) {
            startActivity(new Intent(getApplicationContext(), MainActivity.class));
            finish();
        }
        pRegisterBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String password = pPassword.getText().toString().trim();
                String email = pEmail.getText().toString().trim();
                String fullName = pFullName.getText().toString().trim();

                if (TextUtils.isEmpty(fullName)) {
                    pFullName.setError("Full name required.");
                    return;
                }
                if (TextUtils.isEmpty(email)) {
                    pEmail.setError("Email required.");
                    return;
                }
                if (TextUtils.isEmpty(password) || password.length() < 8) {
                    pPassword.setError("Password is required and must have at least 8 characters.");
                    return;
                }

                progressBar.setVisibility(View.VISIBLE);
                fAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(Register.this, "Your account has been added", Toast.LENGTH_SHORT).show();

                            userId = fAuth.getCurrentUser().getUid();
                            DocumentReference documentReference = fStore.collection("users").document(userId);
                            Map<String,Object> user = new HashMap<>();
                            user.put("fName",fullName);
                            user.put("email",email);
                            documentReference.set(user).addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    Log.d(TAG, "onSuccess: user Profile is created for "+ userId);
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Log.d(TAG, "onFailure: " + e.toString());
                                }
                            });
                            startActivity(new Intent(getApplicationContext(), MainActivity.class));
                        }
                        else {
                            Toast.makeText(Register.this, "Error has occurred !" + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                            progressBar.setVisibility(View.GONE);
                        }
                    }
                });
            }
        });

        pLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),Login.class));
            }
        });
    }
}