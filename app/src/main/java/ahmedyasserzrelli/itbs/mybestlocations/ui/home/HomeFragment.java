package ahmedyasserzrelli.itbs.mybestlocations.ui.home;

import android.app.AlertDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import ahmedyasserzrelli.itbs.mybestlocations.Config;
import ahmedyasserzrelli.itbs.mybestlocations.JSONParser;
import ahmedyasserzrelli.itbs.mybestlocations.Position;
import ahmedyasserzrelli.itbs.mybestlocations.PositionAdapter;
import ahmedyasserzrelli.itbs.mybestlocations.databinding.FragmentHomeBinding;

public class HomeFragment extends Fragment {

    private ArrayList<Position> data = new ArrayList<>();
    private FragmentHomeBinding binding;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        binding.btnLoad.setOnClickListener(view -> {
            Telechargement t = new Telechargement();
            t.execute();
        });

        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    class Telechargement extends AsyncTask<Void, Void, Void> {

        AlertDialog alert;

        @Override
        protected void onPreExecute() {
            AlertDialog.Builder dialog = new AlertDialog.Builder(requireActivity());
            dialog.setTitle("Téléchargement");
            dialog.setMessage("Veuillez patienter...");
            alert = dialog.create();
            alert.show();
        }

        @Override
        protected Void doInBackground(Void... voids) {
            JSONParser parser = new JSONParser();
            JSONObject result = parser.makeRequest(Config.URL_GETALL);

            try {
                if (result == null) {
                    Log.e("Error", "Server returned null JSON object.");
                    return null;
                }

                Log.d("Response", result.toString());

                int success = result.getInt("success");

                if (success == 0) {
                    String message = result.getString("message");
                    Log.e("Error", message);
                } else {
                    JSONArray tableau = result.getJSONArray("positions");
                    data.clear();

                    for (int i = 0; i < tableau.length(); i++) {
                        JSONObject ligne = tableau.getJSONObject(i);

                        int idposition = ligne.getInt("idposition");
                        String pseudo = ligne.getString("pseudo");
                        String numero = ligne.getString("numero");
                        String longitude = ligne.getString("longitude");
                        String latitude = ligne.getString("latitude");

                        Position p = new Position(idposition, pseudo, numero, longitude, latitude);
                        data.add(p);
                    }
                }

                Thread.sleep(1000); // optional: simulate loading time

            } catch (JSONException | InterruptedException e) {
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void unused) {
            if (alert != null && alert.isShowing()) {
                alert.dismiss();
            }

            PositionAdapter adapter = new PositionAdapter(requireActivity(), data);

            binding.lv.setAdapter(adapter);
        }
    }
}
