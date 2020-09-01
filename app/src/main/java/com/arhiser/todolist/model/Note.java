package com.arhiser.todolist.model;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.room.migration.Migration;
import androidx.sqlite.db.SupportSQLiteDatabase;

@Entity
public class Note implements Parcelable {

    @PrimaryKey(autoGenerate = true)
    public int uid;

    @ColumnInfo(name = "text")
    public String text;

    @ColumnInfo(name = "timestamp")
    public long timestamp;

    @ColumnInfo(name = "timestampend")
    public long timestampend;

    @ColumnInfo(name = "group")
    public String group;

    @ColumnInfo(name = "done")
    public boolean done;

    public Note() {
    }

    public int getUid() {
        return uid;
    }

    public void setUid(int uid) {
        this.uid = uid;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public long getTimestampend() {
        return timestampend;
    }

    public void setTimestampend(long timestampend) {
        this.timestampend = timestampend;
    }

    public String getGroup() {
        return group;
    }

    public void setGroup(String group) {
        this.group = group;
    }

    public boolean isDone() {
        return done;
    }

    public void setDone(boolean done) {
        this.done = done;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Note note = (Note) o;

        if (uid != note.uid) return false;
        if (timestamp != note.timestamp) return false;
        if (timestampend != note.timestampend) return false;
        if (done != note.done) return false;
        return text != null ? text.equals(note.text) : note.text == null & group != null ? group.equals(note.group) : note.group == null;
    }

    @Override
    public int hashCode() {
        int result = uid;
        result = 31 * result + (text != null ? text.hashCode() : 0);
        result = 31 * result + (group != null ? group.hashCode() : 0);
        result = 31 * result + (int) (timestamp ^ (timestamp >>> 32));
        result = 31 * result + (int) (timestampend ^ (timestampend >>> 32));
        result = 31 * result + (done ? 1 : 0);
        return result;
    }

    protected Note(Parcel in) {
        uid = in.readInt();
        text = in.readString();
        group = in.readString();
        timestamp = in.readLong();
        timestampend = in.readLong();
        done = in.readByte() != 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(uid);
        dest.writeString(text);
        dest.writeString(group);
        dest.writeLong(timestamp);
        dest.writeLong(timestampend);
        dest.writeByte((byte) (done ? 1 : 0));
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<Note> CREATOR = new Creator<Note>() {
        @Override
        public Note createFromParcel(Parcel in) {
            return new Note(in);
        }

        @Override
        public Note[] newArray(int size) {
            return new Note[size];
        }
    };

    /*public static final Migration MIGRATION_1_2 = new Migration(1, 2) {
        @Override
        public void migrate(@NonNull SupportSQLiteDatabase database) {
            database.execSQL("ALTER TABLE Note ADD COLUMN timespampend INTEGER DEFAULT 0 NOT NULL");
            database.execSQL("ALTER TABLE Note ADD COLUMN group STRING DEFAULT 0 NOT NULL");
        }
    };*/
}
