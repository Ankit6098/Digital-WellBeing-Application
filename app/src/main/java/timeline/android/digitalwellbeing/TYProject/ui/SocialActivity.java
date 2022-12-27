package timeline.android.digitalwellbeing.TYProject.ui;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.core.content.ContextCompat;
import androidx.core.view.WindowCompat;
import androidx.core.widget.NestedScrollView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import android.animation.ObjectAnimator;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.provider.CalendarContract;
import android.transition.AutoTransition;
import android.transition.Fade;
import android.transition.Slide;
import android.transition.TransitionManager;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.OverScroller;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.airbnb.lottie.LottieAnimationView;
import com.amar.library.ui.StickyScrollView;
import com.amar.library.ui.interfaces.IScrollViewListener;
import com.anychart.core.annotations.Line;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.github.mmin18.widget.RealtimeBlurView;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.mikhaellopez.circularprogressbar.CircularProgressBar;
import com.squareup.picasso.Picasso;
import com.yangp.ypwaveview.YPWaveView;

import java.text.SimpleDateFormat;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Formatter;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.TimeZone;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import timeline.android.digitalwellbeing.TYProject.EducationActivity;
import timeline.android.digitalwellbeing.TYProject.FocusActivity;
import timeline.android.digitalwellbeing.TYProject.R;
import timeline.android.digitalwellbeing.TYProject.Reminders;
import timeline.android.digitalwellbeing.TYProject.WorkActivity;
import timeline.android.digitalwellbeing.TYProject.adapter.EducationContract;
import timeline.android.digitalwellbeing.TYProject.adapter.EducationTaskRecyclerAdapter;
import timeline.android.digitalwellbeing.TYProject.adapter.HealthWaterHistoryRecyclerAdapter;
import timeline.android.digitalwellbeing.TYProject.adapter.MainArticleAdapter;
import timeline.android.digitalwellbeing.TYProject.adapter.WeightRecyclerAdapter;
import timeline.android.digitalwellbeing.TYProject.contract.HealthContract;
import timeline.android.digitalwellbeing.TYProject.contract.WeightContract;
import timeline.android.digitalwellbeing.TYProject.db.EducationDBHelper;
import timeline.android.digitalwellbeing.TYProject.db.HealthDBHelper;
import timeline.android.digitalwellbeing.TYProject.db.HealthProfileDBHelper;
import timeline.android.digitalwellbeing.TYProject.model.Article;
import timeline.android.digitalwellbeing.TYProject.model.ResponseModel;
import timeline.android.digitalwellbeing.TYProject.rests.APIInterface;
import timeline.android.digitalwellbeing.TYProject.rests.ApiClient;
import timeline.android.digitalwellbeing.TYProject.service.NotifyService;
import timeline.android.digitalwellbeing.TYProject.util.AppUtil;
import timeline.android.digitalwellbeing.TYProject.util.PreferenceManager;

public class SocialActivity extends AppCompatActivity {


    private static final String TITLE = "social Mode";
    private BottomNavigationView bottomNavigationView;
    private NestedScrollView scrollView;
    private RealtimeBlurView goalsBlurView, profileBlurView;
    private CardView socialCard, socialCardBg, menuTop, card1, waterGoalsCard;
    private TextView socialCardTextView, dateTextView, logoTextView, targetWeightTextview, currentWeightTextview, goalsPercentageTextview, kgTextview, bmiTextview;
    private AppBarLayout appBarLayout;
    private CollapsingToolbarLayout collapsingToolbarLayout;
    private Toolbar toolbar;
    private CoordinatorLayout coordinatorLayout;
    private ImageView logoImage, arrow, arrowExpanded, profileArrow, profileArrowExpanded;

    private CardView workIndicate, educationIndicate, socialIndicate, focusIndicate;
    private SwitchCompat socialSwitch;

    private Calendar calendar;
    private SimpleDateFormat dateFormat;
    private String date;

    private int mDay;
    long milliSeconds, startTime, timeBuff, updateTime, pauseTime, resumeTime;
    private Handler handler;
    private boolean running, pause;
    int milliSec;
    private TextView totalTime;

    private LinearLayout hiddenView;
    private LinearLayout healthProfileHiddenView;
    private LinearLayout weightAddLayout, heightAddLayout;
    private LinearLayout weightGoalLayout;

    private EditText weightEditText, setGoalEditText, heightEditText;

    private RelativeLayout infoLayout;
    private LottieAnimationView timeLottie, bmiLottie;
    private static final String API_KEY = "9f54f6fbf38f488ebd84a5a222f52f22";


    private SQLiteDatabase mDatabase;
    private SQLiteDatabase healthDatabase;
    private HealthWaterHistoryRecyclerAdapter healthWaterHistoryRecyclerAdapter;
    private WeightRecyclerAdapter weightRecyclerAdapter;
    HealthDBHelper healthDBHelper;
    HealthProfileDBHelper healthProfileDBHelper;
    private RecyclerView waterRecyclerView, profileRecyclerView;
    private Dialog dialog;
    private int progressValue = 0;
    private RelativeLayout goalsInfoLayout;
    private CircularProgressBar circularProgressBar;

    private int totalItems = 0;
    private int totalWeight = 0;
    private YPWaveView waveView;

    private OverScroller mScroller;

    private LineChart lineChart;
    private LineDataSet lineDataSet;
    private ArrayList<ILineDataSet> dataSets;
    private LineData lineData;
    private Typeface tfLight;

    ViewPager mViewPager, viewPager2;
    TabLayout tabLayout, tabLayout2;
    String[] array, descriptionArray, weightLossArray, weightLossDescriptionArray;
    ArrayList<Integer> arrayImage, weightLossImage;

    private RadioButton radioButton1, radioButton2;

    private StorageReference storageReference;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
    FirebaseAuth firebaseAuth;
    private FirebaseAuth.AuthStateListener authListener;
    private TextView userNameText;
    private ImageView profileImage;
    private LottieAnimationView profileLottie;



    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        WindowCompat.setDecorFitsSystemWindows(getWindow(), false);

        getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        getWindow().setStatusBarColor(getResources().getColor(R.color.colorSocialMode));
        requestWindowFeature(Window.FEATURE_NO_TITLE); //will hide the title

        Objects.requireNonNull(getSupportActionBar()).hide(); // hide the title bar


        setContentView(R.layout.activity_social);
        array = new String[]{"Quality Over Quantity", "Eat Regular Meals", "Gradually Increase Portion Size", "Light Product? No Thanks!", "Track Your Calories"};
        weightLossArray = new String[]{"How to actually lose weight", "Exercise and calories burn", "Eat varied, colorful, nutritionally dense foods.", "Keep a food and weight diary", "Engage in regular physical activity and exercise"};
        descriptionArray = new String[]{getString(R.string.Gain1), getString(R.string.Gain2), getString(R.string.Gain3), getString(R.string.Gain4), getString(R.string.Gain5)};
        weightLossDescriptionArray = new String[]{getString(R.string.Loss1), getString(R.string.Loss2), getString(R.string.Loss3), getString(R.string.Loss4), getString(R.string.Loss5)};

        arrayImage = new ArrayList<Integer>();
        arrayImage.add(R.drawable.keto);
        arrayImage.add(R.drawable.salad);
        arrayImage.add(R.drawable.portion);
        arrayImage.add(R.drawable.nothanks);
        arrayImage.add(R.drawable.trackcalories);

        weightLossImage = new ArrayList<Integer>();
        weightLossImage.add(R.drawable.lossweight);
        weightLossImage.add(R.drawable.salad);
        weightLossImage.add(R.drawable.nutrition);
        weightLossImage.add(R.drawable.weightdiary);
        weightLossImage.add(R.drawable.exercise);


        findViews();
        setUpViewPager();
        setTabLayout();
        radioButtonSetup();

        tfLight = Typeface.createFromAsset(getAssets(), "OpenSans-Light.ttf");
        totalTime = findViewById(R.id.total_time_textview);

        profileLottie = findViewById(R.id.profile_lottie);
        userNameText = findViewById(R.id.user_name_textview);
        logoImage = findViewById(R.id.temp_logo);
        profileImage = findViewById(R.id.profile_image);
        bmiLottie = findViewById(R.id.bmi_lottie);
        goalsBlurView = findViewById(R.id.goal_blur_view);
        profileBlurView = findViewById(R.id.profile_blur_view);
        weightAddLayout = findViewById(R.id.weight_add_layout);
        heightAddLayout = findViewById(R.id.height_add_layout);
        weightGoalLayout = findViewById(R.id.weight_goals_add_layout);
        weightEditText = findViewById(R.id.weight_edit_text);
        heightEditText = findViewById(R.id.height_edit_text);
        setGoalEditText = findViewById(R.id.weight_goal_edit_text);
        targetWeightTextview = findViewById(R.id.target_textview);
        currentWeightTextview = findViewById(R.id.current_weight_textview);
        goalsPercentageTextview = findViewById(R.id.goals_percentage_text);
        kgTextview = findViewById(R.id.kg_text_view);
        bmiTextview = findViewById(R.id.bmi_text_view);
        circularProgressBar = findViewById(R.id.circularProgressBar);
        targetWeightTextview.setText("Your Target " + PreferenceManager.getInstance().getInt("weight_goal") + " kg");



        lineChart = findViewById(R.id.line_chart);
        lineDataSet = new LineDataSet(null, null);
        dataSets = new ArrayList<>();


        healthDBHelper = new HealthDBHelper(SocialActivity.this);
        healthProfileDBHelper = new HealthProfileDBHelper(SocialActivity.this);
        mDatabase = healthDBHelper.getWritableDatabase();
        healthDatabase = healthProfileDBHelper.getWritableDatabase();
        configureChart();


        goalsCardConfigure();
        calculateBMI();


        socialCardTextView = findViewById(R.id.social_card_textview);
        menuTop = findViewById(R.id.menu_top);
        waveView = findViewById(R.id.wave_view);
        dateTextView = findViewById(R.id.date_textview);
        logoTextView = findViewById(R.id.logo_textview);
        socialCardBg = findViewById(R.id.social_card_bg);
        scrollView = findViewById(R.id.social_scrollview);
        appBarLayout = findViewById(R.id.appBarLayout);
        collapsingToolbarLayout = findViewById(R.id.collapsing_layout);
        toolbar = findViewById(R.id.toolbar);
        coordinatorLayout = findViewById(R.id.coordinatorLayout);
        card1 = findViewById(R.id.card1);

        calendar = Calendar.getInstance();
        dateFormat = new SimpleDateFormat("EEEE, MMM d");
        date = dateFormat.format(calendar.getTime());
        dateTextView.setText(date);

        workIndicate = findViewById(R.id.work_indicate);
        educationIndicate = findViewById(R.id.education_indicate);
        socialIndicate = findViewById(R.id.social_indicate);
        focusIndicate = findViewById(R.id.focus_indicate);
        socialSwitch = findViewById(R.id.toggle_social);


        waterRecyclerView = findViewById(R.id.water_recycler_view);
        waterRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        healthWaterHistoryRecyclerAdapter = new HealthWaterHistoryRecyclerAdapter(this, getAllItems());
        waterRecyclerView.setAdapter(healthWaterHistoryRecyclerAdapter);



        /**
         *
         *
         *
         *
         *
         * On Swipe
         *
         *
         *
         *
         *
         */

        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {

                return false;

            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                removeItem((long) viewHolder.itemView.getTag());

            }

            @Override
            public void onChildDraw(@NonNull Canvas c, @NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
                super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
                final int DIRECTION_RIGHT = 1;
                final int DIRECTION_LEFT = 0;

                if (actionState == ItemTouchHelper.ACTION_STATE_SWIPE && isCurrentlyActive) {
                    int direction = dX > 0 ? DIRECTION_RIGHT : DIRECTION_LEFT;
                    int absoluteDisplacement = Math.abs((int) dX);

                    Drawable deleteIcon = ContextCompat.getDrawable(getApplicationContext(), R.drawable.ic_baseline_delete_black);
                    int intrinsicWidth = Objects.requireNonNull(deleteIcon).getIntrinsicWidth();
                    int intrinsicHeight = deleteIcon.getIntrinsicHeight();

                    switch (direction) {
                        case DIRECTION_RIGHT:
                            break;

                        case DIRECTION_LEFT:
                            View itemView = viewHolder.itemView;
                            float height = itemView.getBottom() - itemView.getTop();
                            float width = height / 3;

                            GradientDrawable bg = new GradientDrawable();
                            Paint paint = new Paint();

                            bg.setColor(Color.RED);
                            bg.setBounds(itemView.getLeft(), itemView.getTop(), itemView.getRight(), itemView.getBottom());
                            bg.setCornerRadius(22f);
                            bg.draw(c);

//                            Paint p = new Paint();
//                            p.setColor(Color.parseColor("#2F2FD3"));
//                            RectF background = new RectF(itemView.getLeft(), itemView.getTop(), dX, itemView.getBottom());
//                            c.drawRect(background, p);
//                            icon = BitmapFactory.decodeResource(getApplicationContext().getResources(), R.drawable.ic_baseline_delete_black);
//                            RectF icon_dest = new RectF(itemView.getLeft() + width, itemView.getTop() + width, itemView.getLeft() + 2 * width, itemView.getBottom() - width);
//                            c.drawBitmap(icon, , icon_dest, p);
                            int itemHeight = itemView.getBottom() - itemView.getTop();

                            int iconTop = itemView.getTop() + (itemHeight - intrinsicHeight) / 2;
                            int iconMargin = (itemHeight - intrinsicHeight) / 2;
                            int iconLeft = itemView.getRight() - iconMargin - intrinsicWidth;
                            int iconRight = itemView.getRight() - iconMargin;
                            int iconBottom = iconTop + intrinsicHeight;

                            deleteIcon.setBounds(iconLeft, iconTop, iconRight, iconBottom);
                            deleteIcon.draw(c);
                            break;
                    }

                }
            }
        }).attachToRecyclerView(waterRecyclerView);

        totalItems = Objects.requireNonNull(waterRecyclerView.getAdapter()).getItemCount();

        iterateDatabase();


        /**
         *
         *
         *
         *Firebase Storage Reference
         *
         *
         *
         */
        firebaseAuth = FirebaseAuth.getInstance();
        final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        authListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user == null) {
                    // user auth state is changed - user is null
                    // launch login activity
                    startActivity(new Intent(SocialActivity.this, SignInActivity.class));
                    finish();
                }
            }
        };

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

        String uid = Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid();

        databaseReference.child(uid).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String name = dataSnapshot.child("name").getValue(String.class);
                userNameText.setAlpha(0f);
                userNameText.setText(name);
                userNameText.animate().alpha(1f).setStartDelay(0).start();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(SocialActivity.this, "Fail to get data.", Toast.LENGTH_SHORT).show();
            }
        });

        profileRecyclerView = findViewById(R.id.profile_recycler_view);
        profileRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        weightRecyclerAdapter = new WeightRecyclerAdapter(this, getAllWeight());
        profileRecyclerView.setAdapter(weightRecyclerAdapter);

        handler = new Handler() ;
        int highScore = PreferenceManager.getInstance().getInt("healthTime");
        milliSec = highScore;
        totalTime.setText(String.format(getResources().getString(R.string.total), AppUtil.formatMilliSeconds(highScore)));

        socialSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (PreferenceManager.getInstance().getSocialSettings(PreferenceManager.PREF_SOCIAL_TOGGLE) != isChecked) {
                    PreferenceManager.getInstance().putBoolean(PreferenceManager.PREF_SOCIAL_TOGGLE, isChecked);
                    setResult(1);

                }
                if (PreferenceManager.getInstance().getWorkSettings(PreferenceManager.PREF_SOCIAL_TOGGLE)) {
                    TransitionManager.beginDelayedTransition(socialIndicate, new AutoTransition());
                    socialIndicate.setVisibility(View.VISIBLE);

                } else {
                    TransitionManager.beginDelayedTransition(socialIndicate, new Fade());
                    socialIndicate.setVisibility(View.GONE);

                }
                running = isChecked;
                if (running) {
                    startTime = SystemClock.uptimeMillis();
                    handler.postDelayed(runnable, 0);
                } else {
                    PreferenceManager.getInstance().putInt("healthTime", (int) updateTime);
                    timeBuff += milliSeconds;
                    milliSec = 0;
                    handler.removeCallbacks(runnable);
                }
            }
        });

        /**
         *
         *
         *
         *
         *
         * On Swipe Weight
         *
         *
         *
         *
         *
         */

        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {

                return false;

            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                removeWeight((long) viewHolder.itemView.getTag());

            }

            @Override
            public void onChildDraw(@NonNull Canvas c, @NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
                super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);

                final int DIRECTION_RIGHT = 1;
                final int DIRECTION_LEFT = 0;

                if (actionState == ItemTouchHelper.ACTION_STATE_SWIPE && isCurrentlyActive) {
                    int direction = dX > 0 ? DIRECTION_RIGHT : DIRECTION_LEFT;
                    int absoluteDisplacement = Math.abs((int) dX);

                    Drawable deleteIcon = ContextCompat.getDrawable(getApplicationContext(), R.drawable.ic_baseline_delete_black);
                    int intrinsicWidth = Objects.requireNonNull(deleteIcon).getIntrinsicWidth();
                    int intrinsicHeight = deleteIcon.getIntrinsicHeight();

                    if (direction == DIRECTION_LEFT) {
                        View itemView = viewHolder.itemView;
                        float height = itemView.getBottom() - itemView.getTop();
                        float width = height / 3;

                        GradientDrawable bg = new GradientDrawable();
                        Paint paint = new Paint();

                        bg.setColor(Color.RED);
                        bg.setBounds(itemView.getLeft(), itemView.getTop(), itemView.getRight(), itemView.getBottom());
                        bg.setCornerRadius(22f);
                        bg.draw(c);

                        int itemHeight = itemView.getBottom() - itemView.getTop();

                        int iconTop = itemView.getTop() + (itemHeight - intrinsicHeight) / 2;
                        int iconMargin = (itemHeight - intrinsicHeight) / 2;
                        int iconLeft = itemView.getRight() - iconMargin - intrinsicWidth;
                        int iconRight = itemView.getRight() - iconMargin;
                        int iconBottom = iconTop + intrinsicHeight;

                        deleteIcon.setBounds(iconLeft, iconTop, iconRight, iconBottom);
                        deleteIcon.draw(c);
                    }

                }
            }
        }).attachToRecyclerView(profileRecyclerView);
        totalWeight = Objects.requireNonNull(profileRecyclerView.getAdapter()).getItemCount();

        iterateDatabase();

        /**
         *
         *
         *
         *
         *
         * News Api
         *
         *
         *
         *
         *
         */


        final RecyclerView mainRecycler = findViewById(R.id.activity_main_rv);
        mainRecycler.setLayoutManager(new LinearLayoutManager(this));


        final APIInterface apiService = ApiClient.getClient().create(APIInterface.class);

        Call<ResponseModel> call = apiService.getBusinessNews("in","health",API_KEY);

        call.enqueue(new Callback<ResponseModel>() {
            @Override
            public void onResponse(@NonNull Call<ResponseModel>call, @NonNull Response<ResponseModel> response) {
                assert response.body() != null;
                if(response.body().getStatus().equals("ok")) {
                    List<Article> articleList = response.body().getArticles();
                    if(articleList.size()>0) {
                        final MainArticleAdapter mainArticleAdapter = new MainArticleAdapter(articleList);
//                        mainArticleAdapter.setOnRecyclerViewItemClickListener(this);
                        mainRecycler.setAdapter(mainArticleAdapter);
                    }
                }
            }
            @Override
            public void onFailure(Call<ResponseModel>call, Throwable t) {
                Log.e("out", t.toString());
            }
        });



        /**
         *
         *
         *
         *
         *
         * Toggle Button
         *
         *
         *
         *
         *
         */


        timeLottie = findViewById(R.id.time_lottie);
        timeLottie.setMaxProgress(0.5f);

        infoLayout = findViewById(R.id.info_layout);
        card1 = findViewById(R.id.card1);

        card1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (infoLayout.getVisibility() == View.GONE || infoLayout.getVisibility() == View.INVISIBLE) {
                    infoLayout.setTranslationY(400);
                    infoLayout.animate().translationY(0).start();
                    infoLayout.setVisibility(View.VISIBLE);

                } else {
                    TransitionManager.beginDelayedTransition(infoLayout, new Slide(Gravity.BOTTOM));
                    infoLayout.setVisibility(View.GONE);
                }
            }
        });


        goalsInfoLayout = findViewById(R.id.goals_info_layout);
        waterGoalsCard = findViewById(R.id.card2);

        waterGoalsCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (goalsInfoLayout.getVisibility() == View.GONE || goalsInfoLayout.getVisibility() == View.INVISIBLE) {
                    goalsInfoLayout.setTranslationY(400);
                    goalsInfoLayout.animate().translationY(0).start();
                    goalsInfoLayout.setVisibility(View.VISIBLE);

                } else {
                    TransitionManager.beginDelayedTransition(goalsInfoLayout, new Slide(Gravity.BOTTOM));
                    goalsInfoLayout.setVisibility(View.GONE);
                }
            }
        });

        if (PreferenceManager.getInstance().getWorkSettings(PreferenceManager.PREF_WORK_TOGGLE)) {
            TransitionManager.beginDelayedTransition(workIndicate, new AutoTransition());
            workIndicate.setVisibility(View.VISIBLE);

        } else {
            TransitionManager.beginDelayedTransition(workIndicate, new Fade());
            workIndicate.setVisibility(View.GONE);

        }

        if (PreferenceManager.getInstance().getWorkSettings(PreferenceManager.PREF_EDUCATION_TOGGLE)) {
            TransitionManager.beginDelayedTransition(educationIndicate, new AutoTransition());
            educationIndicate.setVisibility(View.VISIBLE);

        } else {
            TransitionManager.beginDelayedTransition(educationIndicate, new Fade());
            educationIndicate.setVisibility(View.GONE);

        }



        if (PreferenceManager.getInstance().getWorkSettings(PreferenceManager.PREF_FOCUS_TOGGLE)) {
            TransitionManager.beginDelayedTransition(focusIndicate, new AutoTransition());
            focusIndicate.setVisibility(View.VISIBLE);

        } else {
            TransitionManager.beginDelayedTransition(focusIndicate, new Fade());
            focusIndicate.setVisibility(View.GONE);

        }


        String activity = getIntent().getStringExtra("activity");

        if (activity != null) {
            switch (Objects.requireNonNull(activity)) {
                case "education":
                    menuTop.setVisibility(View.VISIBLE);
                    menuTop.setTranslationX(-200);
                    ObjectAnimator cardAnimation1 = ObjectAnimator.ofFloat(menuTop, "translationX", 0);
                    cardAnimation1.setDuration(350);
                    cardAnimation1.start();
                    break;
                case "home":
                    menuTop.setVisibility(View.VISIBLE);
                    menuTop.setTranslationX(-600);
                    ObjectAnimator cardAnimation2 = ObjectAnimator.ofFloat(menuTop, "translationX", 0);
                    cardAnimation2.setDuration(350);
                    cardAnimation2.start();
                    break;
                case "work":
                    menuTop.setVisibility(View.VISIBLE);
                    menuTop.setTranslationX(-400);
                    ObjectAnimator cardAnimation3 = ObjectAnimator.ofFloat(menuTop, "translationX", 0);
                    cardAnimation3.setDuration(350);
                    cardAnimation3.start();
                    break;
                case "focus":
                    menuTop.setVisibility(View.VISIBLE);
                    menuTop.setTranslationX(200);
                    ObjectAnimator cardAnimation4 = ObjectAnimator.ofFloat(menuTop, "translationX", 0);
                    cardAnimation4.setDuration(350);
                    cardAnimation4.start();
                    break;


            }
        }


        appBarLayout.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                if (Math.abs(verticalOffset) == appBarLayout.getTotalScrollRange()) {
                    //collapsed
                    socialCardBg.animate().scaleX((float)0.4).setDuration(3000);
                    socialCardBg.animate().scaleY((float)0.4).setDuration(3000);
                    socialCardBg.animate().alpha(0).setDuration(0);

                    socialCardTextView.animate().scaleX((float)0.4).setDuration(3000);
                    socialCardTextView.animate().scaleY((float)0.4).setDuration(3000);

                    socialCardTextView.setVisibility(View.VISIBLE);
                    socialCardTextView.animate().alpha(1).setDuration(0);

                    dateTextView.setVisibility(View.GONE);



                } else if (verticalOffset == 0) {
                    socialCardBg.animate().scaleX((float)1).setDuration(100);
                    socialCardBg.animate().scaleY((float)1).setDuration(100);
                    socialCardBg.animate().alpha(1).setDuration(0);

                    socialCardTextView.animate().scaleX((float)1).setDuration(100);
                    socialCardTextView.animate().scaleY((float)1).setDuration(100);
                    socialCardTextView.animate().alpha(1).setDuration(0);

                    socialCardTextView.setVisibility(View.GONE);
                    dateTextView.setVisibility(View.VISIBLE);



                } else {
                    final int scrollRange = appBarLayout.getTotalScrollRange();
                    float offsetFactor = (float) (-verticalOffset) / (float) scrollRange;
                    float scaleFactor = 1F - offsetFactor * .6F;
                    socialCardBg.animate().scaleX(scaleFactor);
                    socialCardBg.animate().scaleY(scaleFactor);
                    socialCardBg.animate().alpha((float) (scaleFactor)).setDuration(0);


                    socialCardTextView.animate().scaleX(scaleFactor);
                    socialCardTextView.animate().scaleY(scaleFactor);

                    socialCardTextView.setVisibility(View.GONE);
                    dateTextView.setVisibility(View.VISIBLE);


                }


            }

        });


        /**
         *
         *
         *
         *
         *
         * Hidden View
         *
         *
         *
         *
         *
         */


        arrow = findViewById(R.id.arrow_button);
        arrowExpanded = findViewById(R.id.arrow_expanded_button);

        hiddenView = findViewById(R.id.hidden_view);

        arrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (hiddenView.getVisibility() == View.VISIBLE) {

                    // The transition of the hiddenView is carried out
                    //  by the TransitionManager class.
                    // Here we use an object of the AutoTransition
                    // Class to create a default transition.
                    TransitionManager.beginDelayedTransition(scrollView, new AutoTransition());
                    ObjectAnimator cardAnimation1 = ObjectAnimator.ofFloat(arrow, "scaleY", 1);
                    cardAnimation1.setDuration(350);
                    cardAnimation1.start();

                    hiddenView.setVisibility(View.GONE);
                }

                // If the CardView is not expanded, set its visibility
                // to visible and change the expand more icon to expand less.
                else {

                    TransitionManager.beginDelayedTransition(scrollView,
                            new AutoTransition());
                    hiddenView.setVisibility(View.VISIBLE);

                    ObjectAnimator cardAnimation1 = ObjectAnimator.ofFloat(arrow, "scaleY", -1);
                    cardAnimation1.setDuration(350);
                    cardAnimation1.start();
                    goalsBlurView.setVisibility(View.GONE);

                }


            }
        });

        /**
         *
         *
         *
         *
         *
         * Health Card Hidden View
         *
         *
         *
         *
         *
         */



        profileArrow = findViewById(R.id.profile_arrow_button);
        healthProfileHiddenView = findViewById(R.id.profile_hidden_view);
        profileArrowExpanded = findViewById(R.id.profile_arrow_expanded_button);

        profileArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (healthProfileHiddenView.getVisibility() == View.VISIBLE) {

                    // The transition of the hiddenView is carried out
                    //  by the TransitionManager class.
                    // Here we use an object of the AutoTransition
                    // Class to create a default transition.
                    TransitionManager.beginDelayedTransition(scrollView, new AutoTransition());
                    ObjectAnimator cardAnimation1 = ObjectAnimator.ofFloat(profileArrow, "scaleY", 1);
                    cardAnimation1.setDuration(350);
                    cardAnimation1.start();

                    healthProfileHiddenView.setVisibility(View.GONE);
                }

                // If the CardView is not expanded, set its visibility
                // to visible and change the expand more icon to expand less.
                else {

                    TransitionManager.beginDelayedTransition(scrollView,
                            new AutoTransition());
                    healthProfileHiddenView.setVisibility(View.VISIBLE);

                    ObjectAnimator cardAnimation1 = ObjectAnimator.ofFloat(profileArrow, "scaleY", -1);
                    cardAnimation1.setDuration(350);
                    cardAnimation1.start();
                    profileBlurView.setVisibility(View.GONE);

                }


            }
        });

        arrowExpanded.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TransitionManager.beginDelayedTransition(scrollView, new AutoTransition());
                ObjectAnimator cardAnimation1 = ObjectAnimator.ofFloat(arrow, "scaleY", 1);
                cardAnimation1.setDuration(350);
                cardAnimation1.start();
                hiddenView.setVisibility(View.GONE);
                goalsBlurView.setVisibility(View.VISIBLE);

            }
        });

        profileArrowExpanded.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TransitionManager.beginDelayedTransition(scrollView, new AutoTransition());
                ObjectAnimator cardAnimation1 = ObjectAnimator.ofFloat(arrow, "scaleY", 1);
                cardAnimation1.setDuration(350);
                cardAnimation1.start();
                healthProfileHiddenView.setVisibility(View.GONE);
                profileBlurView.setVisibility(View.VISIBLE);

            }
        });




        bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setItemIconTintList(null);

        bottomNavigationView.setSelectedItemId(R.id.menu_social);
        BottomNavigationView.OnNavigationItemSelectedListener navListener = new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                switch (item.getItemId()) {
                    case R.id.menu_home:
                        startActivity(new Intent(getApplicationContext(), MainActivity.class).putExtra("activity", "social"));
                        overridePendingTransition(0, 0);
                        return true;
                    case R.id.menu_work:
                        startActivity(new Intent(getApplicationContext(), WorkActivity.class).putExtra("activity", "social"));
                        overridePendingTransition(0, 0);
                        return true;
                    case R.id.menu_education:
                        startActivity(new Intent(getApplicationContext(), EducationActivity.class).putExtra("activity", "social"));
                        overridePendingTransition(0, 0);
                        return true;
                    case R.id.menu_focus:

                        startActivity(new Intent(getApplicationContext(), FocusActivity.class).putExtra("activity", "social"));
                        overridePendingTransition(0, 0);
                        return true;
                    case R.id.menu_social:
                        return true;

                }
                return false;
            }
        };
        bottomNavigationView.setOnNavigationItemSelectedListener(navListener);
        restoreStatus();
    }

    private int convertDpToPx(int dp){
        return Math.round(dp * (getResources().getDisplayMetrics().xdpi / DisplayMetrics.DENSITY_DEFAULT));
    }
    private void configureChart() {
        lineDataSet.setValues(getDataValues());
        lineDataSet.setColor(Color.parseColor("#0065FF"));
        lineDataSet.setLineWidth(4);
        lineDataSet.setDrawCircles(true);
        lineDataSet.setDrawCircleHole(false);
        lineDataSet.setValueTextSize(10);
        lineDataSet.setDrawFilled(true);
        lineDataSet.setFillDrawable(ContextCompat.getDrawable(this, R.drawable.gradient));

        lineChart.getDescription().setEnabled(false);
        lineChart.setDrawGridBackground(false);

        XAxis x = lineChart.getXAxis();
        x.setEnabled(false);
        YAxis y = lineChart.getAxisLeft();
        y.setTypeface(tfLight);
        y.setLabelCount(6, false);
        y.setTextColor(Color.WHITE);
        y.setPosition(YAxis.YAxisLabelPosition.INSIDE_CHART);
        y.setDrawGridLines(false);
        y.setAxisLineColor(Color.WHITE);
        lineChart.getAxisRight().setEnabled(false);
        lineChart.getLegend().setEnabled(false);

        lineChart.animateXY(2000, 2000);


        dataSets.clear();
        dataSets.add(lineDataSet);
        lineData = new LineData(dataSets);
        lineChart.clear();
        lineChart.setData(lineData);
        lineChart.invalidate();

    }

    private ArrayList<Entry> getDataValues() {
        ArrayList<Entry> dataValues = new ArrayList<>();
        String[] columns = {WeightContract.WeightEntry.COLUMN_NAME};
        Cursor cursor = healthDatabase.query(
                WeightContract.WeightEntry.TABLE_NAME,
                null,
                null,
                null,
                null,
                null,
                null
        );

        for(int i = 0; i < cursor.getCount(); i++) {
            cursor.moveToNext();
            dataValues.add(new Entry(cursor.getFloat(0), cursor.getFloat(1)));
        }
        return dataValues;
    }

//
//    public ArrayList<String> queryXData(){
//        ArrayList<String> xNewData = new ArrayList<String>();
//        String query = "SELECT " + DAILY_DATE + " FROM " + WeightContract.WeightEntry.TABLE_NAME;
//        Cursor cursor = healthDatabase.rawQuery(query, null);
//        for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {
//            xNewData.add(cursor.getString(cursor.getColumnIndex(DAILY_DATE)));
//        }
//        cursor.close();
//        return xNewData;
//    }
//
//    public ArrayList<Float> queryYData(){
//        ArrayList<Float> yNewData = new ArrayList<Float>();
//        String query = "SELECT " + DAILY_TOTAL + " FROM " + WeightContract.WeightEntry.TABLE_NAME;
//        Cursor cursor = healthDatabase.rawQuery(query, null);
//        for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {
//            yNewData.add(cursor.getFloat(cursor.getColumnIndex(DAILY_TOTAL)));
//        }
//        cursor.close();
//        return yNewData;
//    }

    private void restoreStatus() {
        socialSwitch.setChecked(PreferenceManager.getInstance().getSocialSettings(PreferenceManager.PREF_SOCIAL_TOGGLE));
    }

    public int dpToPx(int dp) {
        DisplayMetrics displayMetrics = getApplicationContext().getResources().getDisplayMetrics();
        return Math.round(dp * (displayMetrics.xdpi / DisplayMetrics.DENSITY_DEFAULT));
    }

    public int pxToDp(int px) {
        DisplayMetrics displayMetrics = getApplicationContext().getResources().getDisplayMetrics();
        return Math.round(px / (displayMetrics.xdpi / DisplayMetrics.DENSITY_DEFAULT));
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


    /**
     *
     *
     *
     *
     *
     * Drinking Database
     *
     *
     *
     *
     *
     *
     *
     */

    @RequiresApi(api = Build.VERSION_CODES.O)
    public void addItemToList(View view) {

        addItem();

    }

    @Override
    protected void onPause() {
        super.onPause();
        pauseTime = SystemClock.uptimeMillis();
        PreferenceManager.getInstance().putInt("healthPauseTime", (int) pauseTime);

    }

    @Override
    protected void onResume() {
        super.onResume();
        if (running)
            resumeTime = SystemClock.uptimeMillis() - PreferenceManager.getInstance().getInt("healthPauseTime");


    }


    public Runnable runnable = new Runnable() {

        public void run() {

            milliSeconds = SystemClock.uptimeMillis() - startTime + milliSec;
            updateTime = timeBuff + milliSeconds + resumeTime;

            totalTime.setText(String.format(getResources().getString(R.string.total), AppUtil.formatMilliSeconds(updateTime)));

            handler.postDelayed(this, 0);
        }

    };


    @RequiresApi(api = Build.VERSION_CODES.O)
    private void addItem() {


        SimpleDateFormat formatter = new SimpleDateFormat("EEE, MMM d, HH:mm");
        Date date = new Date();

        String time = String.valueOf(formatter.format(date));

        ContentValues cv = new ContentValues();
        cv.put(HealthContract.HealthEntry.COLUMN_NAME, "Drank 200ml");
        cv.put(HealthContract.HealthEntry.TIME, time);

        mDatabase.insert(HealthContract.HealthEntry.TABLE_NAME, null, cv);
        healthWaterHistoryRecyclerAdapter.swapCursor(getAllItems());



        totalItems = Objects.requireNonNull(waterRecyclerView.getAdapter()).getItemCount();
//        progressValue = 0;

        iterateDatabase();

    }
    private void removeItem(long id) {
        mDatabase.delete(HealthContract.HealthEntry.TABLE_NAME,
                HealthContract.HealthEntry._ID + "=" + id, null);
        healthWaterHistoryRecyclerAdapter.swapCursor(getAllItems());


        totalItems = Objects.requireNonNull(waterRecyclerView.getAdapter()).getItemCount();
        progressValue = 0;
        iterateDatabase();

    }



    private void iterateDatabase() {
        progressValue = totalItems * 200;
        if (progressValue >= 1600)
            waveView.setTextColor(Color.WHITE);
        else
            waveView.setTextColor(Color.parseColor("#4E89FB"));
        waveView.setProgress(progressValue);


    }

    private Cursor getAllItems() {
        return mDatabase.query(
                HealthContract.HealthEntry.TABLE_NAME,
                null,
                null,
                null,
                null,
                null,
                HealthContract.HealthEntry.COLUMN_TIMESTAMP + " DESC"
        );
    }

    public void addReminder(View view) {
        dialog = new Dialog(SocialActivity.this);
        dialog.setContentView(R.layout.floating_popup);

        final TextView textView = dialog.findViewById(R.id.date);
        Button select,add;
        select = dialog.findViewById(R.id.selectDate);
        add = dialog.findViewById(R.id.addButton);
        final EditText message = dialog.findViewById(R.id.message);


        final Calendar newCalender = Calendar.getInstance();
        select.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerDialog dialog = new DatePickerDialog(SocialActivity.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, final int year, final int month, final int dayOfMonth) {

                        final Calendar newDate = Calendar.getInstance();
                        Calendar newTime = Calendar.getInstance();
                        TimePickerDialog time = new TimePickerDialog(SocialActivity.this, new TimePickerDialog.OnTimeSetListener() {
                            @Override
                            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {

                                newDate.set(year,month,dayOfMonth,hourOfDay,minute,0);
                                Calendar tem = Calendar.getInstance();
                                Log.w("TIME",System.currentTimeMillis()+"");
                                if(newDate.getTimeInMillis()-tem.getTimeInMillis()>0)
                                    textView.setText(newDate.getTime().toString());
                                else
                                    Toast.makeText(SocialActivity.this,"Invalid time",Toast.LENGTH_SHORT).show();

                            }
                        },newTime.get(Calendar.HOUR_OF_DAY),newTime.get(Calendar.MINUTE),true);
                        time.show();

                    }
                },newCalender.get(Calendar.YEAR),newCalender.get(Calendar.MONTH),newCalender.get(Calendar.DAY_OF_MONTH));

                dialog.getDatePicker().setMinDate(System.currentTimeMillis());
                dialog.show();

            }
        });


        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (textView.getText().toString() != null) {
                    Reminders reminders = new Reminders();
                    reminders.setMessage(message.getText().toString().trim());
                    Date remind = new Date(textView.getText().toString().trim());
                    reminders.setRemindDate(remind);
                    Log.e("ID chahiye", reminders.getId() + "");

                    Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("GMT+5:30"));
                    calendar.setTime(remind);
                    calendar.set(Calendar.SECOND, 0);
                    Intent intent = new Intent(SocialActivity.this, NotifyService.class);
                    intent.putExtra("Message", "Water Reminder");
                    intent.putExtra("RemindDate", reminders.getRemindDate().toString());
                    intent.putExtra("id", reminders.getId());
                    PendingIntent intent1 = PendingIntent.getBroadcast(SocialActivity.this, reminders.getId(), intent, PendingIntent.FLAG_UPDATE_CURRENT);
                    AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
                    alarmManager.setExact(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), intent1);

                    Toast.makeText(SocialActivity.this, "Inserted Successfully", Toast.LENGTH_SHORT).show();
                    dialog.dismiss();
                }

            }
        });


        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.show();
    }

    /**
     *
     *
     *
     *
     *
     * Weight Database
     *
     *
     *
     *
     *
     *
     *
     */

    public void addWeightToList(View view) {
        if (weightAddLayout.getVisibility() == View.GONE) {
            TransitionManager.beginDelayedTransition(healthProfileHiddenView, new AutoTransition());
            weightAddLayout.setVisibility(View.VISIBLE);
        } else {
            weightAddLayout.setVisibility(View.GONE);

            TransitionManager.beginDelayedTransition(healthProfileHiddenView, new AutoTransition());

        }
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public void addButton(View view) {
        SimpleDateFormat formatter = new SimpleDateFormat("EEE, MMM d");
        Date date = new Date();

        String time = String.valueOf(formatter.format(date));
        String weight = weightEditText.getText().toString();
        ContentValues cv = new ContentValues();
        cv.put(WeightContract.WeightEntry.COLUMN_NAME, weight);
        cv.put(WeightContract.WeightEntry.TIME, time);

        healthDatabase.insert(WeightContract.WeightEntry.TABLE_NAME, null, cv);
        weightRecyclerAdapter.swapCursor(getAllWeight());


        weightEditText.setText("");
        configureChart();
        totalWeight = Objects.requireNonNull(profileRecyclerView.getAdapter()).getItemCount();
//        progressValue = 0;
        goalsCardConfigure();
        calculateBMI();

    }

    private void removeWeight(long id) {
        healthDatabase.delete(WeightContract.WeightEntry.TABLE_NAME,
                WeightContract.WeightEntry._ID + "=" + id, null);
        weightRecyclerAdapter.swapCursor(getAllWeight());

        configureChart();

        totalWeight = Objects.requireNonNull(profileRecyclerView.getAdapter()).getItemCount();
        progressValue = 0;
        goalsCardConfigure();
        calculateBMI();
    }


    private Cursor getAllWeight() {
        return healthDatabase.query(
                WeightContract.WeightEntry.TABLE_NAME,
                null,
                null,
                null,
                null,
                null,
                WeightContract.WeightEntry.COLUMN_TIMESTAMP + " DESC"
        );
    }

    public void addWeightGoal(View view) {
        if (weightGoalLayout.getVisibility() == View.GONE) {
            TransitionManager.beginDelayedTransition(healthProfileHiddenView, new AutoTransition());
            weightGoalLayout.setVisibility(View.VISIBLE);
        } else {
            weightGoalLayout.setVisibility(View.GONE);

            TransitionManager.beginDelayedTransition(healthProfileHiddenView, new AutoTransition());

        }
    }

    public void setGoalButton(View view) {
        String text = setGoalEditText.getText().toString();
        setGoalEditText.setText("");
        PreferenceManager.getInstance().putInt("weight_goal", (int) Integer.parseInt(text));
        targetWeightTextview.setText("Your Target " + PreferenceManager.getInstance().getInt("weight_goal") + " kg");
        goalsCardConfigure();
        calculateBMI();

    }

    public void goalsCardConfigure() {
        progressValue = 0;

        String selectQueryLast = "SELECT  * FROM " + WeightContract.WeightEntry.TABLE_NAME + " ORDER BY " + WeightContract.WeightEntry.COLUMN_TIMESTAMP + " DESC";
        Cursor cursor = healthDatabase.rawQuery(selectQueryLast, null);
        if (cursor.moveToNext()) {
            progressValue = Integer.parseInt(cursor.getString(cursor.getColumnIndex("weight")));
        }
        cursor.close();

        kgTextview.setText(String.valueOf(progressValue) + " kg");
        currentWeightTextview.setText("Current Weight " + progressValue + " kg");

        calculatePercentage();

    }

    public void setHeight(View view) {
        if (heightAddLayout.getVisibility() == View.GONE) {
            TransitionManager.beginDelayedTransition(healthProfileHiddenView, new AutoTransition());
            heightAddLayout.setVisibility(View.VISIBLE);
        } else {
            heightAddLayout.setVisibility(View.GONE);

            TransitionManager.beginDelayedTransition(healthProfileHiddenView, new AutoTransition());

        }
    }

    public void addHeight(View view) {
        PreferenceManager.getInstance().putInt("height", Integer.parseInt(heightEditText.getText().toString()));
        calculateBMI();
    }

    public void calculatePercentage() {
        float targetWeight = 0;
        float baseValue = 0;
        float currentWeight = 0;
        float newTargetValue = 0;
        int percentage = 0;

        String selectQueryFirst = "SELECT  * FROM " + WeightContract.WeightEntry.TABLE_NAME + " ORDER BY " + WeightContract.WeightEntry.COLUMN_NAME;
        Cursor cursor2 = healthDatabase.rawQuery(selectQueryFirst, null);
        if (cursor2.moveToNext()) {
            baseValue = Float.parseFloat(cursor2.getString(cursor2.getColumnIndex("weight")));
        }
        cursor2.close();


        currentWeight = progressValue;
        targetWeight = PreferenceManager.getInstance().getInt("weight_goal");
        newTargetValue = targetWeight - baseValue;
        percentage = (int) (((currentWeight - baseValue) / newTargetValue) * 100);
        circularProgressBar.setProgressWithAnimation(percentage);
        goalsPercentageTextview.setText(percentage + "%");
        calculateBMI();


    }

    public void calculateBMI() {
        double height = 0;
        double weight = 0;
        progressValue = 0;

        String selectQueryLast = "SELECT  * FROM " + WeightContract.WeightEntry.TABLE_NAME + " ORDER BY " + WeightContract.WeightEntry.COLUMN_TIMESTAMP + " DESC";
        Cursor cursor = healthDatabase.rawQuery(selectQueryLast, null);
        if (cursor.moveToNext()) {
            progressValue = Integer.parseInt(cursor.getString(cursor.getColumnIndex("weight")));
        }
        cursor.close();
        height = (double) PreferenceManager.getInstance().getInt("height") / 100;
        weight = progressValue;
        double bmi = weight / (height * height);
        Formatter formatter = new Formatter();
        formatter.format("%s %.2f", "BMI", bmi);
        bmiTextview.setText(formatter.toString());

        if (bmi <= 18.50)
            bmiLottie.setMinAndMaxProgress(0f, 0.2f);
        else if (bmi > 18.50 && bmi <= 24.90) {
            bmiLottie.setMinAndMaxProgress(0f, 0.44f);
            bmiLottie.resumeAnimation();
        }
        else if (bmi > 24.90 && bmi <= 29.90)
            bmiLottie.setMinAndMaxProgress(0f, 0.6f);
        else
            bmiLottie.setMinAndMaxProgress(0f, 1f);

    }

    public class SamplePagerAdapter extends FragmentPagerAdapter {

        public SamplePagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {

            switch (position){
                case 0:
                    return CardsFragment.newInstance(array[position], descriptionArray[position], arrayImage.get(position));
                case 1:
                    return CardsFragment.newInstance(array[position], descriptionArray[position], arrayImage.get(position));
                case 2:
                    return CardsFragment.newInstance(array[position], descriptionArray[position], arrayImage.get(position));
                case 3:
                    return CardsFragment.newInstance(array[position], descriptionArray[position], arrayImage.get(position));
                case 4:
                    return CardsFragment.newInstance(array[position], descriptionArray[position], arrayImage.get(position));
                default:
                    return null;
            }

        }

        @Override
        public int getCount() {
            return 5;
        }

    }

    public class WeightLossAdapter extends FragmentPagerAdapter {

        public WeightLossAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {

            switch (position){
                case 0:
                    return WeightLossFragment.newInstance(weightLossArray[position], weightLossDescriptionArray[position], weightLossImage.get(position));
                case 1:
                    return WeightLossFragment.newInstance(weightLossArray[position], weightLossDescriptionArray[position], weightLossImage.get(position));
                case 2:
                    return WeightLossFragment.newInstance(weightLossArray[position], weightLossDescriptionArray[position], weightLossImage.get(position));
                case 3:
                    return WeightLossFragment.newInstance(weightLossArray[position], weightLossDescriptionArray[position], weightLossImage.get(position));
                case 4:
                    return WeightLossFragment.newInstance(weightLossArray[position], weightLossDescriptionArray[position], weightLossImage.get(position));
                default:
                    return null;
            }

        }

        @Override
        public int getCount() {
            return 5;
        }

    }

    void findViews(){
        mViewPager = (ViewPager) findViewById(R.id.pager);
        tabLayout = (TabLayout) findViewById(R.id.tab_dots);

        viewPager2 = (ViewPager) findViewById(R.id.pager2);
        tabLayout2 = (TabLayout) findViewById(R.id.tab_dots2);

        radioButton1 = findViewById(R.id.radio_button1);
        radioButton2 = findViewById(R.id.radio_button2);
    }

    void setUpViewPager(){
        mViewPager.setAdapter(new SamplePagerAdapter(getSupportFragmentManager()));
        mViewPager.setClipToPadding(false);
        mViewPager.setPadding(60,0,60,0);
        mViewPager.setPageMargin(40);

        viewPager2.setAdapter(new WeightLossAdapter(getSupportFragmentManager()));
        viewPager2.setClipToPadding(false);
        viewPager2.setPadding(60,0,60,0);
        viewPager2.setPageMargin(40);
    }

    void setTabLayout(){
        tabLayout.setupWithViewPager(mViewPager, true);
        tabLayout2.setupWithViewPager(viewPager2, true);
    }

    void radioButtonSetup() {
        radioButton1.setChecked(true);
        radioButton2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TransitionManager.beginDelayedTransition(viewPager2, new Slide(Gravity.RIGHT));
                viewPager2.setVisibility(View.VISIBLE);
                mViewPager.setVisibility(View.INVISIBLE);
            }
        });

        radioButton1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TransitionManager.beginDelayedTransition(mViewPager, new Slide(Gravity.LEFT));

                viewPager2.setVisibility(View.INVISIBLE);
                mViewPager.setVisibility(View.VISIBLE);
            }
        });
    }
}
