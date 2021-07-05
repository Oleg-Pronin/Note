package com.example.note.data;

import com.example.note.data.entity.NoteData;
import com.google.firebase.Timestamp;

import java.util.HashMap;
import java.util.Map;

public class NoteDataMapping {

    public static class Fields {
        public final static String TITLE = "title";
        public final static String DATE = "date";
        public final static String DESCRIPTION = "description";
    }

    public static NoteData toNoteData(String id, Map<String, Object> doc) {
        Timestamp timestamp = (Timestamp) doc.get(Fields.DATE);

        NoteData answer = new NoteData(
                (String) doc.get(Fields.TITLE),
                (String) doc.get(Fields.DESCRIPTION),
                timestamp.toDate()
        );

        answer.setId(id);

        return answer;
    }

    public static Map<String, Object> toDocument(NoteData noteData) {
        Map<String, Object> answer = new HashMap<>();

        answer.put(Fields.TITLE, noteData.getTitle());
        answer.put(Fields.DESCRIPTION, noteData.getDescription());
        answer.put(Fields.DATE, noteData.getDate());

        return answer;
    }
}
