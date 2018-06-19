package com.curiosity.jidnyasa.localvore;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

public class DisplayRecipeActivity extends AppCompatActivity {
    TextView title;
    TextView recipeDetail, sharelink;
    Context mContext;
   // ImageView imageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_recipe);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //TextViews
         title = (TextView) findViewById(R.id.recipeDetails);
         recipeDetail = (TextView) findViewById(R.id.recipeDetailsForDisplay);
      //   imageView = (ImageView) findViewById(R.id.recipeImage);
        sharelink = (TextView) findViewById(R.id.shareFB);
        //Display
        Intent intent = this.getIntent();
        String name = intent.getStringExtra("RECIPE_NAME");
        title.setText(name);
        String info = intent.getStringExtra("RECIPE_DETAILS");
        recipeDetail.setText(info);
//        Bitmap bitmap = ((BitmapDrawable)imageView.getDrawable()).getBitmap();
//        imageView.setImageBitmap(bitmap);

        sharelink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String contentshare = "Receipe Name:" +name+" ; Receipe Description:"+info;
                Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
                sharingIntent.setType("text/plain");
                sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT,contentshare);
                startActivity(Intent.createChooser(sharingIntent,"Share"));
            }
        });

    }

}
