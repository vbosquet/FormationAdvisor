package com.company.formationadvisor.activites;

import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.company.formationadvisor.R;
import com.company.formationadvisor.modeles.IPAddress;
import com.company.formationadvisor.taches_asynchrones.RechercherFormationAValider;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class ApprouverFormation extends AppCompatActivity implements RechercherFormationAValider.IRechercheFormationAValider{

    SharedPreferences preferences;
    IPAddress ipAddress;
    String token;
    ArrayList listeLibelleFormation, listeIdFormation, listeIdCentreFormation, listeIdUtilisateur;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_approuver_formation);

        preferences = PreferenceManager.getDefaultSharedPreferences(this);
        token = preferences.getString("token", "");

        ipAddress = new IPAddress();

        RechercherFormationAValider tache = new RechercherFormationAValider(this, ipAddress);
        tache.execute(token);

    }

    @Override
    public void afficherFormationAValider(String string) {
        try {
            JSONObject jsonObject = new JSONObject(string);
            JSONArray jsonArray = jsonObject.getJSONArray("formation_a_valider");

            listeLibelleFormation = new ArrayList();
            listeIdFormation = new ArrayList();
            listeIdCentreFormation = new ArrayList();
            listeIdUtilisateur = new ArrayList();

            for(int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonData = jsonArray.getJSONObject(i);
                listeLibelleFormation.add(jsonData.getString("libelle"));
                listeIdFormation.add(jsonData.getString("id_formation"));
                listeIdCentreFormation.add(jsonData.getString("id_centre_formation"));
                listeIdUtilisateur.add(jsonData.getString("id_utilisateur"));
            }

            ArrayAdapter arrayAdapter = new ArrayAdapter(this, R.layout.activity_listview, listeLibelleFormation);
            final ListView listView = (ListView) findViewById(R.id.formation_a_valider);
            listView.setAdapter(arrayAdapter);

            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    Object o = listView.getItemAtPosition(position);
                    String action = o.toString();

                    for (int i = 0; i<listeLibelleFormation.size(); i++){

                        if (action.equals(listeLibelleFormation.get(i))) {
                            Intent intent = new Intent(getApplicationContext(), FicheFormationAValider.class);
                            intent.putExtra("idFormation", String.valueOf(listeIdFormation.get(i)));
                            intent.putExtra("idCentreFormation", String.valueOf(listeIdCentreFormation.get(i)));
                            intent.putExtra("idUtilisateur", String.valueOf(listeIdUtilisateur.get(i)));
                            startActivity(intent);
                        }
                    }
                }
            });

        } catch (JSONException e) {
            e.printStackTrace();
        }


    }
}
