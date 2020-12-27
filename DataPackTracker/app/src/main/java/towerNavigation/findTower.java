package towerNavigation;

import android.app.Activity;
import android.content.Intent;
import android.content.IntentSender;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.datapacktracker.R;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.ResolvableApiException;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResponse;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

public class findTower extends AppCompatActivity {
    //Initialize Variable
    private LocationRequest locationRequest;
    private Button btn;
    public static final int REQUEST_CHECK_SETTING = 1001;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.find_tower);
    }
    public void process(View view)
    {
        Intent chooser = null;
        btn=findViewById(R.id.LaunchMap);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                locationRequest= LocationRequest.create();
                locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
                locationRequest.setInterval(5000);
                locationRequest.setFastestInterval(2000);

                LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder()
                        .addLocationRequest(locationRequest);
                builder.setAlwaysShow(true);

                Task<LocationSettingsResponse> result = LocationServices.getSettingsClient(getApplicationContext())
                        .checkLocationSettings(builder.build());

                result.addOnCompleteListener(new OnCompleteListener<LocationSettingsResponse>() {
                    @Override
                    public void onComplete(@NonNull Task<LocationSettingsResponse> task) {
                        try {
                            LocationSettingsResponse response = task.getResult(ApiException.class);
                            Toast.makeText(findTower.this, "GPS is On", Toast.LENGTH_SHORT);
                        } catch (ApiException e) {
                            switch (e.getStatusCode()){
                                case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:


                                    try {
                                        ResolvableApiException resolvableApiException = (ResolvableApiException)e;
                                        resolvableApiException.startResolutionForResult(findTower.this,REQUEST_CHECK_SETTING);
                                    } catch (IntentSender.SendIntentException sendIntentException) {

                                    }
                                    break;
                                case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
                                    break;
                            }
                        }
                    }
                });
            }
        });
        Intent intent = new Intent(android.content.Intent.ACTION_VIEW);
        //intent.setData(Uri.parse("geo:address.get(0).getLatitude(),address.get(0).getLongitude()"));
        intent.setData(Uri.parse("geo:23.495491,432.474614"));
        chooser =Intent.createChooser(intent, "Launch Map");
        startActivity(chooser);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_CHECK_SETTING){
            switch (resultCode){
                case Activity.RESULT_OK:

                    Toast.makeText(this, "GPS is Turned On", Toast.LENGTH_SHORT);
                    break;
                case Activity.RESULT_CANCELED:
                    Toast.makeText(this, "GPS is Required to be turned On", Toast.LENGTH_SHORT);
            }
        }
    }
}