package com.example.notes;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class NoteDetailActivity extends AppCompatActivity {

    private EditText noteHeadingEditText;
    private EditText noteDetailsEditText;
    private TextView noteDateTextView;
    private Button saveNoteButton;
    private Button deleteNoteButton;
    private long noteId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note_detail);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(""); // Remove the default title

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        noteHeadingEditText = findViewById(R.id.noteHeadingEditText);
        noteDetailsEditText = findViewById(R.id.noteDetailsEditText);
        noteDateTextView = findViewById(R.id.noteDate);
        saveNoteButton = findViewById(R.id.saveNoteButton);
        deleteNoteButton = findViewById(R.id.deleteNoteButton);

        noteId = getIntent().getLongExtra("noteId", -1);

        if (noteId != -1) {
            loadNoteDetails(noteId);
        } else {
            deleteNoteButton.setVisibility(View.GONE);
        }

        saveNoteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveNote();
            }
        });

        deleteNoteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                deleteNote();
            }
        });
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    private void loadNoteDetails(long noteId) {
        DatabaseHelper databaseHelper = new DatabaseHelper(this);
        Note note = databaseHelper.getNoteById(noteId);

        if (note != null) {
            noteHeadingEditText.setText(note.getHeading());
            noteDetailsEditText.setText(note.getDetails());

            // Format the timestamp into a readable date
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault());
            String formattedDate = sdf.format(new Date(note.getTimestamp()));
            noteDateTextView.setText(formattedDate);
        }
    }

    private void saveNote() {
        String heading = noteHeadingEditText.getText().toString();
        String details = noteDetailsEditText.getText().toString();

        if (TextUtils.isEmpty(heading)) {
            Toast.makeText(this, "Please enter a heading", Toast.LENGTH_SHORT).show();
            return;
        }

        DatabaseHelper databaseHelper = new DatabaseHelper(this);

        if (noteId == -1) {
            long newNoteId = databaseHelper.insertNote(heading, details);
            if (newNoteId != -1) {
                Toast.makeText(this, "Note saved successfully", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Error saving note", Toast.LENGTH_SHORT).show();
            }
        } else {
            boolean updated = databaseHelper.updateNote(noteId, heading, details);
            if (updated) {
                Toast.makeText(this, "Note updated successfully", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Error updating note", Toast.LENGTH_SHORT).show();
            }
        }

        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    private void deleteNote() {
        if (noteId != -1) {
            DatabaseHelper databaseHelper = new DatabaseHelper(this);
            databaseHelper.deleteNoteById(noteId);
            Toast.makeText(this, "Note deleted", Toast.LENGTH_SHORT).show();
        }

        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
    }
}
