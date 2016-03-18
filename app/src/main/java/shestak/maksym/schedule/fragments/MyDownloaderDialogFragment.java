package shestak.maksym.schedule.fragments;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatDialogFragment;
import android.util.Log;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import shestak.maksym.schedule.db.DBHelper;
import shestak.maksym.schedule.src.max.Data;


public class MyDownloaderDialogFragment extends AppCompatDialogFragment {

    final String LOG_TAG = "max";

    public Data getD() {
        return d;
    }

    String FILE_URL;
    public Data d;

    String TAG = "myTag";
    ProgressDialog dialog;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
//		return super.onCreateDialog(savedInstanceState);

        dialog = new ProgressDialog(getActivity(), getTheme());
        dialog.setTitle("Downloading");
        dialog.setMessage("Please wait...");
        dialog.setIndeterminate(true);

        //dialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        dialog.setCanceledOnTouchOutside(true);

        DownloadFromURL downloadFromURL = new DownloadFromURL();
        downloadFromURL.execute();


        //Log.d("max", "onCreateDialog " + Data.GROUPS.get("ІТ-41"));

        return dialog;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }


    class DownloadFromURL extends AsyncTask<Void, Void, Void> {


        @Override
        protected Void doInBackground(Void... params) {
            Data.load();
            Log.d(LOG_TAG, "data loaded");


            DBHelper dbh = new DBHelper(getActivity().getApplicationContext());
            SQLiteDatabase db = dbh.getWritableDatabase();

            dbh.delete(db);

            ContentValues cv = new ContentValues();
            List<String> keys = new ArrayList<>(Data.GROUPS.keySet());


            db.beginTransaction();
            for(String s : keys) {
                cv.clear();
                cv.put("name", s);
                cv.put("num", Data.GROUPS.get(s));
                db.insert("groups", null, cv);
            }
            keys = new ArrayList<>(Data.AUDITORIUMS.keySet());
            for(String s : keys) {
                cv.clear();
                cv.put("name", s);
                cv.put("num", Data.AUDITORIUMS.get(s));
                db.insert("auditoriums", null, cv);
            }
            keys = new ArrayList<>(Data.TEACHERS.keySet());
            for(String s : keys) {
                cv.clear();
                cv.put("name", s);
                cv.put("num", Data.TEACHERS.get(s));
                db.insert("teachers", null, cv);
            }

            db.setTransactionSuccessful();
            db.endTransaction();


            //long rowID = db.insert("mytable", null, cv);
            //Log.d(LOG_TAG, "row inserted, ID = " + rowID);
            dbh.close();
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            dismiss();
            Toast.makeText(getActivity().getApplicationContext(), "Downloaded", Toast.LENGTH_SHORT).show();
        }
    }
}

