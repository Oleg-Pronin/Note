package com.example.note.ui.list;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.example.note.R;
import com.example.note.data.entity.NoteData;
import com.example.note.data.NotesSource;

import java.text.SimpleDateFormat;

public class ListAdapter extends RecyclerView.Adapter<ListAdapter.ViewHolder> {
    private NotesSource dataSource;
    private final Fragment fragment;
    // Слушатель будет устанавливаться извне
    private OnItemClickListener itemClickListener;
    private int menuPosition;

    // Передаём в конструктор источник данных
    // Может быть и запрос к БД
    public ListAdapter(Fragment fragment) {

        this.fragment = fragment;
    }

    public void setDataSource(NotesSource dataSource) {
        this.dataSource = dataSource;
        // Сообщает об установке данных
        notifyDataSetChanged();
    }

    public int getMenuPosition() {
        return menuPosition;
    }

    @NonNull
    @Override
    public ListAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        // Создаём новый элемент пользовательского интерфейса
        // Через Inflater
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item, viewGroup, false);
        // Здесь можно установить всякие параметры
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ListAdapter.ViewHolder viewHolder, int position) {
        // Получить элемент из источника данных (БД, интернет...)
        // Вынести на экран, используя ViewHolder

        viewHolder.setData(dataSource.getNoteData(position));
    }

    @Override
    public int getItemCount() {
        return dataSource.getSize();
    }

    public void setOnItemClickListener(OnItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }

    // Интерфейс для обработки нажатий как в ListView
    public interface OnItemClickListener {
        void onItemClick(View view , int position);
    }

    // Этот класс хранит связь между данными и элементами View
    // Сложные данные могут потребовать несколько View на
    // один пункт списка
    public class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView title;
        private final TextView description;
        private final TextView date;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.listNoteTitle);
            date = itemView.findViewById(R.id.listNoteDate);
            description = itemView.findViewById(R.id.listNoteDescription);

            registerContextMenu(itemView);

            itemView.setOnClickListener(v -> {
                if (itemClickListener != null) {
                    itemClickListener.onItemClick(v, getAdapterPosition());
                }
            });
        }

        private void registerContextMenu(@NonNull View itemView) {
            if (fragment != null){
                itemView.setOnLongClickListener(v -> {
                    menuPosition = getLayoutPosition();
                    return false;
                });

                fragment.registerForContextMenu(itemView);
            }
        }

        public void setData(NoteData noteData){
            title.setText(noteData.getTitle());
            date.setText(new SimpleDateFormat("dd.MM.yy").format(noteData.getDate()));
            description.setText(noteData.getDescription());
        }
    }
}
