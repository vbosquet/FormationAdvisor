package com.company.formationadvisor.taches_asynchrones;

import android.os.AsyncTask;

import com.company.formationadvisor.modeles.IPAddress;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class ModificationProfilUtilisateur extends AsyncTask<String, String, String>{

    private IModifierProfilUtilisateur callback;
    private String ip;

    public ModificationProfilUtilisateur(IModifierProfilUtilisateur callback, IPAddress ipAddress) {
        this.callback = callback;
        this.ip = ipAddress.getIpAddress();
    }

    @Override
    protected String doInBackground(String... params) {
        String nom = params[0];
        String prenom = params[1];
        String email = params[2];
        String pseudo = params[3];
        String token = params[4];

        URL url;

        try {
            url = new URL("http://"+ip+"/webService_Android/modifier_profil_utilisateur.php?nom="+nom+
            "&prenom="+prenom+"&email="+email+"&pseudo="+pseudo+"&token="+token);
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
        callback.afficherConfirmationModificationProfil(string);
    }

    public interface IModifierProfilUtilisateur {
        void afficherConfirmationModificationProfil(String string);
    }
}
