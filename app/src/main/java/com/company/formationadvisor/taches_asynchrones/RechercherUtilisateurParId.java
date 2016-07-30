package com.company.formationadvisor.taches_asynchrones;

import android.os.AsyncTask;
import android.util.Log;

import com.company.formationadvisor.modeles.IPAddress;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class RechercherUtilisateurParId extends AsyncTask<String, String, String>{

    private IRecchercheUtilisateurParId callback;
    private String ip;

    public RechercherUtilisateurParId(IRecchercheUtilisateurParId callback, IPAddress ipAddress) {
        this.callback = callback;
        this.ip = ipAddress.getIpAddress();
    }

    @Override
    protected String doInBackground(String... params) {
        String idUtilisateur = params[0];
        String token = params[1];

        Log.i("ID_UTILISATEUR", idUtilisateur);

        try {
            URL url = new URL("http://"+ip+"/webService_Android/rechercher_utilisateur_par_id.php?id_utilisateur="+idUtilisateur+
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
        callback.afficherInfosUtilisateur(string);
    }

    public interface IRecchercheUtilisateurParId {
        void afficherInfosUtilisateur(String string);
    }
}
