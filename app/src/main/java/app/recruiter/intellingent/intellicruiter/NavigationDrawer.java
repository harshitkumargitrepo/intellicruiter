package app.recruiter.intellingent.intellicruiter;

import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class NavigationDrawer extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    //Background Animation
    RelativeLayout HomeFragment;

    TextView HeaderDrawerSubTitle;
    TextView HeaderDrawerTitle;
    Toolbar toolbar;

    //our database reference object
    DatabaseReference databaseUser;


    //firebase auth object
    private FirebaseAuth firebaseAuth;

    AnimationDrawable fragmentAnimationDrawable;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_navigation_drawer);



        //getting the reference of clients node
        FirebaseDatabase database = FirebaseDatabase.getInstance("https://intellicruiter.firebaseio.com/");
        databaseUser = database.getReference("users");

        /********FAILURE CHECK IF THE USER IS NOT PROPERLY SIGNED ************/
        //getting firebase auth object
        firebaseAuth = FirebaseAuth.getInstance();


        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("Intellicruiter");
        setSupportActionBar(toolbar);

        // animation layout


        /**
         * Removed the Floating button
         */
//        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
//        fab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
//            }
//        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);



        /************** To display name and email on Navigation Drawer Header *********/
        //NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        View headerView = navigationView.getHeaderView(0);
        HeaderDrawerSubTitle = (TextView) headerView.findViewById(R.id.HeaderDrawerSubTitle);
        HeaderDrawerTitle = (TextView) headerView.findViewById(R.id.HeaderDrawerTitle);

        Intent intent = getIntent();

        HeaderDrawerSubTitle.setText(intent.getStringExtra(SignInActivity.USER_EMAIL));
        HeaderDrawerTitle.setText(intent.getStringExtra(SignInActivity.USER_NAME));


        //add this line to display menu1 when the activity is loaded
        displaySelectedScreen(R.id.nav_menu1);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.navigation_drawer, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.SignOut) {
            //logging out the user
            firebaseAuth.signOut();
            //closing activity
            finish();
            //starting login activity
            startActivity(new Intent(this, SignInActivity.class));
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    /**
     * CUSTOM NAVIGATION CODE
     */

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {


        // Handle navigation view item clicks here.
//        int id = item.getItemId();
//
//        if (id == R.id.nav_menu1) {
//            // Handle the camera action
//        } else if (id == R.id.nav_menu2) {
//
//        } else if (id == R.id.nav_menu3) {
//
//        }
//        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
//        drawer.closeDrawer(GravityCompat.START);
//        calling the method displayselectedscreen and passing the id of selected menu

        displaySelectedScreen(item.getItemId());
        HomeFragment = findViewById(R.id.HomeFragment);
        fragmentAnimationDrawable = (AnimationDrawable) HomeFragment.getBackground();
        fragmentAnimationDrawable.setEnterFadeDuration(2500);
        fragmentAnimationDrawable.setExitFadeDuration(2500);
        fragmentAnimationDrawable.start();

        return true;
    }




    /**
     * ORIGNAL CODE BELOW
     */
//    @SuppressWarnings("StatementWithEmptyBody")
//    @Override
//    public boolean onNavigationItemSelected(MenuItem item) {
//        // Handle navigation view item clicks here.
//        int id = item.getItemId();
//
//        if (id == R.id.nav_camera) {
//            // Handle the camera action
//        } else if (id == R.id.nav_gallery) {
//
//        } else if (id == R.id.nav_slideshow) {
//
//        } else if (id == R.id.nav_manage) {
//
//        } else if (id == R.id.nav_share) {
//
//        } else if (id == R.id.nav_send) {
//
//        }
//
//        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
//        drawer.closeDrawer(GravityCompat.START);
//        return true;
//    }

    private void displaySelectedScreen(int itemId) {

        //creating fragment object
        Fragment fragment = null;


        //initializing the fragment object which is selected
        switch (itemId) {
            case R.id.nav_menu1: {
                fragment = new Menu1();

                break;
            }
            case R.id.nav_menu2: {
                fragment = new Menu4();
                break;
            }
            case R.id.nav_menu3: {
                fragment = new Menu3();
                break;

            }
        }

        //replacing the fragment
        if (fragment != null) {
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.content_frame, fragment);
            ft.commit();
            HomeFragment = findViewById(R.id.HomeFragment);
            fragmentAnimationDrawable = (AnimationDrawable) HomeFragment.getBackground();
            fragmentAnimationDrawable.setEnterFadeDuration(2500);
            fragmentAnimationDrawable.setExitFadeDuration(2500);
            fragmentAnimationDrawable.start();

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);

    }
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if ((keyCode == KeyEvent.KEYCODE_BACK)) {

            finish();
            startActivity(getIntent());
            overridePendingTransition(0, 0);
//            Intent intent = new Intent(getApplicationContext(), NavigationDrawer.class);
//            startActivity(intent);
        }
        return super.onKeyDown(keyCode, event);
    }



}
