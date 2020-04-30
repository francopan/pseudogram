package com.example.pseudogram.repository;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.pseudogram.model.Picture;

import java.util.ArrayList;
import java.util.List;

public class PictureDao extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION               = 1;
    private static final String DATABASE_NAME               = "PseudogramDB";
    private static final String TABLE_NAME                  = "picture";
    private static final String TABLE_ID_FIELD              = "id";
    private static final String TABLE_TITLE_FIELD           = "title";
    private static final String TABLE_DESCRIPTION_FIELD     = "description";
    private static final String TABLE_PATH_FIELD            = "path";
    private static final String[] COLUMNS = {TABLE_ID_FIELD, TABLE_TITLE_FIELD, TABLE_DESCRIPTION_FIELD, TABLE_PATH_FIELD};

    public PictureDao(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_TABLE = "CREATE TABLE picture ("+
                "id INTEGER PRIMARY KEY AUTOINCREMENT,"+
                "titulo TEXT,"+
                "descricao TEXT,"+
                "path TEXT)";
        db.execSQL(CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS picture");
        this.onCreate(db);
    }

//    ---------------------------------------------------------------
//    crud

    public Picture insert(Picture picture) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(TABLE_TITLE_FIELD, picture.getTitle());
        values.put(TABLE_DESCRIPTION_FIELD, picture.getDescription());
        values.put(TABLE_PATH_FIELD, picture.getPath());
        db.insert(TABLE_NAME, null, values);

        db.close();
        return picture;
    }

    public Picture get(Integer id) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_NAME, // a. tabela
                COLUMNS, // b. colunas
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
        String query = "SELECT * FROM " + TABLE_NAME + " ORDER BY " + TABLE_TITLE_FIELD;
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
        values.put(TABLE_TITLE_FIELD, picture.getTitle());
        values.put(TABLE_DESCRIPTION_FIELD, picture.getDescription());
        values.put(TABLE_PATH_FIELD, picture.getPath());
        int i = db.update(TABLE_NAME,
                values,
                TABLE_ID_FIELD +" = ?",
                new String[] { picture.getId().toString() }); // parameters
        db.close();
    }

    public void delete(Picture picture) {
        SQLiteDatabase db = this.getWritableDatabase();
        int i = db.delete(TABLE_NAME,
                TABLE_ID_FIELD +" = ?",
                new String[] { picture.getId().toString() });
        db.close();
    }

//    -----------------------------------------------------------
//    populate

    private Picture mapTo(Cursor cursor) {
        Picture picture = new Picture();
        picture.setId(cursor.getInt(0));
        picture.setTitle(cursor.getString(1));
        picture.setDescription(cursor.getString(2));
        picture.setPath(cursor.getString(3));
        return picture;
    }

}
