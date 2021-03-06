package shestak.maksym.schedule;

import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import shestak.maksym.schedule.db.DBHelper;
import shestak.maksym.schedule.db.dao.ClassDao;
import shestak.maksym.schedule.fragments.DatePickerFragment;
import shestak.maksym.schedule.fragments.DownloadScheduleDialogFragment;
import shestak.maksym.schedule.fragments.MyDownloaderDialogFragment;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "max";
    @Bind(R.id.groupAutocomplete) AutoCompleteTextView group;
    @Bind(R.id.teacherAutocomplete) AutoCompleteTextView teacher;
    @Bind(R.id.auditoriumAutocomplete) AutoCompleteTextView auditorium;
    @Bind(R.id.begDateText) TextView begDateText;
    @Bind(R.id.endDateText) TextView endDateText;
    RecyclerView rv;
    @Bind(R.id.relativeLayout) RelativeLayout rl;
    SharedPreferences sharedPreferences;
    List<shestak.maksym.schedule.src.max.Class> classes;
    private ArrayList<String> GROUPS;
    private ArrayList<String> TEACHERS;
    private ArrayList<String> AUDITORIUMS;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                /*
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
                        */
                if (rl.getVisibility() == View.VISIBLE)
                    rl.setVisibility(View.GONE);
                else
                    rl.setVisibility(View.VISIBLE);
            }
        });

        ButterKnife.bind(this);
/*
        sharedPreferences = getPreferences(MODE_PRIVATE);
        if(sharedPreferences.getString("first", "").isEmpty()) {
            Log.d("max", "FIRST LAUNCH" + sharedPreferences.getString("first", ""));
            MyDownloaderDialogFragment fragment = new MyDownloaderDialogFragment();
            fragment.show(getFragmentManager(), "dialogfrag");
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString("first", "n");
            editor.apply();

            initializeAutocomplete();
            group.setAdapter(new ArrayAdapter(this, android.R.layout.simple_dropdown_item_1line, GROUPS.toArray()));
            teacher.setAdapter(new ArrayAdapter(this, android.R.layout.simple_dropdown_item_1line, TEACHERS.toArray()));
            auditorium.setAdapter(new ArrayAdapter(this, android.R.layout.simple_dropdown_item_1line, AUDITORIUMS.toArray()));

        }
        Log.d("max", "FIRST LAUNCH?-" + sharedPreferences.getString("first", ""));
*/
        DBHelper dbHelper = new DBHelper(getApplicationContext());
        if(!dbHelper.isSearchDataLoaded()) {
            Log.d(TAG, getClass().getSimpleName() + ": " + "Downloaded data");
            MyDownloaderDialogFragment fragment = new MyDownloaderDialogFragment();
            fragment.show(getSupportFragmentManager(), "dialogfrag");
        } else {
            Log.d(TAG, getClass().getSimpleName() + ": " + "All data already loaded");
        }


        initializeAutocomplete();
        group.setAdapter(new ArrayAdapter(this, android.R.layout.simple_dropdown_item_1line, GROUPS.toArray()));
        teacher.setAdapter(new ArrayAdapter(this, android.R.layout.simple_dropdown_item_1line, TEACHERS.toArray()));
        auditorium.setAdapter(new ArrayAdapter(this, android.R.layout.simple_dropdown_item_1line, AUDITORIUMS.toArray()));

        Calendar c = Calendar.getInstance();
        SimpleDateFormat df = new SimpleDateFormat("dd.MM.yyyy");
        String formattedDate = df.format(c.getTime());
        begDateText.setText(formattedDate);
        c.add(Calendar.DATE, 10);
        String endDate = df.format(c.getTime());
        endDateText.setText(endDate);

        //todo show loaded schedule
        if(dbHelper.isScheduleLoaded()) {
            ArrayList<ClassDao> classes = new DBHelper(getApplicationContext()).getSchedule();
            RecyclerView rv = (RecyclerView) findViewById(R.id.rv);

            rl.setVisibility(View.GONE);
            rv.setHasFixedSize(true);
            LinearLayoutManager llm = new LinearLayoutManager(getApplicationContext());
            rv.setLayoutManager(llm);
            RVAdapter adapter = new RVAdapter(classes);
            rv.setAdapter(adapter);
        }

    }

    private void initializeAutocomplete() {

        GROUPS = new ArrayList<>();
        TEACHERS = new ArrayList<>();
        AUDITORIUMS = new ArrayList<>();

        DBHelper dbh = new DBHelper(getApplicationContext());
        SQLiteDatabase db = dbh.getReadableDatabase();

        Cursor c = db.query("groups", null, null, null, null, null, null);
        while(c.moveToNext()) {
            String uname = c.getString(c.getColumnIndex("name"));
            GROUPS.add(uname);
        }
        c = db.query("teachers", null, null, null, null, null, null);
        while(c.moveToNext()) {
            String uname = c.getString(c.getColumnIndex("name"));
            TEACHERS.add(uname);
        }
        c = db.query("auditoriums", null, null, null, null, null, null);
        while(c.moveToNext()) {
            String uname = c.getString(c.getColumnIndex("name"));
            AUDITORIUMS.add(uname);
        }



        c.close();
    }

    public void showDatePickerDialog(View v) {
        DatePickerFragment newFragment = new DatePickerFragment();
        newFragment.show(getSupportFragmentManager(), "datePicker");

        Bundle args = new Bundle();
        args.putInt("text", v.getId());
        Log.d("max", "setDate for: " + v.getId());
        newFragment.setArguments(args);
    }

    public void search(View v) {
        //TODO validation
        DBHelper dbHelper = new DBHelper(getApplicationContext());

        String groupText = group.getText().toString();
        String auditoriumText = auditorium.getText().toString();
        String teacherText = teacher.getText().toString();

        short empty = 0;
        if(groupText.isEmpty()) empty++;
        if(auditoriumText.isEmpty()) empty++;
        if(teacherText.isEmpty()) empty++;

        if(empty != 3 && dbHelper.checkGroup(groupText) && dbHelper.checkTeacher(teacherText) &&
                dbHelper.checkAuditorium(auditoriumText)) {
            rl.setVisibility(View.GONE);
            DownloadScheduleDialogFragment fragment = new DownloadScheduleDialogFragment();
            fragment.show(getSupportFragmentManager(), "dScheduleFragment");
        }


    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
