package com.demo.mdb.spring2017finalassessment;

import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;
import java.util.HashMap;

public class RegisterActivity extends AppCompatActivity implements View.OnClickListener {
    public static final int REQUEST_CODE = 1;
    private static final String TAG = "Login Listener";
    public static FirebaseAuth mAuth;
    ImageView tempPicture = null;
    Uri selectedPic;
    String tempName;
    String tempEmail;
    String tempPassword;
    String tempPasswordConfirm;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        /* TODO Part 2
        * Implement registration. If the imageView is clicked, set it to an image from the gallery
        * and store the image as a Uri instance variable (also change the imageView's image to this
        * Uri. If the create new user button is pressed, call createUser using the email and password
        * from the edittexts. Remember that it's email2 and password2 now!
        */
        mAuth = FirebaseAuth.getInstance();

        findViewById(R.id.createnewuser).setOnClickListener(this);
        findViewById(R.id.imageView).setOnClickListener(this);

        EditText editName = (EditText) findViewById(R.id.name);
        EditText editEmail = (EditText) findViewById(R.id.email2);
        final EditText editPassword = (EditText) findViewById(R.id.password2);
        final EditText editPasswordConfirm = (EditText) findViewById(R.id.confirmpassword);

        editName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                tempName = editable.toString();
            }
        });

        editEmail.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                tempEmail = editable.toString();
            }
        });

        editPassword.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                tempPassword = editable.toString();
            }
        });

        editPasswordConfirm.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                tempPasswordConfirm = editable.toString();
            }
        });

        findViewById(R.id.createnewuser).setOnClickListener(this);
    }

    private void createUser(final String email, final String password) {
        /* TODO Part 2.1
         * This part's long, so listen up!
         * Create a user, and if it fails, display a Toast.
         *
         * If it works, we're going to add their image to the database. To do this, we will need a
         * unique user id to identify the user (push isn't the best answer here. Do some Googling!)
         *
         * Now, if THAT works (storing the image), set the name and photo uri of the user (hint: you
         * want to update a firebase user's profile.)
         *
         * Finally, if updating the user profile works, go to the TabbedActivity
         */

        if (!email.equals("") && !password.equals("") && password.length() > 5) {
            mAuth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                // Successful sign-in
                                Log.d(TAG, "createUserWithEmail:success");

                                final String id = FirebaseAuth.getInstance().getCurrentUser().getUid();
                                final DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("users").child(id);

                                StorageReference storageRef = FirebaseStorage.getInstance().getReferenceFromUrl("gs://fall2017finalassessment-db208.appspot.com");
                                StorageReference imageRef = storageRef.child(id + ".png");
                                imageRef.putFile(selectedPic).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                    @Override
                                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                        HashMap<String, String> data = new HashMap<>();
                                        data.put("name", tempName);

                                        ref.setValue(data);

                                        Intent intent = new Intent(RegisterActivity.this, TabbedActivity.class);
                                        startActivity(intent);
                                    }
                                });


                            } else {
                                // Failed sign-in
                                Log.w(TAG, "createUserWithEmail:failure", task.getException());
                                Toast.makeText(RegisterActivity.this, "Authentication failed.",
                                        Toast.LENGTH_SHORT).show();

                            }
                        }
                    });
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        //Assigns the imageview to the users selected picture and stores to picture to be uploaded to Firebase
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE && resultCode == RESULT_OK) {
            tempPicture = (ImageView) findViewById(R.id.imageView);
            selectedPic = data.getData();
            tempPicture.setImageURI(selectedPic);
        }

    }

    @Override
    public void onClick(View v) {
        switch(v.getId()) {
            case R.id.createnewuser:
                if (tempPassword.equals(tempPasswordConfirm)){
                    createUser(tempEmail, tempPassword);
                } else {
                    Toast.makeText(RegisterActivity.this, "Check that your passwords match",
                            Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.imageView:
                Intent getIntent = new Intent(Intent.ACTION_GET_CONTENT);
                getIntent.setType("image/*");

                Intent pickIntent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                pickIntent.setType("image/*");

                Intent chooserIntent = Intent.createChooser(getIntent, "Select Image");
                chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, new Intent[] {pickIntent});

                startActivityForResult(chooserIntent, REQUEST_CODE);
                break;
        }
    }

}
