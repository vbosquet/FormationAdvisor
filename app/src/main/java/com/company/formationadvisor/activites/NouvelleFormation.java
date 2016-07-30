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
import android.widget.EditText;
import android.widget.Toast;

import com.company.formationadvisor.R;
import com.company.formationadvisor.db.UtilisateurDAO;
import com.company.formationadvisor.modeles.IPAddress;
import com.company.formationadvisor.modeles.Utilisateur;
import com.company.formationadvisor.taches_asynchrones.CreerNouvelleFormation;

import org.json.JSONException;
import org.json.JSONObject;

public class NouvelleFormation extends AppCompatActivity implements CreerNouvelleFormation.ICreationFormation {

    EditText nomFormation, dateDebut, dateFin, description;
    JSONObject jsonObject;
    String message, token;
    SharedPreferences preferences;
    Integer idUtilisateur;
    Intent intent;
    IPAddress ipAddress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nouvelle_formation);

        nomFormation = (EditText) findViewById(R.id.nom_formation);
        dateDebut = (EditText) findViewById(R.id.date_debut_formation);
        dateFin = (EditText) findViewById(R.id.date_fin_formation);
        description = (EditText) findViewById(R.id.description_formation);

        preferences = PreferenceManager.getDefaultSharedPreferences(this);
        idUtilisateur = preferences.getInt("id", 0);
        token = preferences.getString("token", "");

        ipAddress = new IPAddress();
    }

    @Override
    public void confirmationCreation(String string) {
        try {
            jsonObject = new JSONObject(string);
            message = jsonObject.getString("success");

            if(message.equals("true")) {
                Toast.makeText(this, "Enregistrement réussi", Toast.LENGTH_SHORT).show();
                intent = new Intent(this, TableauDeBord.class);
                startActivity(intent);

            } else if(message.equals("exist")) {
                Toast.makeText(this, "Cette formation existe déjà.", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Echec de l'enregistrement", Toast.LENGTH_SHORT).show();
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    public void ajouterNouvelleFormation(View view){

        Bundle extra = this.getIntent().getExtras();
        String idCentreFormation = null;

        if (extra != null) {
            idCentreFormation = extra.getString("id_centre_formation");
        }

        Log.i("ID_CENTRE", idCentreFormation);

        if (nomFormation.getText().toString().equals("") ||
                dateDebut.getText().toString().equals("") ||
                dateFin.getText().toString().equals("") ||
                description.getText().toString().equals("")) {
            Toast.makeText(this, "Vous devez remplir tous les champs.", Toast.LENGTH_SHORT).show();
        } else {
            CreerNouvelleFormation tache = new CreerNouvelleFormation(this, ipAddress);
            tache.execute(nomFormation.getText().toString(), dateDebut.getText().toString(),
                    dateFin.getText().toString(), description.getText().toString(),
                    String.valueOf(idUtilisateur), idCentreFormation, token);
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