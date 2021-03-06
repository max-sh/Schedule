package shestak.maksym.schedule.fragments;

import android.app.Dialog;
import android.app.DialogFragment;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatDialogFragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.AutoCompleteTextView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import shestak.maksym.schedule.*;
import shestak.maksym.schedule.db.dao.ClassDao;
import shestak.maksym.schedule.src.max.Class;
import shestak.maksym.schedule.db.DBHelper;
import shestak.maksym.schedule.src.max.Day;
import shestak.maksym.schedule.src.max.Schedule;

public class DownloadScheduleDialogFragment extends AppCompatDialogFragment {
    ProgressDialog dialog;
    private static final String TAG = "max";
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
        new DownloadSchedule().execute(group, teacher, auditorium, beg, end);

        return dialog;
    }

    class DownloadSchedule extends AsyncTask<String, Void, Void> {

        @Override
        protected Void doInBackground(String... params) {
            DBHelper dbh = new DBHelper(getActivity().getApplicationContext());
            SQLiteDatabase db = dbh.getWritableDatabase();
            dbh.deleteSchedule(db);

            String groupId          = params[0];
            String teacherId        = params[1];
            String auditoriumId     = params[2];
            String beg              = params[3];
            String end              = params[4];

            if(groupId.isEmpty()) groupId = "0";
            else groupId = dbh.getGroupId(groupId);
            if(teacherId.isEmpty()) teacherId = "0";
            else teacherId = dbh.getTeacherId(teacherId);
            if(auditoriumId.isEmpty()) auditoriumId = "0";
            else auditoriumId = dbh.getAuditoriumId(auditoriumId);



            Log.d(TAG, getClass().getSimpleName() + ": " + groupId + teacherId + auditoriumId);

            ArrayList<Day> days = Schedule.loadSchedule(groupId, teacherId, auditoriumId,
                    beg, end);

            dbh.writeSchedule(days);
            dbh.close();
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            ArrayList<ClassDao> classes = new DBHelper(getActivity().getApplicationContext()).getSchedule();
            RecyclerView rv = (RecyclerView) getActivity().findViewById(R.id.rv);

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
