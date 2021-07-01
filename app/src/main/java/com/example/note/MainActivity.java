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
import android.view.View;

import com.example.note.observe.Publisher;
import com.example.note.ui.about.AboutFragment;
import com.example.note.ui.add.AddFragment;
import com.example.note.ui.list.ListFragment;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;

public class MainActivity extends AppCompatActivity {
    private Navigation navigation;
    private final Publisher publisher = new Publisher();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        navigation = new Navigation(getSupportFragmentManager());

        initView();
        getNavigation().addFragment(new com.example.note.ui.list.ListFragment(), false);
    }

    private void initView() {
        Toolbar toolbar = initToolbar();
        initDrawer(toolbar);
        initFloatingBtn();
    }

    private Toolbar initToolbar() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

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

            if (navigateFragment(id)) {
                drawer.closeDrawer(GravityCompat.START);
                return true;
            }

            return false;
        });
    }

    private void initFloatingBtn() {
        findViewById(R.id.fab).setOnClickListener(v -> addFragment(new AddFragment()));
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

    @Override
    public void onBackPressed() {
        setVisibilityFloatingBtn(true);

        super.onBackPressed();
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

        // Если fragment добавления, то скрытвать кнопку floatingBtn для добавления нового элемента
        setVisibilityFloatingBtn(!(fragment instanceof AddFragment));

        // Закрыть транзакцию
        fragmentTransaction.commit();
    }


    private void setVisibilityFloatingBtn(boolean isVisibility) {
        FloatingActionButton floatingBtn = findViewById(R.id.fab);

        if (isVisibility) {
            if (floatingBtn.getVisibility() != View.VISIBLE) {
                floatingBtn.setVisibility(View.VISIBLE);
            }
        } else {
            floatingBtn.setVisibility(View.INVISIBLE);
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    public Navigation getNavigation() {
        return navigation;
    }

    public Publisher getPublisher() {
        return publisher;
    }
}