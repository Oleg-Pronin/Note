package com.example.note.data;

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;

public class NoteSourceFirebaseImpl implements NotesSource {
    private FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();
    private static final String NOTES_COLLECTION = "notes";
    private CollectionReference collection = firebaseFirestore.collection(NOTES_COLLECTION);

    private List<NoteData> noteData = new ArrayList<>();

    @Override
    public NotesSource init(NotesSourceResponse notesSourceResponse) {
        return null;
    }

    @Override
    public NoteData getNoteData(int position) {
        return noteData.get(position);
    }

    @Override
    public int getSize() {
        if (noteData == null) {
            return 0;
        }

        return noteData.size();
    }

    @Override
    public void addNoteData(NoteData noteData) {

    }

    @Override
    public void updateNoteData(int position, NoteData noteData) {
        String id = noteData.getId();
        collection.document(id).set(noteData);
    }

    @Override
    public void deleteNoteData(int position) {
        collection.document(noteData.get(position).getId()).delete();
        noteData.remove(position);
    }

    @Override
    public void clearNoteData() {

    }
}
