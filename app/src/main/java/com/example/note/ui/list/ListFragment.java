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
import com.example.note.data.NoteSourceFirebaseImpl;
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

    private boolean isMoveToFirstPosition;

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
    public View onCreateView(
            LayoutInflater inflater,
            ViewGroup container,
            Bundle savedInstanceState
    ) {
        View view = inflater.inflate(R.layout.fragment_list, container, false);

        initFloatingBtn(view);
        initNoteList(view);
        setHasOptionsMenu(true);

        source = new NoteSourceFirebaseImpl().init(notesSource -> {
            adapter.notifyDataSetChanged();
        });

        adapter.setDataSource(source);

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
        adapter = new ListAdapter(this);
        recyclerView.setAdapter(adapter);

        if (isMoveToFirstPosition && source.getSize() > 0) {
            recyclerView.scrollToPosition(0);
            isMoveToFirstPosition = false;
        }

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
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        return onItemSelected(item.getItemId()) || super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onContextItemSelected(@NonNull MenuItem item) {
        return onItemSelected(item.getItemId()) || super.onContextItemSelected(item);
    }

    private boolean onItemSelected(int menuItemId) {
        switch (menuItemId) {
            case R.id.action_card_menu_item_update:
                final int updatePosition = adapter.getMenuPosition();

                navigation.addFragment(
                        NoteFragment.newInstance(source.getNoteData(updatePosition)),
                        true
                );

                publisher.subscribe(noteData -> {
                    source.updateNoteData(updatePosition, noteData);
                    adapter.notifyItemChanged(updatePosition);
                });

                return true;
            case R.id.action_card_menu_item_delete:
                final int deletePosition = adapter.getMenuPosition();

                source.deleteNoteData(deletePosition);
                adapter.notifyItemRemoved(deletePosition);

                return true;
            case R.id.action_main_menu_clear_all:
                source.clearNoteData();
                adapter.notifyDataSetChanged();

                return true;
        }

        return false;
    }
}