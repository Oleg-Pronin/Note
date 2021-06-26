package com.example.note.ui.list;

import android.annotation.SuppressLint;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.HapticFeedbackConstants;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.note.R;
import com.example.note.data.NoteData;
import com.example.note.data.NotesSource;
import com.example.note.data.NotesSourceImpl;
import com.example.note.ui.detail.DetailFragment;

/**
 * A simple {@link Fragment} subclass.
 */
public class ListFragment extends Fragment {
    @Override
    public View onCreateView(
            LayoutInflater inflater,
            ViewGroup container,
            Bundle savedInstanceState
    ) {
        View view = inflater.inflate(R.layout.fragment_list, container, false);

        initNoteList(view);

        return view;
    }

    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    private void initNoteList(View listView) {
        RecyclerView recyclerView = listView.findViewById(R.id.recycler_view_lines);
        // Получим источник данных для списка
        NotesSource source = new NotesSourceImpl();

        // Эта установка служит для повышения производительности системы
        // Указывает, что элементы одинаковые по размеру
        recyclerView.setHasFixedSize(true);

        // Будем работать со встроенным менеджером
        LinearLayoutManager layoutManager = new LinearLayoutManager(requireContext());
        recyclerView.setLayoutManager(layoutManager);

        // Установим адаптер
        final ListAdapter adapter = new ListAdapter(source);
        recyclerView.setAdapter(adapter);

        // Добавим разделитель карточек
        DividerItemDecoration itemDecoration = new DividerItemDecoration(requireContext(), LinearLayoutManager.VERTICAL);
        itemDecoration.setDrawable(getResources().getDrawable(R.drawable.separator, null));
        recyclerView.addItemDecoration(itemDecoration);

        adapter.setOnItemClickListener((view, position) -> {
            view.performHapticFeedback(HapticFeedbackConstants.VIRTUAL_KEY);
            showDetailNote(source.getNoteData(position));
        });
    }

    private void showDetailNote(NoteData noteData) {
        DetailFragment detail = DetailFragment.newInstance(noteData);
        FragmentManager fragmentManager = requireActivity().getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        fragmentTransaction.addToBackStack(null);

        fragmentTransaction.replace(R.id.fragment_container, detail);

        fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
        fragmentTransaction.commit();
    }
}