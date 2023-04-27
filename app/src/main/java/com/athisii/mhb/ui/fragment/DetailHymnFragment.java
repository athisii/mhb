package com.athisii.mhb.ui.fragment;

import static android.view.ViewGroup.LayoutParams.MATCH_PARENT;
import static android.view.ViewGroup.LayoutParams.WRAP_CONTENT;

import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.SwitchCompat;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.core.view.MenuProvider;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.athisii.mhb.App;
import com.athisii.mhb.MainActivity;
import com.athisii.mhb.R;
import com.athisii.mhb.databinding.FragmentDetailHymnBinding;
import com.athisii.mhb.entity.Hymn;
import com.athisii.mhb.entity.HymnVerse;
import com.athisii.mhb.entity.HymnVerseLine;
import com.athisii.mhb.ui.viewmodel.HomeHymnViewModelFactory;
import com.athisii.mhb.ui.viewmodel.HymnViewModel;

import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.SortedMap;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.ForkJoinTask;

public class DetailHymnFragment extends Fragment {
    private MainActivity parentActivity;
    private App application;
    private FragmentDetailHymnBinding binding;
    private SortedMap<HymnVerse, List<HymnVerseLine>> hymnContentSortedMap;
    private List<HymnVerseLine> chorus;
    private HymnViewModel viewModel;

    private Hymn hymn;
    private static final FrameLayout.LayoutParams cardViewLayoutParams;
    private static final FrameLayout.LayoutParams linearLayoutParams;
    private static final ViewGroup.MarginLayoutParams marginLayout;

    static {
        // layout params for CardView
        cardViewLayoutParams = new FrameLayout.LayoutParams(MATCH_PARENT, WRAP_CONTENT);
        cardViewLayoutParams.setMargins(20, 10, 20, 10);

        // layout params for CardView LinearLayout -- must use FrameLayout.LayoutParams
        linearLayoutParams = new FrameLayout.LayoutParams(WRAP_CONTENT, WRAP_CONTENT);
        linearLayoutParams.gravity = Gravity.CENTER;

        // for switch compat
        marginLayout = new ViewGroup.MarginLayoutParams(WRAP_CONTENT, WRAP_CONTENT);
        marginLayout.setMargins(20, 0, 0, 20);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        CountDownLatch countDownLatch = new CountDownLatch(1);
        binding = FragmentDetailHymnBinding.inflate(inflater);
        binding.setLifecycleOwner(getViewLifecycleOwner());
        hymn = DetailHymnFragmentArgs.fromBundle(requireArguments()).getHymn();

        parentActivity = (MainActivity) requireActivity();
        Objects.requireNonNull(parentActivity.getSupportActionBar()).setTitle(hymn.getHymnNumber() + "");
        binding.setLifecycleOwner(getViewLifecycleOwner());
        application = (App) parentActivity.getApplication();

        viewModel = new ViewModelProvider(this, new HomeHymnViewModelFactory(application)).get(HymnViewModel.class);

        //hides bottom  navigation
        parentActivity.getBinding().appBarMain.babMain.setVisibility(View.GONE);

        ForkJoinPool.commonPool().execute(() -> {
            hymnContentSortedMap = viewModel.getHymnContentById(hymn.getId());
            HymnVerse firstVerse = hymnContentSortedMap.firstKey();
            if (firstVerse.isChorus()) {
                chorus = hymnContentSortedMap.get(firstVerse);
                hymnContentSortedMap.remove(firstVerse);
            }
            countDownLatch.countDown();
        });
        try {
            countDownLatch.await();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        displayHymnContent();
        addToolbarMenuItems();
        return binding.getRoot();
    }

    private void displayHymnContent() {
        binding.linearLayout.removeAllViews();
        hymnContentSortedMap
                .forEach((hymnVerse, hymnVerseLines) -> {
                    createCardViewBox(hymnVerseLines, false);
                    if (chorus != null) {
                        createCardViewBox(chorus, true);
                    }
                });
        if (!binding.linearLayout.isInLayout()) {
            binding.linearLayout.requestLayout();
        }
    }

    private void createCardViewBox(List<HymnVerseLine> hymnVerseLines, boolean isChorus) {
        CardView cardView = new CardView(parentActivity);
        cardView.setLayoutParams(cardViewLayoutParams);
        cardView.setRadius(30);
        cardView.setElevation(10);
        LinearLayout cardViewLinearLayout = new LinearLayout(parentActivity);
        cardViewLinearLayout.setOrientation(LinearLayout.VERTICAL);
        cardViewLinearLayout.setLayoutParams(linearLayoutParams);
        cardViewLinearLayout.setPadding(0, 40, 0, 40);
        addHymnVerseLineOnCardViewLinearLayout(hymnVerseLines, cardViewLinearLayout, isChorus);
        cardView.addView(cardViewLinearLayout);
        binding.linearLayout.addView(cardView);
    }

    private void addHymnVerseLineOnCardViewLinearLayout(List<HymnVerseLine> hymnVerseLines, LinearLayout cardViewLinearLayout, boolean isChorus) {
        hymnVerseLines.sort(Comparator.comparingInt(HymnVerseLine::getSerialNumber));
        for (HymnVerseLine hymnVerseLine : hymnVerseLines) {
            TextView tv = new TextView(parentActivity);
            if (application.getSharedPreferences().getBoolean(App.IS_LANGUAGE_ENGLISH, false)) {
                tv.setText(hymnVerseLine.getEnglish());
            } else {
                tv.setText(hymnVerseLine.getMaola());
            }
            tv.setTextSize(application.getSharedPreferences().getInt(App.FONT_SIZE, 16));
            if (isChorus) {
                tv.setTypeface(null, Typeface.BOLD_ITALIC);
            }
            tv.setPadding(20, 5, 20, 5);
            cardViewLinearLayout.addView(tv);
        }
    }


    private void addToolbarMenuItems() {
        parentActivity.addMenuProvider(new MenuProvider() {
            @Override
            public void onCreateMenu(@NonNull Menu menu, @NonNull MenuInflater menuInflater) {
                menuInflater.inflate(R.menu.toolbar_detail_hymn_menu, menu);
                MenuItem langSwitcherMenuItem = menu.findItem(R.id.lang_switcher_menu_item);
                langSwitcherMenuItem.setActionView(getSwitchCompat());
                updateFavMenuItemColor();
            }

            @Override
            public boolean onMenuItemSelected(@NonNull MenuItem menuItem) {
                if (menuItem.getItemId() == R.id.fav_menu_item) {
                    favMenuItemAction();
                    return true;
                }
                // else other menu items
                return false;
            }
        }, getViewLifecycleOwner()); //life cycle aware
    }

    private void updateFavMenuItemColor() {
        Menu menu = parentActivity.getBinding().appBarMain.toolbar.getMenu();
        MenuItem favMenuItem = menu.findItem(R.id.fav_menu_item);
        if (favMenuItem != null && hymn != null) {
            if (hymn.isFavorite()) {
                favMenuItem.getIcon().setTint(ContextCompat.getColor(application, R.color.light_grey));
            } else {
                favMenuItem.getIcon().setTint(ContextCompat.getColor(application, R.color.suva_grey));
            }
        }
    }

    private void favMenuItemAction() {
        hymn.setFavorite(!hymn.isFavorite());
        CountDownLatch countDownLatch = new CountDownLatch(1);
        ForkJoinTask<Boolean> future = ForkJoinPool.commonPool().submit(() -> {
            boolean result = viewModel.updateHymn(hymn);
            countDownLatch.countDown();
            return result;
        });
        try {
            countDownLatch.await();
            if (!Boolean.TRUE.equals(future.get())) {
                Toast.makeText(application, "Something went wrong.", Toast.LENGTH_LONG).show();
            }
            updateFavMenuItemColor();
        } catch (InterruptedException | ExecutionException ex) {
            if (ex instanceof InterruptedException) {
                Thread.currentThread().interrupt();
                return;
            }
            Log.d("error", "****error: " + ex.getMessage());
        }
    }

    @NonNull
    private SwitchCompat getSwitchCompat() {
        SwitchCompat switchCompat = (SwitchCompat) LayoutInflater.from(parentActivity).inflate(R.layout.detail_hymn_lang_switch_compat, null);
        switchCompat.setChecked(!application.getSharedPreferences().getBoolean(App.IS_LANGUAGE_ENGLISH, false));
        switchCompat.setOnCheckedChangeListener((buttonView, isChecked) -> {
            application.getSharedPreferences().edit().putBoolean(App.IS_LANGUAGE_ENGLISH, !isChecked).apply();
            displayHymnContent();
        });
        return switchCompat;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        parentActivity.getBinding().appBarMain.babMain.setVisibility(View.VISIBLE);
    }
}