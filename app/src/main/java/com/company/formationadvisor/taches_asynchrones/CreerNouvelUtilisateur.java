package com.company.formationadvisor.taches_asynchrones;

import android.os.AsyncTask;
import android.util.Log;

import com.company.formationadvisor.modeles.IPAddress;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;

public class CreerNouvelUtilisateur extends AsyncTask<String, String, String>{

    private ICreationUtilisateur callback;
    private String ip;

    public CreerNouvelUtilisateur(ICreationUtilisateur callback, IPAddress ipAddress) {
        this.callback = callback;
        this.ip = ipAddress.getIpAddress();
    }

    @Override
    protected String doInBackground(String... params) {
        String nom = params[0];
        String prenom = params[1];
        String pseudo = params[2];
        String motDePasse = params[3];
        String email = params[4];
        String token = params[5];

        URL url;

        try {
            url = new URL("http://"+ip+"/webService_Android/ajouter_utilisateur.php?nom_utilisateur="+nom+
                    "&prenom_utilisateur="+prenom+"&pseudo="+pseudo+
                    "&mot_de_passe="+motDePasse+"&email="+email+"&token="+token+"&admin=false");
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.connect();

            InputStream inputStream = connection.getInputStream();
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
            StringBuilder stringBuilder = new StringBuilder();
            String line;

            while((line = bufferedReader.readLine()) != null) {
                stringBuilder.append(line);
            }

            bufferedReader.close();
            inputStream.close();

            return stringBuilder.toString();

        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    @Override
    protected void onPostExecute(String string) {
        super.onPostExecute(string);
        callback.confirmationInscription(string);
    }

    public interface ICreationUtilisateur {
        void confirmationInscription(String string);
    }
}
