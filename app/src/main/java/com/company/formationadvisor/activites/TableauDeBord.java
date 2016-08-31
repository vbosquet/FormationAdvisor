package com.company.formationadvisor.activites;


import android.app.FragmentTransaction;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.preference.PreferenceManager;
import android.support.multidex.MultiDex;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SearchView;

import com.company.formationadvisor.R;
import com.company.formationadvisor.modeles.Drawer;
import com.company.formationadvisor.modeles.DrawerAdaptater;
import com.company.formationadvisor.modeles.IPAddress;
import com.company.formationadvisor.taches_asynchrones.RechercherMessageParPseudo;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class TableauDeBord extends AppCompatActivity implements OnMapReadyCallback {

    String[] liste_action_utilisateur = {"Mes messages","Modifier mon profil", "Rechercher ma position actuelle", "Référencer une formation",
            "Modifier mes formations", "Paramètres", "Déconnexion"};
    String[] liste_action_admin = {"Approuver les formations", "Déconnexion"};
    Intent intent;
    SharedPreferences preferences;
    String admin, mActivityTitle;
    ListView mDrawerList;
    DrawerLayout mDrawerLayout;
    ActionBarDrawerToggle mDrawerToggle;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tableau_de_bord);

        MapFragment mMapFragment = MapFragment.newInstance();
        FragmentTransaction fragmentTransaction =
                getFragmentManager().beginTransaction();
        fragmentTransaction.add(R.id.my_container, mMapFragment);
        fragmentTransaction.commit();

        /*MapFragment mapFragment = (MapFragment) getFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);*/

        preferences = PreferenceManager.getDefaultSharedPreferences(this);
        admin = preferences.getString("admin", "");

        handleIntent(getIntent());

        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerList = (ListView) findViewById(R.id.tableau_de_bord);
        mActivityTitle = getTitle().toString();

        addDrawerItems();
        setUpDrawer();

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
    }

    private void addDrawerItems() {

        if (admin.equals("false")) {

            ArrayAdapter<String> adapter = new ArrayAdapter<>(this, R.layout.activity_listview, liste_action_utilisateur);
            mDrawerList.setAdapter(adapter);

            mDrawerList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    Object o = mDrawerList.getItemAtPosition(position);
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
            mDrawerList = (ListView) findViewById(R.id.tableau_de_bord);
            mDrawerList.setAdapter(adapter);

            mDrawerList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    Object o = mDrawerList.getItemAtPosition(position);
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

    public void setUpDrawer() {
        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout, R.string.drawer_open, R.string.drawer_close) {

            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                invalidateOptionsMenu();
            }

            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
                getSupportActionBar().setTitle(mActivityTitle);
                invalidateOptionsMenu();
            }
        };

        mDrawerToggle.setDrawerIndicatorEnabled(true);
        mDrawerLayout.setDrawerListener(mDrawerToggle);
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
        if (mDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        handleIntent(intent);
    }

    private void handleIntent(Intent intent) {
        if(intent.ACTION_SEARCH.equals(intent.getAction())) {
            String query = intent.getStringExtra(SearchManager.QUERY);
            preferences = PreferenceManager.getDefaultSharedPreferences(this);
            SharedPreferences.Editor editor = preferences.edit();
            editor.putString("motCle", query);
            editor.apply();
            Intent intent1 = new Intent(this, ResultatRechercheFormation.class);
            //intent1.putExtra("motCle", query);
            startActivity(intent1);

        }
    }

    @Override
    public void onBackPressed() {

    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        mDrawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        mDrawerToggle.onConfigurationChanged(newConfig);
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        /*googleMap.addMarker(new MarkerOptions()
                .position(new LatLng(0, 0))
                .title("Marker"));*/

    }
}
