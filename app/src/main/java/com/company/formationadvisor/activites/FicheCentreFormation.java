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
import android.widget.ListView;
import android.widget.TextView;

import com.company.formationadvisor.R;
import com.company.formationadvisor.modeles.Formation;
import com.company.formationadvisor.modeles.FormationAdaptater;
import com.company.formationadvisor.modeles.IPAddress;
import com.company.formationadvisor.taches_asynchrones.RechercherFormationParIdCentreFormation;
import com.company.formationadvisor.taches_asynchrones.RechercherParIdCentreFormation;
import com.company.formationadvisor.taches_asynchrones.SupprimerMessage;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class FicheCentreFormation extends AppCompatActivity implements
        RechercherFormationParIdCentreFormation.IRechercheFormationParIdCentreFormation {

    Intent intent;
    String idCentreFormation, token, nomFormation, dateDebut, dateFin, description;
    int idUtilisateur;
    SharedPreferences preferences;
    JSONObject jsonObject;
    ArrayList listeIdFormation, listeNomFormation, listeDateDebut, listeDateFin, listeDescription;
    ArrayList<Formation> listeFormation;
    Formation formation;
    ListView listView;
    IPAddress ipAddress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fiche_centre_formation);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        Bundle extra = this.getIntent().getExtras();
        if (extra != null) {
            idCentreFormation = extra.getString("idCentreFormation");
        }

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        idUtilisateur = preferences.getInt("id", 0);
        token = preferences.getString("token", "");

        ipAddress = new IPAddress();

        RechercherFormationParIdCentreFormation rechercherFormationParIdCentreFormation = new RechercherFormationParIdCentreFormation(this, ipAddress);
        rechercherFormationParIdCentreFormation.execute(idCentreFormation, token);
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

    @Override
    public void afficherInfoFormation(String string) {

        try {
            jsonObject = new JSONObject(string);
            JSONArray jsonArray = jsonObject.getJSONArray("liste_formation");

            listeIdFormation = new ArrayList();
            listeNomFormation = new ArrayList();
            listeDateDebut = new ArrayList();
            listeDateFin = new ArrayList();
            listeDescription = new ArrayList();

            for (int i=0; i<jsonArray.length(); i++) {
                JSONObject jsonData = jsonArray.getJSONObject(i);
                listeIdFormation.add((jsonData.getString("id_formation")));
                listeNomFormation.add(jsonData.getString("libelle"));
                listeDateDebut.add(jsonData.getString("date_de_debut"));
                listeDateFin.add(jsonData.getString("date_de_fin"));
                listeDescription.add(jsonData.getString("description"));
            }

            listeFormation = new ArrayList<>();

            for(int i=0; i<listeNomFormation.size(); i++) {
                nomFormation = (String) listeNomFormation.get(i);
                dateDebut = (String) listeDateDebut.get(i);
                dateFin = (String) listeDateFin.get(i);
                description = (String) listeDescription.get(i);

                formation = new Formation(nomFormation, dateDebut, dateFin, description);
                listeFormation.add(formation);

                listView = (ListView) findViewById(R.id.liste_formation);
                FormationAdaptater formationAdaptater = new FormationAdaptater(this, listeFormation);
                listView.setAdapter(formationAdaptater);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    public void displayMoreInformation(View view) {

        int position = listView.getPositionForView(view);
        String idFormation = String.valueOf(listeIdFormation.get(position));

        preferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("idFormation", idFormation);
        editor.putString("idCentreFormation", idCentreFormation);
        editor.apply();

        intent = new Intent(getApplicationContext(), FicheFormation.class);
        startActivity(intent);


    }
}
