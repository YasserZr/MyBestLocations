package ahmedyasserzrelli.itbs.mybestlocations;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import android.os.Looper;
import androidx.annotation.Nullable;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.Priority;
import android.location.Location;

public class AddPositionActivity extends AppCompatActivity {

    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1;

    private EditText etPseudo, etNumero, etLatitude, etLongitude;
    private Button btnAdd, btnPickFromMap;
    private FusedLocationProviderClient fusedLocationClient;
    private String serverIp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_position);

        etPseudo = findViewById(R.id.etPseudo);
        etNumero = findViewById(R.id.etNumero);
        etLatitude = findViewById(R.id.etLatitude);
        etLongitude = findViewById(R.id.etLongitude);
        btnAdd = findViewById(R.id.btnAdd);
        btnPickFromMap = findViewById(R.id.btnPickFromMap);

        etLatitude.setEnabled(false);
        etLongitude.setEnabled(false);
        btnAdd.setEnabled(false);

        serverIp = getSharedPreferences("config", MODE_PRIVATE).getString("ip", "");
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

        // Check permissions before requesting location updates
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    LOCATION_PERMISSION_REQUEST_CODE);
        } else {
            requestLocationUpdates();
        }

        btnPickFromMap.setOnClickListener(v -> {
            startActivityForResult(new Intent(this, MapPickerActivity.class), 1001);
        });

        btnAdd.setOnClickListener(v -> {
            // TODO: Add your logic for adding the position using API and serverIp
        });
    }

    private void requestLocationUpdates() {
        LocationRequest locationRequest = LocationRequest.create()
                .setInterval(60000)
                .setSmallestDisplacement(100)
                .setPriority(Priority.PRIORITY_HIGH_ACCURACY);

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            return;
        }

        fusedLocationClient.requestLocationUpdates(locationRequest, new LocationCallback() {
            public void onLocationResult(LocationResult result) {
                if (result == null) return;

                Location location = result.getLastLocation();
                etLatitude.setText(String.valueOf(location.getLatitude()));
                etLongitude.setText(String.valueOf(location.getLongitude()));

                if (!serverIp.isEmpty()) btnAdd.setEnabled(true);
            }
        }, Looper.getMainLooper());
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == LOCATION_PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                requestLocationUpdates();
            } else {
                // Handle permission denial here
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode == 1001 && resultCode == RESULT_OK && data != null) {
            etLatitude.setText(String.valueOf(data.getDoubleExtra("lat", 0)));
            etLongitude.setText(String.valueOf(data.getDoubleExtra("lng", 0)));
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
}
