package ahmedyasserzrelli.itbs.mybestlocations;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class DashboardActivity extends AppCompatActivity {
    private EditText etIp;
    private Button btnSaveIp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        etIp = findViewById(R.id.etIp);
        btnSaveIp = findViewById(R.id.btnSaveIp);

        // Load saved IP if it exists
        String savedIp = getSharedPreferences("config", MODE_PRIVATE).getString("ip", "");
        etIp.setText(savedIp);

        btnSaveIp.setOnClickListener(v -> {
            String ip = etIp.getText().toString().trim();

            if (ip.isEmpty()) {
                Toast.makeText(this, "Please enter an IP address.", Toast.LENGTH_SHORT).show();
                return;
            }

            // Optional: Basic format check (you can enhance it if needed)
            if (!ip.matches("\\b(?:\\d{1,3}\\.){3}\\d{1,3}\\b")) {
                Toast.makeText(this, "Invalid IP address format.", Toast.LENGTH_SHORT).show();
                return;
            }

            getSharedPreferences("config", MODE_PRIVATE).edit().putString("ip", ip).apply();
            Toast.makeText(this, "IP saved", Toast.LENGTH_SHORT).show();
        });
    }
}
