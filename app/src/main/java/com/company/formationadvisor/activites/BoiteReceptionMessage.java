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
import android.widget.AdapterView;
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
    String pseudo, token, texteMessage, expediteurMessage, dateEnvoiMessage;
    IPAddress ipAddress;
    ArrayList<Message> listeMessage;
    ArrayList listeIdMessage, listeTexte, listeExpediteur, listeDateEnvoi;
    Message message;
    ListView listView;
    Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_boite_reception_message1);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        ipAddress = new IPAddress();

        preferences = PreferenceManager.getDefaultSharedPreferences(this);
        pseudo = preferences.getString("pseudo", "");
        token = preferences.getString("token", "");

        RechercherMessageParPseudo tache = new RechercherMessageParPseudo(this, ipAddress);
        tache.execute(pseudo, token);
    }

    @Override
    public void afficherResultatRechercheMessage(String string) {

        try {
            JSONObject jsonObject = new JSONObject(string);
            JSONArray jsonArray = jsonObject.getJSONArray("liste_message");

            if (jsonArray.length() > 0) {

                setContentView(R.layout.activity_boite_reception_message2);

                listeIdMessage = new ArrayList();
                listeTexte = new ArrayList();
                listeExpediteur = new ArrayList();
                listeDateEnvoi = new ArrayList();
                listeMessage = new ArrayList<>();

                for(int i = 0; i < jsonArray.length(); i++) {
                    JSONObject jsonData = jsonArray.getJSONObject(i);

                    listeIdMessage.add(jsonData.getString("id_message"));
                    listeTexte.add(jsonData.getString("texte"));
                    listeExpediteur.add(jsonData.getString("expediteur"));
                    listeDateEnvoi.add(jsonData.getString("date_envoi"));
                }

                for(int i = 0; i<listeTexte.size(); i++) {

                    texteMessage = (String) listeTexte.get(i);
                    expediteurMessage = (String) listeExpediteur.get(i);
                    dateEnvoiMessage = (String) listeDateEnvoi.get(i);

                    message = new Message(texteMessage, expediteurMessage, dateEnvoiMessage);
                    listeMessage.add(message);
                }

                listView = (ListView) findViewById(R.id.liste_message);
                MessageAdapater messageAdapater = new MessageAdapater(this, listeMessage);
                listView.setAdapter(messageAdapater);

            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    public void supprimerMessage(View view) {

        int position = listView.getPositionForView(view);
        String idMessage = String.valueOf(listeIdMessage.get(position));

        SupprimerMessage supprimerMessage = new SupprimerMessage(this, ipAddress);
        supprimerMessage.execute(idMessage, token);
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
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                NavUtils.navigateUpFromSameTask(this);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
