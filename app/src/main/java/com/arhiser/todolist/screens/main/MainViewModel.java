package com.arhiser.todolist.screens.main;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.arhiser.todolist.App;
import com.arhiser.todolist.data.NoteDao;
import com.arhiser.todolist.model.Note;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Observer;

public class MainViewModel extends ViewModel {
    public LiveData<List<Note>> noteLiveData = App.getInstance().getNoteDao().getAllLiveData();
    public List<Note> allNote = App.getInstance().getNoteDao().getAll();
    public MutableLiveData<List<Note>> mutableLiveData = new MutableLiveData<>();
    boolean isResizible = false;

    public MutableLiveData<List<Note>> getMutableLiveData() {
        if (!isResizible) {
            sort("1");
        }
        return mutableLiveData;
    }

    public void sort(final String group) {

        Collections.sort(allNote, new Comparator<Note>() {

            @Override
            public int compare(Note note, Note t1) {

                if (!t1.done && note.done) {
                    return 1;
                }
                if (t1.done && !note.done) {
                        return -1;
                }
                else if (!t1.group.equals(group) && note.group.equals(group)) {
                    return -1;
                }
                else if (t1.group.equals(group) && !note.group.equals(group)) {
                    return 1;
                }
                else if (group.equals("Новые")) {
                    return (int) (t1.timestamp - note.timestamp);
                }
                else if (group.equals("Старые")) {
                    return (int) (note.timestamp - t1.timestamp);
                }
                else if (group.equals("Срочные")) {
                    return (int) (t1.timestampend - note.timestampend);
                }
                else if (group.equals("Удалить выполненные")) {
                    App.getInstance().getNoteDao().deleteDone();
                }
                else if (group.equals("Очистить список")) {
                    App.getInstance().getNoteDao().deleteAll();
                }
                return (int) (t1.timestamp - note.timestamp);
            }
        });
        mutableLiveData.setValue(allNote);
    }
}
