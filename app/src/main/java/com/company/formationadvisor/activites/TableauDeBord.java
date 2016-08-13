package com.company.formationadvisor.activites;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.app.NotificationCompat;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SearchView;

import com.company.formationadvisor.R;
import com.company.formationadvisor.modeles.IPAddress;
import com.company.formationadvisor.taches_asynchrones.RechercherMessageParPseudo;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class TableauDeBord extends AppCompatActivity implements RechercherMessageParPseudo.IRechercheMessageParPseudo{

    String[] liste_action_utilisateur = {"Boîte de réception","Modifier mon profil", "Rechercher ma position actuelle", "Référencer une formation",
            "Modifier mes formations", "Paramètres", "Déconnexion"};
    String[] liste_action_admin = {"Approuver les formations", "Déconnexion"};
    Intent intent;
    SharedPreferences preferences;
    String admin, pseudo, token;
    IPAddress ipAddress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tableau_de_bord);

        ipAddress = new IPAddress();
        preferences = PreferenceManager.getDefaultSharedPreferences(this);
        admin = preferences.getString("admin", "");
        pseudo = preferences.getString("pseudo", "");
        token = preferences.getString("token", "");

        handleIntent(getIntent());

        if (admin.equals("false")) {

            RechercherMessageParPseudo tache = new RechercherMessageParPseudo(this, ipAddress);
            tache.execute(pseudo, token);

            ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, R.layout.activity_listview, liste_action_utilisateur);
            final ListView listView = (ListView) findViewById(R.id.tableau_de_bord);
            listView.setAdapter(adapter);

            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    Object o = listView.getItemAtPosition(position);
                    String action = o.toString();

                    if (action.equals(liste_action_utilisateur[0])) {
                        intent = new Intent(getApplicationContext(), BoiteReceptionMessage.class);
                        startActivity(intent);
                    }

                    if (action.equals(liste_action_utilisateur[1])) {
                        intent = new Intent(getApplicationContext(), ModifierProfil.class);
                        startActivity(intent);
                    }

                    if (action.equals(liste_action_utilisateur[2])) {
                        intent = new Intent(getApplicationContext(), RecherchePosition.class);
                        startActivity(intent);
                    }

                    if (action.equals(liste_action_utilisateur[3])) {
                        intent = new Intent(getApplicationContext(), NouveauCentreFormation.class);
                        startActivity(intent);
                    }

                    if (action.equals(liste_action_utilisateur[4])) {
                        intent = new Intent(getApplicationContext(), MaListeDeFormations.class);
                        startActivity(intent);
                    }

                    if (action.equals(liste_action_utilisateur[5])) {
                        intent = new Intent(getApplicationContext(), Parametres.class);
                        startActivity(intent);
                    }

                    if (action.equals(liste_action_utilisateur[6])) {
                        intent = new Intent(getApplicationContext(), MainActivity.class);
                        startActivity(intent);
                    }
                }
            });
        } else {
            ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, R.layout.activity_listview, liste_action_admin);
            final ListView listView = (ListView) findViewById(R.id.tableau_de_bord);
            listView.setAdapter(adapter);

            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    Object o = listView.getItemAtPosition(position);
                    String action = o.toString();

                    if (action.equals(liste_action_admin[0])) {
                        intent = new Intent(getApplicationContext(), ApprouverFormation.class);
                        startActivity(intent);
                    }

                    if (action.equals(liste_action_admin[1])) {
                        intent = new Intent(getApplicationContext(), MainActivity.class);
                        startActivity(intent);
                    }
                }
            });
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.search_menu, menu);

        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        SearchView searchView = (SearchView) menu.findItem(R.id.action_search).getActionView();
        searchView.setIconifiedByDefault(true);
        searchView.setSubmitButtonEnabled(true);
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));

        return true;
    }

    @Override
    protected void onNewIntent(Intent intent) {
        handleIntent(intent);
    }

    private void handleIntent(Intent intent) {
        if(intent.ACTION_SEARCH.equals(intent.getAction())) {
            String query = intent.getStringExtra(SearchManager.QUERY);
            Intent intent1 = new Intent(this, ResultatRechercheFormation.class);
            intent1.putExtra("motCle", query);
            startActivity(intent1);

        }
    }

    @Override
    public void afficherResultatRecherche(String string) {

        try {
            JSONObject jsonObject = new JSONObject(string);
            JSONArray jsonArray = jsonObject.getJSONArray("liste_message");

            if(jsonArray.length() > 0) {
                NotificationCompat.Builder builder = new NotificationCompat.Builder(this);
                builder.setSmallIcon(R.mipmap.ic_launcher);
                builder.setContentTitle("FormationAdvisor");
                builder.setContentText("Vous avez reçu " + jsonArray.length() +" nouveau(x) message(s).");
                builder.setSound(Settings.System.getUriFor(Settings.System.NOTIFICATION_SOUND));
                builder.setVibrate(new long[] {200, 200, 200, 200, 200});

                Intent resultIntent = new Intent(this, MainActivity.class);
                PendingIntent resultPendingIntent = PendingIntent.getActivity(this, 0, resultIntent, PendingIntent.FLAG_UPDATE_CURRENT);
                builder.setContentIntent(resultPendingIntent);

                Notification notification = builder.build();
                int ID = 001;

                NotificationManager manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
                manager.notify(ID, notification);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

    }
}
