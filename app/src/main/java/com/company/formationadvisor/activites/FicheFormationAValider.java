package com.company.formationadvisor.activites;

import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.company.formationadvisor.R;
import com.company.formationadvisor.modeles.IPAddress;
import com.company.formationadvisor.taches_asynchrones.RechercherParIdCentreFormation;
import com.company.formationadvisor.taches_asynchrones.RechercherParIdFormation;
import com.company.formationadvisor.taches_asynchrones.RechercherUtilisateurParId;
import com.company.formationadvisor.taches_asynchrones.ValidationFormation;

import org.json.JSONException;
import org.json.JSONObject;

public class FicheFormationAValider extends AppCompatActivity implements RechercherParIdFormation.IRechercheParIdFormation,
        RechercherParIdCentreFormation.IRechercheParIdCentreFormation,
        RechercherUtilisateurParId.IRecchercheUtilisateurParId,
        ValidationFormation.IValiderFormation {

    TextView auteur, nom, dateDebut, dateFin, description;
    TextView etablissement, rue, codePostal, localite, telephone, email, siteInternet;
    String text1,text2, text3, text4, text5, text6, text7, text8, text9, text10, text11, text12;
    String idFormation, idCentreFormation, idUtilisateur, token;
    Intent intent;
    SharedPreferences preferences;
    IPAddress ipAddress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fiche_formation_avalider);

        auteur = (TextView) findViewById(R.id.auteur_annonce_formation);
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

        ipAddress = new IPAddress();
        preferences = PreferenceManager.getDefaultSharedPreferences(this);
        token = preferences.getString("token", "");

        Bundle extra = this.getIntent().getExtras();
        if (extra != null) {
            idFormation = extra.getString("idFormation");
            idCentreFormation = extra.getString("idCentreFormation");
            idUtilisateur = extra.getString("idUtilisateur");
        }

        RechercherParIdFormation tache1 = new RechercherParIdFormation(this, ipAddress);
        RechercherParIdCentreFormation tache2 = new RechercherParIdCentreFormation(this, ipAddress);
        RechercherUtilisateurParId tache3 = new RechercherUtilisateurParId(this, ipAddress);
        tache1.execute(idFormation, token);
        tache2.execute(idCentreFormation, token);
        tache3.execute(idUtilisateur, token);
    }

    @Override
    public void afficherInfoFormation(String string) {
        try {
            JSONObject jsonObject = new JSONObject(string);
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

    @Override
    public void afficherInfoCentreFormation(String string) {
        try {
            JSONObject jsonObject = new JSONObject(string);
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
    public void afficherInfosUtilisateur(String string) {
        try {
            JSONObject jsonObject = new JSONObject(string);
            text12 = jsonObject.getString("username");
            auteur.setText(text12);

        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    public void contacterAuteurAnnonce(View view) {
    }

    public void approuverAnnonceFormation(View view) {
        ValidationFormation tache4 = new ValidationFormation(this, ipAddress);
        tache4.execute(idFormation, token);
    }

    @Override
    public void confirmationValidation(String string) {
        try {
            JSONObject jsonObject = new JSONObject(string);
            String message = jsonObject.getString("success");

            if (message.equals("true")) {
                Toast.makeText(this, "Validation réussie", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Echec de la validation", Toast.LENGTH_SHORT).show();
            }


        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
