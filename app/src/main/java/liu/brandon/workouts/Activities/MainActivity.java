package liu.brandon.workouts.Activities;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ArgbEvaluator;
import android.animation.FloatEvaluator;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.ActionBar;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.drawable.ColorDrawable;
import android.opengl.Visibility;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.Toolbar;
import android.util.AttributeSet;
import android.util.Log;
import android.util.Xml;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import org.w3c.dom.Attr;
import org.xmlpull.v1.XmlPullParser;

import liu.brandon.workouts.Adapters.IconAdapter;
import liu.brandon.workouts.Fragments.StartFragment;
import liu.brandon.workouts.R;
import liu.brandon.workouts.Fragments.AboutFragment;
import liu.brandon.workouts.Fragments.ExercisesFragment;
import liu.brandon.workouts.UI.FloatingActionButton;
import liu.brandon.workouts.Fragments.HomeFragment;
import liu.brandon.workouts.Fragments.RoutinesFragment;
import liu.brandon.workouts.UI.ScrimInsetsFrameLayout;
import liu.brandon.workouts.Fragments.SettingsFragment;


public class MainActivity extends ActionBarActivity{

    //Activity
    private static final String TAG = "MainActivity";
    private final static int currentapiVersion = android.os.Build.VERSION.SDK_INT;
    private Context mContext;

    //Navigation Drawer
    private IconAdapter mIconAdapter;
    private DrawerLayout mDrawerLayout;
    private android.support.v7.app.ActionBarDrawerToggle mDrawerToggle;
    private ListView mDrawerList;

    //Fragments
    private FragmentManager mSupportFragmentManager;
    private FrameLayout mFragmentLayout;
    private android.support.v7.widget.Toolbar mToolbar;
    private int mCurrentPosition = 0;

    //FAB and Action Menu
    private LinearLayout mFloatingActionMenuContainer;
    private RelativeLayout mRoutinesMenuContainer;
    private RelativeLayout mExercisesMenuContainer;
    private FloatingActionButton mMainButton;
    private FloatingActionButton mExercisesButton;
    private FloatingActionButton mRoutinesButton;
    private boolean isExpanded = false;
    private View mOverlay;

    //Start Screen
    private boolean isStart = false;
    private ValueAnimator mBarAnimator;
    private ValueAnimator mStatusAnimator;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mContext = this;

        initToolbar();
        initFragments();
        initNavDrawer();
        initActionButton();

    }

    private void initToolbar(){
        mToolbar = (android.support.v7.widget.Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle("Home");
    }

    private void initFragments(){
        mFragmentLayout = (FrameLayout)findViewById(R.id.fragment_framelayout);
        mSupportFragmentManager = getSupportFragmentManager();

        HomeFragment mHomeFragment = new HomeFragment();
        mSupportFragmentManager.beginTransaction().add(R.id.fragment_framelayout, mHomeFragment).commit();
    }

    private void initNavDrawer(){
        mDrawerLayout = (DrawerLayout)findViewById(R.id.drawer_layout);
        mDrawerLayout.setStatusBarBackgroundColor(getResources().getColor(R.color.primary_dark));
        mDrawerToggle = new android.support.v7.app.ActionBarDrawerToggle(this, mDrawerLayout, mToolbar, R.string.app_name, R.string.app_name);
        mDrawerToggle.setToolbarNavigationClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "Navigation Listener onCLick" + String.valueOf(isStart));
                onBackPressed();
            }
        });
        mDrawerLayout.setDrawerListener(mDrawerToggle);
        mIconAdapter = new IconAdapter(this, R.layout.drawer_list_item, getResources().getStringArray(R.array.drawer_list));
        mDrawerList = (ListView)findViewById(R.id.drawer_list);
        mDrawerList.setAdapter(mIconAdapter);
        mDrawerList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Log.d(TAG, String.valueOf(mFragmentLayout.getId()));
                view.setSelected(true);
                mDrawerLayout.closeDrawers();

                if (mCurrentPosition != position) {
                    setFragment(position);
                }

            }
        });

        mMainButton = (FloatingActionButton)findViewById(R.id.main_FAB);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
        {
            ScrimInsetsFrameLayout mLayout = (ScrimInsetsFrameLayout)findViewById(R.id.left_drawer);
            mLayout.setElevation(30);
        }
    }

    private void initActionButton(){
        mFloatingActionMenuContainer = (LinearLayout)findViewById(R.id.floating_action_menu_container);
        mExercisesMenuContainer = (RelativeLayout)findViewById(R.id.exercise_button_container);
        mRoutinesMenuContainer = (RelativeLayout)findViewById(R.id.routine_button_container);

        mMainButton = (FloatingActionButton)findViewById(R.id.main_FAB);
        mExercisesButton = (FloatingActionButton)findViewById(R.id.exercise_FAB);
        mRoutinesButton = (FloatingActionButton)findViewById(R.id.routine_FAB);

        mMainButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!isStart)
                    initStartScreen();
                else{
                    addWorkout();
                }
            }
        });

        mOverlay = findViewById(R.id.overlay);

        mExercisesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, AddExerciseActivity.class);
                startActivity(intent);
            }
        });

    }

    private void initStartScreen(){

        isStart = true;

        android.support.v7.app.ActionBar bar = getSupportActionBar();
        bar.setTitle("Start");

        startAnimations();
        toggleDrawer();
    }

    private void addWorkout(){

        if(isExpanded)
            collapseAnimations();
        else
            expansionAnimations();

    }

    @SuppressWarnings("deprecation")
    @TargetApi(21)
    private void startAnimations(){

        Integer colorInit = getResources().getColor(R.color.primary);
        Integer colorFinal = getResources().getColor(R.color.blue_grey_600);

        ValueAnimator colorAnimation = ValueAnimator.ofObject(new ArgbEvaluator(), colorInit, colorFinal);
        colorAnimation.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {

            private ColorDrawable mColorDrawable;

            @Override
            public void onAnimationUpdate(ValueAnimator animator) {
                mColorDrawable = new ColorDrawable((Integer)animator.getAnimatedValue());
                getSupportActionBar().setBackgroundDrawable(mColorDrawable);
            }
        });
        mBarAnimator = colorAnimation;
        colorAnimation.start();

        if(currentapiVersion >= Build.VERSION_CODES.LOLLIPOP){

            Integer colorInitDark = getResources().getColor(R.color.primary_dark);
            Integer colorFinalDark = getResources().getColor(R.color.blue_grey_800);

            ValueAnimator colorAnimationDark = ValueAnimator.ofObject(new ArgbEvaluator(), colorInitDark, colorFinalDark);
            colorAnimationDark.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator animation) {
                    getWindow().setStatusBarColor((Integer) animation.getAnimatedValue());
                }
            });
            mStatusAnimator = colorAnimationDark;
            colorAnimationDark.start();
        }

        mMainButton.setImageResource(R.drawable.ic_content_add);

        Fragment startFragment = new StartFragment();
        FragmentTransaction transaction = mSupportFragmentManager.beginTransaction();
        transaction.replace(mFragmentLayout.getId(), startFragment);
        transaction.commit();
    }

    @SuppressWarnings("deprecation")
    @TargetApi(21)
    private void expansionAnimations(){
        mMainButton.runAnimation(isExpanded);
        isExpanded = true;

        final Integer colorStart = getResources().getColor(R.color.accent);
        final Integer colorFinal = getResources().getColor(R.color.red_600);

        ValueAnimator buttonAnimation = ValueAnimator.ofObject(new ArgbEvaluator(), colorStart, colorFinal);
        buttonAnimation.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {

            @Override
            public void onAnimationUpdate(ValueAnimator animator) {

                if (currentapiVersion >= Build.VERSION_CODES.JELLY_BEAN) {
                    mMainButton.setBackground(mMainButton.getRoundedDrawable((Integer) animator.getAnimatedValue(), getResources().getColor(R.color.red_900),
                            getResources().getColor(R.color.red_300)));
                } else {
                    mMainButton.setBackgroundDrawable(mMainButton.getRoundedDrawable((Integer) animator.getAnimatedValue(), getResources().getColor(R.color.red_900),
                            getResources().getColor(R.color.red_300)));
                }

            }
        });



        final Integer overlayVisible = getResources().getColor(R.color.overlay_visible);
        final Integer overlayInvisible = getResources().getColor(R.color.overlay_invisible);

        ValueAnimator overlayAnimation = ValueAnimator.ofObject(new ArgbEvaluator(), overlayInvisible, overlayVisible);
        overlayAnimation.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {

            private ColorDrawable mColorDrawable;

            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                mColorDrawable = new ColorDrawable((Integer)animation.getAnimatedValue());
                mOverlay.setBackground(mColorDrawable);
            }
        });

        final Float initAlpha = 0f;
        final Float finalAlpha = 100f;

        AlphaAnimation alphaAnimation = new AlphaAnimation(initAlpha, finalAlpha);
        alphaAnimation.setDuration(2000);


        buttonAnimation.start();
        mOverlay.setVisibility(View.VISIBLE);
        mRoutinesMenuContainer.setVisibility(View.VISIBLE);
        mExercisesMenuContainer.setVisibility(View.VISIBLE);
        mExercisesMenuContainer.startAnimation(alphaAnimation);
        mRoutinesMenuContainer.startAnimation(alphaAnimation);
        overlayAnimation.start();



    }

    @SuppressWarnings("deprecation")
    @TargetApi(21)
    private void collapseAnimations() {
        mMainButton.runAnimation(isExpanded);
        isExpanded = false;
        final Integer colorFinal = getResources().getColor(R.color.accent);
        final Integer colorStart = getResources().getColor(R.color.red_600);

        ValueAnimator buttonAnimation = ValueAnimator.ofObject(new ArgbEvaluator(), colorStart, colorFinal);
        buttonAnimation.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {

            @Override
            public void onAnimationUpdate(ValueAnimator animator) {

                if (currentapiVersion >= Build.VERSION_CODES.JELLY_BEAN) {
                    mMainButton.setBackground(mMainButton.getRoundedDrawable((Integer) animator.getAnimatedValue(), getResources().getColor(R.color.pressed),
                            getResources().getColor(R.color.ripple)));
                } else {
                    mMainButton.setBackgroundDrawable(mMainButton.getRoundedDrawable((Integer) animator.getAnimatedValue(), getResources().getColor(R.color.pressed),
                            getResources().getColor(R.color.ripple)));
                }

            }
        });

        final Integer overlayVisible = getResources().getColor(R.color.overlay_visible);
        final Integer overlayInvisible = getResources().getColor(R.color.overlay_invisible);

        ValueAnimator overlayAnimation = ValueAnimator.ofObject(new ArgbEvaluator(), overlayVisible, overlayInvisible);
        overlayAnimation.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {

            private ColorDrawable mColorDrawable;

            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                mColorDrawable = new ColorDrawable((Integer)animation.getAnimatedValue());
                mOverlay.setBackground(mColorDrawable);
            }
        });

        final Float finalAlpha = 0f;
        final Float initAlpha = 100f;

        AlphaAnimation alphaAnimation = new AlphaAnimation(initAlpha, finalAlpha);

        mRoutinesMenuContainer.startAnimation(alphaAnimation);
        mExercisesMenuContainer.startAnimation(alphaAnimation);
        mExercisesMenuContainer.setVisibility(View.GONE);
        mRoutinesMenuContainer.setVisibility(View.GONE);
        overlayAnimation.start();
        buttonAnimation.start();

    }

    private void toggleDrawer(){
        if (isStart){
            mDrawerToggle.setDrawerIndicatorEnabled(false);
            mDrawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        else{
            getSupportActionBar().setDisplayHomeAsUpEnabled(false);
            mDrawerToggle.setDrawerIndicatorEnabled(true);
            mDrawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED);
        }
    }

    @Override
    public void onBackPressed(){
        Log.d(TAG, "onBackPressed, isStart = " + String.valueOf(isStart));
        if(mDrawerLayout.isDrawerOpen(Gravity.START|Gravity.LEFT)){
            mDrawerLayout.closeDrawers();
            return;
        }

        if(isExpanded){
            collapseAnimations();
            return;
        }

        if (isStart){
            isStart = false;
            mBarAnimator.reverse();
            if(currentapiVersion >= Build.VERSION_CODES.LOLLIPOP)
                mStatusAnimator.reverse();

            setFragment(mCurrentPosition);
            mMainButton.setImageResource(R.drawable.ic_exercise);
            toggleDrawer();
            return;
        }

        if(mCurrentPosition!=0)
        {
            setFragment(0);
        }

        else{
            Log.d(TAG, "super onBackPressed");
            super.onBackPressed();
        }
    }

    private void setFragment(int position){

        Fragment mPlaceHolderFragment = null;

        switch(position) {
            case 0:
                mPlaceHolderFragment = new HomeFragment();
                getSupportActionBar().setTitle("Home");
                break;
            case 1:
                mPlaceHolderFragment = new RoutinesFragment();
                getSupportActionBar().setTitle("Routines");
                break;
            case 2:
                mPlaceHolderFragment = new ExercisesFragment();
                getSupportActionBar().setTitle("Exercises");
                break;
            case 3:
                mPlaceHolderFragment = new SettingsFragment();
                getSupportActionBar().setTitle("Settings");
                break;
            case 4:
                mPlaceHolderFragment = new AboutFragment();
                getSupportActionBar().setTitle("About");
                break;
        }

        FragmentTransaction transaction = mSupportFragmentManager.beginTransaction();
        transaction.replace(mFragmentLayout.getId(), mPlaceHolderFragment);
        transaction.commit();
        mCurrentPosition = position;
    }

    @Override
    public void onResume(){
        Log.d(TAG, "onResume");
        super.onResume();
        Intent intent = getIntent();
        Boolean start = intent.getBooleanExtra("start", false);
        if(start){
            mMainButton.performClick();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        Log.d(TAG, String.valueOf(id));

        if (mDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }


        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            Log.d(TAG, "Settings");
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        mDrawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        mDrawerToggle.onConfigurationChanged(newConfig);
    }


}
