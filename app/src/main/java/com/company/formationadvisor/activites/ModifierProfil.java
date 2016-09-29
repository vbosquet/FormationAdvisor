package com.company.formationadvisor.activites;

import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v4.app.NavUtils;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.company.formationadvisor.R;
import com.company.formationadvisor.db.UtilisateurDAO;
import com.company.formationadvisor.modeles.IPAddress;
import com.company.formationadvisor.modeles.Utilisateur;
import com.company.formationadvisor.taches_asynchrones.ModificationProfilUtilisateur;
import com.company.formationadvisor.taches_asynchrones.RechercherUtilisateurParPseudo;

import org.json.JSONException;
import org.json.JSONObject;

import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;

import static java.lang.String.valueOf;

public class ModifierProfil extends AppCompatActivity implements RechercherUtilisateurParPseudo.IRechercheUtilisateurParPseudo,
        ModificationProfilUtilisateur.IModifierProfilUtilisateur {

    SharedPreferences preferences;
    String pseudo, token;
    EditText text1, text2, text3;
    Intent intent;
    IPAddress ipAddress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modifier_profil);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        preferences = PreferenceManager.getDefaultSharedPreferences(this);
        pseudo = preferences.getString("pseudo", "");
        token = preferences.getString("token", "");

        ipAddress = new IPAddress();

        RechercherUtilisateurParPseudo tache = new RechercherUtilisateurParPseudo(this, ipAddress);
        tache.execute(pseudo);

    }

    public void enregistrerModificationProfil(View view) {

        ModificationProfilUtilisateur tache = new ModificationProfilUtilisateur(this, ipAddress);
        tache.execute(text1.getText().toString(), text2.getText().toString(),
                text3.getText().toString(), pseudo, token);
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
    public void afficherResultatRecherche(String string) throws JSONException, NoSuchProviderException, NoSuchAlgorithmException {
        JSONObject jsonObject = new JSONObject(string);
        JSONObject infosUtilisateur = new JSONObject(jsonObject.getString("info_utilisateur"));

        String nomUtilisateur = infosUtilisateur.getString("nom");
        String prenomUtilisateur = infosUtilisateur.getString("prenom");
        String emailUtilisateur = infosUtilisateur.getString("email");

        text1 = (EditText) findViewById(R.id.nom_utilisateur);
        if (text1 != null) {
            text1.setText(nomUtilisateur);
        }

        text2 = (EditText) findViewById(R.id.prenom_utilisateur);
        if (text2 != null) {
            text2.setText(prenomUtilisateur);
        }

        text3 = (EditText) findViewById(R.id.email_utilisateur);
        if (text3 != null) {
            text3.setText(emailUtilisateur);
        }
    }

    @Override
    public void afficherConfirmationModificationProfil(String string) {
        try {
            JSONObject jsonObject = new JSONObject(string);
            String message = jsonObject.getString("success");

            if (message.equals("true")) {
                Toast.makeText(this, "Mise à jour réussie", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Echec de la mise à jour", Toast.LENGTH_SHORT).show();
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
