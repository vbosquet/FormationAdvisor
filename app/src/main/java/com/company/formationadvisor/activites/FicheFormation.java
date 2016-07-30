package com.company.formationadvisor.activites;

import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import com.company.formationadvisor.R;
import com.company.formationadvisor.db.UtilisateurDAO;
import com.company.formationadvisor.modeles.Evaluation;
import com.company.formationadvisor.modeles.EvaluationAdaptater;
import com.company.formationadvisor.modeles.IPAddress;
import com.company.formationadvisor.modeles.Utilisateur;
import com.company.formationadvisor.taches_asynchrones.RechercherEvaluation;
import com.company.formationadvisor.taches_asynchrones.RechercherParIdCentreFormation;
import com.company.formationadvisor.taches_asynchrones.RechercherParIdFormation;
import com.company.formationadvisor.taches_asynchrones.RechercherUtilisateurParId;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import static java.lang.String.valueOf;

public class FicheFormation extends AppCompatActivity implements RechercherParIdCentreFormation.IRechercheParIdCentreFormation,
        RechercherParIdFormation.IRechercheParIdFormation,
        RechercherEvaluation.IRechercheEvaluationParIdFormation {

    TextView nom, dateDebut, dateFin, description;
    TextView etablissement, rue, codePostal, localite, telephone, email, siteInternet;
    String idFormation, idCentreFormation, titre, commentaire, auteur, token;
    JSONObject jsonObject;
    String text1,text2, text3, text4;
    String text5, text6, text7, text8, text9, text10, text11;
    Intent intent;
    ArrayList listeTitreEvaluation, listeCommentaireEvaluation, listePseudoAuteurEvaluation;
    ArrayList<Evaluation> listeEvaluation;
    Evaluation evaluation;
    ListView listView;
    SharedPreferences preferences;
    IPAddress ipAddress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fiche_formation);

        listeTitreEvaluation = new ArrayList();
        listeCommentaireEvaluation = new ArrayList();
        listePseudoAuteurEvaluation = new ArrayList();
        listeEvaluation = new ArrayList<>();

        nom = (TextView) findViewById(R.id.nom_formation);
        dateDebut = (TextView) findViewById(R.id.date_debut_formation);
        dateFin = (TextView) findViewById(R.id.date_fin_formation);
        description = (TextView) findViewById(R.id.description_formation);
        etablissement = (TextView) findViewById(R.id.nom_centre_formation);
        rue = (TextView) findViewById(R.id.adresse_centre_formation);
        codePostal = (TextView) findViewById(R.id.code_postal_centre_formation);
        localite = (TextView) findViewById(R.id.localite_centre_formation);
        telephone = (TextView) findViewById(R.id.telephone_centre_formation);
        email = (TextView) findViewById(R.id.email_centre_formation);
        siteInternet = (TextView) findViewById(R.id.site_internet_centre_formation);

        Bundle extra = this.getIntent().getExtras();
        if (extra != null) {
            idFormation = extra.getString("idFormation");
            idCentreFormation = extra.getString("idCentreFormation");
        }

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        token = preferences.getString("token", "");

        ipAddress = new IPAddress();

        RechercherParIdFormation tache1 = new RechercherParIdFormation(this, ipAddress);
        RechercherParIdCentreFormation tache2 = new RechercherParIdCentreFormation(this, ipAddress);
        RechercherEvaluation tache3 = new RechercherEvaluation(this, ipAddress);

        tache1.execute(idFormation, token);
        tache2.execute(idCentreFormation, token);
        tache3.execute(idFormation, token);
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
    public void afficherInfoFormation(String string) {
        try {
            jsonObject = new JSONObject(string);
            text1 = jsonObject.getString("libelle");
            text2 = jsonObject.getString("date_de_debut");
            text3 = jsonObject.getString("date_de_fin");
            text4 = jsonObject.getString("description");

            nom.setText(text1);
            dateDebut.setText(text2);
            dateFin.setText(text3);
            description.setText(text4);

        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    public void ajouterNouvelleEvaluation(View view) {
        intent = new Intent(this, AjouterEvaluation.class);
        intent.putExtra("idFormation", idFormation);
        startActivity(intent);
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.options_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.tableau_de_bord:
                intent = new Intent(this, TableauDeBord.class);
                startActivity(intent);
                return true;
            case R.id.deconnexion:
                intent = new Intent(this, MainActivity.class);
                startActivity(intent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
