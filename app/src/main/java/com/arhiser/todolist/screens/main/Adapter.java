package com.arhiser.todolist.screens.main;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.SortedList;

import com.arhiser.todolist.App;
import com.arhiser.todolist.R;
import com.arhiser.todolist.data.AppDatabase;
import com.arhiser.todolist.model.Note;
import com.arhiser.todolist.screens.details.NoteDetailsActivity;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class Adapter extends RecyclerView.Adapter<Adapter.NoteViewHolder> {

    /*public interface  DeleteAll {
        void DeleteAll(Note note);
    }*/

    public SortedList<Note> sortedList;
    //private DeleteAll deleteAll;

    public Adapter() {

        sortedList = new SortedList<>(Note.class, new SortedList.Callback<Note>() {


            @Override
            public int compare(Note o1, Note o2) {
                if (!o2.done && o1.done) {
                    return 1;
                }
                if (o2.done && !o1.done) {
                    return -1;
                }
                return (int) (o2.timestamp - o1.timestamp);
            }

            @Override
            public void onChanged(int position, int count) {
                notifyItemRangeChanged(position, count);
            }

            @Override
            public boolean areContentsTheSame(Note oldItem, Note newItem) {
                return oldItem.equals(newItem);
            }

            @Override
            public boolean areItemsTheSame(Note item1, Note item2) {
                return item1.uid == item2.uid;
            }

            @Override
            public void onInserted(int position, int count) {
                notifyItemRangeInserted(position, count);
            }

            @Override
            public void onRemoved(int position, int count) {
                notifyItemRangeRemoved(position, count);
            }

            @Override
            public void onMoved(int fromPosition, int toPosition) {
                notifyItemMoved(fromPosition, toPosition);
            }
        });
    }


    @NonNull
    @Override
    public NoteViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new NoteViewHolder(
                LayoutInflater.from(parent.getContext()).inflate(R.layout.item_note_list, parent, false));
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
        sortedList.replaceAll(notes);
    }

    public void submitList(List<Note> notes) {

    }

    static class NoteViewHolder extends RecyclerView.ViewHolder {

        TextView noteText;
        CheckBox completed;
        TextView noteGroup;
        View backGround;
        View leftTag;
        TextView timestampStart;
        TextView timestampEnd;
        //View delete;

        Note note;

        boolean silentUpdate;

        public NoteViewHolder(@NonNull final View itemView) {
            super(itemView);

            noteText = itemView.findViewById(R.id.note_text);
            completed = itemView.findViewById(R.id.completed);
            noteGroup = itemView.findViewById(R.id.note_help_text);
            backGround = itemView.findViewById(R.id.itemTemplateBackground);
            leftTag = itemView.findViewById(R.id.itemTemplateLeftTag);
            timestampStart = itemView.findViewById(R.id.noteStartDate);
            timestampEnd = itemView.findViewById(R.id.noteEndDate);
            //delete = itemView.findViewById(R.id.delete);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    NoteDetailsActivity.start((Activity) itemView.getContext(), note);
                }
            });

            /*delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    App.getInstance().getNoteDao().delete(note);
                }
            });*/

            completed.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton compoundButton, boolean checked) {
                    if (!silentUpdate) {
                        note.done = checked;
                        App.getInstance().getNoteDao().update(note);
                    }
                    updateStrokeOut();
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
                /*Date datee = new Date(note.timestampend);
            @SuppressLint("SimpleDateFormat") SimpleDateFormat df3 = new SimpleDateFormat("MMM dd yyyy");
            timestampStart.setText(df2.format(datee));*/
            }
            catch (Exception e) {
                e.printStackTrace();
            }

        }
    }
}
