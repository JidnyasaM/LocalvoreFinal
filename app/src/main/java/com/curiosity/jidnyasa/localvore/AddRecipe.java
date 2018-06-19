package com.curiosity.jidnyasa.localvore;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageMetadata;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

public class AddRecipe extends AppCompatActivity {

    public static final int OPEN_CAMERA = 0;
    public static final int ADD_PHOTOS_FROM_DEVICE = 1;
    private static final int GO_BACK_1 = 3;
    private static final int GO_BACK_2 = 4;

    TextView recipeName;
    TextView recipeDesc;

    ImageView recipeImg;

    DatabaseReference topRef;
    FirebaseUser user;
    StorageReference storageRef;
    Uri mediaUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_recipe);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //TextViews
        recipeName = findViewById(R.id.recipeName);
        recipeDesc = findViewById(R.id.recipeDescription);
        
        //ImageView
        recipeImg = findViewById(R.id.recipeImage);

        //Firebase Storage
        user = FirebaseAuth.getInstance().getCurrentUser();
        storageRef = FirebaseStorage.getInstance().getReference();

        //Buttons
        Button openCamera = findViewById(R.id.openCamera);
        Button addPhotosFromDevice = findViewById(R.id.addPhotos);
        Button cancel = findViewById(R.id.cancel);
        Button save = findViewById(R.id.save);
        Button recipeNameClear = findViewById(R.id.recipeName_clear);
        Button recipeDescClear = findViewById(R.id.recipeDescription_clear);

        //Firebase Reference
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        String uid = user.getUid();
        storageRef = FirebaseStorage.getInstance().getReference().child(uid);

        //If the cross on the recipeName is clicked
        recipeNameClear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(recipeName.length()!=0){
                    recipeName.setText("");
                }
            }
        });

        //If the cross on the recipeDescription is clicked
        recipeDescClear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(recipeDesc.length()!=0){
                    recipeDesc.setText("");
                }
            }
        });

        //If Camera is clicked
        openCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setCamera();
            }
        });

        //If Add Photos is clicked
        addPhotosFromDevice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent pickPhoto = new Intent(Intent.ACTION_PICK,
                        android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(pickPhoto, ADD_PHOTOS_FROM_DEVICE);//one can be replaced with any action code
            }
        });

        //If save button is clicked
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ProfileFeed entry = new ProfileFeed();
                entry.setRecUserName(user.getDisplayName());
                entry.setRecName(recipeName.getText().toString());
                entry.setRecDesc(recipeDesc.getText().toString());

                //Save Photos
                if(mediaUri == null){
                    Toast.makeText(AddRecipe.this, R.string.pleaseUseCamera, Toast.LENGTH_SHORT).show();
                } else {
                    StorageMetadata.Builder metaBuilder = new StorageMetadata.Builder();
                    StorageMetadata meta;
                    String mediaName = mediaUri.getLastPathSegment();
                    meta = metaBuilder.setContentType("image/jpeg").build();
                    UploadTask task = storageRef.child("photos/" + mediaName).putFile(mediaUri, meta);

                    String url = storageRef.child("photos/" + mediaName).getDownloadUrl().toString();
                    entry.setUrl(url);

                    topRef.push().setValue(entry);

                    //Going back to Home Page
                    Intent intent1 = new Intent(AddRecipe.this, HomeActivity.class);
                    startActivityForResult(intent1, GO_BACK_1);
                }
            }
        });

        //If cancel button is clicked
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent2 = new Intent(AddRecipe.this, HomeActivity.class);
                startActivityForResult(intent2, GO_BACK_2);
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

    //OnActivityResult
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent imageRecieved) {
        super.onActivityResult(requestCode, resultCode, imageRecieved);
        //ImageView
        //Bitmap photo = null;
        recipeImg = findViewById(R.id.recipeImage);
        switch(requestCode) {
            case OPEN_CAMERA:
                if(resultCode == RESULT_OK){
                    try {
                        InputStream istr = getContentResolver().openInputStream(mediaUri);
                        Bitmap bmp = BitmapFactory.decodeStream(istr);
                        recipeImg.setImageBitmap (bmp);
                        istr.close();
                    } catch (IOException e){
                        e.printStackTrace();
                    }
                }
                break;

            case ADD_PHOTOS_FROM_DEVICE:
                if(resultCode == RESULT_OK){
                    mediaUri = imageRecieved.getData();
                    recipeImg.setImageURI(mediaUri);
                }
                break;
        }
    }

    public void setCamera(){
        Intent capture = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (capture.resolveActivity(getPackageManager()) != null) {
            try {
                File photoFile = createFileName("localvore", ".jpg");
                mediaUri = FileProvider.getUriForFile(this,
                        getPackageName() + ".provider",
                        photoFile);
                capture.putExtra(MediaStore.EXTRA_OUTPUT, mediaUri);
                startActivityForResult(capture, OPEN_CAMERA);
                } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }

    private File createFileName(String prefix, String ext) throws IOException {
        DateTime now = DateTime.now();
        org.joda.time.format.DateTimeFormatter fmt = DateTimeFormat.forPattern("yyyyMMdd-HHmmss");
        File cacheDir = getExternalCacheDir();
        File media = File.createTempFile(prefix + "-" + fmt.print(now), ext, cacheDir);
        return media;
        }

    @Override
    public void onResume(){
        super.onResume();
        topRef = FirebaseDatabase.getInstance().getReference();
    }
}
