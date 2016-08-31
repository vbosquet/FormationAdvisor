package com.company.formationadvisor.activites;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.provider.Settings;
import android.support.v4.app.NavUtils;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.app.NotificationCompat;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.company.formationadvisor.R;
import com.company.formationadvisor.db.UtilisateurDAO;
import com.company.formationadvisor.fragments.ConnexionFragment;
import com.company.formationadvisor.modeles.IPAddress;
import com.company.formationadvisor.modeles.Utilisateur;
import com.company.formationadvisor.taches_asynchrones.RechercherMessageParPseudo;
import com.company.formationadvisor.taches_asynchrones.RechercherUtilisateurParPseudo;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;

public class Connexion extends AppCompatActivity implements ConnexionFragment.IConnexion,
        RechercherUtilisateurParPseudo.IRechercheUtilisateurParPseudo,
        RechercherMessageParPseudo.IRechercheMessageParPseudo {

    EditText pseudo, mot_de_passe;
    Intent intent;
    SharedPreferences preferences;
    String salt, nouveauMotDePassCrypte, motDePasseEnregistre, message;
    IPAddress ipAddress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_connexion);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        pseudo = (EditText) findViewById(R.id.pseudo_utilisateur);
        mot_de_passe = (EditText) findViewById(R.id.mot_de_passe);

        ipAddress = new IPAddress();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                NavUtils.navigateUpFromSameTask(this);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void connexion(View view) throws NoSuchProviderException, NoSuchAlgorithmException {

        RechercherUtilisateurParPseudo tache1 = new RechercherUtilisateurParPseudo(this, ipAddress);
        tache1.execute(pseudo.getText().toString());
    }

    public String crypterMotDePasse(String passwordToHash, String salt) throws NoSuchAlgorithmException, NoSuchProviderException {

        String securePassword = getSecurePassword(passwordToHash, salt);

        return securePassword;
    }

    public String getSecurePassword(String passwordToHash, String salt) throws NoSuchAlgorithmException {

        MessageDigest md1 = MessageDigest.getInstance("SHA256");
        byte[] pwdHash = md1.digest(passwordToHash.getBytes());

        String pwd = byteToString(pwdHash);
        String saltPwd = salt+pwd;

        MessageDigest md2 = MessageDigest.getInstance("MD5");
        byte[] bytes = md2.digest(saltPwd.getBytes());

        return byteToString(bytes);
    }

    private String byteToString(byte[] digest){
        StringBuilder sb = new StringBuilder();

        for (byte aDigest : digest) {
            sb.append(Integer.toString((aDigest & 0xff) + 0x100, 16).substring(1));
        }

        return sb.toString();
    }

    @Override
    public void afficherResultatRecherche(String string) throws JSONException, NoSuchProviderException, NoSuchAlgorithmException {
        JSONObject jsonObject = new JSONObject(string);
        message = jsonObject.getString("success");

        if (message.equals("true")) {

            JSONObject infosUtilisateur = new JSONObject(jsonObject.getString("info_utilisateur"));
            salt = infosUtilisateur.getString("sel");
            motDePasseEnregistre =  infosUtilisateur.getString("mot_de_passe");
            nouveauMotDePassCrypte = crypterMotDePasse(mot_de_passe.getText().toString(), salt);

            if (motDePasseEnregistre.equals(nouveauMotDePassCrypte)) {

                preferences = PreferenceManager.getDefaultSharedPreferences(this);
                SharedPreferences.Editor editor = preferences.edit();
                editor.putString("pseudo", infosUtilisateur.getString("username"));
                editor.putInt("id", Integer.parseInt(infosUtilisateur.getString("id_utilisateur")));
                editor.putString("token", salt);
                editor.putString("admin", infosUtilisateur.getString("admin"));
                editor.apply();

                RechercherMessageParPseudo tache = new RechercherMessageParPseudo(this, ipAddress);
                tache.execute(infosUtilisateur.getString("username"), salt);

                intent = new Intent(this, TableauDeBord.class);
                startActivity(intent);


            } else  {
                Toast.makeText(this, "Votre mot de passe est incorrect.", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(this, "Votre pseudo n'existe pas ou est incorrect.", Toast.LENGTH_SHORT).show();
        }

        }

    @Override
    public void afficherResultatRechercheMessage(String string) {
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

                Intent resultIntent = new Intent(this, BoiteReceptionMessage.class);
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
