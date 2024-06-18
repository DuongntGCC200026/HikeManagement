package com.example.cw_hikermanagementapp;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteStatement;

import androidx.annotation.Nullable;

public class Database extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "hike_management";
    private static final int DATABASE_VERSION = 17;

    public static final String TABLE_NAME = "hike";
    public static final String TABLE_NAME1 = "observation";
    public static final String TABLE_NAME2 = "image";
    public static final String COLUMN_ID = "id";
    public static final String COLUMN_ID1 = "hike_id";
    public static final String COLUMN_ID2 = "ob_id";
    public static final String COLUMN_ID3 = "id_img";
    public static final String COLUMN_NAME = "name";
    public static final String COLUMN_OBSERVATION = "nameOfObservation";
    public static final String COLUMN_LOCATION = "location";
    public static final String COLUMN_DATE = "dateOfTheHike";
    public static final String COLUMN_DATETIME = "timeOfTheObservation";
    public static final String COLUMN_DATETIME1 = "timeOfPicture";
    public static final String COLUMN_TITLE = "title";
    public static final String COLUMN_PARKING = "parking";
    public static final String COLUMN_LENGTH = "length";
    public static final String COLUMN_LEVEL = "level";
    public static final String COLUMN_DESCRIPTION = "description";
    public static final String COLUMN_ADDITIONAL = "additionalComment";
    public static final String COLUMN_START = "timeStart";
    public static final String COLUMN_STOP = "timeStop";
    public static final String COLUMN_DELETE = "isDeleted";
    public static final String COLUMN_DELETE1 = "isDeleted";
    public static final String COLUMN_IMG = "image";
    public static final String FOREIGN_KEY = "FOREIGN KEY(" + COLUMN_ID1 + ") REFERENCES " + TABLE_NAME + "(" + COLUMN_ID + ")";

    public Database(@Nullable Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    public Cursor GetData(String query) {
        SQLiteDatabase database = getReadableDatabase();
        return database.rawQuery(query, null);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        String query = "CREATE TABLE IF NOT EXISTS " + TABLE_NAME +
                "(" +
                COLUMN_ID + " INTEGER PRIMARY KEY," +
                COLUMN_NAME + " VARCHAR(100) NOT NULL," +
                COLUMN_LOCATION + " VARCHAR(200) NOT NULL," +
                COLUMN_DATE + " DATE NOT NULL," +
                COLUMN_PARKING + " INTEGER NOT NULL," +
                COLUMN_LENGTH + " REAL NOT NULL," +
                COLUMN_LEVEL + " INTEGER NOT NULL," +
                COLUMN_DESCRIPTION + " VARCHAR(200)," +
                COLUMN_START + " VARCHAR(100)," +
                COLUMN_STOP + " VARCHAR(100)," +
                COLUMN_DELETE + " INTEGER NOT NULL" +
                ")";
        sqLiteDatabase.execSQL(query);

        String queryOb = "CREATE TABLE IF NOT EXISTS " + TABLE_NAME1 +
                "(" +
                COLUMN_ID2 + " INTEGER PRIMARY KEY," +
                COLUMN_OBSERVATION + " VARCHAR(100) NOT NULL," +
                COLUMN_DATETIME + " VARCHAR(100) NOT NULL," +
                COLUMN_ADDITIONAL + " VARCHAR(200) NOT NULL," +
                COLUMN_DELETE1 + " INTEGER NOT NULL," +
                COLUMN_ID1 + " INTEGER NOT NULL, " +
                FOREIGN_KEY +
                ")";

        sqLiteDatabase.execSQL(queryOb);

        String s = "CREATE TABLE IF NOT EXISTS " + TABLE_NAME2 +
                "(" +
                COLUMN_ID3 + " INTEGER PRIMARY KEY," +
                COLUMN_ID2 + " INTEGER NOT NULL," +
                COLUMN_TITLE + " VARCHAR(100) NOT NULL," +
                COLUMN_DATETIME1 + " VARCHAR(100) NOT NULL," +
                COLUMN_DELETE1 + " INTEGER NOT NULL," +
                COLUMN_IMG + " VARCHAR(100) NOT NULL" +
                ")";

        sqLiteDatabase.execSQL(s);
    }

    public void insertImage(Img image) {
        SQLiteDatabase database = getWritableDatabase();
        String query = "Insert into image values(?,?,?,?,?,?)";
        SQLiteStatement statement = database.compileStatement(query);
        statement.clearBindings();
        statement.bindDouble(1, image.getId());
        statement.bindDouble(2, image.getOb_id());
        statement.bindString(3, image.getTitle());
        statement.bindString(4, image.getDate());
        statement.bindDouble(5, image.getIsDeleted());
        statement.bindBlob(6, image.getImage());
        statement.executeInsert();
    }

    public void updateImage (Img img) {
        SQLiteDatabase db = getWritableDatabase();

        ContentValues content = new ContentValues();
        content.put("title", img.getTitle());
        content.put("isDeleted", img.getIsDeleted());

        String[] whereArgs = {String.valueOf(img.getId())};
        db.update("image", content, "id_img = ?", whereArgs);
    }

    public void insertHike(Hike hike) {
        SQLiteDatabase database = getWritableDatabase();
        String query = "INSERT INTO hike (id, name, location, dateOfTheHike, parking, length, level, description, timeStart, timeStop, isDeleted)" +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        SQLiteStatement statement = database.compileStatement(query);
        statement.clearBindings();
        statement.bindDouble(1, hike.getId());
        statement.bindString(2, hike.getName());
        statement.bindString(3, hike.getLocation());
        statement.bindString(4, hike.getDateOfTheHike());
        statement.bindDouble(5, hike.isParking());
        statement.bindDouble(6, hike.getLength());
        statement.bindDouble(7, hike.getLevel());
        statement.bindString(8, hike.getDescription());
        statement.bindString(9, hike.getTimeStart());
        statement.bindString(10, hike.getTimeStop());
        statement.bindDouble(11, hike.isDeleted());
        statement.executeInsert();
    }

    public void updateHike(Hike hike) {
        SQLiteDatabase db = getWritableDatabase();

        ContentValues content = new ContentValues();
        content.put("name", hike.getName());
        content.put("location", hike.getLocation());
        content.put("dateOfTheHike", hike.getDateOfTheHike());
        content.put("parking", hike.isParking());
        content.put("length", hike.getLength());
        content.put("level", hike.getLevel());
        content.put("description", hike.getDescription());
        content.put("timeStart", hike.getTimeStart());
        content.put("timeStop", hike.getTimeStop());
        content.put("isDeleted", hike.isDeleted());

        String[] whereArgs = {String.valueOf(hike.getId())};
        db.update("hike", content, "id = ?", whereArgs);
    }

    public void insertObservation(Observation observation) {
        SQLiteDatabase database = getWritableDatabase();
        String query = "INSERT INTO observation (ob_id, nameOfObservation, timeOfTheObservation, additionalComment, isDeleted, hike_id)" +
                "VALUES (?, ?, ?, ?, ?, ?)";

        SQLiteStatement statement = database.compileStatement(query);
        statement.clearBindings();
        statement.bindDouble(1, observation.getId());
        statement.bindString(2, observation.getNameOfObservation());
        statement.bindString(3, observation.getDateAndTime());
        statement.bindString(4, observation.getAdditionalComments());
        statement.bindDouble(5, observation.getIsDeleted());
        statement.bindDouble(6, observation.getHikeId());
        statement.executeInsert();
    }

    public void updateObservation(Observation observation) {
        SQLiteDatabase db = getWritableDatabase();

        ContentValues content = new ContentValues();
        content.put("nameOfObservation", observation.getNameOfObservation());
        content.put("timeOfTheObservation", observation.getDateAndTime());
        content.put("additionalComment", observation.getAdditionalComments());
        content.put("isDeleted", observation.getIsDeleted());

        String[] whereArgs = {String.valueOf(observation.getId())};
        db.update("observation", content, "ob_id = ?", whereArgs);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        String query1 = "DROP TABLE IF EXISTS " + TABLE_NAME1;
        String query = "DROP TABLE IF EXISTS " + TABLE_NAME;
        String query2 = "DROP TABLE IF EXISTS " + TABLE_NAME2;
        sqLiteDatabase.execSQL(query1);
        sqLiteDatabase.execSQL(query);
        sqLiteDatabase.execSQL(query2);
        onCreate(sqLiteDatabase);
    }

    public void dropTable(String tableName) {
        SQLiteDatabase db = getWritableDatabase();
        db.execSQL("DROP TABLE IF EXISTS " + tableName);
    }
}