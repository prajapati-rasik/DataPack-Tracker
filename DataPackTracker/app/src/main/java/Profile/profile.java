package Profile;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.res.ResourcesCompat;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.datapacktracker.MenuPage;
import com.example.datapacktracker.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.shape.ShapeAppearanceModel;
import com.google.android.material.shape.ShapePathModel;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.model.Document;

import java.util.HashMap;
import java.util.List;
import java.util.Objects;

import home.HomeActivity;
import user_registration.signup_page;

public class profile extends AppCompatActivity {
    private TextInputEditText emailTextView, firstNameView, lastNameView;
    private TextInputLayout emailField, firstNameField, lastNameField;
    private FirebaseFirestore database;
    private FirebaseAuth auth;
    private CollectionReference ref;
    private FirebaseUser user;
    private String first_name;
    private String last_name;
    private String docid;
    private static final String USERS = "User";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.profile);

        this.emailTextView = findViewById(R.id.email_profile_text_input);
        this.firstNameView = findViewById(R.id.first_name_profile_text_input);
        this.lastNameView = findViewById(R.id.last_name_profile_text_input);
        Button deleteButton = findViewById(R.id.DeleteAccountButton);

        this.emailField = findViewById(R.id.textfieldProfileEmail);
        this.firstNameField = findViewById(R.id.textfieldProfileFirstName);
        this.lastNameField = findViewById(R.id.textfieldProfileLastName);

        this.database = FirebaseFirestore.getInstance();
        this.auth = FirebaseAuth.getInstance();
        this.user = auth.getCurrentUser();
        this.ref = database.collection(USERS);

        if(user == null){
            Toast.makeText(profile.this, "Authantication Error, May be your email is not verified", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(profile.this, MenuPage.class));
        }

        if(!user.isEmailVerified()){
            Toast.makeText(profile.this, "Email not verified", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(profile.this, MenuPage.class));
        }

        this.firstNameField.setEndIconOnClickListener(v -> {
            if(!this.firstNameView.isEnabled()){
                this.firstNameView.setEnabled(true);
                this.firstNameField.setEndIconDrawable(R.drawable.ic_baseline_check_24);
            }else{
                this.firstNameView.setEnabled(false);
                this.firstNameField.setEndIconDrawable(R.drawable.ic_baseline_edit_24);
                saveDetails(1);
            }
        });

        this.lastNameField.setEndIconOnClickListener(v -> {
            if(!this.lastNameView.isEnabled()){
                this.lastNameView.setEnabled(true);
                this.lastNameField.setEndIconDrawable(R.drawable.ic_baseline_check_24);
            }else{
                this.lastNameView.setEnabled(false);
                this.lastNameField.setEndIconDrawable(R.drawable.ic_baseline_edit_24);
                saveDetails(2);
            }
        });

        this.emailTextView.setText(this.user.getEmail());

        this.ref.whereEqualTo("uid", this.user.getUid()).get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                for (DocumentSnapshot document : task.getResult()) {
                    this.first_name = document.getString("first_name");
                    this.last_name = document.getString("last_name");
                    this.firstNameView.setText(this.first_name);
                    this.lastNameView.setText(this.last_name);
                    this.docid = document.getId();
                    break;
                }
            } else {
                Toast.makeText(profile.this, "Data not found", Toast.LENGTH_SHORT).show();
            }
        });

        deleteButton.setOnClickListener(v -> {
            this.ref.document(this.docid).delete().addOnSuccessListener(aVoid -> {
                this.user.delete().addOnSuccessListener(aVoid1 -> {
                    Toast.makeText(profile.this, "Account Deleted", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(profile.this, MenuPage.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                }).addOnFailureListener(e -> {
                    Toast.makeText(profile.this, "Some Error Occured", Toast.LENGTH_SHORT).show();
                    if(this.ref.document(this.docid) == null){
                        HashMap<String,String> reg_user=new HashMap<>();
                        reg_user.put("first_name",this.first_name);
                        reg_user.put("last_name",this.last_name);
                        reg_user.put("uid",this.user.getUid());
                        this.ref.add(reg_user);
                    }
                    Toast.makeText(profile.this, "Data Recovered", Toast.LENGTH_SHORT).show();
                });
            }).addOnFailureListener(e ->{
                Toast.makeText(profile.this, "some error occured", Toast.LENGTH_SHORT).show();
            });
        });
    }

    void saveDetails(int i){
        switch (i){
            case 1: this.ref.document(this.docid).update("first_name", this.firstNameView.getText().toString()).addOnSuccessListener(aVoid -> {
                Toast.makeText(profile.this, "FirstName updated", Toast.LENGTH_SHORT).show();
                profile.this.first_name = profile.this.firstNameView.getText().toString();
            }).addOnFailureListener(e -> {
                        Toast.makeText(profile.this, "Some Error Occured", Toast.LENGTH_SHORT).show();
                        profile.this.firstNameView.setText(profile.this.first_name);
                    }
            );
            break;
            case 2: this.ref.document(this.docid).update("last_name", this.lastNameView.getText().toString()).addOnSuccessListener(aVoid -> {
                Toast.makeText(profile.this, "LastName updated", Toast.LENGTH_SHORT).show();
                profile.this.last_name = profile.this.lastNameView.getText().toString();
            }).addOnFailureListener(e -> {
                        Toast.makeText(profile.this, "Some Error Occured", Toast.LENGTH_SHORT).show();
                        profile.this.lastNameView.setText(profile.this.last_name);
                    }
            );
            break;
            default:
                Toast.makeText(profile.this, "Some Error Occured", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}