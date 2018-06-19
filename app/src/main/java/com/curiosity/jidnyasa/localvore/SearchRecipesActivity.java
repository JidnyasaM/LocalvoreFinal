package com.curiosity.jidnyasa.localvore;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
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

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class SearchRecipesActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    public static final int ADD_RECIPE = 1;
    public static final int EDIT_PROFILE = 2;
    public static final int LOGOUT = 3;
    public static final int HOME = 4;

    EditText searchKeyword;
    Button searchButton;
    DashboardAdapter dashboardAdapter;

    private FirebaseAuth mAuth;
    RecyclerView searchEntries;
    DatabaseReference databaseReference;
    public static ArrayList<ProfileFeed> dashContents = new ArrayList<ProfileFeed>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_recipes);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        searchKeyword = findViewById(R.id.typeSearchRecipe);
        searchButton = findViewById(R.id.searchButton);
        searchEntries = findViewById(R.id.searchEntries);

        mAuth = FirebaseAuth.getInstance();
        //Initializing the database reference
        databaseReference = FirebaseDatabase.getInstance().getReference();

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String keyword = searchKeyword.getText().toString();
            }
        });
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
        getMenuInflater().inflate(R.menu.search_recipes, menu);
        return true;
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.edit_profile) {
            Intent launchEditProfile = new Intent(SearchRecipesActivity.this, EditProfile.class);
            startActivityForResult(launchEditProfile,EDIT_PROFILE);

        } else if (id == R.id.my_feed) {
            Intent launchSearchRecipe = new Intent(SearchRecipesActivity.this, HomeActivity.class);
            startActivityForResult(launchSearchRecipe,HOME);

        } else if(id == R.id.logout) {
            mAuth.signOut();
            Intent logoutIntent = new Intent(SearchRecipesActivity.this, LogoutActivity.class);
            startActivityForResult(logoutIntent,LOGOUT);
            finish();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    protected void onStart() {
        super.onStart();
        //attaching value event listener
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                dashContents.clear();

                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    //getting artist
                    ProfileFeed entry = (ProfileFeed) postSnapshot.getValue(ProfileFeed.class);
                    entry.set_key(postSnapshot.getKey());
                    dashContents.add(entry);
                }

                dashboardAdapter = new DashboardAdapter(getApplicationContext(), dashContents);
                RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(SearchRecipesActivity.this);
                searchEntries.setAdapter(dashboardAdapter);
                searchEntries.setLayoutManager(layoutManager);
                searchEntries.setHasFixedSize(true);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
