package ahmedyasserzrelli.itbs.mybestlocations;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import ahmedyasserzrelli.itbs.mybestlocations.Config;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import android.os.Looper;
import androidx.annotation.Nullable;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.Priority;
import android.location.Location;

import org.json.JSONException;
import org.json.JSONObject;

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
            String pseudo = etPseudo.getText().toString();
            String numero = etNumero.getText().toString();
            String latitude = etLatitude.getText().toString();
            String longitude = etLongitude.getText().toString();

            if (pseudo.isEmpty() || numero.isEmpty() || latitude.isEmpty() || longitude.isEmpty()) {
                Toast.makeText(this, "Please fill all fields.", Toast.LENGTH_SHORT).show();
            } else {
                // Call AsyncTask to add the position to the server
                new AddPositionTask().execute(pseudo, numero, latitude, longitude);
            }
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

    // AsyncTask to handle adding position to the server
    private class AddPositionTask extends AsyncTask<String, Void, Boolean> {

        @Override
        protected Boolean doInBackground(String... params) {
            String pseudo = params[0];
            String numero = params[1];
            String latitude = params[2];
            String longitude = params[3];

            // Build the URL for adding the position
            String url = Config.URL_ADDPOSITION + "?pseudo=" + pseudo
                    + "&numero=" + numero + "&latitude=" + latitude + "&longitude=" + longitude;

            // Create JSONParser to send request
            JSONParser parser = new JSONParser();
            JSONObject result = parser.makeRequest(url);

            try {
                if (result != null && result.getInt("success") == 1) {
                    return true; // Position added successfully
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

            return false; // Failed to add position
        }

        @Override
        protected void onPostExecute(Boolean success) {
            if (success) {
                Toast.makeText(AddPositionActivity.this, "Position added successfully!", Toast.LENGTH_SHORT).show();
                finish(); // Close the activity
            } else {
                Toast.makeText(AddPositionActivity.this, "Failed to add position. Please try again.", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
