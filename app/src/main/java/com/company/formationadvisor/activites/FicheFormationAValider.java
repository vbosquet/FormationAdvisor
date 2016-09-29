package com.company.formationadvisor.activites;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v4.app.NavUtils;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.company.formationadvisor.R;
import com.company.formationadvisor.modeles.IPAddress;
import com.company.formationadvisor.taches_asynchrones.AjouterLocationCentreFormation;
import com.company.formationadvisor.taches_asynchrones.EnvoyerMessage;
import com.company.formationadvisor.taches_asynchrones.GetCoordinatesFromAddress;
import com.company.formationadvisor.taches_asynchrones.RechercherParIdCentreFormation;
import com.company.formationadvisor.taches_asynchrones.RechercherParIdFormation;
import com.company.formationadvisor.taches_asynchrones.RechercherUtilisateurParId;
import com.company.formationadvisor.taches_asynchrones.ValidationFormation;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.IDN;

public class FicheFormationAValider extends AppCompatActivity implements RechercherParIdFormation.IRechercheParIdFormation,
        RechercherParIdCentreFormation.IRechercheParIdCentreFormation,
        RechercherUtilisateurParId.IRecchercheUtilisateurParId,
        ValidationFormation.IValiderFormation,
        EnvoyerMessage.IEnvoiNouveauMessage,
        GetCoordinatesFromAddress.IGetCoordinatesFromAddress,
        AjouterLocationCentreFormation.IAjouterLocationCentreFormation{

    TextView nomAuteur, prenomAuteur, nom, dateDebut, dateFin, description;
    TextView etablissement, rue, codePostal, localite, telephone, email, siteInternet;
    String text1,text2, text3, text4, text5, text6, text7, text8, text9, text10, text11, text12, text13, text14;
    String idFormation, idCentreFormation, idUtilisateur, token, pseudoExpediteurMessage;
    Intent intent;
    SharedPreferences preferences;
    IPAddress ipAddress;
    EditText titreMessage, texteMessage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fiche_formation_avalider);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        nomAuteur = (TextView) findViewById(R.id.nom_auteur_annonce_formation);
        prenomAuteur = (TextView) findViewById(R.id.prenom_auteur_annonce_formation);
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
        pseudoExpediteurMessage = preferences.getString("pseudo", "");

        Bundle extra = this.getIntent().getExtras();
        if (extra != null) {
            idFormation = extra.getString("idFormation");
            idCentreFormation = extra.getString("idCentreFormation");
            idUtilisateur = extra.getString("idUtilisateur");
        }

        RechercherParIdFormation rechercherParIdFormation = new RechercherParIdFormation(this, ipAddress);
        RechercherParIdCentreFormation rechercherParIdCentreFormation = new RechercherParIdCentreFormation(this, ipAddress);
        RechercherUtilisateurParId rechercherUtilisateurParId = new RechercherUtilisateurParId(this, ipAddress);

        rechercherParIdFormation.execute(idFormation, token);
        rechercherParIdCentreFormation.execute(idCentreFormation, token);
        rechercherUtilisateurParId.execute(idUtilisateur, token);
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
            text13 = jsonObject.getString("nom");
            text14 = jsonObject.getString("prenom");

            nomAuteur.setText(text13);
            prenomAuteur.setText(text14);

        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    public void approuverAnnonceFormation() {
        String titreMessage = "Validation de votre annonce";
        String texteMessage = "Votre annonce a été validée.";

        ValidationFormation validationFormation = new ValidationFormation(this, ipAddress);
        validationFormation.execute(idFormation, token);

        EnvoyerMessage envoyerMessage = new EnvoyerMessage(this, ipAddress);
        envoyerMessage.execute(titreMessage, texteMessage, pseudoExpediteurMessage, text12, token);

        String adresseComplete = text6 + ", " + text7 + " " + text8;
        GetCoordinatesFromAddress getCoordinatesFromAddress = new GetCoordinatesFromAddress(this);
        getCoordinatesFromAddress.execute(adresseComplete);
    }

    public void contacterAuteurAnnonce() {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater inflater = this.getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.dialog_contact, null);

        titreMessage = (EditText) dialogView.findViewById(R.id.titre_message);
        texteMessage = (EditText) dialogView.findViewById(R.id.texte_message);

        builder.setTitle("Contacter l'auteur de l'annonce");
        builder.setView(dialogView);
        builder.setPositiveButton("Contacter", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                EnvoyerMessage envoyerMessage = new EnvoyerMessage(FicheFormationAValider.this, ipAddress);
                envoyerMessage.execute(titreMessage.getText().toString(), texteMessage.getText().toString(), pseudoExpediteurMessage, text12, token);
            }
        });

        builder.setNegativeButton("Annuler", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });

        AlertDialog dialog = builder.create();
        dialog.show();
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.formation_a_valider_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.contact:
                contacterAuteurAnnonce();
                return true;
            case R.id.check:
                approuverAnnonceFormation();
                return true;
            case android.R.id.home:
                NavUtils.navigateUpFromSameTask(this);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void afficherConfirmationEnvoiNouveauMessage(String string) {
        try {
            JSONObject jsonObject = new JSONObject(string);
            String message = jsonObject.getString("success");

            if (message.equals("true")) {
                Toast.makeText(this, "Votre message a bien été envoyé.", Toast.LENGTH_SHORT).show();

            } else {
                Toast.makeText(this, "L'envoi de votre message a échoué.", Toast.LENGTH_SHORT).show();
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void getCoordinates(String string) {
        try {
            JSONObject jsonObject = new JSONObject(string);
            JSONArray jsonArray = jsonObject.getJSONArray("results");

            for(int i = 0; i<jsonArray.length(); i++) {
                JSONObject item = jsonArray.getJSONObject(i);
                JSONObject geoJson = item.getJSONObject("geometry");
                JSONObject locJson = geoJson.getJSONObject("location");
                Double latitude = Double.parseDouble(locJson.getString("lat"));
                Double longitude = Double.parseDouble(locJson.getString("lng"));

                AjouterLocationCentreFormation ajouterLocationCentreFormation = new AjouterLocationCentreFormation(this, ipAddress);
                ajouterLocationCentreFormation.execute(text5,
                        String.valueOf(latitude),
                        String.valueOf(longitude),
                        idCentreFormation, token);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void confirmationEnregistrementLocation(String string) {
        Log.i("LOCATION", string);
    }
}
