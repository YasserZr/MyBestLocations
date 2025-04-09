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

        btnSaveIp.setOnClickListener(v -> {
            String ip = etIp.getText().toString();
            getSharedPreferences("config", MODE_PRIVATE).edit().putString("ip", ip).apply();
            Toast.makeText(this, "IP saved", Toast.LENGTH_SHORT).show();
        });
    }
}
