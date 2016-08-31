package com.company.formationadvisor.modeles;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.company.formationadvisor.R;

import java.util.ArrayList;

public class MessageAdapater extends ArrayAdapter<Message> {

    public MessageAdapater(Context context, ArrayList<Message> message) {
        super(context, 0, message);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Message message = getItem(position);
        ViewHolder viewHolder;

        if(convertView == null) {
            viewHolder = new ViewHolder();
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.list_view_message, parent, false);
            viewHolder.texte = (TextView) convertView.findViewById(R.id.texte_message);
            viewHolder.expediteur = (TextView) convertView.findViewById(R.id.expediteur_message);
            //viewHolder.dateEnvoi = (TextView) convertView.findViewById(R.id.date_envoi_messsage);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        viewHolder.texte.setText(message.getTexte());
        viewHolder.expediteur.setText("Le " + message
                .getDateEnvoi() + ", " + message.getExpediteur() + " vous a écrit : ");
        //viewHolder.dateEnvoi.setText(message.getDateEnvoi());

        return  convertView;
    }

    public static class ViewHolder {
        TextView texte;
        TextView expediteur;
        TextView dateEnvoi;
    }
}
