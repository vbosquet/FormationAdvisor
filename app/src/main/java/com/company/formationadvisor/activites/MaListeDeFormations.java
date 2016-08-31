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
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.company.formationadvisor.R;
import com.company.formationadvisor.db.UtilisateurDAO;
import com.company.formationadvisor.modeles.IPAddress;
import com.company.formationadvisor.modeles.Utilisateur;
import com.company.formationadvisor.taches_asynchrones.RechercherParIdUtilisateur;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.StringTokenizer;

public class MaListeDeFormations extends AppCompatActivity implements RechercherParIdUtilisateur.IRechercheParIdUtilisateur {

    ArrayList listeLibelleFormation, listeIdFormation, listeIdCentreFormation;
    SharedPreferences preferences;
    Integer idUtilisateur;
    String token;
    Intent intent;
    IPAddress ipAddress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ma_liste_de_formations);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        preferences = PreferenceManager.getDefaultSharedPreferences(this);
        idUtilisateur = preferences.getInt("id", 0);
        token = preferences.getString("token", "");

        ipAddress = new IPAddress();

        RechercherParIdUtilisateur tache = new RechercherParIdUtilisateur(this, ipAddress);
        tache.execute(idUtilisateur, token);
    }

    @Override
    public void afficherInfoFormation(final String string) {
        Log.i("CONTENT_STRING", string);
        try {
            JSONObject jsonObject = new JSONObject(string);
            JSONArray jsonArray = jsonObject.getJSONArray("liste_formation");

            if(jsonArray.length() == 0) {
                Toast.makeText(this, "Vous n'avez référencé aucune formation.", Toast.LENGTH_SHORT).show();
            }

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
            final ListView listView = (ListView) findViewById(R.id.liste_de_mes_formations);
            listView.setAdapter(adapter);

            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    Object o = listView.getItemAtPosition(position);
                    String action = o.toString();

                    for (int i = 0; i<listeLibelleFormation.size(); i++){

                        if (action.equals(listeLibelleFormation.get(i))) {
                            Intent intent = new Intent(getApplicationContext(), ModifierFormation.class);
                            intent.putExtra("idFormation", String.valueOf(listeIdFormation.get(i)));
                            intent.putExtra("idCentreFormation", String.valueOf(listeIdCentreFormation.get(i)));
                            startActivity(intent);
                        }
                    }
                }
            });

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
            /*case R.id.tableau_de_bord:
                intent = new Intent(this, TableauDeBord.class);
                startActivity(intent);
                return true;
            case R.id.deconnexion:
                intent = new Intent(this, MainActivity.class);
                startActivity(intent);
                return true;*/
            case  R.id.nouveau:
                intent = new Intent(this, NouveauCentreFormation.class);
                startActivity(intent);
                return true;
            case android.R.id.home:
                NavUtils.navigateUpFromSameTask(this);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
