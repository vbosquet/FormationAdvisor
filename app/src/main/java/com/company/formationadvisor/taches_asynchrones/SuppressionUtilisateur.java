package com.company.formationadvisor.taches_asynchrones;


import android.os.AsyncTask;

import com.company.formationadvisor.modeles.IPAddress;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class SuppressionUtilisateur extends AsyncTask<String, String, String>{

    private ISupprimerUtilisateur callback;
    private String ip;

    public SuppressionUtilisateur(ISupprimerUtilisateur callback, IPAddress ipAddress) {
        this.callback = callback;
        this.ip = ipAddress.getIpAddress();
    }

    @Override
    protected String doInBackground(String... params) {

        String idUtilisateur = params[0];
        String token = params[1];

        URL url;
        try {

            url = new URL("http://"+ip+"/webService_Android/supprimer_utilisateur.php?id_utilisateur="+idUtilisateur+
                    "&token="+token);
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
        callback.afficherConfirmationSuppressionUtilisateur(string);
    }

    public interface ISupprimerUtilisateur{
        void afficherConfirmationSuppressionUtilisateur(String string);
    }
}
