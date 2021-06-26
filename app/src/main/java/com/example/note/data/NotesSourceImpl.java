package com.example.note.data;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class NotesSourceImpl implements NotesSource {
    private final List<NoteData> dataSource;

    public NotesSourceImpl() {
        dataSource = new ArrayList<>();

        for (int i = 0; i < 20; i++) {
            dataSource.add(
                    new NoteData(
                            "Название заметки №" + i,
                            "Краткое описание заметки №" + i,
                            new Date()
                    )
            );
        }
    }

    @Override
    public NoteData getNoteData(int position) {
        return dataSource.get(position);
    }

    @Override
    public int size() {
        return dataSource.size();
    }
}
