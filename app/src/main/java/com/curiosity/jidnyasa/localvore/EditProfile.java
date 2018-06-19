
package com.curiosity.jidnyasa.localvore;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;

public class EditProfile extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    public static final int MY_FEED = 1;
    public static final int LOGOUT = 2;
    public static final int SEARCH_RECIPE = 3;
    private FirebaseAuth mAuth;
    FirebaseUser user;

    EditText changeUserName;
    TextView viewEmailID;
    Button saveProfileChanges;

    String name, viewEmail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mAuth = FirebaseAuth.getInstance();
        user = FirebaseAuth.getInstance().getCurrentUser();

        //Views
        changeUserName = findViewById(R.id.editUserID);
        viewEmailID = findViewById(R.id.viewEmailId);

        //Button
        saveProfileChanges = findViewById(R.id.saveProfile);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        //Get username and email from Firebase
        name = user.getDisplayName();
        viewEmail = user.getEmail();

        //Set the username and email from Firebase
        changeUserName.setText(name);
        viewEmailID.setText(viewEmail);

        //If save button is clicked
        saveProfileChanges.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String changedUserName = changeUserName.getText().toString();
                //Saving user name
                FirebaseUser user = mAuth.getCurrentUser();
                UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                        .setDisplayName(changedUserName).build();
                user.updateProfile(profileUpdates);
            }
        });
    }

    //Keypad disappears if you touch anywhere on the screen
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        InputMethodManager imm = (InputMethodManager)getSystemService(Context.
                INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
        return false;
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
        getMenuInflater().inflate(R.menu.edit_profile, menu);
        return true;
    }


    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.home) {
            Intent launchEditProfile = new Intent(EditProfile.this, HomeActivity.class);
            startActivityForResult(launchEditProfile,MY_FEED);

        } else if (id == R.id.search_recipes_EP) {
            Intent launchEditProfile = new Intent(EditProfile.this, SearchRecipesActivity.class);
            startActivityForResult(launchEditProfile,SEARCH_RECIPE);

        } else if(id == R.id.logout_EP) {
            mAuth.signOut();
            Intent logoutIntent = new Intent(EditProfile.this, LogoutActivity.class);
            startActivityForResult(logoutIntent,LOGOUT);
            finish();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
