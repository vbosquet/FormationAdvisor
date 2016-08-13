package com.company.formationadvisor.taches_asynchrones;

import android.os.AsyncTask;

import com.company.formationadvisor.modeles.IPAddress;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.Date;


public class EnvoyerMessage extends AsyncTask<String, String, String> {

    private IEnvoiNouveauMessage callback;
    private String ip;

    public EnvoyerMessage(IEnvoiNouveauMessage callback, IPAddress ipAddress) {
        this.callback = callback;
        this.ip = ipAddress.getIpAddress();
    }

    @Override
    protected String doInBackground(String... params) {
        String titreMessage = params[0];
        String texteMessage = params[1];
        String idExpediteur = params[2];
        String idDestinataire = params[3];
        String token = params[4];
        String dateEnvoi = new SimpleDateFormat("yyyy-MM-dd").format(new Date());

        URL url;

        try {
            String encodedString1 = URLEncoder.encode(titreMessage, "UTF-8");
            String encodeString2 = URLEncoder.encode(texteMessage, "UTF-8");

            url = new URL("http://"+ip+"/webService_Android/ajouter_message.php?titre="+encodedString1+
                "&texte="+encodeString2+"&expediteur="+idExpediteur+"&destinataire="+idDestinataire+
                    "&date_envoi="+dateEnvoi+"&token="+token);
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
        callback.afficherConfirmationEnvoiNouveauMessage(string);
    }

    public interface IEnvoiNouveauMessage {
        void afficherConfirmationEnvoiNouveauMessage(String string);
    }
}
