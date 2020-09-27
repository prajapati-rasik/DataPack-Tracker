package home;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.datapacktracker.R;
import com.google.firebase.auth.FirebaseAuth;

import user_registration.login_page;
import user_registration.signup_page;

public class HomeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        Button signin_button = findViewById(R.id.signin_button);
        Button register_button = findViewById(R.id.register_button);

        signin_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(HomeActivity.this, login_page.class);
                startActivity(i);
                finish();
            }
        });

        register_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(HomeActivity.this, signup_page.class);
                startActivity(i);
                finish();
            }
        });
    }

    protected void onStart() {
        super.onStart();

        if (FirebaseAuth.getInstance().getCurrentUser() != null){
            startActivity(new Intent(HomeActivity.this , MainActivity.class));
            finish();
        }
    }
}