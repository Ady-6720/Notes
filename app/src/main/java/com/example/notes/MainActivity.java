package com.example.notes;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import java.util.Collections;
import java.util.List;

public class MainActivity extends AppCompatActivity implements NoteAdapter.OnNoteClickListener {

    private ListView noteListView;
    private Button newNoteButton;
    private NoteAdapter adapter;
    private DatabaseHelper databaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        noteListView = findViewById(R.id.noteListView);
        newNoteButton = findViewById(R.id.newNoteButton);

        // Initialize DatabaseHelper and fetch notes from SQLite database
        databaseHelper = new DatabaseHelper(this);
        refreshNotesList();

        // Set click listener for creating a new note
        newNoteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, NoteDetailActivity.class);
                startActivity(intent);
            }
        });

        // Set long click listener for deleting a note
        noteListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int position, long id) {
                showDeleteDialog(position);
                return true;
            }
        });
    }

    private void refreshNotesList() {
        List<Note> notes = fetchNotesFromDatabase(databaseHelper);
        // Sort notes with the newest on top
        Collections.sort(notes, (note1, note2) -> Long.compare(note2.getTimestamp(), note1.getTimestamp()));
        adapter = new NoteAdapter(this, notes, this);
        noteListView.setAdapter(adapter);
    }

    private List<Note> fetchNotesFromDatabase(DatabaseHelper databaseHelper) {
        return databaseHelper.getAllNotes();
    }

    private void showDeleteDialog(final int position) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Are you sure you want to delete this note?")
                .setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Note noteToDelete = adapter.getItem(position);
                        deleteNoteFromDatabase(noteToDelete.getId());
                        refreshNotesList();
                        showToast("Note deleted");
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        // User canceled the operation
                    }
                })
                .show();
    }

    private void deleteNoteFromDatabase(long noteId) {
        databaseHelper.deleteNoteById(noteId);
    }

    private void showToast(String message) {
        // Display a toast message
    }

    @Override
    public void onNoteClick(Note note) {
        // Handle note click (open NoteDetailActivity for editing)
        Intent intent = new Intent(MainActivity.this, NoteDetailActivity.class);
        intent.putExtra("noteId", note.getId());
        startActivity(intent);
    }

    @Override
    protected void onResume() {
        super.onResume();
        refreshNotesList(); // Refresh the list when returning to this activity
    }
}
