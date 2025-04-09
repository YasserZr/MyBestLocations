package ahmedyasserzrelli.itbs.mybestlocations;

import android.os.Parcel;
import android.os.Parcelable;

public class Position implements Parcelable {
    private int idposition;
    private String pseudo;
    private String numero;
    private String longitude;
    private String latitude;

    public Position(int idposition, String pseudo, String numero, String longitude, String latitude) {
        this.idposition = idposition;
        this.pseudo = pseudo;
        this.numero = numero;
        this.longitude = longitude;
        this.latitude = latitude;
    }

    // Getters
    public int getIdposition() {
        return idposition;
    }

    public String getPseudo() {
        return pseudo;
    }

    public String getNumero() {
        return numero;
    }

    public String getLongitude() {
        return longitude;
    }

    public String getLatitude() {
        return latitude;
    }

    // Setters
    public void setIdposition(int idposition) {
        this.idposition = idposition;
    }

    public void setPseudo(String pseudo) {
        this.pseudo = pseudo;
    }

    public void setNumero(String numero) {
        this.numero = numero;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    // Convenience methods to get lat/lng as double
    public double getLatAsDouble() {
        return Double.parseDouble(latitude);
    }

    public double getLngAsDouble() {
        return Double.parseDouble(longitude);
    }

    @Override
    public String toString() {
        return pseudo + " (" + latitude + ", " + longitude + ")";
    }

    // Parcelable implementation
    protected Position(Parcel in) {
        idposition = in.readInt();
        pseudo = in.readString();
        numero = in.readString();
        longitude = in.readString();
        latitude = in.readString();
    }

    public static final Creator<Position> CREATOR = new Creator<Position>() {
        @Override
        public Position createFromParcel(Parcel in) {
            return new Position(in);
        }

        @Override
        public Position[] newArray(int size) {
            return new Position[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int flags) {
        parcel.writeInt(idposition);
        parcel.writeString(pseudo);
        parcel.writeString(numero);
        parcel.writeString(longitude);
        parcel.writeString(latitude);
    }
}
