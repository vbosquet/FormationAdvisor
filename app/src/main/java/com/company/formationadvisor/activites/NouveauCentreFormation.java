package com.company.formationadvisor.activites;

import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v4.app.NavUtils;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.company.formationadvisor.R;
import com.company.formationadvisor.modeles.IPAddress;
import com.company.formationadvisor.modeles.Organisme;
import com.company.formationadvisor.taches_asynchrones.RechercherFormationParIdUtilisateur;
import com.company.formationadvisor.taches_asynchrones.RechercherParIdCentreFormation;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;


public class NouveauCentreFormation extends AppCompatActivity implements
        RechercherFormationParIdUtilisateur.IRechercherFormationParIdUtilisateur,
        RechercherParIdCentreFormation.IRechercheParIdCentreFormation, AdapterView.OnItemSelectedListener {

    EditText etablissement, rue, codePostal, localite, telephone, email, siteInternet;
    String token;
    Intent intent;
    SharedPreferences preferences;
    int idUtilisateur;
    IPAddress ipAddress;
    ArrayList<String> menuItems, listeNomEtablissement, listIdCentreFormation, listeRueEtablissement, listeCodePostalEtablissement, listeLocaliteEtablissement, listeTelephoneEtablissement, listeEmailEtablissement, listeSiteInternetEtablissement;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nouveau_centre_formation);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        etablissement = (EditText) findViewById(R.id.nom_centre_formation);
        rue = (EditText) findViewById(R.id.adresse_centre_formation);
        codePostal = (EditText) findViewById(R.id.code_postal_centre_formation);
        localite = (EditText) findViewById(R.id.localite_centre_formation);
        telephone = (EditText) findViewById(R.id.telephone_centre_formation);
        email = (EditText) findViewById(R.id.email_centre_formation);
        siteInternet = (EditText) findViewById(R.id.site_internet_centre_formation);

        preferences = PreferenceManager.getDefaultSharedPreferences(this);
        idUtilisateur = preferences.getInt("id", 0);
        token = preferences.getString("token", "");

        ipAddress = new IPAddress();

        listeNomEtablissement = new ArrayList<>();
        listIdCentreFormation = new ArrayList<>();
        listeRueEtablissement = new ArrayList<>();
        listeCodePostalEtablissement = new ArrayList<>();
        listeLocaliteEtablissement = new ArrayList<>();
        listeTelephoneEtablissement = new ArrayList<>();
        listeEmailEtablissement = new ArrayList<>();
        listeSiteInternetEtablissement = new ArrayList<>();
        menuItems = new ArrayList<>();
        menuItems.add("Ajouter organisme");

        RechercherFormationParIdUtilisateur rechercherFormationParIdUtilisateur = new RechercherFormationParIdUtilisateur(this, ipAddress);
        rechercherFormationParIdUtilisateur.execute(String.valueOf(idUtilisateur), token);
    }

    public void continuerEnregistrementFormation(View view) {

        if (etablissement.getText().toString().equals("") ||
                rue.getText().toString().equals("") ||
                codePostal.getText().toString().equals("") ||
                localite.getText().toString().equals("") ||
                telephone.getText().toString().equals("") ||
                email.getText().toString().equals("") ||
                siteInternet.getText().toString().equals("")){
            Toast.makeText(this, "Vous devez remplir tous les champs.", Toast.LENGTH_SHORT).show();
        } else {
            Organisme newOrganisme = new Organisme(etablissement.getText().toString(), rue.getText().toString(), codePostal.getText().toString(),
                    localite.getText().toString(), telephone.getText().toString(), email.getText().toString(), siteInternet.getText().toString());

            intent = new Intent(this, NouvelleFormation.class);
            intent.putExtra("organismeData", newOrganisme);
            startActivity(intent);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.drop_down_menu, menu);
        MenuItem item = menu.findItem(R.id.nom_etablissement_formation);
        final Spinner spinner = (Spinner) MenuItemCompat.getActionView(item);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, R.layout.activity_drop_down_menu, menuItems);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(this);

        return true;
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
    public void afficherInfoFormation(String string) {
        try {
            JSONObject jsonObject = new JSONObject(string);
            JSONArray jsonArray = jsonObject.getJSONArray("liste_formation");

            for (int i = 0; i<jsonArray.length(); i++) {
                JSONObject jsonData = jsonArray.getJSONObject(i);
                String idCentreFormation = jsonData.getString("id_centre_formation");

                RechercherParIdCentreFormation rechercherParIdCentreFormation = new RechercherParIdCentreFormation(this, ipAddress);
                rechercherParIdCentreFormation.execute(idCentreFormation, token);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void afficherInfoCentreFormation(String string) {
        try {
            JSONObject jsonObject = new JSONObject(string);
            String nomEtablissement = jsonObject.getString("libelle");
            String rueEtablissement = jsonObject.getString("adresse");
            String codePostalEtablissement = jsonObject.getString("code_postal");
            String localiteEtablissement = jsonObject.getString("localite");
            String telephoneEtablissement = jsonObject.getString("numero_de_telephone");
            String emailEtablissement = jsonObject.getString("email");
            String siteInternetEtablissement = jsonObject.getString("site_internet");
            String idEtablissement = jsonObject.getString("id_centre_formation");
            String dernierNomAjoute = menuItems.get(menuItems.size() - 1);

            if (!nomEtablissement.equals(dernierNomAjoute)) {
                menuItems.add(nomEtablissement);
                listeNomEtablissement.add(nomEtablissement);
                listIdCentreFormation.add(idEtablissement);
                listeRueEtablissement.add(rueEtablissement);
                listeCodePostalEtablissement.add(codePostalEtablissement);
                listeLocaliteEtablissement.add(localiteEtablissement);
                listeTelephoneEtablissement.add(telephoneEtablissement);
                listeEmailEtablissement.add(emailEtablissement);
                listeSiteInternetEtablissement.add(siteInternetEtablissement);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        String itemSelected = String.valueOf(menuItems.get(position));

        for (int i = 0; i<listeNomEtablissement.size(); i++) {
            if (itemSelected.equals(listeNomEtablissement.get(i))) {
                Organisme newOrganisme = new Organisme(listeNomEtablissement.get(i), listeRueEtablissement.get(i), listeCodePostalEtablissement.get(i),
                        listeLocaliteEtablissement.get(i), listeTelephoneEtablissement.get(i), listeEmailEtablissement.get(i), listeSiteInternetEtablissement.get(i));
                intent = new Intent(getApplicationContext(), NouvelleFormation.class);
                intent.putExtra("organismeData", newOrganisme);
                startActivity(intent);
            }
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}
