package com.arhiser.todolist.screens.main;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.arhiser.todolist.App;
import com.arhiser.todolist.data.NoteDao;
import com.arhiser.todolist.model.Note;

import java.util.List;

public class MainViewModel extends ViewModel {
    public LiveData<List<Note>> noteLiveData = App.getInstance().getNoteDao().getAllLiveData();

    public LiveData<List<Note>> getNoteLiveData() {
        return noteLiveData;



    }

}
