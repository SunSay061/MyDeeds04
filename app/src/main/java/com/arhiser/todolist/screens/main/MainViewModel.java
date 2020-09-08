package com.arhiser.todolist.screens.main;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;
import com.arhiser.todolist.App;
import com.arhiser.todolist.data.NoteDao;
import com.arhiser.todolist.model.Note;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Observer;

public class MainViewModel extends ViewModel  {

    public LiveData<List<Note>> noteLiveData = App.getInstance().getNoteDao().getAllLiveData();
    String sortType = "";
    public LiveData<List<Note>> getLiveData() {
        return noteLiveData;
    }
    public List<Note> sort(List<Note> notesList) {
        Collections.sort(notesList, new Comparator<Note>() {
            @Override
            public int compare(Note note, Note t1) {
                if (!t1.done && note.done) {
                    return 1; }
                else if (t1.done && !note.done) {
                    return -1; }
                else if (!t1.group.equals(sortType) && note.group.equals(sortType)) {
                    return -1; }
                else if (t1.group.equals(sortType) && !note.group.equals(sortType)) {
                    return 1; }
                else if (sortType.equals("Новые")) {
                    return (int) (t1.timestamp - note.timestamp); }
                else if (sortType.equals("Старые")) {
                    return (int) (note.timestamp - t1.timestamp); }
                else if (sortType.equals("Срочные")) {
                    if(note.timestampend == 0){
                        return 1; }
                    else if(t1.timestampend == 0){
                        return -1; }
                    return (int) (note.timestampend - t1.timestampend); }
                return (int) (t1.timestamp - note.timestamp);
            }
        });
        return notesList;
       }

}
