package com.company.formationadvisor.activites;

import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v4.app.NavUtils;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.company.formationadvisor.R;
import com.company.formationadvisor.modeles.IPAddress;
import com.company.formationadvisor.taches_asynchrones.AjouterLocationCentreFormation;
import com.company.formationadvisor.taches_asynchrones.CreerNouveauCentreFormation;
import com.company.formationadvisor.taches_asynchrones.GetCoordinatesFromAddress;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


public class NouveauCentreFormation extends AppCompatActivity implements CreerNouveauCentreFormation.ICreationCentreFormation {

    EditText etablissement, rue, codePostal, localite, telephone, email, siteInternet;
    JSONObject jsonObject;
    String idCentreFormation, token;
    Intent intent;
    SharedPreferences preferences;
    int idUtilisateur;
    IPAddress ipAddress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nouveau_centre_formation);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        etablissement = (EditText) findViewById(R.id.nom_centre_formation);
        rue = (EditText) findViewById(R.id.adresse_centre_formation);
        codePostal = (EditText) findViewById(R.id.code_postal_centre_formation);
        localite = (EditText) findViewById(R.id.localite_centre_formation);
        telephone = (EditText) findViewById(R.id.telephone_centre_formation);
        email = (EditText) findViewById(R.id.email_centre_formation);
        siteInternet = (EditText) findViewById(R.id.site_internet_centre_formation);

        preferences = PreferenceManager.getDefaultSharedPreferences(this);
        idUtilisateur = preferences.getInt("id", 0);
        token = preferences.getString("token", "");

        ipAddress = new IPAddress();
    }

    public void continuerEnregistrementFormation(View view) {

        if (etablissement.getText().toString().equals("") ||
                rue.getText().toString().equals("") ||
                codePostal.getText().toString().equals("") ||
                localite.getText().toString().equals("") ||
                telephone.getText().toString().equals("") ||
                email.getText().toString().equals("") ||
                siteInternet.getText().toString().equals("")){
            Toast.makeText(this, "Vous devez remplir tous les champs.", Toast.LENGTH_SHORT).show();
        } else {
            CreerNouveauCentreFormation creerNouveauCentreFormation = new CreerNouveauCentreFormation(this, ipAddress);
            creerNouveauCentreFormation.execute(etablissement.getText().toString(), rue.getText().toString(), codePostal.getText().toString(),
                    localite.getText().toString(), telephone.getText().toString(), email.getText().toString(),
                    siteInternet.getText().toString(), token);
        }
    }

    @Override
    public void recuperationIdCentreFormation(String string) throws JSONException {
        jsonObject = new JSONObject(string);
        idCentreFormation = jsonObject.getString("id_centre_formation");

        intent = new Intent(this, NouvelleFormation.class);
        intent.putExtra("id_centre_formation", idCentreFormation);
        startActivity(intent);
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
