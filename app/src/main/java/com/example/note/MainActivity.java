package com.example.note;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;

import com.example.note.ui.about.AboutFragment;
import com.example.note.ui.add.AddFragment;
import com.example.note.ui.list.ListFragment;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;

import java.util.Objects;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initView();
        addFragment(new com.example.note.ui.list.ListFragment());
    }

    private void initView() {
        Toolbar toolbar = initToolbar();
        initDrawer(toolbar);
        initFloatingBtn();
    }

    private Toolbar initToolbar() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        return toolbar;
    }

    // регистрация drawer
    private void initDrawer(Toolbar toolbar) {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this,
                drawer,
                toolbar,
                R.string.navigation_drawer_open,
                R.string.navigation_drawer_close
        );

        drawer.addDrawerListener(toggle);
        toggle.syncState();

        // Обработка навигационного меню
        NavigationView navigationView = findViewById(R.id.nav_view);

        navigationView.setNavigationItemSelectedListener(item -> {
            int id = item.getItemId();

            if (navigateFragment(id)){
                drawer.closeDrawer(GravityCompat.START);
                return true;
            }

            return false;
        });
    }

    private void initFloatingBtn() {
        FloatingActionButton floatingBtn = findViewById(R.id.fab);

        floatingBtn.setOnClickListener(v -> addFragment(new AddFragment()));
    }

    private boolean navigateFragment(int id) {
        switch (id) {
            case R.id.nav_list:
                addFragment(new ListFragment());
                return true;
            case R.id.nav_about:
                addFragment(new AboutFragment());
                return true;
        }

        return false;
    }

    public void addFragment(Fragment fragment) {
        //Получить менеджер фрагментов
        FragmentManager fragmentManager = getSupportFragmentManager();
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