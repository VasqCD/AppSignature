package com.example.appsignature;



import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

public class DatabaseHelper extends SQLiteOpenHelper {
    // Nombre de la base de datos y versión
    private static final String DATABASE_NAME = "SignaturesDB";
    private static final int DATABASE_VERSION = 1;

    // Nombre de la tabla y columnas
    private static final String TABLE_SIGNATURES = "signatures";
    private static final String COLUMN_ID = "id";
    private static final String COLUMN_DESCRIPTION = "description";
    private static final String COLUMN_SIGNATURE = "digital_signature";

    // Consulta para crear la tabla
    private static final String CREATE_TABLE_SIGNATURES =
            "CREATE TABLE " + TABLE_SIGNATURES + "("
                    + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                    + COLUMN_DESCRIPTION + " TEXT,"
                    + COLUMN_SIGNATURE + " BLOB)";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE_SIGNATURES);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_SIGNATURES);
        onCreate(db);
    }

    public long insertSignature(Signature signature) {
        try {
            SQLiteDatabase db = this.getWritableDatabase();
            ContentValues values = new ContentValues();
            values.put(COLUMN_DESCRIPTION, signature.getDescription());
            values.put(COLUMN_SIGNATURE, signature.getDigitalSignature());

            return db.insert(TABLE_SIGNATURES, null, values);
        } catch (Exception e) {
            Log.e("DatabaseHelper", "Error al insertar firma", e);
            return -1;
        }
    }

    public List<Signature> getAllSignatures() {
        List<Signature> signatures = new ArrayList<>();

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = null;

        try {
            cursor = db.query(TABLE_SIGNATURES,
                    new String[]{COLUMN_ID, COLUMN_DESCRIPTION, COLUMN_SIGNATURE},
                    null, null, null, null, null);

            int idColumnIndex = cursor.getColumnIndex(COLUMN_ID);
            int descColumnIndex = cursor.getColumnIndex(COLUMN_DESCRIPTION);
            int signatureColumnIndex = cursor.getColumnIndex(COLUMN_SIGNATURE);

            // Verificar si los índices de columna son válidos
            if (idColumnIndex == -1 || descColumnIndex == -1 || signatureColumnIndex == -1) {
                Log.e("DatabaseHelper", "Una o más columnas no existen en la tabla");
                return signatures;
            }

            // Recorrer el cursor
            while (cursor.moveToNext()) {
                Signature signature = new Signature();
                signature.setId(cursor.getInt(idColumnIndex));
                signature.setDescription(cursor.getString(descColumnIndex));
                signature.setDigitalSignature(cursor.getBlob(signatureColumnIndex));
                signatures.add(signature);
            }
        } catch (Exception e) {
            Log.e("DatabaseHelper", "Error al recuperar firmas", e);
        } finally {
            // Cerrar el cursor
            if (cursor != null) {
                cursor.close();
            }
        }

        return signatures;
    }
}