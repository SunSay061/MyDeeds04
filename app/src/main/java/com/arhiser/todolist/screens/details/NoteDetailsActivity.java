package com.arhiser.todolist.screens.details;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;

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

public class NoteDetailsActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String EXTRA_NOTE = "NoteDetailsActivity.EXTRA_NOTE";

    public Note note;

    private EditText editText;

    private View backView;

    private Button btnAdd;

    private Button btnRdy;

    private Spinner spinner;

    Adapter adapter = new Adapter();

    private String[] data = {"Еда", "Одежда", "Гигиена", "Бытовая химия", "Бытовая техника", "Другое"};

    public static void start(Activity caller, Note note) {
        Intent intent = new Intent(caller, NoteDetailsActivity.class);
        if (note != null) {
            intent.putExtra(EXTRA_NOTE, note);
        }
        caller.startActivity(intent);
    }

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
        backView = findViewById(R.id.details);

        btnAdd = findViewById(R.id.itemCreateBtnAdd);
        btnAdd.setOnClickListener(this);
        btnRdy = findViewById(R.id.itemCreateBtnReady);
        btnRdy.setOnClickListener(this);

        ArrayAdapter<String> iSAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, data);
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
    }



    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
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
                    adapter.notifyDataSetChanged();

                }
                break;
            case R.id.itemCreateBtnReady:
                finish();
                break;
        }
    }

    /*@Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.deleteall:
                //for (int z = 0; z < App.getInstance().getNoteDao().deleteAll();)
                App.getInstance().getNoteDao().deleteAll();
        }
        return super.onOptionsItemSelected(item);
    }*/
}
