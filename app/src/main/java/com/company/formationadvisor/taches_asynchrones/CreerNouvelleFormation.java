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
import java.net.URL;
import java.net.URLEncoder;

public class CreerNouvelleFormation extends AsyncTask<String, String, String> {

    private ICreationFormation callback;
    private String ip;

    public CreerNouvelleFormation(ICreationFormation callback, IPAddress ipAddress) {
        this.callback = callback;
        this.ip = ipAddress.getIpAddress();
    }

    @Override
    protected String doInBackground(String... params) {
        String nomFormation = params[0];
        String dateDebut = params[1];
        String dateFin = params[2];
        String description = params[3];
        String idUtilisateur = params[4];
        String idCentreFormation = params[5];
        String token = params[6];

        try {

            String encodedString1 = URLEncoder.encode(description, "UTF-8");
            String encodeString2 = URLEncoder.encode(nomFormation, "UTF-8");
            URL url = new URL("http://"+ip+"/webService_Android/ajouter_formation.php?nom_formation="+encodeString2+
                    "&date_debut="+dateDebut+"&date_fin="+dateFin+"&description="+encodedString1+
                    "&id_centre_formation="+ idCentreFormation+
                    "&id_utilisateur="+idUtilisateur+
                    "&token="+token+
                    "&validation=false");
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

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return  null;
    }

    @Override
    protected void onPostExecute(String string) {
        super.onPostExecute(string);
        callback.confirmationCreation(string);
    }

    public interface ICreationFormation {
        void confirmationCreation(String string);
    }
}
