package timeline.android.digitalwellbeing.TYProject.ui;

import android.animation.ObjectAnimator;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.view.WindowCompat;
import androidx.core.widget.NestedScrollView;

import android.transition.AutoTransition;
import android.transition.TransitionManager;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.airbnb.lottie.LottieAnimationView;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.util.Objects;

import timeline.android.digitalwellbeing.TYProject.EducationActivity;
import timeline.android.digitalwellbeing.TYProject.FocusActivity;
import timeline.android.digitalwellbeing.TYProject.ProfileActivity;
import timeline.android.digitalwellbeing.TYProject.R;
import timeline.android.digitalwellbeing.TYProject.SaveSharedPreference;
import timeline.android.digitalwellbeing.TYProject.WorkActivity;
import timeline.android.digitalwellbeing.TYProject.util.PreferenceManager;

public class SettingsActivity extends AppCompatActivity {

    Switch mSwitchSystem;
    Switch mSwitchUninstall;
    private TextView logoutButton;
    private BottomNavigationView bottomNavigationView;


    private ImageView settings_arrow, profile_arrow, policy_arrow, about_arrow, profileImage;
    private LinearLayout settingsHiddenView, policyHiddenView, aboutHiddenView;
    private LottieAnimationView profileLottie, settingsLottie, policyLottie, aboutLottie;
    private NestedScrollView scrollView;

    private CardView homeCard, workCard, learnCard, healthCard, focusCard;


    private StorageReference storageReference;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
    FirebaseAuth firebaseAuth;
    private FirebaseAuth.AuthStateListener authListener;
    private TextView userNameText;
    private TextView occupationTextView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        WindowCompat.setDecorFitsSystemWindows(getWindow(), false);

        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        setWindowFlag(this, WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS, false);
        getWindow().setStatusBarColor(Color.TRANSPARENT);

        requestWindowFeature(Window.FEATURE_NO_TITLE); //will hide the title
        Objects.requireNonNull(getSupportActionBar()).hide(); // hide the title bar


        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        homeCard = findViewById(R.id.cardOne);
        workCard = findViewById(R.id.cardTwo);
        learnCard = findViewById(R.id.cardThree);
        healthCard = findViewById(R.id.cardFour);
        focusCard = findViewById(R.id.cardFive);


        settings_arrow = findViewById(R.id.settings_arrow_button);
        about_arrow = findViewById(R.id.about_arrow_button);
        profile_arrow = findViewById(R.id.profile_arrow_button);
        policy_arrow = findViewById(R.id.policy_arrow_button);
        settingsHiddenView = findViewById(R.id.settings_hidden_view);
        aboutHiddenView = findViewById(R.id.about_hidden_view);
        policyHiddenView = findViewById(R.id.policy_hidden_view);
        profileLottie = findViewById(R.id.profile_lottie_notification);
        settingsLottie = findViewById(R.id.settings_lottie_notification);
        policyLottie = findViewById(R.id.policy_lottie_notification);
        aboutLottie = findViewById(R.id.about_lottie_notification);
        scrollView = findViewById(R.id.nested_scroll);
        profileImage = findViewById(R.id.profile_image);
        occupationTextView = findViewById(R.id.occupation_textview);

        userNameText = findViewById(R.id.user_name_textview);
        firebaseAuth = FirebaseAuth.getInstance();
        final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        authListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user == null) {
                    // user auth state is changed - user is null
                    // launch login activity
                    startActivity(new Intent(SettingsActivity.this, SignInActivity.class));
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
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference("Users");
        getData();


        settings_arrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (settingsHiddenView.getVisibility() == View.VISIBLE) {
                    TransitionManager.beginDelayedTransition(scrollView, new AutoTransition());
                    ObjectAnimator cardAnimation1 = ObjectAnimator.ofFloat(settings_arrow, "scaleY", 1);
                    cardAnimation1.setDuration(350);
                    cardAnimation1.start();
                    settingsLottie.setMinAndMaxProgress(0.5f, 1f);
                    settingsLottie.resumeAnimation();

                    settingsHiddenView.setVisibility(View.GONE);
                } else {
                    TransitionManager.beginDelayedTransition(scrollView,
                            new AutoTransition());
                    settingsHiddenView.setVisibility(View.VISIBLE);

                    ObjectAnimator cardAnimation1 = ObjectAnimator.ofFloat(settings_arrow, "scaleY", -1);
                    cardAnimation1.setDuration(350);
                    cardAnimation1.start();
                    settingsLottie.setMinAndMaxProgress(0f, 0.5f);
                    settingsLottie.playAnimation();

                }


            }
        });

        policy_arrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (policyHiddenView.getVisibility() == View.VISIBLE) {
                    TransitionManager.beginDelayedTransition(scrollView, new AutoTransition());
                    ObjectAnimator cardAnimation1 = ObjectAnimator.ofFloat(policy_arrow, "scaleY", 1);
                    cardAnimation1.setDuration(350);
                    cardAnimation1.start();
                    policyLottie.setMinAndMaxProgress(0.5f, 1f);
                    policyLottie.resumeAnimation();

                    policyHiddenView.setVisibility(View.GONE);
                } else {
                    TransitionManager.beginDelayedTransition(scrollView,
                            new AutoTransition());
                    policyHiddenView.setVisibility(View.VISIBLE);

                    ObjectAnimator cardAnimation1 = ObjectAnimator.ofFloat(policy_arrow, "scaleY", -1);
                    cardAnimation1.setDuration(350);
                    cardAnimation1.start();
                    policyLottie.setMinAndMaxProgress(0f, 0.5f);
                    policyLottie.playAnimation();

                }


            }
        });


        about_arrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (aboutHiddenView.getVisibility() == View.VISIBLE) {
                    TransitionManager.beginDelayedTransition(scrollView, new AutoTransition());
                    ObjectAnimator cardAnimation1 = ObjectAnimator.ofFloat(about_arrow, "scaleY", 1);
                    cardAnimation1.setDuration(350);
                    cardAnimation1.start();
                    aboutLottie.setMinAndMaxProgress(0.5f, 1f);
                    aboutLottie.resumeAnimation();

                    aboutHiddenView.setVisibility(View.GONE);
                } else {
                    TransitionManager.beginDelayedTransition(scrollView,
                            new AutoTransition());
                    aboutHiddenView.setVisibility(View.VISIBLE);

                    ObjectAnimator cardAnimation1 = ObjectAnimator.ofFloat(about_arrow, "scaleY", -1);
                    cardAnimation1.setDuration(350);
                    cardAnimation1.start();
                    aboutLottie.setMinAndMaxProgress(0f, 0.5f);
                    aboutLottie.playAnimation();

                }


            }
        });


        profile_arrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(SettingsActivity.this, ProfileActivity.class));

            }
        });




        logoutButton = findViewById(R.id.logout_button);

        logoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                SaveSharedPreference.clearStatus(v.getContext());
                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(SettingsActivity.this, LoginActivity.class));
            }
        });

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setTitle(R.string.settings);
        }

        // hide system
        mSwitchSystem = findViewById(R.id.switch_system_apps);
        mSwitchSystem.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (PreferenceManager.getInstance().getSystemSettings(PreferenceManager.PREF_SETTINGS_HIDE_SYSTEM_APPS) != b) {
                    PreferenceManager.getInstance().putBoolean(PreferenceManager.PREF_SETTINGS_HIDE_SYSTEM_APPS, b);
                    setResult(1);
                }
            }
        });

        findViewById(R.id.group_system).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mSwitchSystem.performClick();
            }
        });

        // hide uninstall
        mSwitchUninstall = findViewById(R.id.switch_uninstall_appps);
        mSwitchUninstall.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (PreferenceManager.getInstance().getUninstallSettings(PreferenceManager.PREF_SETTINGS_HIDE_UNINSTALL_APPS) != b) {
                    PreferenceManager.getInstance().putBoolean(PreferenceManager.PREF_SETTINGS_HIDE_UNINSTALL_APPS, b);
                    setResult(1);
                }
            }
        });

        findViewById(R.id.group_uninstall).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mSwitchUninstall.performClick();
            }
        });

        // ignore list
        findViewById(R.id.group_ignore).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(SettingsActivity.this, IgnoreActivity.class));
            }
        });

        restoreStatus();
    }
    private void getData() {
        String uid = Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid();

        databaseReference.child(uid).addListenerForSingleValueEvent(new ValueEventListener() {

            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                userNameText.setAlpha(0f);
                occupationTextView.setAlpha(0f);

                userNameText.setText(dataSnapshot.child("name").getValue(String.class));
                occupationTextView.setText(dataSnapshot.child("occupation").getValue(String.class));

                userNameText.animate().alpha(1f).setStartDelay(0).start();
                occupationTextView.animate().alpha(1f).setStartDelay(0).start();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(SettingsActivity.this, "Fail to get data.", Toast.LENGTH_SHORT).show();
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

    private void restoreStatus() {
        mSwitchSystem.setChecked(PreferenceManager.getInstance().getSystemSettings(PreferenceManager.PREF_SETTINGS_HIDE_SYSTEM_APPS));
        mSwitchUninstall.setChecked(PreferenceManager.getInstance().getUninstallSettings(PreferenceManager.PREF_SETTINGS_HIDE_UNINSTALL_APPS));
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

    public void focusIntent(View view) {
        startActivity(new Intent(this, FocusActivity.class));
    }

    public void healthIntent(View view) {
        startActivity(new Intent(this, SocialActivity.class));
    }

    public void learnIntent(View view) {
        startActivity(new Intent(this, EducationActivity.class));
    }

    public void workIntent(View view) {
        startActivity(new Intent(this, WorkActivity.class));
    }

    public void homeIntent(View view) {
        startActivity(new Intent(this, MainActivity.class));
    }

    public void backButton(View view) {
        startActivity(new Intent(SettingsActivity.this, MainActivity.class));
    }
}
