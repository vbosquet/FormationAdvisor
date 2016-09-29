package com.company.formationadvisor.activites;

import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.company.formationadvisor.R;
import com.company.formationadvisor.modeles.IPAddress;
import com.company.formationadvisor.taches_asynchrones.RechercherFormationParIdUtilisateur;
import com.company.formationadvisor.taches_asynchrones.SuppressionCentreFormation;
import com.company.formationadvisor.taches_asynchrones.SuppressionEvaluation;
import com.company.formationadvisor.taches_asynchrones.SuppressionFormationParIdUtilisateur;
import com.company.formationadvisor.taches_asynchrones.SuppressionLocalite;
import com.company.formationadvisor.taches_asynchrones.SuppressionUtilisateur;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class SupprimerCompte extends AppCompatActivity implements SuppressionEvaluation.ISupprimerEvaluation,
        SuppressionFormationParIdUtilisateur.ISupprimerFormationParIdUtilisateur,
        SuppressionUtilisateur.ISupprimerUtilisateur,
        RechercherFormationParIdUtilisateur.IRechercherFormationParIdUtilisateur,
        SuppressionCentreFormation.ISuppressionCentreFormation,
        SuppressionLocalite.ISuppressionLocalite {

    SharedPreferences preferences;
    String pseudo, token;
    int idUtilisateur;
    IPAddress ipAddress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_supprimer_compte);

        preferences = PreferenceManager.getDefaultSharedPreferences(this);
        idUtilisateur = preferences.getInt("id", 0);
        pseudo = preferences.getString("pseudo", "");
        token = preferences.getString("token", "");

        ipAddress = new IPAddress();

    }

    public  void supressionCompte(View view) {

        SuppressionEvaluation suppressionEvaluation = new SuppressionEvaluation(this, ipAddress);
        suppressionEvaluation.execute(pseudo, token);

        RechercherFormationParIdUtilisateur rechercherFormationParIdUtilisateur = new RechercherFormationParIdUtilisateur(this, ipAddress);
        rechercherFormationParIdUtilisateur.execute(String.valueOf(idUtilisateur), token);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void confirmerSuppressionEvaluation(String string) {
        try {
            JSONObject jsonObject = new JSONObject(string);
            String message = jsonObject.getString("success");

            if (message.equals("true") || message.equals("empty")) {
                SuppressionFormationParIdUtilisateur suppressionFormationParIdUtilisateur = new SuppressionFormationParIdUtilisateur(this, ipAddress);
                suppressionFormationParIdUtilisateur.execute(String.valueOf(idUtilisateur), token);

            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void confirmerSuppressionFormation(String string) {
        try {
            JSONObject jsonObject = new JSONObject(string);
            String message = jsonObject.getString("success");

            if (message.equals("true") || message.equals("empty")) {
                SuppressionUtilisateur suppressionUtilisateur = new SuppressionUtilisateur(this, ipAddress);
                suppressionUtilisateur.execute(String.valueOf(idUtilisateur), token);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void afficherConfirmationSuppressionUtilisateur(String string) {
        try {
            JSONObject jsonObject = new JSONObject(string);
            String message = jsonObject.getString("success");

            if (message.equals("true")) {
                Toast.makeText(this, "Supression réussie", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(this, MainActivity.class);
                startActivity(intent);
            } else {
                Toast.makeText(this, "Echec de la suppression", Toast.LENGTH_SHORT).show();
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void afficherInfoFormation(String string) {
        try {
            JSONObject jsonObject = new JSONObject(string);
            JSONArray jsonArray = jsonObject.getJSONArray("liste_formation");

            for (int i = 0; i<jsonArray.length(); i++) {
                JSONObject jsonData = jsonArray.getJSONObject(i);
                String idCentreFormation = jsonData.getString("id_centre_formation");

                SuppressionCentreFormation suppressionCentreFormation = new SuppressionCentreFormation(this, ipAddress);
                suppressionCentreFormation.execute(idCentreFormation, token);

                SuppressionLocalite suppressionLocalite = new SuppressionLocalite(this, ipAddress);
                suppressionLocalite.execute(idCentreFormation, token);
            }


        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void confirmerSuppressionCentreFormation(String string) {
        Log.i("SUPPRESSION_CENTRE", string);
    }

    @Override
    public void confirmerSuppresionLocalite(String string) {
        Log.i("SUPPRESSION_LOCALITE", string);
    }
}
