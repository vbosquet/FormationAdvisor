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
import android.widget.Toast;

import com.company.formationadvisor.R;
import com.company.formationadvisor.db.UtilisateurDAO;
import com.company.formationadvisor.modeles.IPAddress;
import com.company.formationadvisor.modeles.Utilisateur;
import com.company.formationadvisor.taches_asynchrones.SuppressionEvaluation;
import com.company.formationadvisor.taches_asynchrones.SuppressionFormationParIdUtilisateur;
import com.company.formationadvisor.taches_asynchrones.SuppressionUtilisateur;

import org.json.JSONException;
import org.json.JSONObject;

public class SupprimerCompte extends AppCompatActivity implements SuppressionEvaluation.ISupprimerEvaluation,
        SuppressionFormationParIdUtilisateur.ISupprimerFormationParIdUtilisateur,
        SuppressionUtilisateur.ISupprimerUtilisateur {

    SharedPreferences preferences;
    String pseudo, token, message1, message2, message3;
    int idUtilisateur;
    Intent intent;
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

        SuppressionEvaluation tache1 = new SuppressionEvaluation(this, ipAddress);
        tache1.execute(pseudo, token);
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

    @Override
    public void confirmerSuppressionEvaluation(String string) {
        try {
            JSONObject jsonObject = new JSONObject(string);
            message1 = jsonObject.getString("success");

            if (message1.equals("true") || message1.equals("empty")) {
                SuppressionFormationParIdUtilisateur tache2 = new SuppressionFormationParIdUtilisateur(this, ipAddress);
                tache2.execute(String.valueOf(idUtilisateur), token);

            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void confirmerSuppressionFormation(String string) {
        try {
            JSONObject jsonObject = new JSONObject(string);
            message2 = jsonObject.getString("success");

            if (message2.equals("true") || message2.equals("empty")) {
                SuppressionUtilisateur tache3 = new SuppressionUtilisateur(this, ipAddress);
                tache3.execute(String.valueOf(idUtilisateur), token);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void afficherConfirmationSuppressionUtilisateur(String string) {
        try {
            JSONObject jsonObject = new JSONObject(string);
            message3 = jsonObject.getString("success");

            if (message3.equals("true")) {
                Toast.makeText(this, "Supression réussie", Toast.LENGTH_SHORT).show();
                intent = new Intent(this, MainActivity.class);
                startActivity(intent);
            } else {
                Toast.makeText(this, "Echec de la suppression", Toast.LENGTH_SHORT).show();
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
