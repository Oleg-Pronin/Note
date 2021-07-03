package com.example.note.data;

import com.example.note.data.entity.NoteData;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class NotesSourceImpl implements NotesSource {
    private List<NoteData> dataSource;

    @Override
    public NotesSource init(NotesSourceResponse notesSourceResponse) {
        dataSource = new ArrayList<>();

        for (int i = 0; i < 5; i++) {
            addNoteData(new NoteData(
                    "Название заметки №" + i,
                    "Краткое описание заметки №" + i,
                    Calendar.getInstance().getTime()
            ));
        }

        if (notesSourceResponse != null) {
            notesSourceResponse.initialized(this);
        }

        return this;
    }

    @Override
    public NoteData getNoteData(int position) {
        return dataSource.get(position);
    }

    @Override
    public int getSize() {
        return dataSource.size();
    }

    @Override
    public void addNoteData(NoteData noteData) {
        dataSource.add(0, noteData);
    }

    @Override
    public void updateNoteData(int position, NoteData noteData) {
        dataSource.set(position, noteData);
    }

    @Override
    public void deleteNoteData(int position) {
        dataSource.remove(position);
    }

    @Override
    public void clearNoteData() {
        dataSource.clear();
    }
}
