package com.company.formationadvisor.modeles;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.company.formationadvisor.R;

import java.util.ArrayList;

public class FormationAdaptater extends ArrayAdapter<Formation>{

    public FormationAdaptater(Context context, ArrayList<Formation> formation) {
        super(context, 0, formation);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        Formation formation = getItem(position);
        ViewHolder viewHolder;

        if(convertView == null) {
            viewHolder = new ViewHolder();
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.list_view_formation, parent, false);
            viewHolder.nom = (TextView) convertView.findViewById(R.id.nom_formation);
            viewHolder.dateDebut = (TextView) convertView.findViewById(R.id.date_debut_formation);
            viewHolder.dateFin = (TextView) convertView.findViewById(R.id.date_fin_formation);
            viewHolder.description = (TextView) convertView.findViewById(R.id.description_formation);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        viewHolder.nom.setText(formation.getNom());
        viewHolder.dateDebut.setText(formation.getDateDebut());
        viewHolder.dateFin.setText(formation.getDateFin());
        viewHolder.description.setText(formation.getDescription());

        return convertView;
    }

    public static class ViewHolder {
        TextView nom;
        TextView dateDebut;
        TextView dateFin;
        TextView description;
    }
}
