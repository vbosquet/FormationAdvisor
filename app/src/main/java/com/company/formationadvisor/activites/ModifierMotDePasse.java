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
import android.widget.EditText;
import android.widget.Toast;

import com.company.formationadvisor.R;
import com.company.formationadvisor.db.UtilisateurDAO;
import com.company.formationadvisor.modeles.IPAddress;
import com.company.formationadvisor.modeles.Utilisateur;
import com.company.formationadvisor.taches_asynchrones.ModificationMotDePasse;
import com.company.formationadvisor.taches_asynchrones.RechercherUtilisateurParPseudo;

import org.json.JSONException;
import org.json.JSONObject;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static java.lang.String.valueOf;

public class ModifierMotDePasse extends AppCompatActivity implements RechercherUtilisateurParPseudo.IRechercheUtilisateurParPseudo,
        ModificationMotDePasse.IModifierMotDePasseActuel {

    SharedPreferences preferences;
    String pseudo, salt, motDePasseEnregistre, motDePasseActuelCrypte, nouveauMotDePasseCrypte, message,token;

    EditText motDePasseActuel, nouveauMotDePasse, confirmationMotDePasse;

    Intent intent;

    IPAddress ipAddress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modifier_mot_de_passe);

        motDePasseActuel = (EditText) findViewById(R.id.mot_de_passe_actuel);
        nouveauMotDePasse = (EditText) findViewById(R.id.nouveau_mot_de_passe);
        confirmationMotDePasse = (EditText) findViewById(R.id.confirmation_mot_de_passe);

        preferences = PreferenceManager.getDefaultSharedPreferences(this);
        pseudo = preferences.getString("pseudo", "");
        token = preferences.getString("token", "");

        ipAddress = new IPAddress();

    }

    public void modifierMotDePasse (View view) throws NoSuchProviderException, NoSuchAlgorithmException {

        RechercherUtilisateurParPseudo tache = new RechercherUtilisateurParPseudo(this, ipAddress);
        tache.execute(pseudo);

    }

    public boolean isValidPassword(String password) {
        Pattern pattern;
        Matcher matcher;

        final String PASSWORD_PATTERN = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=\\S+$).{4,}$";

        pattern = Pattern.compile(PASSWORD_PATTERN);
        matcher = pattern.matcher(password);

        return matcher.matches();
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

            motDePasseEnregistre = infosUtilisateur.getString("mot_de_passe");
            salt = infosUtilisateur.getString("sel");
            motDePasseActuelCrypte = crypterMotDePasse(motDePasseActuel.getText().toString(), salt);

            if (!motDePasseEnregistre.equals(motDePasseActuelCrypte)) {
                Toast.makeText(this, "Le mot de passe actuel est incorrect.", Toast.LENGTH_SHORT).show();
            } else {

                if (!nouveauMotDePasse.getText().toString().equals(confirmationMotDePasse.getText().toString())) {
                    Toast.makeText(this, "Vous n'avez pas entré deux fois le même mot de passe.", Toast.LENGTH_SHORT).show();
                } else if (!isValidPassword(nouveauMotDePasse.getText().toString()) || !isValidPassword(confirmationMotDePasse.getText().toString())) {
                    Toast.makeText(this, "Mot de passe invalide", Toast.LENGTH_SHORT).show();
                } else {

                    nouveauMotDePasseCrypte = crypterMotDePasse(nouveauMotDePasse.getText().toString(), salt);
                    Log.i("CONTENT_CRYPTE_M", nouveauMotDePasseCrypte);

                    ModificationMotDePasse tache = new ModificationMotDePasse(this, ipAddress);
                    tache.execute(nouveauMotDePasseCrypte, token, pseudo);
                }
            }
        }
    }

    @Override
    public void afficherConfirmationModificationMotDePasse(String string) {
        try {
            JSONObject jsonObject = new JSONObject(string);
            String confirmationModification = jsonObject.getString("success");

            if (confirmationModification.equals("true")) {
                Toast.makeText(this, "Modification réussie.", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Echec de la modification.", Toast.LENGTH_SHORT).show();
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

    }
}
