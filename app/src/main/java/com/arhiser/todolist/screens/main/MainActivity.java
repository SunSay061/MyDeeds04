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
    Note note;
    public RadioButton testCheck;
    //String sortType = "";

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
        mainViewModel.getLiveData().observe(this, new Observer<List<Note>>() {
            @Override
            public void onChanged(List<Note> notes) {
                adapter.setItems((ArrayList<Note>) notes);
                //adapter.notifyData();
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

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        Adapter adapter = new Adapter();
        MainViewModel mainViewModel = ViewModelProviders.of(this).get(MainViewModel.class);


        switch (item.getItemId()) {

            case R.id.deleteall:
                App.getInstance().getNoteDao().deleteAll();
                return true;
            case R.id.deleteselect:
                App.getInstance().getNoteDao().deleteDone();
                //adapter.notifyDataSetChanged();
                return true;
            case R.id.firstfood:
                mainViewModel.sortType = "Еда";
                adapter.setItems(mainViewModel.sort(adapter.sortedList));
                //mainViewModel.sort(adapter.sortedList);
                return true;
            case R.id.firstchem:
                mainViewModel.sortType = "Бытовая химия";
                //mainViewModel.sort(adapter.sortedList);
                return true;
            case R.id.firstcloth:
                mainViewModel.sortType = "Одежда";
                //mainViewModel.sort(adapter.sortedList);
                return true;
            case R.id.firsthug:
                mainViewModel.sortType = "Гигиена";
                //mainViewModel.sort(adapter.sortedList);
                return true;
            case R.id.firstapplies:
                mainViewModel.sortType = "Бытовая техника";
               // mainViewModel.sort(adapter.sortedList);
                return true;
            case R.id.firstother:
                mainViewModel.sortType = "Другое";
                //mainViewModel.sort(adapter.sortedList);
                return true;
            case R.id.firstnew:
                mainViewModel.sortType = "Новые";
                //mainViewModel.sort(adapter.sortedList);
                return true;
            case R.id.firstold:
                mainViewModel.sortType = "Старые";
                //mainViewModel.sort(adapter.sortedList);
                return true;
            case R.id.firstendtime:
                mainViewModel.sortType = "Срочные";
                //adapter.setItems(mainViewModel.sort(adapter.sortedList));
                //mainViewModel.sort(adapter.sortedList);
                return true;


        }
        //adapter.setItems(mainViewModel.sort(adapter.sortedList));
        return super.onOptionsItemSelected(item);
    }
}
