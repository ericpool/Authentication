package com.example.authentication;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

public class registerUser extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private Bitmap bitmap;
    private Uri uri;
    private String fullname;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FirebaseApp.initializeApp(this);
        setContentView(R.layout.activity_register_user);
        
    }
    public void imageClick(View v)
    {
        startActivityForResult(new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.INTERNAL_CONTENT_URI), 1);
    }
    public void onClickSubmit(View v){
        EditText fnameET = findViewById(R.id.fnameET);
        EditText lnameET = findViewById(R.id.lnameET);
        EditText passwordET = findViewById(R.id.passwordET);
        EditText confirmPasswordET = findViewById(R.id.confirmPassET);
        EditText emailET = findViewById(R.id.emailAddressET);

        if(fnameET.getText().toString().isEmpty() || lnameET.getText().toString().isEmpty() || passwordET.getText().toString().isEmpty() || emailET.getText().toString().isEmpty())
        {
            fullname = fnameET.getText().toString() + " " + lnameET.getText().toString();
            if(!passwordET.getText().toString().equals(confirmPasswordET.getText().toString())) {
                Toast.makeText(this, "Passwords do not match", Toast.LENGTH_SHORT).show();
            }
            else {
                createNewAccount(emailET.getText().toString(), passwordET.getText().toString());



            }
        }
        else {
            Toast.makeText(this, "Fields are Empty", Toast.LENGTH_SHORT).show();
        }



    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //Detects request codes
        if(requestCode==1 && resultCode == Activity.RESULT_OK) {
            Uri selectedImage = data.getData();

            try {

                bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), selectedImage);
                this.uri = getImageUri();

            } catch (FileNotFoundException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }

    }

    public void createNewAccount(String email, String password) {
        this.mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d("TAG HERE", "createUserWithEmail:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            updateUser(user);
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w("TAG HERE", "createUserWithEmail:failure", task.getException());
                            Toast.makeText(registerUser.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();

                        }

                        // ...
                    }
                });
    }
    public Uri getImageUri() {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(this.getContentResolver(), bitmap, "Image", null);
        return Uri.parse(path);
    }

    private void updateUser(FirebaseUser user) {


        UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                .setDisplayName(this.fullname)
                .setPhotoUri(uri)
                .build();

        user.updateProfile(profileUpdates)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful())
                        {
                            Log.d("TAG", "User profile updated.");
                        }
                    }
                });
    }
}
