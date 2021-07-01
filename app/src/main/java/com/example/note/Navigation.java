package com.example.note;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.note.ui.add.AddFragment;
import com.example.note.ui.list.ListFragment;

public class Navigation {
    private final FragmentManager fragmentManager;

    public Navigation(FragmentManager fragmentManager){
        this.fragmentManager = fragmentManager;
    }

    public void addFragment(Fragment fragment, boolean useBackStack) {
        // Открыть транзакцию
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.fragment_container, fragment);

        if (!(fragment instanceof ListFragment)) {
            fragmentTransaction.addToBackStack(null);
        }

        // Закрыть транзакцию
        fragmentTransaction.commit();
    }
}
