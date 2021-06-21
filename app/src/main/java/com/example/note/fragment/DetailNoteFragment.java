package com.example.note.fragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.os.Parcelable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.note.R;
import com.example.note.entity.Note;

import java.text.SimpleDateFormat;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link DetailNoteFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class DetailNoteFragment extends Fragment {
    public static final String ARG_NOTE = "Note";
    private Note note;

    public static DetailNoteFragment newInstance(Note note) {
        DetailNoteFragment fragment = new DetailNoteFragment();

        Bundle args = new Bundle();
        args.putParcelable(ARG_NOTE, note);

        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            note = getArguments().getParcelable(ARG_NOTE);
        }
    }

    @Override
    public View onCreateView(
            LayoutInflater inflater,
            ViewGroup container,
            Bundle savedInstanceState
    ) {
        View view = inflater.inflate(
                R.layout.fragment_detail_note,
                container,
                false
        );

        SimpleDateFormat formatForDateNow = new SimpleDateFormat("dd.MM.yyyy H:m:s");

        TextView noteName = view.findViewById(R.id.detailNoteTitle);
        noteName.setText(note.getName());

        TextView notePrev = view.findViewById(R.id.detailNotePrev);
        notePrev.setText(note.getDescription());

        TextView noteDate = view.findViewById(R.id.detailNoteDate);
        noteDate.setText(formatForDateNow.format(note.getCreateDate()));

        return view;
    }
}