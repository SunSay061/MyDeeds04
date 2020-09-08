package com.arhiser.todolist.screens.details;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.arhiser.todolist.App;
import com.arhiser.todolist.R;
import com.arhiser.todolist.data.AppDatabase;
import com.arhiser.todolist.model.Note;
import com.arhiser.todolist.screens.main.Adapter;
import com.arhiser.todolist.screens.main.MainActivity;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class NoteDetailsActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String EXTRA_NOTE = "NoteDetailsActivity.EXTRA_NOTE";
    //private static final TAG = "NoteDetailsActivity";
    public Note note;
    private EditText editText;
    private Button btnAdd;
    private Button btnRdy;
    private Spinner spinner;
    //Adapter adapter = new Adapter();
    private String[] data = {"Еда", "Одежда", "Гигиена", "Бытовая химия", "Бытовая техника", "Другое"};


    private TextView itemTimeAdd;
    private DatePickerDialog.OnDateSetListener dialogDateListener;

    public static void start(Activity caller, Note note) {
        Intent intent = new Intent(caller, NoteDetailsActivity.class);
        if (note != null) {
            intent.putExtra(EXTRA_NOTE, note);
        }
        caller.startActivity(intent);
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_note_details);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        //getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        //getSupportActionBar().setHomeButtonEnabled(true);

        setTitle("Новая задача");

        editText = findViewById(R.id.text);

        itemTimeAdd = findViewById(R.id.itemTimeEndAdd);
        //itemTimeAdd.setOnClickListener(this);

        btnAdd = findViewById(R.id.itemCreateBtnAdd);
        btnAdd.setOnClickListener(this);
        btnRdy = findViewById(R.id.itemCreateBtnReady);
        btnRdy.setOnClickListener(this);

        final ArrayAdapter<String> iSAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, data);
        iSAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner = findViewById(R.id.itemCreateSpinner);
        spinner.setAdapter(iSAdapter);
        spinner.setPrompt("Title");
        spinner.setSelection(5);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if(spinner.getSelectedItem().toString().equals("Еда")) {
                    findViewById(R.id.details).setBackgroundResource(R.drawable.gradient_note_style_food);
                }
                else if(spinner.getSelectedItem().toString().equals("Одежда")) {
                    findViewById(R.id.details).setBackgroundResource(R.drawable.gradient_note_style_cloth);
                }
                else if(spinner.getSelectedItem().toString().equals("Гигиена")) {
                    findViewById(R.id.details).setBackgroundResource(R.drawable.gradient_note_style_hug);
                }
                else if(spinner.getSelectedItem().toString().equals("Бытовая химия")) {
                    findViewById(R.id.details).setBackgroundResource(R.drawable.gradient_note_style_chem);
                }
                else if(spinner.getSelectedItem().toString().equals("Бытовая техника")) {
                    findViewById(R.id.details).setBackgroundResource(R.drawable.gradient_note_style_appl);
                }
                else if(spinner.getSelectedItem().toString().equals("Другое")) {
                    findViewById(R.id.details).setBackgroundResource(R.drawable.gradient_note_style_neutral);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        if (getIntent().hasExtra(EXTRA_NOTE)) {
            note = getIntent().getParcelableExtra(EXTRA_NOTE);
            editText.setText(note.text);
        } else {
            note = new Note();
        }

        itemTimeAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar cal = Calendar.getInstance();
                int year = cal.get(Calendar.YEAR);
                int month = cal.get(Calendar.MONTH);
                int day = cal.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog dialog = new DatePickerDialog(NoteDetailsActivity.this, android.R.style.Theme_Holo_Light_Dialog_MinWidth, dialogDateListener, year, month, day);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.show();
            }
        });

        dialogDateListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                year = year - 1900;
                Date date = new Date(year, month, day);
                @SuppressLint("SimpleDateFormat") SimpleDateFormat df2 = new SimpleDateFormat("dd MMM yyy");
                itemTimeAdd.setText(df2.format(date));
                @SuppressLint("SimpleDateFormat") SimpleDateFormat df3 = new SimpleDateFormat("dd MMM yyy");
                String dateInString = itemTimeAdd.getText().toString();
                try {
                    Date date2 = df3.parse(dateInString);
                    assert date2 != null;
                    note.timestampend = date2.getTime();
                } catch (ParseException e) {
                    e.printStackTrace();
                }

            }
        };
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.itemCreateBtnAdd:
                if (editText.getText().length() > 0) {
                    note.text = editText.getText().toString();
                    note.group = spinner.getSelectedItem().toString();
                    note.done = false;

                    note.timestamp = System.currentTimeMillis();

                    if (getIntent().hasExtra(EXTRA_NOTE)) {
                        App.getInstance().getNoteDao().update(note);
                    } else {
                        App.getInstance().getNoteDao().insert(note);
                    }
                    editText.setText("");
                    //adapter.notifyDataSetChanged();

                }
                break;
            case R.id.itemCreateBtnReady:
                finish();
                break;
            /*case  R.id.itemTimeEndAdd:
                Calendar cal = Calendar.getInstance();
                int year = cal.get(Calendar.YEAR);
                int month = cal.get(Calendar.MONTH);
                int day = cal.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog dialog = new DatePickerDialog(NoteDetailsActivity.this, android.R.style.Theme_Holo_Light_Dialog_MinWidth, dialogDateListener, year, month, day);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.show();*/
        }
    }


}
