package com.company.formationadvisor.modeles;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.company.formationadvisor.R;

import java.util.ArrayList;

public class EvaluationAdaptater extends ArrayAdapter<Evaluation> {

    public EvaluationAdaptater(Context context, ArrayList<Evaluation> evaluation) {
        super(context, 0, evaluation);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        Evaluation evaluation = getItem(position);
        ViewHolder viewHolder;

        if(convertView == null) {
            viewHolder = new ViewHolder();
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.list_view_evaluation, parent, false);
            viewHolder.titre = (TextView) convertView.findViewById(R.id.titre_evaluation);
            viewHolder.commentaire = (TextView) convertView.findViewById(R.id.commentaire_evaluation);
            viewHolder.auteur = (TextView) convertView.findViewById(R.id.auteur_evaluation);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        viewHolder.titre.setText(evaluation.getTitre());
        viewHolder.commentaire.setText(evaluation.getCommentaire());
        viewHolder.auteur.setText(evaluation.getAuteur());

        return convertView;
    }

    public static class ViewHolder {
        TextView titre;
        TextView commentaire;
        TextView auteur;
    }
}
