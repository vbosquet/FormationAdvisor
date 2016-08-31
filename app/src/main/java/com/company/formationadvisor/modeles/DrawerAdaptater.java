package com.company.formationadvisor.modeles;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.company.formationadvisor.R;

import java.util.ArrayList;

public class DrawerAdaptater extends ArrayAdapter<Drawer> {

    public DrawerAdaptater(Context context, ArrayList<Drawer> drawer) {
        super(context, 0, drawer);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        Drawer drawer = getItem(position);
        ViewHolder viewHolder;

        if(convertView == null) {
            viewHolder = new ViewHolder();
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.list_view_drawer, parent, false);
            viewHolder.item = (TextView) convertView.findViewById(R.id.libelle_action_utilisateur);
            viewHolder.image = (ImageView) convertView.findViewById(R.id.image_action_utilisateur);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        viewHolder.item.setText(drawer.getItem());

        return convertView;
    }

    public static class ViewHolder {
        TextView item;
        ImageView image;
    }
}
