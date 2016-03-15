package shestak.maksym.schedule;

import android.app.DialogFragment;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.TextView;
import android.widget.Toast;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import butterknife.Bind;
import butterknife.ButterKnife;
import shestak.maksym.schedule.db.DBHelper;
import shestak.maksym.schedule.fragments.DatePickerFragment;
import shestak.maksym.schedule.fragments.MyDownloaderDialogFragment;
import shestak.maksym.schedule.src.max.Data;

public class MainActivity extends AppCompatActivity {
    @Bind(R.id.groupAutocomplete) AutoCompleteTextView group;
    @Bind(R.id.teacherAutocomplete) AutoCompleteTextView teacher;
    @Bind(R.id.auditoriumAutocomplete) AutoCompleteTextView auditorium;
    @Bind(R.id.begDateText) TextView begDateText;
    @Bind(R.id.endDateText) TextView endDateText;


    RecyclerView rv;
    SharedPreferences sharedPreferences;

    private ArrayList<String> GROUPS;
    private ArrayList<String> TEACHERS;
    private ArrayList<String> AUDITORIUMS;

    List<Class> classes;


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
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        ButterKnife.bind(this);

        sharedPreferences = getPreferences(MODE_PRIVATE);
        if(sharedPreferences.getString("first", "").isEmpty()) {
            Log.d("max", "FIRST LAUNCH" + sharedPreferences.getString("first", ""));
            MyDownloaderDialogFragment fragment = new MyDownloaderDialogFragment();
            fragment.show(getFragmentManager(), "dialogfrag");
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString("first", "n");
            editor.commit();
        }
        Log.d("max", "FIRST LAUNCH?-" + sharedPreferences.getString("first", ""));


        initializeAutocomplete();
        group.setAdapter(new ArrayAdapter(this, android.R.layout.simple_dropdown_item_1line, GROUPS.toArray()));
        teacher.setAdapter(new ArrayAdapter(this, android.R.layout.simple_dropdown_item_1line, TEACHERS.toArray()));
        auditorium.setAdapter(new ArrayAdapter(this, android.R.layout.simple_dropdown_item_1line, AUDITORIUMS.toArray()));



        rv = (RecyclerView)findViewById(R.id.rv);
        rv.setHasFixedSize(true);
        LinearLayoutManager llm = new LinearLayoutManager(getApplicationContext());
        rv.setLayoutManager(llm);

        initializeData();
        RVAdapter adapter = new RVAdapter(classes);
        rv.setAdapter(adapter);

    }

    private void initializeData() {
        classes = new ArrayList<>();
        classes.add(new Class("Комп'ютерна графіка", "практична робота",
                "Г1310", "Баранова Ірина Володимирівна", "ІТ-41", "09:50-11:10", "2-га пара", "05.03.2016 Субота"));
        classes.add(new Class("Комп'ютерні мережі", "лекція",
                "Г1305", "Гайдабрус Богдан Володимирович", "ІТ-41", "09:50-11:10", "2-га пара", ""));
        classes.add(new Class("Політологія", "практична робота",
                "Г710", "Панченко Юлія Валеріївна", "ІТ-41", "09:50-11:10", "2-га пара", ""));

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
        newFragment.show(getFragmentManager(), "datePicker");

        Bundle args = new Bundle();
        args.putInt("text", v.getId());
        Log.d("max", "setDate for: " + v.getId());
        newFragment.setArguments(args);
    }

    public void search(View v) {
        String g = group.getText().toString();

        Log.d("max", "search btn: " + g);
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
