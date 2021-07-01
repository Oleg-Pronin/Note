package com.example.note.ui.list;

import android.annotation.SuppressLint;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
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
import android.widget.Toast;

import com.example.note.R;
import com.example.note.data.NoteData;
import com.example.note.data.NotesSource;
import com.example.note.data.NotesSourceImpl;
import com.example.note.ui.detail.DetailFragment;

/**
 * A simple {@link Fragment} subclass.
 */
public class ListFragment extends Fragment {
    NotesSource source;
    ListAdapter adapter;

    @Override
    public View onCreateView(
            LayoutInflater inflater,
            ViewGroup container,
            Bundle savedInstanceState
    ) {
        View view = inflater.inflate(R.layout.fragment_list, container, false);

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
        // Получим источник данных для списка
        source = new NotesSourceImpl();

        // Эта установка служит для повышения производительности системы
        // Указывает, что элементы одинаковые по размеру
        recyclerView.setHasFixedSize(true);

        // Будем работать со встроенным менеджером
        LinearLayoutManager layoutManager = new LinearLayoutManager(requireContext());
        recyclerView.setLayoutManager(layoutManager);

        // Установим адаптер
        adapter = new ListAdapter(source, this);
        recyclerView.setAdapter(adapter);

        // Добавим разделитель карточек
//        DividerItemDecoration itemDecoration = new DividerItemDecoration(requireContext(), LinearLayoutManager.VERTICAL);
//        itemDecoration.setDrawable(getResources().getDrawable(R.drawable.separator, null));
//        recyclerView.addItemDecoration(itemDecoration);

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

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.activity_main_menu, menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.action_clear:
                source.clearNoteData();
                adapter.notifyDataSetChanged();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onCreateContextMenu(@NonNull ContextMenu menu,
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
        switch(item.getItemId()) {
            case R.id.card_menu_item_update:
                // Do some stuff
                Toast.makeText(getContext(), "Обновить №" + position, Toast.LENGTH_SHORT).show();

                adapter.notifyItemChanged(position);
                return true;
            case R.id.card_menu_item_delete:
                source.deleteNoteData(position);
                adapter.notifyItemRemoved(position);

                Toast.makeText(getContext(), "Удалить №" + position, Toast.LENGTH_SHORT).show();
                return true;
        }

        return super.onContextItemSelected(item);
    }
}