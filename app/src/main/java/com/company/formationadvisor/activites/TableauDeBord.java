package com.company.formationadvisor.activites;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SearchView;

import com.company.formationadvisor.R;

public class TableauDeBord extends AppCompatActivity {

    String[] liste_action_utilisateur = {"Modifier mon profil", "Rechercher ma position actuelle", "Référencer une formation",
            "Modifier mes formations", "Paramètres", "Déconnexion"};

    Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tableau_de_bord);

        handleIntent(getIntent());

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, R.layout.activity_listview, liste_action_utilisateur);
        final ListView listView = (ListView) findViewById(R.id.tableau_de_bord);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Object o = listView.getItemAtPosition(position);
                String action = o.toString();

                if (action.equals(liste_action_utilisateur[0])) {
                    intent = new Intent(getApplicationContext(), ModifierProfil.class);
                    startActivity(intent);
                }

                if (action.equals(liste_action_utilisateur[1])) {
                    intent = new Intent(getApplicationContext(), RecherchePosition.class);
                    startActivity(intent);
                }

                if (action.equals(liste_action_utilisateur[2])) {
                    intent = new Intent(getApplicationContext(), NouveauCentreFormation.class);
                    startActivity(intent);
                }

                if (action.equals(liste_action_utilisateur[3])) {
                    intent = new Intent(getApplicationContext(), MaListeDeFormations.class);
                    startActivity(intent);
                }

                if (action.equals(liste_action_utilisateur[4])) {
                    intent = new Intent(getApplicationContext(), Parametres.class);
                    startActivity(intent);
                }

                if (action.equals(liste_action_utilisateur[5])) {
                    intent = new Intent(getApplicationContext(), MainActivity.class);
                    startActivity(intent);
                }
            }
        });
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
            Log.i("CONTENT_QUERY", query);
            Intent intent1 = new Intent(this, ResultatRechercheFormation.class);
            intent1.putExtra("motCle", query);
            startActivity(intent1);

        }
    }
}
