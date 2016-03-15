package shestak.maksym.schedule.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.HashMap;

public class DBHelper extends SQLiteOpenHelper {

    public DBHelper(Context context) {
        // конструктор суперкласса
        super(context, "myDB", null, 1);

    }

    @Override
    public void onCreate(SQLiteDatabase db) {


        db.execSQL("create table groups ("
                + "id integer primary key autoincrement,"
                + "num text,"
                + "name text" + ");");

        db.execSQL("create table auditoriums ("
                + "id integer primary key autoincrement,"
                + "num text,"
                + "name text" + ");");
        db.execSQL("create table teachers ("
                + "id integer primary key autoincrement,"
                + "num text,"
                + "name text" + ");");

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("drop table if exists groups");
        db.execSQL("drop table if exists auditoriums");
        db.execSQL("drop table if exists teachers");
        onCreate(db);
    }

    public void delete(SQLiteDatabase db) {
        db.execSQL("drop table if exists groups");
        db.execSQL("drop table if exists auditoriums");
        db.execSQL("drop table if exists teachers");
        onCreate(db);
    }
}
