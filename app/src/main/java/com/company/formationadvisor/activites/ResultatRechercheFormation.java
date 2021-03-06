package com.company.formationadvisor.activites;

import android.app.SearchManager;
import android.content.Context;
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
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.Toast;

import com.company.formationadvisor.R;
import com.company.formationadvisor.modeles.IPAddress;
import com.company.formationadvisor.taches_asynchrones.RechercheParMotCle;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class ResultatRechercheFormation extends AppCompatActivity implements RechercheParMotCle.IRechercherParMotCle{

    ArrayList listeLibelleFormation, listeIdFormation, listeIdCentreFormation;
    String motCle, message, token;
    Intent intent;
    SharedPreferences preferences;
    IPAddress ipAddress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_resultat_recherche_formation);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        preferences = PreferenceManager.getDefaultSharedPreferences(this);
        token = preferences.getString("token", "");
        motCle = preferences.getString("motCle", "");

        ipAddress = new IPAddress();

        RechercheParMotCle rechercheParMotCle = new RechercheParMotCle(this, ipAddress);
        rechercheParMotCle.execute(motCle, token);

        handleIntent(getIntent());
    }

    @Override
    public void afficherResultatRecherche(String string) {
        try {
            JSONObject jsonObject = new JSONObject(string);
            message = jsonObject.getString("SUCCESS");

            if(message.equals("false")){
                Toast.makeText(this, "Aucun résultat ne correspond à votre recherche.", Toast.LENGTH_SHORT).show();
            } else {

                JSONArray jsonArray = jsonObject.getJSONArray("liste_formation");

                listeLibelleFormation = new ArrayList();
                listeIdFormation = new ArrayList();
                listeIdCentreFormation = new ArrayList();

                for(int i = 0; i<jsonArray.length(); i++) {
                    JSONObject jsonData = jsonArray.getJSONObject(i);
                    listeLibelleFormation.add(jsonData.getString("libelle"));
                    listeIdFormation.add(jsonData.getString("id_formation"));
                    listeIdCentreFormation.add(jsonData.getString("id_centre_formation"));
                }

                ArrayAdapter adapter = new ArrayAdapter(this, R.layout.activity_listview, listeLibelleFormation);
                final ListView listView = (ListView) findViewById(R.id.resultat_recherche_formation);
                listView.setAdapter(adapter);

                listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        for (int i = 0; i<listeLibelleFormation.size(); i++) {

                            if (position == i) {

                                preferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                                SharedPreferences.Editor editor = preferences.edit();
                                editor.putString("idFormation", String.valueOf(listeIdFormation.get(i)));
                                editor.putString("idCentreFormation", String.valueOf(listeIdCentreFormation.get(i)));
                                editor.apply();

                                intent = new Intent(getApplicationContext(), FicheFormation.class);
                                startActivity(intent);
                            }
                        }
                    }
                });
            }

        } catch (JSONException e) {
            e.printStackTrace();
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
    protected void onNewIntent(Intent intent) {
        setIntent(intent);
        handleIntent(intent);
    }

    private void handleIntent(Intent intent) {
        if (intent.ACTION_SEARCH.equals(intent.getAction())) {
            String query = intent.getStringExtra(SearchManager.QUERY);
            preferences = PreferenceManager.getDefaultSharedPreferences(this);
            SharedPreferences.Editor editor = preferences.edit();
            editor.putString("motCle", query);
            editor.apply();
            Intent newIntent = new Intent(this, ResultatRechercheFormation.class);
            startActivity(newIntent);
        }
    }
}
