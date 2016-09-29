package com.company.formationadvisor.activites;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v4.app.NavUtils;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.company.formationadvisor.R;
import com.company.formationadvisor.fragments.ConnexionFragment;
import com.company.formationadvisor.modeles.IPAddress;
import com.company.formationadvisor.taches_asynchrones.RechercherUtilisateurParPseudo;

import org.json.JSONException;
import org.json.JSONObject;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;

public class Connexion extends AppCompatActivity implements ConnexionFragment.IConnexion,
        RechercherUtilisateurParPseudo.IRechercheUtilisateurParPseudo {

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
        if (string == null) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Problème de connexion");
            builder.setMessage("Apparemment, tu n'as pas de connexion à internet. Vérifie que ton wifi soit bien activé.");
            builder.setNegativeButton("Ok", new DialogInterface.OnClickListener(){

                @Override
                public void onClick(DialogInterface dialog, int which) {

                }
            });

            AlertDialog dialog = builder.create();
            dialog.show();

        } else {

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

                    if (infosUtilisateur.getString("admin").equals("true")) {
                        intent = new Intent(this, ApprouverFormation.class);
                        startActivity(intent);

                    } else {
                        intent = new Intent(this, TableauDeBord.class);
                        startActivity(intent);
                    }

                } else  {
                    Toast.makeText(this, "Votre mot de passe est incorrect.", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(this, "Votre pseudo n'existe pas ou est incorrect.", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
