package com.example.note.data;

import com.example.note.data.entity.NoteData;

public interface NotesSource {
    NotesSource init(NotesSourceResponse notesSourceResponse);

    NoteData getNoteData(int position);

    int getSize();
    void addNoteData(NoteData noteData);
    void updateNoteData(int position, NoteData noteData);
    void deleteNoteData(int position);
    void clearNoteData();
}
