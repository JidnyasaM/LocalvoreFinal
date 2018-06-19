package com.curiosity.jidnyasa.localvore;

import android.content.Context;
import android.content.Intent;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import java.util.regex.Pattern;


public class NewAccountActivity extends AppCompatActivity {

    private static final Pattern EMAIL_REGEX = Pattern.compile(
            "^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$",
            Pattern.CASE_INSENSITIVE);

    EditText userName;
    EditText email;
    EditText password;
    EditText confirmPassword;

    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_account);
        mAuth = FirebaseAuth.getInstance();


        //Edit Text
        userName = findViewById(R.id.userName);
        email = findViewById(R.id.email1);
        password = findViewById(R.id.password1);
        confirmPassword = findViewById(R.id.password2);

        //Button
        Button signUp = findViewById(R.id.signUpButton);
        Button UserNameClear = findViewById(R.id.useName_clear);
        Button EmailClear = findViewById(R.id.email1_clear);
        Button Password1Clear = findViewById(R.id.password1_clear);
        Button Password2Clear = findViewById(R.id.password2_clear);

        signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createNewAccount();
            }
        });


        //If the cross on UserName edit text is clicked
        UserNameClear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(userName.length()!=0){
                    userName.setText("");
                }
            }
        });

        //If the cross on Email edit text is clicked
        EmailClear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(email.length()!=0){
                    email.setText("");
                }
            }
        });

        //If the cross on Password edit text is clicked
        Password1Clear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(password.length()!=0){
                    password.setText("");
                }
            }
        });

        //If the cross on Confirm Password edit text is clicked
        Password2Clear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(confirmPassword.length()!=0){
                    confirmPassword.setText("");
                }
            }
        });
    }

    ///Keypad disappears if you touch anywhere on the screen
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        InputMethodManager imm = (InputMethodManager)getSystemService(Context.
                INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
        return false;
    }

    public void createNewAccount() {

        //Edit Text
        userName = findViewById(R.id.userName);
        email = findViewById(R.id.email1);
        password = findViewById(R.id.password1);
        confirmPassword = findViewById(R.id.password2);

        String emailStr = email.getText().toString();
        if (userName.length() == 0 || email.length() == 0 || password.length() == 0 ||
                confirmPassword.length() == 0) {
            Toast.makeText(NewAccountActivity.this,R.string.enterValues,
                    Toast.LENGTH_SHORT).show();
            return;
        }

        if (!EMAIL_REGEX.matcher(emailStr).find()) {
            Snackbar.make(email, R.string.enterEmailValue,
                    Snackbar.LENGTH_LONG).show();
            return;
        }

        String passStr = password.getText().toString();
        String verifyPassStr = confirmPassword.getText().toString();

        if (!verifyPassStr.equals(passStr)) {
            Toast.makeText(NewAccountActivity.this,R.string.enterCorrectPassword,
                    Toast.LENGTH_SHORT).show();
            return;
        }

        String userNameInput = userName.getText().toString();
        mAuth.createUserWithEmailAndPassword(emailStr, passStr).addOnCompleteListener(NewAccountActivity.this, task -> {
            if (task.isSuccessful()) {
                //Saving user name
                FirebaseUser user = mAuth.getCurrentUser();
                UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                        .setDisplayName(userNameInput).build();
                user.updateProfile(profileUpdates);
                Intent intent = new Intent(NewAccountActivity.this, HomeActivity.class);
                startActivity (intent);
            } else {
                String msg = task.getException().getMessage();
                Snackbar.make(email, msg, Snackbar.LENGTH_SHORT).show();
            }

        });

    }
}
