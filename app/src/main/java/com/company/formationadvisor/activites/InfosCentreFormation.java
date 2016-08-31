package com.company.formationadvisor.activites;

import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v4.app.NavUtils;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.TextView;

import com.company.formationadvisor.R;
import com.company.formationadvisor.modeles.IPAddress;
import com.company.formationadvisor.taches_asynchrones.RechercherParIdCentreFormation;

import org.json.JSONException;
import org.json.JSONObject;

public class InfosCentreFormation extends AppCompatActivity implements RechercherParIdCentreFormation.IRechercheParIdCentreFormation{

    TextView etablissement, rue, codePostal, localite, telephone, email, siteInternet;
    IPAddress ipAddress;
    SharedPreferences preferences;
    String token, idCentreFormation;
    JSONObject jsonObject;
    String text5, text6, text7, text8, text9, text10, text11;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_infos_centre_formation);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        etablissement = (TextView) findViewById(R.id.nom_centre_formation);
        rue = (TextView) findViewById(R.id.adresse_centre_formation);
        codePostal = (TextView) findViewById(R.id.code_postal_centre_formation);
        localite = (TextView) findViewById(R.id.localite_centre_formation);
        telephone = (TextView) findViewById(R.id.telephone_centre_formation);
        email = (TextView) findViewById(R.id.email_centre_formation);
        siteInternet = (TextView) findViewById(R.id.site_internet_centre_formation);

        ipAddress = new IPAddress();

        preferences = PreferenceManager.getDefaultSharedPreferences(this);
        token = preferences.getString("token", "");
        idCentreFormation = preferences.getString("idCentreFormation", "");

        RechercherParIdCentreFormation tache = new RechercherParIdCentreFormation(this, ipAddress);
        tache.execute(idCentreFormation, token);
    }

    @Override
    public void afficherInfoCentreFormation(String string) {

        try {
            jsonObject = new JSONObject(string);
            text5 = jsonObject.getString("libelle");
            text6 = jsonObject.getString("adresse");
            text7 = jsonObject.getString("code_postal");
            text8 = jsonObject.getString("numero_de_telephone");
            text9 = jsonObject.getString("email");
            text10 = jsonObject.getString("site_internet");
            text11 = jsonObject.getString("localite");

            etablissement.setText(text5);
            rue.setText(text6);
            codePostal.setText(text7);
            telephone.setText(text8);
            email.setText(text9);
            siteInternet.setText(text10);
            localite.setText(text11);

        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                NavUtils.navigateUpFromSameTask(this);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
