package ahmedyasserzrelli.itbs.mybestlocations;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;

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

        tvInfo.setText(p.getPseudo() + " - " + p.getNumero());

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
                        positions.remove(position);
                        notifyDataSetChanged();
                    })
                    .setNegativeButton("Cancel", null)
                    .show();
        });

        return convertView;
    }
}
