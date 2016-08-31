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
import com.company.formationadvisor.taches_asynchrones.CreerNouvelleEvaluation;

import org.json.JSONException;
import org.json.JSONObject;

public class AjouterEvaluation extends AppCompatActivity implements CreerNouvelleEvaluation.ICreationNouvelleEvaluation{

    String idFormation, pseudo, token, message;
    SharedPreferences preferences;
    EditText titre, commentaire;
    JSONObject jsonObject;
    Intent intent;
    IPAddress ipAddress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ajouter_evaluation);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        preferences = PreferenceManager.getDefaultSharedPreferences(this);
        pseudo = preferences.getString("pseudo", "");
        token = preferences.getString("token", "");

        Bundle extra = this.getIntent().getExtras();
        if (extra != null) {
            idFormation = extra.getString("idFormation");
        }

        titre = (EditText) findViewById(R.id.titre_evaluation);
        commentaire = (EditText) findViewById(R.id.commentaire_evaluation);

        ipAddress = new IPAddress();
    }

    public void enregistrerNouvelleEvaluation(View view) {
        if (titre.getText().toString().equals("") ||
                commentaire.getText().toString().equals("")){
            Toast.makeText(this, "Vous devez remplir tous les champs.", Toast.LENGTH_SHORT).show();
        } else {
            CreerNouvelleEvaluation tache = new CreerNouvelleEvaluation(this, ipAddress);
            tache.execute(titre.getText().toString(), commentaire.getText().toString(), idFormation, pseudo, token);
        }
    }

    @Override
    public void confirmerEnregistrement(String string) {
        try {
            jsonObject = new JSONObject(string);
            message = jsonObject.getString("success");

            if (message.equals("true")) {
                Toast.makeText(this, "Enregistrement réussi", Toast.LENGTH_SHORT).show();
                intent = new Intent(this, FicheFormation.class);
                startActivity(intent);
            } else if(message.equals("exist")) {
                Toast.makeText(this, "Vous avez déjà commenté cette formation.", Toast.LENGTH_SHORT).show();
            } else  {
               Toast.makeText(this, "Echec de l'enregistrement", Toast.LENGTH_SHORT).show();
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    /*@Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.options_menu, menu);
        return true;
    }*/

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            /*case R.id.tableau_de_bord:
                intent = new Intent(this, TableauDeBord.class);
                startActivity(intent);
                return true;
            case R.id.deconnexion:
                intent = new Intent(this, MainActivity.class);
                startActivity(intent);
                return true;*/
            case android.R.id.home:
                NavUtils.navigateUpFromSameTask(this);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
