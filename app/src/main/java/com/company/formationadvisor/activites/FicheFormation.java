package com.company.formationadvisor.activites;

import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v4.app.NavUtils;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.company.formationadvisor.R;
import com.company.formationadvisor.modeles.IPAddress;
import com.company.formationadvisor.taches_asynchrones.RechercherParIdFormation;
import org.json.JSONException;
import org.json.JSONObject;

public class FicheFormation extends AppCompatActivity implements RechercherParIdFormation.IRechercheParIdFormation {

    TextView nom, description, dateDebut, dateFin;
    String idFormation, token;
    JSONObject jsonObject;
    String text1,text2, text3, text4;
    Intent intent;
    SharedPreferences preferences;
    IPAddress ipAddress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fiche_formation);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        nom = (TextView) findViewById(R.id.nom_formation);
        description = (TextView) findViewById(R.id.description_formation);
        dateDebut = (TextView) findViewById(R.id.date_debut_formation);
        dateFin = (TextView) findViewById(R.id.date_fin_formation);

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        token = preferences.getString("token", "");
        idFormation = preferences.getString("idFormation", "");

        ipAddress = new IPAddress();

        RechercherParIdFormation rechercherParIdFormation = new RechercherParIdFormation(this, ipAddress);
        rechercherParIdFormation.execute(idFormation, token);
    }

    @Override
    public void afficherInfoFormation(String string) {
        try {
            jsonObject = new JSONObject(string);
            text1 = jsonObject.getString("libelle");
            text2 = jsonObject.getString("description");
            text3 = jsonObject.getString("date_de_debut");
            text4 = jsonObject.getString("date_de_fin");

            nom.setText(text1);
            description.setText(text2);
            dateDebut.setText(text3);
            dateFin.setText(text4);

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

    public void displayComments(View view) {
        intent = new Intent(getApplicationContext(), Commentaires.class);
        startActivity(intent);
    }

    public void displayTrainingCenterInfo(View view) {
        intent = new Intent(getApplicationContext(), InfosCentreFormation.class);
        startActivity(intent);
    }
}
