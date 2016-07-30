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
import android.widget.ListView;
import android.widget.TextView;

import com.company.formationadvisor.R;
import com.company.formationadvisor.db.UtilisateurDAO;
import com.company.formationadvisor.modeles.Formation;
import com.company.formationadvisor.modeles.FormationAdaptater;
import com.company.formationadvisor.modeles.IPAddress;
import com.company.formationadvisor.modeles.Utilisateur;
import com.company.formationadvisor.taches_asynchrones.RechercherFormationParIdCentreFormation;
import com.company.formationadvisor.taches_asynchrones.RechercherParIdCentreFormation;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class FicheCentreFormation extends AppCompatActivity implements RechercherParIdCentreFormation.IRechercheParIdCentreFormation,
        RechercherFormationParIdCentreFormation.IRechercheFormationParIdCentreFormation {

    Intent intent;
    String idCentreFormation, token;
    int idUtilisateur;
    //UtilisateurDAO utilisateurDAO;
    //Utilisateur utilisateur;
    SharedPreferences preferences;

    TextView nom, rue, codePostal, localite, telephone, email, siteInternet;
    String text1, text2, text3, text4, text5, text6, text7;
    JSONObject jsonObject;

    ArrayList listeNomFormation, listeDateDebut, listeDateFin, listeDescription;
    ArrayList<Formation> listeFormation;
    Formation formation;
    String nomFormation, dateDebut, dateFin, description;

    ListView listView;

    IPAddress ipAddress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fiche_centre_formation);

        nom = (TextView) findViewById(R.id.nom_centre_formation);
        rue = (TextView) findViewById(R.id.adresse_centre_formation);
        codePostal = (TextView) findViewById(R.id.code_postal_centre_formation);
        localite = (TextView) findViewById(R.id.localite_centre_formation);
        telephone = (TextView) findViewById(R.id.telephone_centre_formation);
        email = (TextView) findViewById(R.id.email_centre_formation);
        siteInternet = (TextView) findViewById(R.id.site_internet_centre_formation);

        Bundle extra = this.getIntent().getExtras();
        if (extra != null) {
            idCentreFormation = extra.getString("idCentreFormation");
        }

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        idUtilisateur = preferences.getInt("id", 0);

        /*utilisateurDAO = new UtilisateurDAO(this);
        utilisateur = new Utilisateur();
        utilisateurDAO.openReadable();
        utilisateur = utilisateurDAO.getUtilisateurById(idUtilisateur);
        utilisateurDAO.close();
        token = utilisateur.getSel();*/
        token = preferences.getString("token", "");
        Log.i("TOKEN", token);

        ipAddress = new IPAddress();

        RechercherParIdCentreFormation tache1 = new RechercherParIdCentreFormation(this, ipAddress);
        RechercherFormationParIdCentreFormation tache2 = new RechercherFormationParIdCentreFormation(this, ipAddress);
        tache1.execute(idCentreFormation, token);
        tache2.execute(idCentreFormation, token);
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
    public void afficherInfoCentreFormation(String string) {
        try {
            jsonObject = new JSONObject(string);
            text1 = jsonObject.getString("libelle");
            text2 = jsonObject.getString("adresse");
            text3 = jsonObject.getString("code_postal");
            text4 = jsonObject.getString("numero_de_telephone");
            text5 = jsonObject.getString("email");
            text6 = jsonObject.getString("site_internet");
            text7 = jsonObject.getString("localite");

            nom.setText(text1);
            rue.setText(text2);
            codePostal.setText(text3);
            telephone.setText(text4);
            email.setText(text5);
            siteInternet.setText(text6);
            localite.setText(text7);

        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void afficherInfoFormation(String string) {
        Log.i("CONTENT_STRING", string);

        try {
            jsonObject = new JSONObject(string);
            JSONArray jsonArray = jsonObject.getJSONArray("liste_formation");

            listeNomFormation = new ArrayList();
            listeDateDebut = new ArrayList();
            listeDateFin = new ArrayList();
            listeDescription = new ArrayList();

            for (int i=0; i<jsonArray.length(); i++) {
                JSONObject jsonData = jsonArray.getJSONObject(i);
                listeNomFormation.add(jsonData.getString("libelle"));
                listeDateDebut.add(jsonData.getString("date_de_debut"));
                listeDateFin.add(jsonData.getString("date_de_fin"));
                listeDescription.add(jsonData.getString("description"));
            }

            listeFormation = new ArrayList<>();

            for(int i=0; i<listeNomFormation.size(); i++) {
                nomFormation = (String) listeNomFormation.get(i);
                dateDebut = (String) listeDateDebut.get(i);
                dateFin = (String) listeDateFin.get(i);
                description = (String) listeDescription.get(i);

                formation = new Formation(nomFormation, dateDebut, dateFin, description);
                listeFormation.add(formation);

                listView = (ListView) findViewById(R.id.liste_formation);
                FormationAdaptater formationAdaptater = new FormationAdaptater(this, listeFormation);
                listView.setAdapter(formationAdaptater);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

    }
}
