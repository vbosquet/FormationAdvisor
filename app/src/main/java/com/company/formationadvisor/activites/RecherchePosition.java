package com.company.formationadvisor.activites;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Criteria;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.location.LocationProvider;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.company.formationadvisor.R;
import com.company.formationadvisor.modeles.IPAddress;
import com.company.formationadvisor.modeles.FormationCoordinates;
import com.company.formationadvisor.taches_asynchrones.RechercherAdressesCentreFormation;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class RecherchePosition extends AppCompatActivity implements RechercherAdressesCentreFormation.IRechercheAdressesCentreFormation{

    TextView latitude, longitude;
    double maLatitude, maLongitude;
    String latitudeText, longitudeText, adresse;
    FormationCoordinates locationAdresse;

    Intent intent;

    Map<String, String> listeAdresses, listeId;
    String adresseComplete;
    ArrayList listeLibelleCentreFormation;
    ListView listView;

    IPAddress ipAddress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recherche_position);

        latitude = (TextView) findViewById(R.id.latitude_position_actuelle);
        longitude = (TextView) findViewById(R.id.longitude_position_actuelle);

        ipAddress = new IPAddress();

        RechercherAdressesCentreFormation tache = new RechercherAdressesCentreFormation(this, ipAddress);
        tache.execute();

        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        ArrayList<LocationProvider> listProviders = new ArrayList<>();
        List<String> names = locationManager.getProviders(true);

        for (String name : names) {
            listProviders.add(locationManager.getProvider(name));
        }

        Criteria criteria = new Criteria();
        criteria.setAccuracy(Criteria.ACCURACY_COARSE);
        criteria.setSpeedAccuracy(Criteria.ACCURACY_COARSE);
        criteria.setAltitudeRequired(false);
        criteria.setBearingRequired(true);
        criteria.setCostAllowed(false);
        criteria.setPowerRequirement(Criteria.POWER_MEDIUM);
        criteria.setSpeedRequired(true);


        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 0);
        }

        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 60000, 150, new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {

                maLatitude = location.getLatitude();
                maLongitude = location.getLongitude();

                latitudeText = String.valueOf(location.getLatitude());
                longitudeText = String.valueOf(location.getLongitude());

                latitude.setText(latitudeText);
                longitude.setText(longitudeText);

            }

            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {

            }

            @Override
            public void onProviderEnabled(String provider) {
                Toast.makeText(getApplicationContext(), "GPS Enabled", Toast.LENGTH_SHORT).show();

            }

            @Override
            public void onProviderDisabled(String provider) {
                Toast.makeText(getApplicationContext(), "GPS DISABLED", Toast.LENGTH_SHORT).show();

            }
        });
        Location location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);

        //latitude.setText(String.valueOf(location.getLatitude()));
        //longitude.setText(String.valueOf(location.getLongitude()));

    }

    public FormationCoordinates getLocationFromAddress (String address) throws IOException {
        Geocoder geocoder = new Geocoder(this);
        List<Address> addresses;
        FormationCoordinates p1;
        addresses = geocoder.getFromLocationName(address, 5);

        if (address == null) {
            return null;
        }

        Address location = addresses.get(0);
        double lat = location.getLatitude();
        double lng = location.getLongitude();

        p1 = new FormationCoordinates(lat, lng);

        return p1;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void afficherAdressesCentreFormation(String string) {
        try {
            JSONObject jsonObject = new JSONObject(string);
            JSONArray jsonArray = jsonObject.getJSONArray("liste_centre_formation");

            listeAdresses = new HashMap<>();
            listeId = new HashMap<>();

            for (int i=0; i<jsonArray.length(); i++) {
                JSONObject jsonData = jsonArray.getJSONObject(i);
                adresseComplete = jsonData.getString("adresse") + jsonData.getString("localite");
                listeAdresses.put(jsonData.getString("libelle"), adresseComplete);
                listeId.put(jsonData.getString("libelle"), jsonData.getString("id_centre_formation"));
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    public void afficherCentreFormationProximite(View view) {
        listeLibelleCentreFormation = new ArrayList();

        if(listeAdresses.size() == 0) {
            Toast.makeText(this, "Aucune formation référencée ne se trouve à proximité.", Toast.LENGTH_SHORT).show();
        }

        for(Map.Entry entry:listeAdresses.entrySet()) {
            adresse = (String) entry.getValue();
            locationAdresse = new FormationCoordinates();
            try {
                locationAdresse = getLocationFromAddress(adresse);
            } catch (IOException e) {
                e.printStackTrace();
            }

            if((locationAdresse.getLatitude() <= maLatitude + 0.5 &&
                    locationAdresse.getLatitude() >= maLatitude - 0.5) ||
                    (locationAdresse.getLongitude() <= maLongitude + 0.5 &&
                    locationAdresse.getLongitude() >= maLongitude - 0.5)) {
                listeLibelleCentreFormation.add(entry.getKey());
            }
        }

        ArrayAdapter adapter = new ArrayAdapter(this, R.layout.activity_listview, listeLibelleCentreFormation);
        listView = (ListView) findViewById(R.id.liste_centre_proximite);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Object o = listView.getItemAtPosition(position);
                String action = o.toString();

                for (int i=0; i<listeLibelleCentreFormation.size(); i++) {
                    if(action.equals(listeLibelleCentreFormation.get(i))) {
                        intent = new Intent(getApplicationContext(), FicheCentreFormation.class);
                        intent.putExtra("idCentreFormation", listeId.get(listeLibelleCentreFormation.get(i)));
                        startActivity(intent);
                    }
                }
            }
        });

    }
}
