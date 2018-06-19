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

import java.util.regex.Pattern;

public class WelcomePage extends AppCompatActivity {

    public static final int Home_Activity = 1;
    public static final int New_Account_Activity = 2;
    private static final Pattern EMAIL_REGEX = Pattern.compile(
            "^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$",
            Pattern.CASE_INSENSITIVE);
    private FirebaseAuth mAuth;
    EditText email;
    EditText password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome_page);

        //Firebase
        mAuth = FirebaseAuth.getInstance();

        //Edit Text
        email = findViewById(R.id.email);
        password = findViewById(R.id.password);

        //Button
        final Button login = findViewById(R.id.login);
        final Button createAccount = findViewById(R.id.newaccount);
        final Button emailClear = findViewById(R.id.email_clear);
        final Button passwordClear = findViewById(R.id.password_clear);

        //If login is clicked
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Disappear keypad on button click
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(email.getWindowToken(), 0);

                //login method
                loginApp();
            }
        });

        //If Create An Account is clicked
        createAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent launchNewIntent1 = new Intent(WelcomePage.this, NewAccountActivity.class);
                startActivityForResult(launchNewIntent1,New_Account_Activity);
            }
        });

        //If the cross on Email edit text is clicked
        emailClear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(email.length()!=0){
                    email.setText("");
                } else {
                    Toast.makeText(WelcomePage.this,R.string.enterEmailValue,Toast.LENGTH_SHORT).show();
                    return;
                }
            }
        });

        //If the cross on Password edit text is clicked
        passwordClear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(password.length()!=0){
                    password.setText("");
                } else {
                    Toast.makeText(WelcomePage.this,R.string.enterPasswordValue,Toast.LENGTH_SHORT).show();
                    return;
                }
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


    //Login
    public void loginApp(){
        //Edit Text
        email = findViewById(R.id.email);
        password = findViewById(R.id.password);
        String emailstr = email.getText().toString();
        String passwstr = password.getText().toString();

        //If Email or password is not entered.
        if (emailstr.length() == 0 || passwstr.length() == 0) {
            Toast.makeText(WelcomePage.this, R.string.enterEmailValue, Toast.LENGTH_SHORT).show();
            return;
        }


        if (!EMAIL_REGEX.matcher(emailstr).find()) {
            Snackbar.make(email, R.string.enterEmailValue,
                    Snackbar.LENGTH_LONG).show();
            return;
        }

        mAuth.signInWithEmailAndPassword(emailstr, passwstr).addOnCompleteListener(WelcomePage.this, task -> {
            if (task.isSuccessful()) {
                Intent toMain = new Intent(WelcomePage.this, HomeActivity.class);
                //toMain.putExtra("email", emailstr);
                startActivity(toMain);
                finish();
                } else {
                Snackbar.make(email, R.string.enterTheEmail, Snackbar.LENGTH_LONG).show();
                }
        });
    }

    @Override

    public void onResume(){
        super.onResume();
        FirebaseUser user = mAuth.getCurrentUser();
        if (user != null) {
            Intent toMain = new Intent(this, HomeActivity.class);
            startActivity(toMain);
            finish();
        }
    }
}
