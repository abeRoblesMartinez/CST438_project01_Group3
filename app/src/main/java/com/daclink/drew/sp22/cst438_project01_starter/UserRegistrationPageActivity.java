package com.daclink.drew.sp22.cst438_project01_starter;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import androidx.room.Room;


public class UserRegistrationPageActivity extends AppCompatActivity {
    private EditText username;
    private EditText password;
    private EditText verPassword;
    private Button register;
    private Button goBack;

    private UserDAO mUserDAO;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_registration);

        getDatabase();

        username = findViewById(R.id.userNameRegister);
        password = findViewById(R.id.passwordRegister);
        verPassword = findViewById(R.id.passwordVerify);
        register = findViewById(R.id.registerButton);
        goBack = findViewById(R.id.returnButton);

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String uName = String.valueOf(username.getText());
                String newPassword = String.valueOf(password.getText());
                String newVerPassword = String.valueOf(verPassword.getText());

                User user = mUserDAO.findUser(uName);
                if (user == null){
                    if (newPassword != newVerPassword){
                        mUserDAO.insertUser(new User(uName, newPassword));
                    }
                } else {

                }

            }
        });

    }

    private void getDatabase(){
        mUserDAO = Room.databaseBuilder(this, UserDb.class, UserDb.DATABASE_NAME)
                .allowMainThreadQueries()
                .build()
                .getPersonDAO();
    }
}