package com.example.pseudogram.repository;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.pseudogram.model.Picture;

import java.util.ArrayList;
import java.util.List;

public class FotoDao extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME               = "FotoDB";
    private static final String TABELA_NAME                 = "foto";
    private static final String TABELA_CAMPO_ID             = "id";
    private static final String TABELA_CAMPO_TITULO         = "titulo";
    private static final String TABELA_CAMPO_DESCRICAO      = "descricao";
    private static final String TABELA_CAMPO_PATH           = "path";
    private static final String[] COLUNAS = {TABELA_CAMPO_ID, TABELA_CAMPO_TITULO, TABELA_CAMPO_DESCRICAO, TABELA_CAMPO_PATH};

    public FotoDao(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_TABLE = "CREATE TABLE foto ("+
                "id INTEGER PRIMARY KEY AUTOINCREMENT,"+
                "titulo TEXT,"+
                "descricao TEXT,"+
                "path TEXT)";
        db.execSQL(CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS foto");
        this.onCreate(db);
    }

//    ---------------------------------------------------------------
//    crud

    public Picture insert(Picture picture) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(TABELA_CAMPO_TITULO, picture.getTitulo());
        values.put(TABELA_CAMPO_DESCRICAO, picture.getDescricao());
        values.put(TABELA_CAMPO_PATH, picture.getPath());
        db.insert(TABELA_NAME, null, values);

        db.close();
        return picture;
    }

    public Picture get(Integer id) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABELA_NAME, // a. tabela
                COLUNAS, // b. colunas
                " id = ?", // c. colunas para comparar
                new String[] { String.valueOf(id) }, // d. parâmetros
                null, // e. group by
                null, // f. having
                null, // g. order by
                null); // h. limit
        if (cursor == null) {
            return null;
        } else {
            cursor.moveToFirst();
            Picture picture = mapTo(cursor);
            db.close();

            return picture;
        }
    }

    public List<Picture> getAll() {
        List<Picture> data = new ArrayList<>();
        String query = "SELECT * FROM " + TABELA_NAME + " ORDER BY " + TABELA_CAMPO_TITULO;
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.rawQuery(query, null);
        if (cursor.moveToFirst()) {
            do {
                data.add(mapTo(cursor));
            } while (cursor.moveToNext());
        }

        db.close();
        return data;
    }

    public void update(Picture picture) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(TABELA_CAMPO_TITULO, picture.getTitulo());
        values.put(TABELA_CAMPO_DESCRICAO, picture.getDescricao());
        values.put(TABELA_CAMPO_PATH, picture.getPath());
        int i = db.update(TABELA_NAME, //tabela
                values, // valores
                TABELA_CAMPO_ID +" = ?", // colunas para comparar
                new String[] { picture.getId().toString() }); //parâmetros
        db.close();
    }

    public void delete(Picture picture) {
        SQLiteDatabase db = this.getWritableDatabase();
        int i = db.delete(TABELA_NAME, //tabela
                TABELA_CAMPO_ID +" = ?", // colunas para comparar
                new String[] { picture.getId().toString() });
        db.close();
    }

//    -----------------------------------------------------------
//    populate

    private Picture mapTo(Cursor cursor) {
        Picture picture = new Picture();
        picture.setId(cursor.getInt(0));
        picture.setTitulo(cursor.getString(1));
        picture.setDescricao(cursor.getString(2));
        picture.setPath(cursor.getString(3));
        return picture;
    }

}
