package com.example.pseudogram.repository;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.pseudogram.model.Foto;

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

    public Foto insert(Foto foto) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(TABELA_CAMPO_TITULO, foto.getTitulo());
        values.put(TABELA_CAMPO_DESCRICAO, foto.getDescricao());
        values.put(TABELA_CAMPO_PATH, foto.getPath());
        db.insert(TABELA_NAME, null, values);

        db.close();
        return foto;
    }

    public Foto get(Integer id) {
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
            Foto foto = mapTo(cursor);
            db.close();

            return foto;
        }
    }

    public List<Foto> getAll() {
        List<Foto> data = new ArrayList<>();
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

    public void update(Foto foto) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(TABELA_CAMPO_TITULO, foto.getTitulo());
        values.put(TABELA_CAMPO_DESCRICAO, foto.getDescricao());
        values.put(TABELA_CAMPO_PATH, foto.getPath());
        int i = db.update(TABELA_NAME, //tabela
                values, // valores
                TABELA_CAMPO_ID +" = ?", // colunas para comparar
                new String[] { foto.getId().toString() }); //parâmetros
        db.close();
    }

    public void delete(Foto foto) {
        SQLiteDatabase db = this.getWritableDatabase();
        int i = db.delete(TABELA_NAME, //tabela
                TABELA_CAMPO_ID +" = ?", // colunas para comparar
                new String[] { foto.getId().toString() });
        db.close();
    }

//    -----------------------------------------------------------
//    populate

    private Foto mapTo(Cursor cursor) {
        Foto foto = new Foto();
        foto.setId(cursor.getInt(0));
        foto.setTitulo(cursor.getString(1));
        foto.setDescricao(cursor.getString(2));
        foto.setPath(cursor.getString(3));
        return foto;
    }

}
