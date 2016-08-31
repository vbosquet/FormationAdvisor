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
import android.widget.ListView;

import com.company.formationadvisor.R;
import com.company.formationadvisor.modeles.Evaluation;
import com.company.formationadvisor.modeles.EvaluationAdaptater;
import com.company.formationadvisor.modeles.IPAddress;
import com.company.formationadvisor.taches_asynchrones.RechercherEvaluation;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class Commentaires extends AppCompatActivity implements RechercherEvaluation.IRechercheEvaluationParIdFormation {

    JSONObject jsonObject;
    SharedPreferences preferences;
    IPAddress ipAddress;
    String token, idFormation, auteur, commentaire, titre;
    ArrayList listeTitreEvaluation, listeCommentaireEvaluation, listePseudoAuteurEvaluation;
    ArrayList<Evaluation> listeEvaluation;
    Evaluation evaluation;
    ListView listView;
    Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_commentaires);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        ipAddress = new IPAddress();
        preferences = PreferenceManager.getDefaultSharedPreferences(this);
        token = preferences.getString("token", "");
        idFormation = preferences.getString("idFormation", "");

        listeTitreEvaluation = new ArrayList();
        listeCommentaireEvaluation = new ArrayList();
        listePseudoAuteurEvaluation = new ArrayList();
        listeEvaluation = new ArrayList<>();

        RechercherEvaluation tache2 = new RechercherEvaluation(this, ipAddress);
        tache2.execute(idFormation, token);
    }

    @Override
    public void afficherInfoEvaluation(String string) {
        try {
            jsonObject = new JSONObject(string);
            JSONArray jsonArray = jsonObject.getJSONArray("liste_evaluation");

            for(int i = 0; i<jsonArray.length(); i++) {
                JSONObject jsonData = jsonArray.getJSONObject(i);
                listeTitreEvaluation.add(jsonData.getString("titre"));
                listeCommentaireEvaluation.add(jsonData.getString("commentaire"));
                listePseudoAuteurEvaluation.add(jsonData.getString("auteur"));
            }

            for(int i = 0; i<listeTitreEvaluation.size(); i++) {

                titre = (String) listeTitreEvaluation.get(i);
                commentaire = (String) listeCommentaireEvaluation.get(i);
                auteur = (String) listePseudoAuteurEvaluation.get(i);

                evaluation = new Evaluation(titre, commentaire, auteur);
                listeEvaluation.add(evaluation);
            }

            listView = (ListView) findViewById(R.id.liste_evaluation);
            EvaluationAdaptater evaluationAdaptater = new EvaluationAdaptater(this, listeEvaluation);
            listView.setAdapter(evaluationAdaptater);


        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void ajouterNouvelleEvaluation() {
        intent = new Intent(this, AjouterEvaluation.class);
        intent.putExtra("idFormation", idFormation);
        startActivity(intent);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.options_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case  R.id.nouveau:
                ajouterNouvelleEvaluation();
                return true;
            case android.R.id.home:
                NavUtils.navigateUpFromSameTask(this);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
