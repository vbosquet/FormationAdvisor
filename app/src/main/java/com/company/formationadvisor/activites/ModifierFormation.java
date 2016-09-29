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
import com.company.formationadvisor.taches_asynchrones.ModificationCentreFormation;
import com.company.formationadvisor.taches_asynchrones.ModificationFormation;
import com.company.formationadvisor.taches_asynchrones.RechercherParIdCentreFormation;
import com.company.formationadvisor.taches_asynchrones.RechercherParIdFormation;
import com.company.formationadvisor.taches_asynchrones.SuppressionFormationParIdFormation;
import com.company.formationadvisor.taches_asynchrones.SuppressionLocalite;

import org.json.JSONException;
import org.json.JSONObject;

public class ModifierFormation extends AppCompatActivity implements RechercherParIdFormation.IRechercheParIdFormation,
        RechercherParIdCentreFormation.IRechercheParIdCentreFormation,
        ModificationFormation.IModificationFormation,
        ModificationCentreFormation.IModificationCentreFormation,
        SuppressionFormationParIdFormation.ISuppressionFormation {

    EditText nom, dateDebut, dateFin, description;
    EditText etablissement, rue, codePostal, localite;
    EditText telephone, email, siteInternet;
    JSONObject jsonObject;
    String text1,text2, text3, text4;
    String text5, text6, text7, text8, text9, text10, text11;
    String idFormation, idCentreFormation, message, token;
    int idUtilisateur;
    Intent intent;
    SharedPreferences preferences;
    IPAddress ipAddress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modifier_formation);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        nom = (EditText) findViewById(R.id.nom_formation);
        dateDebut = (EditText) findViewById(R.id.date_debut_formation);
        dateFin = (EditText) findViewById(R.id.date_fin_formation);
        description = (EditText) findViewById(R.id.description_formation);
        etablissement = (EditText) findViewById(R.id.nom_centre_formation);
        rue = (EditText) findViewById(R.id.adresse_centre_formation);
        codePostal = (EditText) findViewById(R.id.code_postal_centre_formation);
        localite = (EditText) findViewById(R.id.localite_centre_formation);
        telephone = (EditText) findViewById(R.id.telephone_centre_formation);
        email = (EditText) findViewById(R.id.email_centre_formation);
        siteInternet = (EditText) findViewById(R.id.site_internet_centre_formation);

        Bundle extra = this.getIntent().getExtras();
        if (extra != null) {
            idFormation = extra.getString("idFormation");
            idCentreFormation = extra.getString("idCentreFormation");
        }

        preferences = PreferenceManager.getDefaultSharedPreferences(this);
        idUtilisateur = preferences.getInt("id", 0);
        token = preferences.getString("token", "");

        ipAddress = new IPAddress();

        RechercherParIdFormation rechercherParIdFormation = new RechercherParIdFormation(this, ipAddress);
        RechercherParIdCentreFormation rechercherParIdCentreFormation = new RechercherParIdCentreFormation(this, ipAddress);

        rechercherParIdFormation.execute(idFormation, token);
        rechercherParIdCentreFormation.execute(idCentreFormation, token);
    }

    public void modifierFormation() {
        ModificationFormation modificationFormation = new ModificationFormation(this, ipAddress);
        modificationFormation.execute(nom.getText().toString(), description.getText().toString(),
                dateDebut.getText().toString(), dateFin.getText().toString(), idFormation, token);

        ModificationCentreFormation modificationCentreFormation = new ModificationCentreFormation(this, ipAddress);
        modificationCentreFormation.execute(etablissement.getText().toString(), rue.getText().toString(),
                codePostal.getText().toString(), telephone.getText().toString(),
                email.getText().toString(), siteInternet.getText().toString(), idCentreFormation,
                localite.getText().toString(), token);
    }

    public void supprimerFormation() {
        SuppressionFormationParIdFormation suppressionFormationParIdFormation = new SuppressionFormationParIdFormation(this, ipAddress);
        suppressionFormationParIdFormation.execute(idFormation, token);
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
    public void confirmationModificationFormation(String string) {
        try {
            jsonObject = new JSONObject(string);
            message = jsonObject.getString("success");

            if (message.equals("true")) {
                Toast.makeText(this, "Modification réussie", Toast.LENGTH_SHORT).show();
                intent = new Intent(this, ModifierFormation.class);
                intent.putExtra("idFormation", idFormation);
                intent.putExtra("idCentreFormation", idCentreFormation);
                startActivity(intent);

            } else {
                Toast.makeText(this, "Echec des modifications", Toast.LENGTH_SHORT).show();
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void confirmationModificationCentreFormation(String string) {
        try {
            jsonObject = new JSONObject(string);
            message = jsonObject.getString("success");

            if (message.equals("true")) {
                Toast.makeText(this, "Modification réussie", Toast.LENGTH_SHORT).show();
                intent = new Intent(this, ModifierFormation.class);
                intent.putExtra("idFormation", idFormation);
                intent.putExtra("idCentreFormation", idCentreFormation);
                startActivity(intent);
            } else {
                Toast.makeText(this, "Echec des modifications", Toast.LENGTH_SHORT).show();
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void confirmationSuppressionFormation(String string) {
        try {
            jsonObject = new JSONObject(string);
            message = jsonObject.getString("success");

            if (message.equals("true")) {
                Toast.makeText(this, "Suppression réussie", Toast.LENGTH_SHORT).show();
                intent = new Intent(this, MaListeDeFormations.class);
                startActivity(intent);
            } else {
                Toast.makeText(this, "Echec de la suppression", Toast.LENGTH_SHORT).show();
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.formation_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.editer_formation:
                modifierFormation();
                return true;
            case R.id.supprimer_formation:
                supprimerFormation();
                return true;
            case android.R.id.home:
                NavUtils.navigateUpFromSameTask(this);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
