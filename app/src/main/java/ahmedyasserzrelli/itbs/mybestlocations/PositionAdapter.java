package ahmedyasserzrelli.itbs.mybestlocations;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class PositionAdapter extends ArrayAdapter<Position> {
    Context context;
    ArrayList<Position> positions;

    public PositionAdapter(Context context, ArrayList<Position> positions) {
        super(context, 0, positions);
        this.context = context;
        this.positions = positions;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Position p = getItem(position);

        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.item_position, parent, false);
        }

        TextView tvInfo = convertView.findViewById(R.id.tvInfo);
        Button btnShowMap = convertView.findViewById(R.id.btnShowMap);
        Button btnDelete = convertView.findViewById(R.id.btnDelete);

        if (p != null) {
            String pseudo = p.getPseudo();
            String numero = p.getNumero();

            if (pseudo != null && numero != null) {
                tvInfo.setText(pseudo + " - " + numero);
            } else {
                tvInfo.setText("Invalid Position");
            }
        }

        btnShowMap.setOnClickListener(v -> {
            Intent intent = new Intent(context, MapsActivity.class);
            intent.putExtra("lat", p.getLatitude());
            intent.putExtra("lng", p.getLongitude());
            context.startActivity(intent);
        });

        btnDelete.setOnClickListener(v -> {
            new AlertDialog.Builder(context)
                    .setTitle("Confirm Deletion")
                    .setMessage("Are you sure you want to delete this location?")
                    .setPositiveButton("Yes", (dialog, which) -> {
                        // Call AsyncTask to delete position from the database
                        new DeletePositionTask(position).execute(p.getNumero(), p.getPseudo());
                    })
                    .setNegativeButton("Cancel", null)
                    .show();
        });

        return convertView;
    }


    // AsyncTask to handle the delete request
    private class DeletePositionTask extends AsyncTask<String, Void, Boolean> {
        private int positionToRemove;

        // Constructor to pass the position to be removed from the list
        public DeletePositionTask(int positionToRemove) {
            this.positionToRemove = positionToRemove;
        }

        @Override
        protected Boolean doInBackground(String... params) {
            String numero = params[0];
            String pseudo = params[1];

            // Build the URL for deleting the position
            String url = Config.URL_DELETEPOSITION + "?numero=" + numero + "&pseudo=" + pseudo;

            // Create JSONParser to send request
            JSONParser parser = new JSONParser();
            JSONObject result = parser.makeRequest(url);

            try {
                if (result != null && result.getInt("success") == 1) {
                    // Return true if delete was successful
                    return true;
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

            // Return false if something goes wrong
            return false;
        }

        @Override
        protected void onPostExecute(Boolean success) {
            if (success) {
                // Remove the item from the list and notify the adapter
                positions.remove(positionToRemove);
                notifyDataSetChanged();
            } else {
                // Optionally, you can show a failure message here
                Toast.makeText(context, "Failed to delete position", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
