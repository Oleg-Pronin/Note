package com.example.note.data;

public interface NotesSource {
    NoteData getNoteData(int position);

    int size();
}
