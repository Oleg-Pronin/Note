package com.example.note.ui.bottomSheet;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;

import com.example.note.R;
import com.example.note.ui.bottomSheet.interfaces.OnDeleteDialogListener;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

public class DeleteBottomSheetFragment extends BottomSheetDialogFragment {
    protected OnDeleteDialogListener dialogListener;

    // Установим слушатель диалога
    public void setOnDialogListener(OnDeleteDialogListener deleteDialogListener) {
        this.dialogListener = deleteDialogListener;
    }

    public static DeleteBottomSheetFragment newInstance() {
        return new DeleteBottomSheetFragment();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_delete_dialog, container, false);
        // Запретим пользователю выходить без выбора
        setCancelable(true);

        view.findViewById(R.id.btnDeleteYes).setOnClickListener(view1 -> {
            dismiss();

            if (dialogListener != null)
                dialogListener.onDialogYes();
        });

        view.findViewById(R.id.btnDeleteNo).setOnClickListener(view12 -> {
            dismiss();

            if (dialogListener != null)
                dialogListener.onDialogNo();
        });

        return view;
    }
}
