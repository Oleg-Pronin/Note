package com.example.note.data;

public interface NotesSource {
    NoteData getNoteData(int position);

    int getSize();
    void addNoteData(NoteData noteData);
    void updateNoteData(int position, NoteData noteData);
    void deleteNoteData(int position);
    void clearNoteData();
}
