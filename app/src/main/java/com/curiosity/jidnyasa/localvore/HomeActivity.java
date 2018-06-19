package com.curiosity.jidnyasa.localvore;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class HomeActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    public static final int ADD_RECIPE = 1;
    public static final int EDIT_PROFILE = 2;
    public static final int LOGOUT = 3;
    public static final int SEARCH_RECIPE = 4;
    public static ArrayList<ProfileFeed> dashContents = new ArrayList<ProfileFeed>();

    private FirebaseAuth mAuth;
    RecyclerView dashEntries;
    DatabaseReference topRef;
    DashboardAdapter dashboardAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
       // dashContents.clear();
        //Firebase Authentication Instantiation
        mAuth = FirebaseAuth.getInstance();

        //Button
        Button addrec = findViewById(R.id.addRecipes);

        //Recycler View
        dashEntries = findViewById(R.id.dashEntries);

        //Navigation Drawer
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = findViewById(R.id.nav_view_home);
        navigationView.setNavigationItemSelectedListener(this);

        //Add recipe button listener
        addrec.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent launchNewIntent = new Intent(HomeActivity.this, AddRecipe.class);
                startActivityForResult(launchNewIntent, ADD_RECIPE);
            }

        });

        //Initializing the database reference
        topRef = FirebaseDatabase.getInstance().getReference();
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
        getMenuInflater().inflate(R.menu.home, menu);
        return true;
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.edit_profile) {
            Intent launchEditProfile = new Intent(HomeActivity.this, EditProfile.class);
            startActivityForResult(launchEditProfile,EDIT_PROFILE);

        } else if (id == R.id.search_recipes) {
            Intent launchSearchRecipe = new Intent(HomeActivity.this, SearchRecipesActivity.class);
            startActivityForResult(launchSearchRecipe,SEARCH_RECIPE);

        } else if(id == R.id.logout) {
            mAuth.signOut();
            Intent logoutIntent = new Intent(HomeActivity.this, LogoutActivity.class);
            startActivityForResult(logoutIntent,LOGOUT);
            finish();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onResume(){
        super.onResume();
        mAuth = FirebaseAuth.getInstance();
        dashContents.clear();
        topRef = FirebaseDatabase.getInstance().getReference();
    }

    @Override
    public void onPause(){
        super.onPause();
    }


    @Override
    protected void onStart() {
        super.onStart();
        //attaching value event listener
        topRef.addValueEventListener(new ValueEventListener() {
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
                RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(HomeActivity.this);
                dashEntries.setAdapter(dashboardAdapter);
                dashEntries.setLayoutManager(layoutManager);
                dashEntries.setHasFixedSize(true);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }


}
