package com.company.formationadvisor.activites;

import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v4.app.NavUtils;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.company.formationadvisor.R;
import com.company.formationadvisor.modeles.Evaluation;
import com.company.formationadvisor.modeles.EvaluationAdaptater;
import com.company.formationadvisor.modeles.IPAddress;
import com.company.formationadvisor.taches_asynchrones.RechercherEvaluation;
import com.company.formationadvisor.taches_asynchrones.RechercherParIdCentreFormation;
import com.company.formationadvisor.taches_asynchrones.RechercherParIdFormation;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import static java.lang.String.valueOf;

public class FicheFormation extends AppCompatActivity implements RechercherParIdFormation.IRechercheParIdFormation {

    TextView nom, description, dateDebut, dateFin;
    String idFormation, titre, commentaire, auteur, token;
    JSONObject jsonObject;
    String text1,text2, text3, text4;
    Intent intent;
    ListView listView;
    SharedPreferences preferences;
    IPAddress ipAddress;
    String[] listeActionUtilisateur = {"Organisme", "Commentaires"};

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

        /*Bundle extra = this.getIntent().getExtras();
        if (extra != null) {
            idFormation = extra.getString("idFormation");
            idCentreFormation = extra.getString("idCentreFormation");
        }*/

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        token = preferences.getString("token", "");
        idFormation = preferences.getString("idFormation", "");

        ipAddress = new IPAddress();

        RechercherParIdFormation tache1 = new RechercherParIdFormation(this, ipAddress);
        tache1.execute(idFormation, token);

        ArrayAdapter arrayAdapter = new ArrayAdapter(this, R.layout.activity_listview, listeActionUtilisateur);
        listView = (ListView) findViewById(R.id.autres_infos);
        if (listView != null) {
            listView.setAdapter(arrayAdapter);
        }

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Object o = listView.getItemAtPosition(position);
                String action = o.toString();

                if (action.equals(listeActionUtilisateur[0])) {
                    intent = new Intent(getApplicationContext(), InfosCentreFormation.class);
                    startActivity(intent);
                }

                if (action.equals(listeActionUtilisateur[1])) {
                    intent = new Intent(getApplicationContext(), Commentaires.class);
                    startActivity(intent);
                }
            }
        });
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
}
