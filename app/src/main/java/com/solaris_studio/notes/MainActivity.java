package com.solaris_studio.notes;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.View;
import android.widget.Toast;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private RecyclerView recyclerViewNotes;
    public static final ArrayList<Notes> notes = new ArrayList<>();
    private NotesDBHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        recyclerViewNotes = findViewById(R.id.recyclerViewNotes);
        dbHelper = new NotesDBHelper(this);
        SQLiteDatabase database = dbHelper.getWritableDatabase();
//        if (notes.isEmpty()) {
//            notes.add(new Notes("Парикмахер", "Сделать прическу", "Понедельник", 2));
//            notes.add(new Notes("Футбол", "Сыграть с командой во дворе", "Вторник", 3));
//            notes.add(new Notes("Магазин", "Купить новые джинсы", "Среда", 1));
//            notes.add(new Notes("Стоматолог", "Вылечить зубы", "Четверг", 2));
//            notes.add(new Notes("Баскетбол", "Сыграть со школьной командой", "Пятница", 1));
//        }
//        for (Notes notes : notes) {
//            ContentValues contentValues = new ContentValues();
//            contentValues.put(NotesContract.NotesEntry.COLUMN_TITLE, notes.getTitle());
//            contentValues.put(NotesContract.NotesEntry.COLUMN_DESCRIPTION, notes.getDescription());
//            contentValues.put(NotesContract.NotesEntry.COLUMN_DAY_OF_WEEK, notes.getDayOfWeek());
//            contentValues.put(NotesContract.NotesEntry.COLUMN_PRIORITY, notes.getPriority());
//            database.insert(NotesContract.NotesEntry.TABLE_NAME, null, contentValues);
//        }
        ArrayList<Notes> notesFromDB = new ArrayList<>();
        Cursor cursor = database.query(NotesContract.NotesEntry.TABLE_NAME, null, null,
                null, null, null, null);
         while (cursor.moveToNext()){
             String title = cursor.getString(cursor.getColumnIndex(NotesContract.NotesEntry.COLUMN_TITLE));
             String description = cursor.getString(cursor.getColumnIndex(NotesContract.NotesEntry.COLUMN_DESCRIPTION));
             String dayOfWeek = cursor.getString(cursor.getColumnIndex(NotesContract.NotesEntry.COLUMN_DAY_OF_WEEK));
             int priority = cursor.getInt(cursor.getColumnIndex(NotesContract.NotesEntry.COLUMN_PRIORITY));
             Notes note = new Notes(title, description, dayOfWeek, priority);
             notesFromDB.add(note);

         }
         cursor.close();
        NotesAdapter adapter = new NotesAdapter(notesFromDB);
        recyclerViewNotes.setLayoutManager(new LinearLayoutManager(this));
        recyclerViewNotes.setAdapter(adapter);
        adapter.setOnNoteClickListener(new NotesAdapter.OnNoteClickListener() {
            @Override
            public void onNoteClick(int position) {
            Toast.makeText(MainActivity.this, "clicked", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onLongClick(int position) {
                remove(position);
                adapter.notifyDataSetChanged();
            }
        });
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                remove(viewHolder.getAdapterPosition());
                adapter.notifyDataSetChanged();
            }
        });
        itemTouchHelper.attachToRecyclerView(recyclerViewNotes);
    }

    private void remove (int position){
        notes.remove(position);

    }

    public void onClickAddNotes(View view) {
        Intent intent = new Intent(this, addNodeActivity.class);
        startActivity(intent);
    }
}