package com.example.note.data;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class NotesSourceImpl implements NotesSource {
    private final List<NoteData> dataSource;

    public NotesSourceImpl() {
        dataSource = new ArrayList<>();

        for (int i = 0; i < 20; i++) {
            addNoteData(new NoteData(
                    "Название заметки №" + i,
                    "Краткое описание заметки №" + i,
                    Calendar.getInstance().getTime()
            ));
        }
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
        dataSource.add(noteData);
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
