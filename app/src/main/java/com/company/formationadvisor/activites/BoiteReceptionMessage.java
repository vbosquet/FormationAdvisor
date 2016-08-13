package com.company.formationadvisor.activites;

import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.company.formationadvisor.R;
import com.company.formationadvisor.modeles.IPAddress;
import com.company.formationadvisor.modeles.Message;
import com.company.formationadvisor.modeles.MessageAdapater;
import com.company.formationadvisor.taches_asynchrones.RechercherMessageParPseudo;
import com.company.formationadvisor.taches_asynchrones.SupprimerMessage;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class BoiteReceptionMessage extends AppCompatActivity implements RechercherMessageParPseudo.IRechercheMessageParPseudo,
        SupprimerMessage.ISuppressionMessage {

    SharedPreferences preferences;
    String pseudo, token, titreMessage, texteMessage, expediteurMessage, dateEnvoiMessage;
    IPAddress ipAddress;
    ArrayList<Message> listeMessage;
    ArrayList listeTitre, listeTexte, listeExpediteur, listeDateEnvoi;
    Message message;
    ListView listView;
    Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_boite_reception_message);

        ipAddress = new IPAddress();

        preferences = PreferenceManager.getDefaultSharedPreferences(this);
        pseudo = preferences.getString("pseudo", "");
        token = preferences.getString("token", "");

        RechercherMessageParPseudo tache = new RechercherMessageParPseudo(this, ipAddress);
        tache.execute(pseudo, token);
    }

    @Override
    public void afficherResultatRecherche(String string) {
        try {
            JSONObject jsonObject = new JSONObject(string);
            JSONArray jsonArray = jsonObject.getJSONArray("liste_message");

            listeTitre = new ArrayList();
            listeTexte = new ArrayList();
            listeExpediteur = new ArrayList();
            listeDateEnvoi = new ArrayList();
            listeMessage = new ArrayList<>();

            for(int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonData = jsonArray.getJSONObject(i);
                listeTitre.add(jsonData.getString("titre"));
                listeTexte.add(jsonData.getString("texte"));
                listeExpediteur.add(jsonData.getString("expediteur"));
                listeDateEnvoi.add(jsonData.getString("date_envoi"));
            }

            for(int i = 0; i<listeTitre.size(); i++) {

                titreMessage = (String) listeTitre.get(i);
                texteMessage = (String) listeTexte.get(i);
                expediteurMessage = (String) listeExpediteur.get(i);
                dateEnvoiMessage = (String) listeDateEnvoi.get(i);

                message = new Message(titreMessage, texteMessage, expediteurMessage, dateEnvoiMessage);
                listeMessage.add(message);
            }

            listView = (ListView) findViewById(R.id.liste_message);
            MessageAdapater messageAdapater = new MessageAdapater(this, listeMessage);
            listView.setAdapter(messageAdapater);

        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    public void supprimerMessage(View view) {
        SupprimerMessage tache = new SupprimerMessage(this, ipAddress);
        tache.execute(pseudo, token);
    }

    @Override
    public void afficherConfirmationSuppressionMessage(String string) {
        try {
            JSONObject jsonObject = new JSONObject(string);
            String message = jsonObject.getString("success");

            if (message.equals("true")) {
                Toast.makeText(this, "Votre message a bien été supprimé.", Toast.LENGTH_SHORT).show();
                intent = new Intent(this, BoiteReceptionMessage.class);
                startActivity(intent);

            } else {
                Toast.makeText(this, "Impossible de supprimer votre message.", Toast.LENGTH_SHORT).show();
            }

        } catch (JSONException e) {
            e.printStackTrace();
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
