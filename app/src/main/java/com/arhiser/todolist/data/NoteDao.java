package com.arhiser.todolist.data;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.RawQuery;
import androidx.room.Update;
import androidx.sqlite.db.SupportSQLiteQuery;

import com.arhiser.todolist.model.Note;

import java.util.List;

@Dao
public interface NoteDao {

    @Query("SELECT * FROM Note")
    List<Note> getAll();

    @Query("SELECT * FROM Note")
    LiveData<List<Note>> getAllLiveData();

    @Query("SELECT * FROM Note WHERE uid IN (:noteIds)")
    List<Note> loadAllByIds(int[] noteIds);

    @Query("SELECT * FROM Note WHERE uid = :uid LIMIT 1")
    Note findById(int uid);

    @Query("DELETE FROM Note")
    void deleteAll();

    @Query("DELETE FROM Note WHERE done = 1")
    void deleteDone();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(Note note);

    @Update
    void update(Note note);

    @Delete
    void delete(Note note);

}
