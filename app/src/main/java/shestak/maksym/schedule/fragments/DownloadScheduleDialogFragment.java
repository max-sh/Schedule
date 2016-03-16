package shestak.maksym.schedule.fragments;

import android.app.Dialog;
import android.app.DialogFragment;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.AutoCompleteTextView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import shestak.maksym.schedule.*;
import shestak.maksym.schedule.Class;
import shestak.maksym.schedule.db.DBHelper;
import shestak.maksym.schedule.src.max.Day;
import shestak.maksym.schedule.src.max.Schedule;

public class DownloadScheduleDialogFragment extends DialogFragment {
    ProgressDialog dialog;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        dialog = new ProgressDialog(getActivity(), getTheme());
        dialog.setTitle("Downloading");
        dialog.setMessage("Please wait...");
        dialog.setIndeterminate(true);

        dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        dialog.setCanceledOnTouchOutside(true);

        String beg = ((TextView) getActivity()
                .findViewById(R.id.begDateText)).getText().toString();
        String end = ((TextView) getActivity()
                .findViewById(R.id.endDateText)).getText().toString();
        String group = ((AutoCompleteTextView) getActivity()
                .findViewById(R.id.groupAutocomplete)).getText().toString();
        String auditorium = ((AutoCompleteTextView) getActivity()
                .findViewById(R.id.auditoriumAutocomplete)).getText().toString();
        String teacher = ((AutoCompleteTextView) getActivity()
                .findViewById(R.id.teacherAutocomplete)).getText().toString();



        //Log.d("max", "GROUP:::: " + group);
        new DownloadSchedule().execute(group);

        return dialog;
    }

    class DownloadSchedule extends AsyncTask<String, Void, Void> {

        @Override
        protected Void doInBackground(String... params) {
            DBHelper dbh = new DBHelper(getActivity().getApplicationContext());
            SQLiteDatabase db = dbh.getWritableDatabase();

            dbh.deleteSchedule(db);
            Cursor cursor = db.query(
                    "groups",
                    null,
                    "name = ?",
                    new String[] {params[0]},
                    null,
                    null,
                    null);
            cursor.moveToFirst();
            Log.d("max", "GROUP: " + params[0]);

            String groupId = cursor.getString(cursor.getColumnIndex("num"));

            List<Day> days = Schedule.loadSchedule(groupId, "16.03.2016", "20.03.2016");
            //Log.d("max", "days size" + String.valueOf(days.size()));



            List<shestak.maksym.schedule.src.max.Class> classes;
            ContentValues cv = new ContentValues();
            long n = -1;
            db.beginTransaction();
            for(Day d : days) {
                //Log.d("max", d.data);
                classes = d.classes;
                for(shestak.maksym.schedule.src.max.Class c : classes) {
                    //Log.d("max", c.toString());
                    cv.clear();
                    cv.put("title", c.title);
                    cv.put("type", c.type);
                    cv.put("auditorium", c.auditorium);
                    cv.put("lecturer", c.lecturer);
                    cv.put("groupn", c.group);
                    cv.put("classn", c.classN);
                    cv.put("day", d.data);
                    n = db.insert("classes", null, cv);
                }
            }
            //Log.d("max", "classes in DB: " + String.valueOf(n));
            db.setTransactionSuccessful();
            db.endTransaction();

            dbh.close();

            //TODO check this !!!!!

            return null;
        }
        @Override
        protected void onPostExecute(Void aVoid) {

            ArrayList<Class> classes = new ArrayList<>();
            RecyclerView rv = (RecyclerView) getActivity().findViewById(R.id.rv);

            DBHelper dbh = new DBHelper(getActivity().getApplicationContext());
            SQLiteDatabase db = dbh.getReadableDatabase();

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

                classes.add(new Class(
                        c.getString(c.getColumnIndex("title")),
                        c.getString(c.getColumnIndex("type")),
                        c.getString(c.getColumnIndex("auditorium")),
                        c.getString(c.getColumnIndex("lecturer")),
                        c.getString(c.getColumnIndex("groupn")),
                        "time",
                        c.getString(c.getColumnIndex("classn")),
                        curDate));
                //Log.d("max", "+" + c.getString(c.getColumnIndex("auditorium")) + "+");

            }
            //Log.d("max", "cursor: " + String.valueOf(c.getCount()));
            c.close();
            //Log.d("max", "classes" + String.valueOf(classes.size()));


            rv = (RecyclerView) getActivity().findViewById(R.id.rv);
            rv.setHasFixedSize(true);
            LinearLayoutManager llm = new LinearLayoutManager(getActivity().getApplicationContext());
            rv.setLayoutManager(llm);

            RVAdapter adapter = new RVAdapter(classes);
            rv.setAdapter(adapter);


            dismiss();
            super.onPostExecute(aVoid);
        }

    }
}