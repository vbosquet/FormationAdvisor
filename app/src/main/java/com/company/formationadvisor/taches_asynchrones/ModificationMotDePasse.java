package com.company.formationadvisor.taches_asynchrones;

import android.os.AsyncTask;

import com.company.formationadvisor.modeles.IPAddress;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class ModificationMotDePasse extends AsyncTask<String, String, String>{

    private IModifierMotDePasseActuel callback;
    private String ip;

    public ModificationMotDePasse(IModifierMotDePasseActuel callback, IPAddress ipAddress) {
        this.callback = callback;
        this.ip = ipAddress.getIpAddress();
    }


    @Override
    protected String doInBackground(String... params) {
        String motDePasse = params[0];
        String token = params[1];
        String pseudo = params[2];

        URL url;

        try {
            url = new URL("http://"+ip+"/webService_Android/modifier_mot_de_passe.php?mot_de_passe="+motDePasse+"&token="+token+
            "&pseudo="+pseudo);
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
        callback.afficherConfirmationModificationMotDePasse(string);
    }

    public interface IModifierMotDePasseActuel{
        void afficherConfirmationModificationMotDePasse(String string);
    }
}
