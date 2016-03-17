package shestak.maksym.schedule.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import shestak.maksym.schedule.db.dao.ClassDao;
import shestak.maksym.schedule.src.max.*;
import shestak.maksym.schedule.src.max.Class;

public class DBHelper extends SQLiteOpenHelper {

    private static final String TABLE_GROUPS = "groups";
    private static final String TABLE_AUDITORIUMS = "auditoriums";
    private static final String TABLE_TEACHERS = "teachers";
    private static final String TABLE_CLASSES = "classes";




    public DBHelper(Context context) {
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
        db.execSQL("create table classes ("
                + "id integer primary key autoincrement,"
                + "day text,"
                + "title text,"
                + "type text,"
                + "auditorium text,"
                + "lecturer text,"
                + "groupn text,"
                + "classn text" + ");");

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("drop table if exists groups");
        db.execSQL("drop table if exists auditoriums");
        db.execSQL("drop table if exists teachers");
        db.execSQL("drop table if exists classes");
        onCreate(db);
    }

    public void delete(SQLiteDatabase db) {
        db.execSQL("drop table if exists groups");
        db.execSQL("drop table if exists auditoriums");
        db.execSQL("drop table if exists teachers");
        db.execSQL("drop table if exists classes");
        onCreate(db);
    }
    public void deleteSchedule(SQLiteDatabase db) {
        db.execSQL("drop table if exists classes");
        db.execSQL("create table classes ("
                + "id integer primary key autoincrement,"
                + "day text,"
                + "title text,"
                + "type text,"
                + "auditorium text,"
                + "lecturer text,"
                + "groupn text,"
                + "classn text" + ");");
    }

    //TODO loadSchedule
    public ArrayList<ClassDao> getSchedule() {
        ArrayList<ClassDao> classes = new ArrayList<>();

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.query("classes", null, null, null, null, null, null);


        String prevDate = "";
        String curDate;
        while(c.moveToNext()) {

            if(c.getString(c.getColumnIndex("day")).compareTo(prevDate) == 0)
                curDate = "";
            else {
                curDate = c.getString(c.getColumnIndex("day"));
                prevDate = curDate;
            }
            classes.add(new ClassDao(
                    c.getString(c.getColumnIndex("title")),
                    c.getString(c.getColumnIndex("type")),
                    c.getString(c.getColumnIndex("auditorium")),
                    c.getString(c.getColumnIndex("lecturer")),
                    c.getString(c.getColumnIndex("groupn")),
                    c.getString(c.getColumnIndex("classn")),
                    curDate));
        }
        c.close();

        return classes;
    }
    public void writeSchedule(ArrayList<Day> days) {
        SQLiteDatabase db = this.getReadableDatabase();
        ContentValues cv = new ContentValues();
        List<Class> classes;

        db.beginTransaction();
        for(Day d : days) {
            classes = d.classes;
            for(shestak.maksym.schedule.src.max.Class c : classes) {
                cv.clear();
                cv.put("title", c.title);
                cv.put("type", c.type);
                cv.put("auditorium", c.auditorium);
                cv.put("lecturer", c.lecturer);
                cv.put("groupn", c.group);
                cv.put("classn", c.classN);
                cv.put("day", d.data);
                db.insert("classes", null, cv);
            }
        }
        db.setTransactionSuccessful();
        db.endTransaction();
        db.close();;
    }

    public String getGroupId(String group) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(
                TABLE_GROUPS, null,
                "name = ?",
                new String[] {group},
                null, null, null);
        cursor.moveToFirst();
        return cursor.getString(cursor.getColumnIndex("num"));
    }
    public String getAuditoriumId(String auditorium) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(
                TABLE_AUDITORIUMS,
                null,
                "name = ?",
                new String[] {auditorium},
                null,
                null,
                null);
        cursor.moveToFirst();
        return cursor.getString(cursor.getColumnIndex("num"));
    }
    public String getTeacherId(String teacher) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(
                TABLE_TEACHERS,
                null,
                "name = ?",
                new String[] {teacher},
                null,
                null,
                null);
        cursor.moveToFirst();
        return cursor.getString(cursor.getColumnIndex("num"));
    }

    public boolean checkGroup(String group) {
        if(group.isEmpty()) return true;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(
                TABLE_GROUPS, null,
                "name = ?",
                new String[] {group},
                null, null, null);
        if(cursor.getCount() > 0) return true;
        return false;
    }
    public boolean checkTeacher(String teacher) {
        if(teacher.isEmpty()) return true;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(
                TABLE_TEACHERS, null,
                "name = ?",
                new String[] {teacher},
                null, null, null);
        if(cursor.getCount() > 0) return true;
        return false;
    }
    public boolean checkAuditorium(String auditorium) {
        if(auditorium.isEmpty()) return true;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(
                TABLE_AUDITORIUMS, null,
                "name = ?",
                new String[] {auditorium},
                null, null, null);
        if(cursor.getCount() > 0) return true;
        return false;
    }


    public boolean isSearchDataLoaded() {
        SQLiteDatabase db = this.getReadableDatabase();

        String count = "SELECT count(*) FROM groups";
        Cursor mcursor = db.rawQuery(count, null);
        mcursor.moveToFirst();
        int icount = mcursor.getInt(0);

        if(icount>0)
            return true;
        else
            return false;
    }
    public boolean isScheduleLoaded() {
        SQLiteDatabase db = this.getReadableDatabase();

        String count = "SELECT count(*) FROM classes";
        Cursor mcursor = db.rawQuery(count, null);
        mcursor.moveToFirst();
        int icount = mcursor.getInt(0);

        if(icount>0)
            return true;
        else
            return false;
    }
}
