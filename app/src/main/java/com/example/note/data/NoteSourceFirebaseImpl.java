package com.example.note.data;

import android.util.Log;

import com.example.note.data.entity.NoteData;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class NoteSourceFirebaseImpl implements NotesSource {
    // TODO: Для отладки. УДАЛИТЬ
    private static final String TAG = "NoteSourceFirebaseImpl";

    private static final String NOTES_COLLECTION = "notes";
    // Получение базы данных
    private final FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();
    // Получение всех коллекции
    private final CollectionReference collection = firebaseFirestore.collection(NOTES_COLLECTION);
    private List<NoteData> notesData = new ArrayList<>();

    @Override
    public NotesSource init(NotesSourceResponse notesSourceResponse) {
        // Получить всю коллекцию, отсортировать по полю "Дата"
        collection
                .orderBy(NoteDataMapping.Fields.DATE, Query.Direction.DESCENDING)
                .get()
                .addOnCompleteListener(task -> {
                    // При удачном считывании данных загрузим список карточек
                    if (task.isSuccessful()) {
                        notesData = new ArrayList<>();

                        for (QueryDocumentSnapshot document : task.getResult()) {
                            String id = document.getId();
                            Map<String, Object> doc = document.getData();

                            notesData.add(NoteDataMapping.toNoteData(id, doc));
                        }

                        Log.d(TAG, "Success " + notesData.size() + " qnt");

                        notesSourceResponse.initialized(NoteSourceFirebaseImpl.this);
                    } else {
                        Log.d(TAG, "Get failed, task.exception = ", task.getException());
                    }
                })
                .addOnFailureListener(e -> Log.d(TAG, "Get failed, exception = ", e));

        return this;
    }

    @Override
    public NoteData getNoteData(int position) {
        return notesData.get(position);
    }

    @Override
    public int getSize() {
        if (notesData == null) {
            return 0;
        }

        return notesData.size();
    }

    @Override
    public void addNoteData(NoteData noteData) {
        collection
                .add(NoteDataMapping.toDocument(noteData))
                .addOnSuccessListener(documentReference -> noteData.setId(documentReference.getId()))
                .addOnFailureListener(e -> Log.d(TAG, "Failed add, exception = ", e));

        notesData.add(noteData);
    }

    @Override
    public void updateNoteData(int position, NoteData noteData) {
        collection
                .document(noteData.getId())
                .set(NoteDataMapping.toDocument(noteData))
                .addOnFailureListener(e -> Log.d(TAG, "Failed update, exception = ", e));

        notesData.set(position, noteData);
    }

    @Override
    public void deleteNoteData(int position) {
        collection
                .document(notesData.get(position).getId())
                .delete()
                .addOnFailureListener(e -> Log.d(TAG, "Failed delete, exception = ", e));

        notesData.remove(position);
    }

    @Override
    public void clearNoteData() {
        for (NoteData noteData : notesData) {
            collection
                    .document(noteData.getId())
                    .delete()
                    .addOnFailureListener(e -> Log.d(TAG, "Failed deleteAll, exception = ", e));
        }

        notesData = new ArrayList<>();
    }
}
