package com.company.formationadvisor.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.company.formationadvisor.modeles.Utilisateur;

public class UtilisateurDAO {

    public static final String NOM_TABLE = "utilisateur";

    public static final String COLONNE_ID = "id";
    public static final String COLONNE_NOM = "nom";
    public static final String COLONNE_PRENOM = "prenom";
    public static final String COLONNE_PSEUDO = "pseudo";
    public static final String COLONNE_MOT_DE_PASSE = "mot_de_passe";
    public static final String COLONNE_EMAIL = "email";
    public static final String COLONNE_SEL = "sel";

    public static final String CREATE_TABLE = "CREATE TABLE " + NOM_TABLE + " ("
            + COLONNE_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
            + COLONNE_NOM + " TEXT NOT NULL,"
            + COLONNE_PRENOM + " TEXT NOT NULL,"
            + COLONNE_PSEUDO + " TEXT NOT NULL,"
            + COLONNE_MOT_DE_PASSE + " TEXT NOT NULL,"
            + COLONNE_EMAIL + " TEXT NOT NULL, "
            + COLONNE_SEL + " TEXT NOT NULL);";

    public static final String UPGRADE_TABLE = "DROP TABLE " + NOM_TABLE;

    private DBHelper dbHelper;
    private Context context;
    private SQLiteDatabase db;

    public UtilisateurDAO (Context context) {
        this.context = context;
    }

    public UtilisateurDAO openWritable() {
        dbHelper = new DBHelper(context);
        db = dbHelper.getWritableDatabase();
        return this;
    }

    public UtilisateurDAO openReadable() {
        dbHelper = new DBHelper(context);
        db = dbHelper.getReadableDatabase();
        return this;
    }

    public void close() {
        dbHelper.close();
        db.close();
    }

    public long insert (Utilisateur utilisateur) {
        ContentValues cv = new ContentValues();
        cv.put(COLONNE_NOM, utilisateur.getNom());
        cv.put(COLONNE_PRENOM, utilisateur.getPrenom());
        cv.put(COLONNE_PSEUDO, utilisateur.getPseudo());
        cv.put(COLONNE_MOT_DE_PASSE, utilisateur.getMot_de_passe());
        cv.put(COLONNE_EMAIL, utilisateur.getEmail());
        cv.put(COLONNE_SEL, utilisateur.getSel());
        return db.insert(NOM_TABLE, null, cv);
    }

    public Cursor getCursorByPseudo(String pseudo) {
        Cursor c = db.query(NOM_TABLE, null, COLONNE_PSEUDO + "=\"" + pseudo +"\"", null, null, null, null);

        if (c.getCount() > 0) {
            c.moveToFirst();
            return c;
        } else {
            return null;
        }
    }

    public Cursor getCursorById(int id) {
        Cursor c = db.query(NOM_TABLE, null, COLONNE_ID + "=" + id, null, null, null, null);

        if (c.getCount() > 0) {
            c.moveToFirst();
            return c;
        } else {
            return null;
        }
    }

    public static Utilisateur cursorToUtilisateur(Cursor cursor) {
        Utilisateur utilisateur = new Utilisateur();
        utilisateur.setId(cursor.getInt(cursor.getColumnIndex(COLONNE_ID)));
        utilisateur.setNom(cursor.getString(cursor.getColumnIndex(COLONNE_NOM)));
        utilisateur.setPrenom(cursor.getString(cursor.getColumnIndex(COLONNE_PRENOM)));
        utilisateur.setPseudo(cursor.getString(cursor.getColumnIndex(COLONNE_PSEUDO)));
        utilisateur.setMot_de_passe(cursor.getString(cursor.getColumnIndex(COLONNE_MOT_DE_PASSE)));
        utilisateur.setEmail(cursor.getString(cursor.getColumnIndex(COLONNE_EMAIL)));
        utilisateur.setSel(cursor.getString(cursor.getColumnIndex(COLONNE_SEL)));
        return utilisateur;
    }

    public Utilisateur getUtilisateurByPseudo (String pseudo) {
        Cursor cursor = getCursorByPseudo(pseudo);

        if (cursor != null) {
            return cursorToUtilisateur(cursor);
        } else {
            return null;
        }
    }

    public Utilisateur getUtilisateurById (int id) {
        Cursor cursor = getCursorById(id);

        if (cursor != null) {
            return cursorToUtilisateur(cursor);
        } else {
            return null;
        }
    }

    public int update(Utilisateur utilisateur) {
        ContentValues cv = new ContentValues();
        cv.put(COLONNE_NOM, utilisateur.getNom());
        cv.put(COLONNE_PRENOM, utilisateur.getPrenom());
        cv.put(COLONNE_PSEUDO, utilisateur.getPseudo());
        cv.put(COLONNE_MOT_DE_PASSE, utilisateur.getMot_de_passe());
        cv.put(COLONNE_EMAIL, utilisateur.getEmail());
        cv.put(COLONNE_SEL, utilisateur.getSel());
        return db.update(NOM_TABLE, cv, COLONNE_ID + "=" + utilisateur.getId(), null);
    }

    public int delete (Utilisateur utilisateur) {
        return db.delete(NOM_TABLE, COLONNE_ID + "=" + utilisateur.getId(), null);
    }
}
