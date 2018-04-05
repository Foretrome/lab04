package com.example.foretrome.lab04;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.design.widget.TabLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.WindowManager;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;



public class MainActivity extends AppCompatActivity {

    /**
     * Used to check if this is the first time the app is opened.
     */
    private boolean firstTime = true;

    /**
     * The resultcode used for the Login activity.
     */
    private int myReslutCode = 123;

    /**
     * the user's username.
     */
    public static String username;

    //TAG
    private String TAG = "mainActivity";

    //Firebase auth
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Hides the keyboard on startup of the activity
        getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        //If it is the users first time running the app, they will be prompted to enter an username
        checkIfFirstTime();
        username = getUsername();

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        /*
      The {@link android.support.v4.view.PagerAdapter} that will provide
      fragments for each of the sections. We use a
      {@link FragmentPagerAdapter} derivative, which will keep every
      loaded fragment in memory. If this becomes too memory intensive, it
      may be best to switch to a
      {@link android.support.v4.app.FragmentStatePagerAdapter}.
     */
        SectionsPagerAdapter mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.
        //The {@link ViewPager} that will host the section contents.
        ViewPager mViewPager = findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        TabLayout tabLayout = findViewById(R.id.tabs);

        mViewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.addOnTabSelectedListener(new TabLayout.ViewPagerOnTabSelectedListener(mViewPager));

        mAuth = FirebaseAuth.getInstance();

        //Authenticates the user
        signInAnonymously();

    }

    /**
     * gets the user's username from the preferences.
     *
     * @return - returns the username of the currently logged inn user
     */
    private String getUsername() {
        String username;
        SharedPreferences unamePref = this.getSharedPreferences("username", Context.MODE_PRIVATE);
        username = unamePref.getString("username", "BOB");
        return username;
    }

    /**
     * Checks if the user has used the app before.
     */
    private void checkIfFirstTime() {
        if (firstTime) {
            SharedPreferences fTimePref = this.getSharedPreferences("firstTime", Context.MODE_PRIVATE);
            firstTime = fTimePref.getBoolean("firstTime", true);    //true = default value
            if (firstTime) {
                Intent i = new Intent(this, Login.class);
                startActivityForResult(i, myReslutCode);
            }
        }
    }

    /**
     * Signs the user inn as an anon.
     * Copied from Firebase documentation.
     */
    private void signInAnonymously() {
        mAuth.signInAnonymously()
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInAnonymously:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInAnonymously:failure", task.getException());
                        }
                    }
                });
    }

    @Override
    protected void onStart() {
        super.onStart();
        //Checks if the user is logged in
        FirebaseUser currentUser = mAuth.getCurrentUser();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (myReslutCode == requestCode) {
            if (resultCode == RESULT_OK) {
                //FIRST TIME
                SharedPreferences fTimePref = this.getSharedPreferences("firstTime", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = fTimePref.edit();
                editor.putBoolean("firstTime", false);
                editor.apply();

                //USERNAME
                String uNameFromActivity = data.getStringExtra("usernameFromIntent");
                SharedPreferences saveUname = this.getSharedPreferences("username", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor2 = saveUname.edit();
                editor2.putString("username", uNameFromActivity);
                editor2.apply();
            }
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

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            switch (position) {
                case 0:
                    //Uses a bundle to send constructor data since a constructor can't be used in a fragment
                    Bundle chatBundle = new Bundle();
                    Log.d("TEST", "Sets username in the bundle: " + username);
                    chatBundle.putString("username", getUsername()); //Sends the username

                    Chat chatFragment = new Chat();
                    chatFragment.setArguments(chatBundle);  //Attaches the username to the fragment. Have to be done before the fragment is added to the fragmentManager

                    return chatFragment;

                case 1:
                    Friends friendsFragment = new Friends();
                    return friendsFragment;
            }
            return null;
        }

        @Override
        public int getCount() {
            // Show 2 total pages.
            return 2;
        }
    }

    public Context getContext(){
        return this;
    }
}
