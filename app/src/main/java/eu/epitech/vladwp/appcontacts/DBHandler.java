package eu.epitech.vladwp.appcontacts;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class DBHandler extends SQLiteOpenHelper {

    private static int DB_VERSION = 1;

    private static String DB_NAME = "Contact.db";

    private static String DB_TABLE = "contactTable";

    private static String COL_ID = "id";
    private static String COL_NAME = "name";
    private static String COL_NUMBER = "number";
    private static String COL_EMAIL = "email";
    private static String COL_IMAGE = "image";

    public DBHandler(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_TABLE = "CREATE TABLE " + DB_TABLE + "(" +
                COL_ID + " INTEGER PRIMARY KEY," +
                COL_NAME + " TEXT," +
                COL_NUMBER + " TEXT," +
                COL_EMAIL + " TEXT," +
                COL_IMAGE + " BLOB)";
        db.execSQL(CREATE_TABLE);
    }


    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXIST " + DB_TABLE);

        onCreate(db);
    }

    public long addContact(String name, String number, String email, byte[] image) {
        long retourInsert = -1;
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues contentValues = new ContentValues();
        contentValues.put(COL_NAME, name);
        contentValues.put(COL_NUMBER, number);
        contentValues.put(COL_EMAIL, email);
        contentValues.put(COL_IMAGE, image);

        if (!checkDuplicateContact(contentValues)) {
            retourInsert =  db.insert(DB_TABLE, null, contentValues);
        }
        db.close();
        return retourInsert;
    }

    private boolean checkDuplicateContact(ContentValues contentValues) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(DB_TABLE, null, COL_NAME + " = ?",
                new String[]{
                        String.valueOf(contentValues.get(COL_NAME))
                },
                null, null, null);

        if (cursor != null && cursor.getCount() > 0) {
            cursor.close();

            return true;
        } else {
            return false;
        }
    }

    public List<Model> getAllContacts(){
        List<Model> ListModel = new ArrayList<>();

        String myQuery = "SELECT * FROM " + DB_TABLE;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(myQuery, null);

        if (cursor.moveToFirst()){
            do {
                Model model = new Model();
                model.setId(Integer.parseInt(cursor.getString(0)));
                model.setName(String.valueOf(cursor.getString(1)));
                model.setNumber(String.valueOf(cursor.getString(2)));
                model.setEmail(String.valueOf(cursor.getString(3)));
                if (cursor.getBlob(4) != null) {
                    model.setImage(cursor.getBlob(4));
                }
                ListModel.add(model);
            }while (cursor.moveToNext());
        }
        cursor.close();

        return ListModel;
    }

    public List<Model> getSpecificContact(String Id){
        List<Model> ListModel = new ArrayList<>();
        int newID = Integer.parseInt(Id);

        String myQuery = "SELECT * FROM " + DB_TABLE + " WHERE " + COL_ID + " = '" + newID + "'";

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(myQuery, null);

        if (cursor.moveToFirst()) {
            do {
                Model model = new Model();
                model.setName(String.valueOf(cursor.getString(1)));
                model.setNumber(String.valueOf(cursor.getString(2)));
                model.setEmail(String.valueOf(cursor.getString(3)));
                model.setImage(cursor.getBlob(4));

                ListModel.add(model);
            } while (cursor.moveToNext());
        }
        cursor.close();

        return ListModel;
    }

    public void updateContact(String Id, String newName, String newNumber, String newEmail, byte[] newImage) {
        SQLiteDatabase db = this.getWritableDatabase();
            ContentValues contentValues = new ContentValues();
            contentValues.put(COL_NAME, newName);
            contentValues.put(COL_NUMBER, newNumber);
            contentValues.put(COL_EMAIL, newEmail);
            contentValues.put(COL_IMAGE, newImage);
            //updating rows
            db.update(DB_TABLE, contentValues, COL_ID + " = ?",
                    new String[]{
                            Id
                    }
            );
            db.close();
    }

    public void deleteContact(String Id) {
        SQLiteDatabase db = this.getWritableDatabase();
        int newID = Integer.parseInt(Id);

        db.delete(DB_TABLE, COL_ID + " = ?",
                new String[]{
                        Id
                });
        db.close();
    }
}
