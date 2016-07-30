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

public class CreerNouvelleEvaluation extends AsyncTask<Object, Object, String>{

    private ICreationNouvelleEvaluation callback;
    private String ip;

    public CreerNouvelleEvaluation (ICreationNouvelleEvaluation callback, IPAddress ipAddress) {
        this.callback = callback;
        this.ip = ipAddress.getIpAddress();
    }

    @Override
    protected String doInBackground(Object... params) {
        String titre = (String) params[0];
        String commentaire = (String) params[1];
        Integer idFormation = Integer.parseInt((String) params[2]);
        String pseudo = (String) params[3];
        String token = (String) params[4];

        try {

            String encodedString1 = URLEncoder.encode(titre, "UTF-8");
            String encodedString2 = URLEncoder.encode(commentaire, "UTF-8");

            URL url = new URL("http://"+ip+"/webService_Android/ajouter_evaluation.php?titre="+encodedString1+
                    "&commentaire="+encodedString2+
                    "&idFormation="+idFormation+
                    "&pseudo="+pseudo+
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

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    @Override
    protected void onPostExecute(String string) {
        super.onPostExecute(string);
        callback.confirmerEnregistrement(string);
    }

    public interface ICreationNouvelleEvaluation{
        void confirmerEnregistrement(String string);
    }
}
