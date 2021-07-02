package com.example.note.ui.list;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.ContextMenu;
import android.view.HapticFeedbackConstants;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.example.note.MainActivity;
import com.example.note.Navigation;
import com.example.note.R;
import com.example.note.data.NotesSource;
import com.example.note.data.NotesSourceImpl;
import com.example.note.observe.Publisher;
import com.example.note.ui.add.NoteFragment;
import com.example.note.ui.detail.DetailFragment;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

/**
 * A simple {@link Fragment} subclass.
 */
public class ListFragment extends Fragment {
    private NotesSource source;
    private ListAdapter adapter;
    private Navigation navigation;
    private Publisher publisher;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        MainActivity activity = (MainActivity) context;
        navigation = activity.getNavigation();
        publisher = activity.getPublisher();
    }

    @Override
    public void onDetach() {
        navigation = null;
        publisher = null;
        super.onDetach();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        /*
         * Получим источник данных для списка
         * Поскольку onCreateView запускается каждый раз
         * при возврате в фрагмент, данные надо создавать один раз
         * */
        source = new NotesSourceImpl();
    }

    @Override
    public View onCreateView(
            LayoutInflater inflater,
            ViewGroup container,
            Bundle savedInstanceState
    ) {
        View view = inflater.inflate(R.layout.fragment_list, container, false);

        initFloatingBtn(view);
        initNoteList(view);
        setHasOptionsMenu(true);

        return view;
    }

    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    private void initNoteList(View listView) {
        RecyclerView recyclerView = listView.findViewById(R.id.recycler_view_lines);

        // Эта установка служит для повышения производительности системы
        // Указывает, что элементы одинаковые по размеру
        recyclerView.setHasFixedSize(true);

        // Будем работать со встроенным менеджером
        LinearLayoutManager layoutManager = new LinearLayoutManager(requireContext());
        recyclerView.setLayoutManager(layoutManager);

        // Установим адаптер
        adapter = new ListAdapter(source, this);
        recyclerView.setAdapter(adapter);

        adapter.setOnItemClickListener((view, position) -> {
            view.performHapticFeedback(HapticFeedbackConstants.VIRTUAL_KEY);
            navigation.addFragment(
                    DetailFragment.newInstance(source.getNoteData(position)),
                    true
            );
        });
    }

    private void initFloatingBtn(View view) {
        FloatingActionButton floatingBtn = view.findViewById(R.id.fab);

        if (floatingBtn.getVisibility() == View.INVISIBLE) {
            floatingBtn.setVisibility(View.VISIBLE);
        }

        floatingBtn.setOnClickListener(v -> onClickAddNote());
    }

    private void onClickAddNote() {
        navigation.addFragment(new NoteFragment(), true);

        publisher.subscribe(noteData -> {
            source.addNoteData(noteData);
            adapter.notifyItemInserted(source.getSize() - 1);
        });
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.activity_main_menu, menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.action_clear) {
            source.clearNoteData();
            adapter.notifyDataSetChanged();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onCreateContextMenu(
        @NonNull ContextMenu menu,
        @NonNull View v,
        @Nullable ContextMenu.ContextMenuInfo menuInfo
    ) {
        super.onCreateContextMenu(menu, v, menuInfo);
        MenuInflater inflater = requireActivity().getMenuInflater();

        inflater.inflate(R.menu.card_menu, menu);
    }

    @Override
    public boolean onContextItemSelected(@NonNull MenuItem item) {
        int position = adapter.getMenuPosition();

        switch (item.getItemId()) {
            case R.id.card_menu_item_update:
                navigation.addFragment(
                        NoteFragment.newInstance(source.getNoteData(position)),
                        true
                );

                publisher.subscribe(noteData -> {
                    source.updateNoteData(position, noteData);
                    adapter.notifyItemChanged(position);
                });

                return true;
            case R.id.card_menu_item_delete:
                source.deleteNoteData(position);
                adapter.notifyItemRemoved(position);

                return true;
        }

        return super.onContextItemSelected(item);
    }
}