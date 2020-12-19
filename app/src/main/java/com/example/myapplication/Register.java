package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class Register extends AppCompatActivity {
    private FirebaseAuth mAuth;
    private FirebaseFirestore fStore;


    EditText username;
    EditText email;
    EditText password;
    EditText confirm_password;
    EditText refferal_code;
    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.regsiter);
        mAuth = FirebaseAuth.getInstance();
        fStore= FirebaseFirestore.getInstance();

        username=findViewById(R.id.username);
        email=findViewById(R.id.email);
        password=findViewById(R.id.password);
        confirm_password=findViewById(R.id.confirm_password);
        refferal_code=findViewById(R.id.refferal_code);
        progressBar=findViewById(R.id.progressBar);


    }





    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.

        //updateUI(currentUser);
    }
    private void register() {

        String  username_create=username.getText().toString();
        String  email_create=email.getText().toString();
        String password_create=password.getText().toString();
        String confirm_password_create=confirm_password.getText().toString();
        String  refferal_code_create=refferal_code.getText().toString();



        if (TextUtils.isEmpty(username_create)) {
            username.setError("Username is Required!");
            return;
        }
        if (TextUtils.isEmpty(email_create)) {
            email.setError("Email is Required!");
           return;
        }
        if (TextUtils.isEmpty(password_create)) {
            password.setError("Password is Required!");
            return;
        }
        if (TextUtils.isEmpty(confirm_password_create)) {
            confirm_password.setError("Please enter password confirmation!");

            return;
        }
        if (TextUtils.isEmpty(refferal_code_create)) {
            refferal_code.setError("Refferal code is Required!");
            return;
        }
        if (password.length() < 8) {
            password.setError("Password must be 8 charactors!");
        }

        if (!confirm_password_create.equals(password_create)) {
            confirm_password.setError("confirm password is not match ");
            return;
        }


        progressBar.setVisibility(View.VISIBLE);


        mAuth.createUserWithEmailAndPassword(email_create, password_create)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d("TAG", "createUserWithEmail:success");
                           //FirebaseUser user = mAuth.getCurrentUser();





                            final String  userID=mAuth.getCurrentUser().getUid();
//                            //----firebasefireStore --------------------------
                            DocumentReference documentReference=fStore.collection("Users").document(userID);
                            Map<String,Object> user=new HashMap<>();
                            user.put("reffernal_code",refferal_code.getText().toString());
                            user.put("uname",username.getText().toString());

                            //user.put("Phone Number",phone);
                            documentReference.set(user).addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    Log.d("TAG","ON Success :user Profile is Create for "+userID);
                                    progressBar.setVisibility(View.INVISIBLE);
                                   // registerbutton.setVisibility(View.VISIBLE);
                                    Toast.makeText(Register.this,"success !",Toast.LENGTH_SHORT).show();

                                   // startActivity(new Intent(Register.this,Login.class));finish();
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Log.d("TAG"," UnSuccess :user Profile is Create for "+userID);
                                }
                            });


                            //updateUI(user);
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w("TAG", "createUserWithEmail:failure", task.getException());
                            Toast.makeText(Register.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                          //  updateUI(null);
                        }

                        // ...
                    }
                });
    }

    public void register(View view) {


        register();
    }
}