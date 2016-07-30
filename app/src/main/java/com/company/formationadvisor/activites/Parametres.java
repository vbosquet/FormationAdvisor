package com.company.formationadvisor.activites;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.company.formationadvisor.R;

public class Parametres extends AppCompatActivity {

    String[] liste_action_utilisateur = {"Modifier mon mot de passe", "Supprimer mon compte"};

    Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_parametres);

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, R.layout.activity_listview, liste_action_utilisateur);
        final ListView listView = (ListView) findViewById(R.id.parametres);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Object o = listView.getItemAtPosition(position);
                String action = o.toString();

                if (action == liste_action_utilisateur[0]) {
                    intent = new Intent(getApplicationContext(), ModifierMotDePasse.class);
                    startActivity(intent);
                }

                if (action == liste_action_utilisateur[1]) {
                    intent = new Intent(getApplicationContext(), SupprimerCompte.class);
                    startActivity(intent);
                }
            }
        });
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
