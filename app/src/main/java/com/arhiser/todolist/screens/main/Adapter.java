package com.arhiser.todolist.screens.main;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.graphics.Paint;
import android.os.Build;
import android.telephony.TelephonyManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.RadioButton;
import android.widget.TextView;
import androidx.lifecycle.ViewModelProvider;
import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.SortedList;
import androidx.room.Database;

import com.arhiser.todolist.App;
import com.arhiser.todolist.R;
import com.arhiser.todolist.data.AppDatabase;
import com.arhiser.todolist.model.Note;
import com.arhiser.todolist.screens.details.NoteDetailsActivity;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

public class Adapter extends RecyclerView.Adapter<Adapter.NoteViewHolder> {
    public List<Note> sortedList;
    private OnNoteListener mOnNoteListener;

    public Adapter(OnNoteListener onNoteListener){
        sortedList = new ArrayList<>();
        this.mOnNoteListener = onNoteListener;
    }

    @NonNull
    @Override
    public NoteViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new NoteViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_note_list, parent, false), mOnNoteListener);
    }

    @Override
    public void onBindViewHolder(@NonNull NoteViewHolder holder, int position) {
        holder.bind(sortedList.get(position));
    }

    @Override
    public int getItemCount() {
        return sortedList.size();
    }

    public void setItems(List<Note> notes) {
        sortedList.clear();
        sortedList.addAll(notes);
        notifyDataSetChanged();

    }
    public void updateAdapter() {

        notifyDataSetChanged();
    }


    public void submitList(List<Note> notes) {

    }

    static class NoteViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView noteText;
        CheckBox completed;
        TextView noteGroup;
        View backGround;
        View leftTag;
        TextView timestampStart;
        TextView timestampEnd;
        Note note;
        boolean silentUpdate;

        OnNoteListener onNoteListener;

        /*interface Callback{
            void callingBack();
        }
        Callback callback;

        public void registerCallBack(Callback callback){
            this.callback = callback;
        }*/

        public NoteViewHolder(@NonNull final View itemView, final OnNoteListener onNoteListener) {
            super(itemView);

            noteText = itemView.findViewById(R.id.note_text);
            completed = itemView.findViewById(R.id.completed);
            noteGroup = itemView.findViewById(R.id.note_help_text);
            backGround = itemView.findViewById(R.id.itemTemplateBackground);
            leftTag = itemView.findViewById(R.id.itemTemplateLeftTag);
            timestampStart = itemView.findViewById(R.id.noteStartDate);
            timestampEnd = itemView.findViewById(R.id.noteEndDate);
            this.onNoteListener = onNoteListener;

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    NoteDetailsActivity.start((Activity) itemView.getContext(), note);
                }
            });

            completed.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton compoundButton, boolean checked) {
                    if (!silentUpdate) {
                        note.done = checked;
                        App.getInstance().getNoteDao().update(note);
                    }
                    updateStrokeOut();
                    onNoteListener.onNoteClick();
                }

            });

        }


        @SuppressLint("ResourceAsColor")
        public void bind(Note note) {
            this.note = note;

            noteText.setText(note.text);
            noteGroup.setText(note.group);
            setDate();
            updateStrokeOut();
            setBackGround();

            silentUpdate = true;
            completed.setChecked(note.done);
            silentUpdate = false;
        }

        private void updateStrokeOut() {

            if (note.done) {
                noteText.setPaintFlags(noteText.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
            } else {
                noteText.setPaintFlags(noteText.getPaintFlags() & ~Paint.STRIKE_THRU_TEXT_FLAG);
            }


        }

        @SuppressLint("ResourceAsColor")
        public void setBackGround() {
            if(!noteGroup.getText().toString().equals("")) {

                if (noteGroup.getText().toString().equals("Еда")) {
                    backGround.setBackgroundResource(R.drawable.gradient_note_style_food);
                    leftTag.setBackgroundColor(R.color.foodNoteColor);

                } else if (noteGroup.getText().toString().equals("Одежда")) {
                    backGround.setBackgroundResource(R.drawable.gradient_note_style_cloth);
                    leftTag.setBackgroundColor(R.color.clothNoteColor);

                } else if (noteGroup.getText().toString().equals("Гигиена")) {
                    backGround.setBackgroundResource(R.drawable.gradient_note_style_hug);
                    leftTag.setBackgroundColor(R.color.hugNoteColor);

                } else if (noteGroup.getText().toString().equals("Бытовая химия")) {
                    backGround.setBackgroundResource(R.drawable.gradient_note_style_chem);
                    leftTag.setBackgroundColor(R.color.chemNoteColor);

                } else if (noteGroup.getText().toString().equals("Бытовая техника")) {
                    backGround.setBackgroundResource(R.drawable.gradient_note_style_appl);
                    leftTag.setBackgroundColor(R.color.appleNoteColor);

                } else if (noteGroup.getText().toString().equals("Другое")) {
                    backGround.setBackgroundResource(R.drawable.gradient_note_style_neutral);
                    leftTag.setBackgroundColor(R.color.otherNoteColor);
                }
            }
        }
        public void setDate() {
            Date dates = new Date(note.timestamp);
            @SuppressLint("SimpleDateFormat") SimpleDateFormat df2 = new SimpleDateFormat("MMM dd yyyy");
            timestampStart.setText(df2.format(dates));

            try {
                //long currentDate = System.currentTimeMillis();
                Date datee = new Date(note.timestampend);
                @SuppressLint("SimpleDateFormat") SimpleDateFormat df3 = new SimpleDateFormat("MMM dd yyyy");
                if(note.timestampend > 0) {
                    timestampEnd.setText(df3.format(datee));
                }
                else {
                    timestampEnd.setText("");
                    //note.timestampend = Long.MAX_VALUE;
                }
                /*if(note.timestampend - currentDate < 25920000) {
                    setBackGround();
                }*/


            }
            catch (Exception e) {

                e.printStackTrace();
            }

        }

        @Override
        public void onClick(View view) {

        }
    }
    public interface OnNoteListener{
        void onNoteClick();
    }
}
