package com.example.note.data;

public interface NotesSource {
    NotesSource init(NotesSourceResponse notesSourceResponse);

    NoteData getNoteData(int position);

    int getSize();
    void addNoteData(NoteData noteData);
    void updateNoteData(int position, NoteData noteData);
    void deleteNoteData(int position);
    void clearNoteData();
}
