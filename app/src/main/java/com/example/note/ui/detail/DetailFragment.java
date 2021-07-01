package com.example.note.ui.detail;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.note.R;
import com.example.note.data.NoteData;

import java.text.SimpleDateFormat;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link DetailFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class DetailFragment extends Fragment {
    public static final String ARG_NOTE = "Note";
    private NoteData noteData;

    public static DetailFragment newInstance(NoteData noteData) {
        DetailFragment fragment = new DetailFragment();

        Bundle args = new Bundle();
        args.putParcelable(ARG_NOTE, noteData);

        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            noteData = getArguments().getParcelable(ARG_NOTE);
        }
    }

    @Override
    public View onCreateView(
            LayoutInflater inflater,
            ViewGroup container,
            Bundle savedInstanceState
    ) {
        View view = inflater.inflate(
                R.layout.fragment_detail,
                container,
                false
        );

        if (noteData == null) {
            return view;
        }

        SimpleDateFormat formatForDateNow = new SimpleDateFormat("dd.MM.yyyy H:m:s");

        TextView noteName = view.findViewById(R.id.detailTitle);
        noteName.setText(noteData.getTitle());

        TextView notePrev = view.findViewById(R.id.detailDescription);
        notePrev.setText(noteData.getDescription());

        TextView noteDate = view.findViewById(R.id.detailDate);
        noteDate.setText(formatForDateNow.format(noteData.getDate()));

        return view;
    }
}