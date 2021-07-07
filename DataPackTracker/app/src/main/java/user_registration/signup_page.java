package user_registration;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.example.datapacktracker.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Objects;

import home.HomeActivity;


public class signup_page extends AppCompatActivity {

    private EditText email,pass,first_name,last_name,confirm;
    private FirebaseAuth auth;
    private ProgressBar progressBar;
    private FirebaseFirestore db;
    String fname,lname;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup_page);
        Button reg = findViewById(R.id.signup_button);
        email=findViewById(R.id.email1);
        pass=findViewById(R.id.password1);
        auth=FirebaseAuth.getInstance();
        first_name = findViewById(R.id.firstname1);
        last_name = findViewById(R.id.lastname1);
        confirm = findViewById(R.id.confirm_password1);
        progressBar = findViewById(R.id.progressBar);
        db = FirebaseFirestore.getInstance();
        Button goToLogin = findViewById(R.id.go_login);

        goToLogin.setOnClickListener(v -> {
            startActivity(new Intent(signup_page.this, login_page.class));
            finish();
        });

        reg.setOnClickListener(v -> {
            String txt_email=email.getText().toString().trim();
            String txt_pass=pass.getText().toString().trim();
            fname = first_name.getText().toString().trim();
            lname = last_name.getText().toString().trim();
            String confirmpass = confirm.getText().toString().trim();
            if (TextUtils.isEmpty(txt_email)){
                Toast.makeText(signup_page.this,"email is Empty",Toast.LENGTH_SHORT).show();
            }
            else if(TextUtils.isEmpty(txt_pass)){
                Toast.makeText(signup_page.this,"Please enter password",Toast.LENGTH_LONG).show();
            }
            else if(TextUtils.isEmpty(fname)){
                Toast.makeText(signup_page.this,"first name is empty",Toast.LENGTH_SHORT).show();
            }
            else if(TextUtils.isEmpty(lname)){
                Toast.makeText(signup_page.this,"last name is empty",Toast.LENGTH_SHORT).show();
            }
            else if(txt_pass.length()<6){
                Toast.makeText(signup_page.this,"Password Length is less than 6",Toast.LENGTH_SHORT).show();
            }
            else if(!confirmpass.equals(txt_pass)){
                Toast.makeText(signup_page.this,"Password doesn't match",Toast.LENGTH_SHORT).show();
            }
            else{
                register_user(txt_email,txt_pass);
            }

        });

    }
    private void register_user(String email, String pass) {
        progressBar.setVisibility(View.VISIBLE);
        auth.createUserWithEmailAndPassword(email,pass).addOnCompleteListener(signup_page.this, new OnCompleteListener<AuthResult>() {
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (!task.isSuccessful()){
                    Toast.makeText(signup_page.this, "Registration Failed : "+ Objects.requireNonNull(task.getException()).getMessage(),Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(signup_page.this, HomeActivity.class));
                    finish();
                }else{
                    FirebaseUser user = auth.getCurrentUser();
                    HashMap<String,String> reg_user=new HashMap<>();
                    reg_user.put("first_name",fname);
                    reg_user.put("last_name",lname);
                    assert user != null;
                    reg_user.put("uid",user.getUid());
                    db.collection("User").add(reg_user).addOnCompleteListener(task1 -> {
                        if(task1.isSuccessful()){
                            sendVerificationEmail();
                        }else{
                            Toast.makeText(signup_page.this,"error occured",Toast.LENGTH_SHORT).show();
                            progressBar.setVisibility(View.GONE);
                        }
                    });
                }
            }
        });
    }

    private void sendVerificationEmail()
    {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        assert user != null;
        user.sendEmailVerification()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        FirebaseAuth.getInstance().signOut();
                        Toast.makeText(signup_page.this,"Registering User Successful",Toast.LENGTH_SHORT).show();
                        progressBar.setVisibility(View.GONE);
                        startActivity(new Intent(signup_page.this, login_page.class));
                        finish();
                    }
                    else
                    {
                        overridePendingTransition(0, 0);
                        Toast.makeText(signup_page.this,"Error while email verification",Toast.LENGTH_SHORT).show();
                        finish();
                        overridePendingTransition(0, 0);
                        startActivity(getIntent());
                    }
                });
    }
}
