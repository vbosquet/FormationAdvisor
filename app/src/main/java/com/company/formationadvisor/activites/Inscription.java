package com.company.formationadvisor.activites;

import android.content.DialogInterface;
import android.content.Intent;
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
import com.company.formationadvisor.db.UtilisateurDAO;
import com.company.formationadvisor.fragments.InscriptionFragment;
import com.company.formationadvisor.modeles.IPAddress;
import com.company.formationadvisor.modeles.Utilisateur;
import com.company.formationadvisor.taches_asynchrones.CreerNouveauToken;
import com.company.formationadvisor.taches_asynchrones.CreerNouvelUtilisateur;
import com.company.formationadvisor.taches_asynchrones.RechercherUtilisateurParPseudo;

import org.json.JSONException;
import org.json.JSONObject;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.SecureRandom;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Inscription extends AppCompatActivity implements InscriptionFragment.IInscription,
        CreerNouvelUtilisateur.ICreationUtilisateur,
        CreerNouveauToken.ICreationNouveauToken,
        RechercherUtilisateurParPseudo.IRechercheUtilisateurParPseudo {

    EditText nom, prenom, pseudo, mot_de_passe, email;
    String motDePasseCrypte, hexString, message;
    byte[] salt;
    Intent intent;
    IPAddress ipAddress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inscription);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        nom = (EditText) findViewById(R.id.nom_utilisateur);
        prenom = (EditText) findViewById(R.id.prenom_utilisateur);
        pseudo = (EditText) findViewById(R.id.pseudo_utilisateur);
        mot_de_passe = (EditText) findViewById(R.id.mot_de_passe);
        email = (EditText) findViewById(R.id.email_utilisateur);

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
    public void onRegisterInscription(View view) throws NoSuchAlgorithmException, NoSuchProviderException {

        motDePasseCrypte = crypterMotDePasse(mot_de_passe.getText().toString());

        RechercherUtilisateurParPseudo tache1 = new RechercherUtilisateurParPseudo(this, ipAddress);
        tache1.execute(pseudo.getText().toString());
    }

    public boolean isValidPassword(String password) {
        Pattern pattern;
        Matcher matcher;

        final String PASSWORD_PATTERN = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=\\S+$).{4,}$";

        pattern = Pattern.compile(PASSWORD_PATTERN);
        matcher = pattern.matcher(password);

        return matcher.matches();
    }

    public boolean isValidEmail(String email) {
        Pattern pattern;
        Matcher matcher;

        final String EMAIL_PATTERN = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";

        pattern = Pattern.compile(EMAIL_PATTERN);
        matcher = pattern.matcher(email);

        return  matcher.matches();
    }

    @Override
    public void confirmationInscription(String string) {
        try {
            JSONObject jsonObject = new JSONObject(string);
            String message = jsonObject.getString("success");

            if(message.equals("true")) {
                Toast.makeText(this, "Enregistrement réussi", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Echec de l'enregistrement", Toast.LENGTH_SHORT).show();
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public String crypterMotDePasse(String passwordToHash) throws NoSuchAlgorithmException, NoSuchProviderException {
        salt = getSalt();

        hexString = byteToString(salt);

        Log.i("CONTENT_SEL", hexString);

        String securePassword = getSecurePassword(passwordToHash, salt);
        return securePassword;
    }

    public String getSecurePassword(String passwordToHash, byte[] salt) throws NoSuchAlgorithmException {

        MessageDigest md1 = MessageDigest.getInstance("SHA256");
        byte[] pwdHash = md1.digest(passwordToHash.getBytes());

        String pwd = byteToString(pwdHash);
        String saltStr = byteToString(salt);
        String saltPwd = saltStr+pwd;

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

    public byte[] getSalt() throws NoSuchProviderException, NoSuchAlgorithmException {
            SecureRandom sr = SecureRandom.getInstance("SHA1PRNG");
            byte[] salt = new byte[16];
            sr.nextBytes(salt);
            return salt;
    }

    @Override
    public void confirmationCreationNouveauToken(String string) {
        try {
            JSONObject jsonObject = new JSONObject(string);
            String message = jsonObject.getString("success");
            Log.i("CONTENT_MESSAGE", message);

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void afficherResultatRecherche(String string) {

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

            try {
                JSONObject jsonObject = new JSONObject(string);
                message = jsonObject.getString("success");

                if (message.equals("false")) {

                    if (!isValidEmail(email.getText().toString())) {
                        Toast.makeText(this, "Adresse email invalide", Toast.LENGTH_SHORT).show();
                    } else if (!isValidPassword(mot_de_passe.getText().toString())) {
                        Toast.makeText(this, "Mot de passe invalide", Toast.LENGTH_SHORT).show();
                    } else {
                        CreerNouveauToken tache2 = new CreerNouveauToken(this, ipAddress);
                        tache2.execute(hexString);

                        CreerNouvelUtilisateur tache3 = new CreerNouvelUtilisateur(this, ipAddress);
                        tache3.execute(nom.getText().toString(), prenom.getText().toString(),
                                pseudo.getText().toString(), motDePasseCrypte,
                                email.getText().toString(),
                                hexString);

                        intent = new Intent(this, TableauDeBord.class);
                        startActivity(intent);
                    }

                } else {
                    Toast.makeText(this, "Ce pseudo existe déjà", Toast.LENGTH_SHORT).show();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
}
