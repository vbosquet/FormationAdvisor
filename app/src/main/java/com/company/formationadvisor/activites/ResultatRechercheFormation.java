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
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.company.formationadvisor.R;
import com.company.formationadvisor.db.UtilisateurDAO;
import com.company.formationadvisor.modeles.IPAddress;
import com.company.formationadvisor.modeles.Utilisateur;
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

        Bundle extra = this.getIntent().getExtras();
        if(extra != null) {
            motCle = extra.getString("motCle");
        }

        preferences = PreferenceManager.getDefaultSharedPreferences(this);
        token = preferences.getString("token", "");

        ipAddress = new IPAddress();

        RechercheParMotCle tache = new RechercheParMotCle(this, ipAddress);
        tache.execute(motCle, token);
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
                        Object o = listView.getItemAtPosition(position);
                        String action = o.toString();

                        for (int i = 0; i<listeLibelleFormation.size(); i++){

                            if (action.equals(listeLibelleFormation.get(i))) {
                                Intent intent = new Intent(getApplicationContext(), FicheFormation.class);
                                intent.putExtra("idFormation", String.valueOf(listeIdFormation.get(i)));
                                intent.putExtra("idCentreFormation", String.valueOf(listeIdCentreFormation.get(i)));
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
