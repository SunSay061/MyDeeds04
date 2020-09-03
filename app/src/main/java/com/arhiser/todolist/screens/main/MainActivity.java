package com.arhiser.todolist.screens.main;

import android.os.Build;
import android.os.Bundle;

import com.arhiser.todolist.App;
import com.arhiser.todolist.R;
import com.arhiser.todolist.model.Note;
import com.arhiser.todolist.screens.details.NoteDetailsActivity;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.SortedList;

import android.view.MenuInflater;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.RadioButton;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity /*implements View.OnClickListener*/ {

    private RecyclerView recyclerView;
    private MenuInflater inflater;
    public RadioButton testCheck;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        recyclerView = findViewById(R.id.list);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, RecyclerView.VERTICAL, false);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));

        final Adapter adapter = new Adapter();
        recyclerView.setAdapter(adapter);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NoteDetailsActivity.start(MainActivity.this, null);
            }
        });

        MainViewModel mainViewModel = ViewModelProviders.of(this).get(MainViewModel.class);
        mainViewModel.getMutableLiveData().observe(this, new Observer<List<Note>>() {
            @Override
            public void onChanged(List<Note> notes) {
                adapter.setItems((ArrayList<Note>) notes);
                //recyclerView.invalidate();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_details, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        MainViewModel mainViewModel = ViewModelProviders.of(this).get(MainViewModel.class);

        switch (item.getItemId()) {

            case R.id.deleteall:
                mainViewModel.sort("Очистить список");
                return true;
            case R.id.deleteselect:
                mainViewModel.sort("Удалить выполненные");
                return true;
            case R.id.firstfood:
                mainViewModel.sort("Еда");
                return true;
            case R.id.firstchem:
                mainViewModel.sort("Бытовая химия");
                return true;
            case R.id.firstcloth:
                mainViewModel.sort("Одежда");
                return true;
            case R.id.firsthug:
                mainViewModel.sort("Гигиена");
                return true;
            case R.id.firstapplies:
                mainViewModel.sort("Бытовая техника");
                return true;
            case R.id.firstother:
                mainViewModel.sort("Другое");
                return true;
            case R.id.firstnew:
                mainViewModel.sort("Новые");
                return true;
            case R.id.firstold:
                mainViewModel.sort("Старые");
                return true;
            case R.id.firstendtime:
                mainViewModel.sort("Срочные");
                return true;

        }

        return super.onOptionsItemSelected(item);
    }
}
