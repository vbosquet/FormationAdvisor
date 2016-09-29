package com.company.formationadvisor.taches_asynchrones;

import android.os.AsyncTask;

import com.company.formationadvisor.modeles.IPAddress;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;

public class CreerNouveauToken extends AsyncTask<String, String, String>{

    private ICreationNouveauToken callback;
    private String ip;

    public CreerNouveauToken (ICreationNouveauToken callback, IPAddress ipAddress) {
        this.callback = callback;
        this.ip = ipAddress.getIpAddress();
    }

    @Override
    protected String doInBackground(String... params) {
        String token = params[0];

        try {
            URL url = new URL("http://"+ip+"/webService_Android/ajouter_token.php?contenu_token="+token);

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

    protected void onPostExecute(String string) {
        super.onPostExecute(string);
        callback.confirmationCreationNouveauToken(string);

    }

    public interface ICreationNouveauToken{
        void confirmationCreationNouveauToken(String string);
    }
}
