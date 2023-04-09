package com.athisii.mhb;

import android.os.Bundle;
import android.util.Log;
import android.view.Menu;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.athisii.mhb.databinding.ActivityMainBinding;
import com.athisii.mhb.entity.Hymn;

import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.ForkJoinPool;


public class MainActivity extends AppCompatActivity {
    private ActivityMainBinding binding;
    private AppBarConfiguration appBarConfiguration;
    private NavController navController;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setSupportActionBar(binding.appBarMain.toolbar);
        navController = ((NavHostFragment) Objects.requireNonNull(getSupportFragmentManager().findFragmentById(R.id.nav_host))).getNavController();
        Set<Integer> topLevelDestinationIds = Set.of(R.id.home_hymn_fragment, R.id.favourite_hymn_fragment, R.id.compose_song_fragment, R.id.home_bible_fragment, R.id.bookmark_bible_fragment);

        appBarConfiguration = new AppBarConfiguration.Builder(topLevelDestinationIds)
                .setOpenableLayout(binding.drawer)
                .build();

        NavigationUI.setupWithNavController(binding.drawerNavView, navController);
        NavigationUI.setupWithNavController(binding.appBarMain.bnvMain, navController);
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);

        binding.appBarMain.switcher.setOnCheckedChangeListener((buttonView, isChecked) -> switchMode(isChecked));
        addBottomNavigationListener();

        App application = (App) getApplication();
        ForkJoinPool.commonPool().execute(() -> {
            List<Hymn> hymns = application.getRepository().getAllHymns();
            Log.d("info", "*************** Hymns ******************\n" + hymns);
        });
    }


    @Override
    public boolean onSupportNavigateUp() {
        return NavigationUI.navigateUp(navController, appBarConfiguration) || super.onSupportNavigateUp();
    }

    private void addBottomNavigationListener() {
        binding.appBarMain.bnvMain.setOnItemSelectedListener(item -> {
            if (item.getItemId() != 0) {
                navController.popBackStack();
                navController.navigate(item.getItemId());
            }
            return true;
        });

        binding.appBarMain.bnvMain.setOnItemReselectedListener(item -> {
            //Do nothing.
        });
    }

    private void switchMode(boolean isChecked) {
        navController.popBackStack();
        Menu bottomNavMenu = binding.appBarMain.bnvMain.getMenu();
        bottomNavMenu.clear();
        if (isChecked) {
            bottomNavMenu.add(Menu.NONE, R.id.home_hymn_fragment, 1, "Hymns").setChecked(true).setIcon(R.drawable.ic_home);
            bottomNavMenu.add(Menu.NONE, R.id.favourite_hymn_fragment, 2, "Favourites").setIcon(R.drawable.ic_favourite);
            bottomNavMenu.add(Menu.NONE, R.id.compose_song_fragment, 3, "Compose").setIcon(R.drawable.ic_compose);
            navController.navigate(R.id.home_hymn_fragment);
        } else {
            bottomNavMenu.add(Menu.NONE, R.id.home_bible_fragment, 1, "Bible").setChecked(true).setIcon(R.drawable.ic_home);
            bottomNavMenu.add(Menu.NONE, R.id.bookmark_bible_fragment, 2, "Bookmark").setIcon(R.drawable.ic_bookmark);
            navController.navigate(R.id.home_bible_fragment);
        }
    }


}