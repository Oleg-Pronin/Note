package com.example.note.fragment;

import android.content.res.Configuration;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.example.note.R;
import com.example.note.entity.Note;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ListNotesFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ListNotesFragment extends Fragment {

    private boolean isLandscape;
    private Note note;
    public static final String CURRENT_NOTE = "CurrentNote";
    HashMap<String, Note> noteList;

    public ListNotesFragment() {
        noteList = new HashMap<>();

        for (int i = 0; i < 20; i++) {
            noteList.put(
                    String.valueOf(i),
                    new Note(
                            "Название заметки №" + i,
                            "Краткое описание заметки №" + i,
                            new Date()
                    )
            );
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_list_notes, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initNoteList();

        isLandscape = getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE;

        // Если это не первое создание, то восстановим текущую позицию
        if (savedInstanceState != null) {
            // Восстановление текущей позиции.
            note = savedInstanceState.getParcelable(CURRENT_NOTE);
        } else {
            note = null;
        }

        // Если можно нарисовать рядом герб, то сделаем это
        if (isLandscape && note != null) {
            showDetailNote(note);
        }
    }

    private void initNoteList() {
        ScrollView scrollView = (ScrollView) getView();
        assert scrollView != null;

        LinearLayout linLayout = scrollView.findViewById(R.id.linLayout);
        LayoutInflater ltInflater = getLayoutInflater();

        SimpleDateFormat formatForDateNow = new SimpleDateFormat("dd.MM.yyyy H:m:s");

        noteList.forEach((s, note) -> {
            View item = ltInflater.inflate(R.layout.item, linLayout, false);
            item.setHapticFeedbackEnabled(true);

            TextView noteName = item.findViewById(R.id.listNoteTitle);
            noteName.setText(note.getName());

            TextView notePrev = item.findViewById(R.id.listNotePrev);
            notePrev.setText(note.getDescription());

            TextView noteDate = item.findViewById(R.id.listNoteDate);
            noteDate.setText(formatForDateNow.format(note.getCreateDate()));

            linLayout.addView(item);

            item.setOnClickListener(v -> showDetailNote(note));
        });
    }

    private void showDetailNote(Note note) {
        // Создаём новый фрагмент с текущей позицией для вывода герба
        DetailNoteFragment detail = DetailNoteFragment.newInstance(note);
        // Выполняем транзакцию по замене фрагмента
        FragmentManager fragmentManager = requireActivity().getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        if (isLandscape) {
            fragmentTransaction.replace(R.id.detailNotes, detail); // замена фрагмента
        } else {
            fragmentTransaction.replace(R.id.listNotes, detail); // замена фрагмента
        }

        fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
        fragmentTransaction.commit();
    }
}