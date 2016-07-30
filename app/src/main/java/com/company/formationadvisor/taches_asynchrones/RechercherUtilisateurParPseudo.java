package com.company.formationadvisor.taches_asynchrones;

import android.os.AsyncTask;
import android.util.Log;

import com.company.formationadvisor.modeles.IPAddress;

import org.json.JSONException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;

public class RechercherUtilisateurParPseudo extends AsyncTask<String, String, String>{

    private IRechercheUtilisateurParPseudo callback;
    private String ip;

    public RechercherUtilisateurParPseudo (IRechercheUtilisateurParPseudo callback, IPAddress ipAddress) {
        this.callback = callback;
        this.ip = ipAddress.getIpAddress();
    }

    @Override
    protected String doInBackground(String... params) {
        String pseudo = params[0];

        URL url;

        try {
            url = new URL("http://"+ip+"/webService_Android/rechercher_id_utilisateur_par_username.php?pseudo="+pseudo);

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
        try {
            callback.afficherResultatRecherche(string);
        } catch (JSONException | NoSuchAlgorithmException | NoSuchProviderException e) {
            e.printStackTrace();
        }
    }

    public interface IRechercheUtilisateurParPseudo {
        void afficherResultatRecherche(String string) throws JSONException, NoSuchProviderException, NoSuchAlgorithmException;
    }
}
