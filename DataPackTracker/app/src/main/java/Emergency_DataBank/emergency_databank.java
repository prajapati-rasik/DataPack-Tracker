package Emergency_DataBank;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.ProgressBar;

import androidx.appcompat.app.AppCompatActivity;

import com.example.datapacktracker.R;

public class emergency_databank extends AppCompatActivity {

    ProgressBar timeProgressBar;
    ProgressBar dataProgressBar;
    SharedPreferences sharedpreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.emergency_databank);
    }
}
