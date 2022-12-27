package timeline.android.digitalwellbeing.TYProject;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.view.WindowCompat;

import android.animation.ArgbEvaluator;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.provider.Settings;
import android.transition.AutoTransition;
import android.transition.Slide;
import android.transition.TransitionManager;
import android.util.Patterns;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.airbnb.lottie.LottieAnimationView;
import com.github.mmin18.widget.RealtimeBlurView;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.util.Objects;

import timeline.android.digitalwellbeing.TYProject.ui.SettingsActivity;
import timeline.android.digitalwellbeing.TYProject.ui.SignInActivity;

public class ProfileActivity extends AppCompatActivity {
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
    FirebaseAuth firebaseAuth;


    private TextInputLayout passwordEditTextLayout;
    private EditText nameEditText, emailEditText, passwordEditTextInput, occupationEditTextInput;
    private FloatingActionButton floatingUpdateButton;
    private FirebaseAuth.AuthStateListener authListener;
    private String name = "";

    private Button passwordChangeButton;
    private LottieAnimationView loadingLottie, successLottie, profileLottie;
    private String email = "";

    private ImageView profileImage, editProfileImage;
    private StorageReference storageReference;
    private TextView userNameText, helloTextView;

    private CardView infoCard, optionsCard;
    private RealtimeBlurView topBlurView;

    private TextView occupationTextView;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        WindowCompat.setDecorFitsSystemWindows(getWindow(), false);



        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        setWindowFlag(this, WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS, false);
        getWindow().setStatusBarColor(Color.TRANSPARENT);

        requestWindowFeature(Window.FEATURE_NO_TITLE); //will hide the title
        Objects.requireNonNull(getSupportActionBar()).hide(); // hide the title bar

        setContentView(R.layout.activity_profile);
        helloTextView = findViewById(R.id.hello_textview);

        passwordChangeButton = findViewById(R.id.password_change_button);
        infoCard = findViewById(R.id.info_card);
        topBlurView = findViewById(R.id.blurView1);
        loadingLottie = findViewById(R.id.loading_lottie);
        profileLottie = findViewById(R.id.profile_lottie);
        successLottie = findViewById(R.id.success_lottie);
        passwordEditTextLayout = findViewById(R.id.sign_password_layout);
        userNameText = findViewById(R.id.user_name_textview);
        occupationEditTextInput = findViewById(R.id.occupation_input);
        occupationTextView = findViewById(R.id.occupation_textview);

        profileImage = findViewById(R.id.profile_image);
        editProfileImage = findViewById(R.id.edit_profile_image);

        firebaseAuth = FirebaseAuth.getInstance();

        final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        authListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user == null) {
                    // user auth state is changed - user is null
                    // launch login activity
                    startActivity(new Intent(ProfileActivity.this, SignInActivity.class));
                    finish();
                }
            }
        };


        /**
         *
         *
         *
         *Firebase Storage Reference
         *
         *
         *
         */

        storageReference = FirebaseStorage.getInstance().getReference();
        StorageReference profileRef = storageReference.child("users/" + Objects.requireNonNull(firebaseAuth.getCurrentUser()).getUid() + "/profile.jpg");
        profileRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Picasso.get().load(uri).into(profileImage);
                profileImage.setAlpha(0f);
                profileImage.animate().alpha(1f).start();
                profileImage.setVisibility(View.VISIBLE);

            }
        }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        profileLottie.setVisibility(View.VISIBLE);
                        profileImage.setVisibility(View.INVISIBLE);
                    }
                });


        nameEditText = findViewById(R.id.register_name_input);
        emailEditText = findViewById(R.id.sign_email_input);
        passwordEditTextInput = findViewById(R.id.sign_password_input);
        floatingUpdateButton = findViewById(R.id.floating_submit_button);


        emailEditText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if (passwordEditTextLayout.getVisibility() == View.GONE) {
                    passwordEditTextLayout.setAlpha(0f);
                    passwordEditTextLayout.setVisibility(View.VISIBLE);
                    passwordEditTextLayout.animate().alpha(1f).setStartDelay(100);
                }
            }
        });

        nameEditText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if (passwordEditTextLayout.getVisibility() == View.GONE) {
                    passwordEditTextLayout.setAlpha(0f);
                    passwordEditTextLayout.setVisibility(View.VISIBLE);
                    passwordEditTextLayout.animate().alpha(1f).setStartDelay(100);
                }
            }
        });

        occupationEditTextInput.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if (passwordEditTextLayout.getVisibility() == View.GONE) {
                    passwordEditTextLayout.setAlpha(0f);
                    passwordEditTextLayout.setVisibility(View.VISIBLE);
                    passwordEditTextLayout.animate().alpha(1f).setStartDelay(100);
                }
            }
        });


        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference("Users");

        editProfileImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent galleryIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(galleryIntent, 1000);
            }
        });

        getData();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1000) {
            if (resultCode == Activity.RESULT_OK) {
                Uri imageUri = Objects.requireNonNull(data).getData();
//                profileImage.setImageURI(imageUri);

                profileImage.setVisibility(View.GONE);
                profileLottie.setVisibility(View.GONE);
                successLottie.setAlpha(0f);
                successLottie.setVisibility(View.VISIBLE);
                successLottie.animate().alpha(1f).start();
                successLottie.setProgress(0f);
                successLottie.resumeAnimation();
                uploadImageToFirebase(imageUri);
            }
        }
    }

    private void uploadImageToFirebase(Uri imageUri) {

        StorageReference fileRef = storageReference.child("users/" + Objects.requireNonNull(firebaseAuth.getCurrentUser()).getUid() + "/profile.jpg");

        fileRef.putFile(imageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        fileRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {
                                Picasso.get().load(uri).into(profileImage);
                                profileImage.setAlpha(0f);
                                profileImage.animate().alpha(1f).start();
                                profileImage.setVisibility(View.VISIBLE);
                                successLottie.setAlpha(1f);
                                successLottie.animate().alpha(0f).setStartDelay(500).start();
                                successLottie.setVisibility(View.INVISIBLE);

                            }
                        });
                        helloTextView.setText("Updated Successfully!");
                        helloTextView.setTextColor(Color.parseColor("#0065FF"));
                        TransitionManager.beginDelayedTransition(infoCard, new Slide(Gravity.TOP));
                        infoCard.setVisibility(View.VISIBLE);
                        topBlurView.setVisibility(View.VISIBLE);

                        topBlurView.setTranslationY(-200);
                        ObjectAnimator animator = ObjectAnimator.ofFloat(topBlurView, "translationY", 0);
                        animator.setDuration(300);

                        animator.start();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception exception) {
                        helloTextView.setText("Failed to Update!");
                        helloTextView.setTextColor(Color.RED);
                        helloTextView.setTextSize(16);
                        TransitionManager.beginDelayedTransition(infoCard, new Slide(Gravity.TOP));
                        infoCard.setVisibility(View.VISIBLE);
                        topBlurView.setVisibility(View.VISIBLE);

                        topBlurView.animate().translationY(0).setDuration(200).setStartDelay(0).start();                     }
                });
    }


    private void getData() {
        String uid = Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid();

        databaseReference.child(uid).addListenerForSingleValueEvent(new ValueEventListener() {

            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                nameEditText.setAlpha(0f);
                emailEditText.setAlpha(0f);
                userNameText.setAlpha(0f);
                occupationEditTextInput.setAlpha(0f);
                occupationTextView.setAlpha(0f);

                nameEditText.setText(dataSnapshot.child("name").getValue(String.class));
                emailEditText.setText(dataSnapshot.child("email").getValue(String.class));
                email = dataSnapshot.child("email").getValue(String.class);
                userNameText.setText(dataSnapshot.child("name").getValue(String.class));
                occupationEditTextInput.setText(dataSnapshot.child("occupation").getValue(String.class));
                occupationTextView.setText(dataSnapshot.child("occupation").getValue(String.class));

                nameEditText.animate().alpha(1f).setStartDelay(500).start();
                emailEditText.animate().alpha(1f).setStartDelay(500).start();
                userNameText.animate().alpha(1f).setStartDelay(0).start();
                occupationEditTextInput.animate().alpha(1f).setStartDelay(0).start();
                occupationTextView.animate().alpha(1f).setStartDelay(0).start();



            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(ProfileActivity.this, "Fail to get data.", Toast.LENGTH_SHORT).show();
            }
        });


        // calling add value event listener method
//        // for getting the values from database.
//        databaseReference.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot snapshot) {
//                for(DataSnapshot ds : snapshot.getChildren()) {
//
//                    if (ds.getValue(String.class).equals(firebaseAuth.getUid().toString()))
//
//
//                }
//
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError error) {
//                // calling on cancelled method when we receive
//                // any error or we are not able to get the data.
//            }
//        });
    }

    public static void setWindowFlag(Activity activity, final int bits, boolean on) {
        Window win = activity.getWindow();
        WindowManager.LayoutParams winParams = win.getAttributes();
        if (on) {
            winParams.flags |= bits;
        } else {
            winParams.flags &= ~bits;
        }
        win.setAttributes(winParams);
    }

    public void passwordChangeLink(View view) {
        String email = emailEditText.getText().toString().trim();
        loadingLottie.setAlpha(0f);

        loadingLottie.setVisibility(View.VISIBLE);
        passwordChangeButton.setText("");

        loadingLottie.animate().alpha(1f).setStartDelay(100).start();

        if (email.isEmpty()) {
            emailEditText.setError("Email is required!");
            emailEditText.requestFocus();
            loadingLottie.setVisibility(View.GONE);
            passwordChangeButton.setText("Send Password Change Link");
            return;
        }

        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            emailEditText.setError("Please provide valid Email!");
            emailEditText.requestFocus();
            loadingLottie.setVisibility(View.GONE);
            passwordChangeButton.setText("Send Password Change Link");
            return;
        }

        firebaseAuth.sendPasswordResetEmail(email).addOnCompleteListener(new OnCompleteListener<Void>() {
            @SuppressLint("UseCompatLoadingForDrawables")
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    passwordChangeButton.setText("Link Sent to Email");
                    loadingLottie.setVisibility(View.GONE);
                    Toast.makeText(ProfileActivity.this, "Check your email to reset your password!", Toast.LENGTH_LONG).show();
                } else {
                    passwordChangeButton.setText("Try Again After some Time!");
                    loadingLottie.setVisibility(View.GONE);
                    Toast.makeText(ProfileActivity.this, "Try again! Something wrong happened!", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    private void updateEmail() {
        String uid = Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid();
        String email1 = emailEditText.getText().toString().trim();
        String name = nameEditText.getText().toString().trim();
        String password = passwordEditTextInput.getText().toString().trim();
        String occupation = occupationEditTextInput.getText().toString();

        if (password != null && email != null) {
            FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
            // Get auth credentials from the user for re-authentication
            try {
                AuthCredential credential = EmailAuthProvider.getCredential(email, password); // Current Login Credentials \\
                user.reauthenticate(credential)
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {
                                    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                                    user.updateEmail(email1)
                                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                @Override
                                                public void onComplete(@NonNull Task<Void> task) {
                                                    if (task.isSuccessful()) {
                                                        databaseReference.child(uid).child("email").setValue(email1).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                            @Override
                                                            public void onComplete(@NonNull Task<Void> task) {
                                                                if (task.isSuccessful()) {
                                                                    databaseReference.child(uid).child("name").setValue(name).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                        @Override
                                                                        public void onComplete(@NonNull Task<Void> task) {
                                                                            if (task.isSuccessful()) {
                                                                                databaseReference.child(uid).child("occupation").setValue(occupation).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                                    @Override
                                                                                    public void onComplete(@NonNull Task<Void> task) {
                                                                                        passwordEditTextLayout.setVisibility(View.GONE);
                                                                                        helloTextView.setText("Updated Successfully!");
                                                                                        helloTextView.setTextColor(Color.parseColor("#0065FF"));
                                                                                        TransitionManager.beginDelayedTransition(infoCard, new Slide(Gravity.TOP));
                                                                                        infoCard.setVisibility(View.VISIBLE);

                                                                                        topBlurView.setTranslationY(-200);
                                                                                        topBlurView.setVisibility(View.VISIBLE);
                                                                                        ObjectAnimator animator = ObjectAnimator.ofFloat(topBlurView, "translationY", 0);
                                                                                        animator.setDuration(300);

                                                                                        animator.start();
                                                                                        getData();

                                                                                    }
                                                                                });
                                                                            } else {
                                                                                helloTextView.setText("Occupation Failed to Update!");
                                                                                helloTextView.setTextColor(Color.RED);
                                                                                helloTextView.setTextSize(16);
                                                                                TransitionManager.beginDelayedTransition(infoCard, new Slide(Gravity.TOP));
                                                                                infoCard.setVisibility(View.VISIBLE);

                                                                                topBlurView.setVisibility(View.VISIBLE);
                                                                                topBlurView.animate().translationY(0).setDuration(200).setStartDelay(0).start();
                                                                            }
                                                                        }
                                                                    });
                                                                } else {
                                                                    helloTextView.setText("Email Failed to Update!");
                                                                    helloTextView.setTextColor(Color.RED);
                                                                    helloTextView.setTextSize(16);
                                                                    TransitionManager.beginDelayedTransition(infoCard, new Slide(Gravity.TOP));
                                                                    infoCard.setVisibility(View.VISIBLE);

                                                                    topBlurView.setVisibility(View.VISIBLE);
                                                                    topBlurView.animate().translationY(0).setDuration(200).setStartDelay(0).start();
                                                                }
                                                            }
                                                        });

                                                    } else {
                                                        helloTextView.setText("Incorrect Password");
                                                        helloTextView.setTextColor(Color.RED);
                                                        helloTextView.setTextSize(16);
                                                        TransitionManager.beginDelayedTransition(infoCard, new Slide(Gravity.TOP));
                                                        infoCard.setVisibility(View.VISIBLE);

                                                        topBlurView.setVisibility(View.VISIBLE);
                                                        topBlurView.animate().translationY(0).setDuration(200).setStartDelay(0).start();
                                                    }

                                                }


                                            });
                                } else {
                                    helloTextView.setText("Incorrect Password");
                                    helloTextView.setTextColor(Color.RED);
                                    helloTextView.setTextSize(16);
                                    TransitionManager.beginDelayedTransition(infoCard, new Slide(Gravity.TOP));
                                    infoCard.setVisibility(View.VISIBLE);

                                    topBlurView.setVisibility(View.VISIBLE);
                                    topBlurView.animate().translationY(0).setDuration(200).setStartDelay(0).start();
                                }
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        helloTextView.setText("Incorrect Password");
                        helloTextView.setTextColor(Color.RED);
                        helloTextView.setTextSize(16);
                        TransitionManager.beginDelayedTransition(infoCard, new Slide(Gravity.TOP));
                        infoCard.setVisibility(View.VISIBLE);

                        topBlurView.setVisibility(View.VISIBLE);
                        topBlurView.animate().translationY(0).setDuration(200).setStartDelay(0).start();
                    }
                });
            } catch (Exception e) {
                helloTextView.setText("Enter Password");
                helloTextView.setTextColor(Color.RED);
                helloTextView.setTextSize(16);
                TransitionManager.beginDelayedTransition(infoCard, new Slide(Gravity.TOP));
                infoCard.setVisibility(View.VISIBLE);

                topBlurView.setVisibility(View.VISIBLE);
                topBlurView.animate().translationY(0).setDuration(200).setStartDelay(0).start();

                passwordEditTextLayout.setVisibility(View.VISIBLE);

            }

        }
    }

    public void updateDetails(View view) {
        updateEmail();
        getData();

    }

    public void backButton(View view) {
        startActivity(new Intent(ProfileActivity.this, SettingsActivity.class));
    }

    public void hideInfo(View view) {
        TransitionManager.beginDelayedTransition(infoCard, new Slide(Gravity.TOP));
        infoCard.setVisibility(View.GONE);

        ObjectAnimator animator = ObjectAnimator.ofFloat(topBlurView, "translationY", -200);
        animator.setDuration(300);
        animator.start();

        topBlurView.setAlpha(0);
        topBlurView.animate().alpha(1).setDuration(300).setStartDelay(0).start();
        topBlurView.setVisibility(View.GONE);
    }
}