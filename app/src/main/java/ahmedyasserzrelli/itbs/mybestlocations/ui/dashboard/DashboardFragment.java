// DashboardFragment.java
package ahmedyasserzrelli.itbs.mybestlocations.ui.dashboard;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import ahmedyasserzrelli.itbs.mybestlocations.R;

public class DashboardFragment extends Fragment {

    private EditText etIp;
    private Button btnSaveIp;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_dashboard, container, false);

        etIp = root.findViewById(R.id.etIp);
        btnSaveIp = root.findViewById(R.id.btnSaveIp);

        btnSaveIp.setOnClickListener(v -> {
            String ip = etIp.getText().toString();
            getActivity().getSharedPreferences("config", getActivity().MODE_PRIVATE).edit().putString("ip", ip).apply();
            Toast.makeText(getActivity(), "IP saved", Toast.LENGTH_SHORT).show();
        });

        return root;
    }
}
