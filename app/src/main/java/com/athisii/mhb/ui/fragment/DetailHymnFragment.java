package com.athisii.mhb.ui.fragment;

import static android.view.ViewGroup.LayoutParams.MATCH_PARENT;
import static android.view.ViewGroup.LayoutParams.WRAP_CONTENT;

import android.graphics.Typeface;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import com.athisii.mhb.App;
import com.athisii.mhb.R;
import com.athisii.mhb.databinding.FragmentDetailHymnBinding;
import com.athisii.mhb.entity.HymnVerse;
import com.athisii.mhb.entity.HymnVerseLine;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ForkJoinPool;

public class DetailHymnFragment extends Fragment {
    private AppCompatActivity parentActivity;
    private App application;
    private FragmentDetailHymnBinding binding;
    private Map<HymnVerse, List<HymnVerseLine>> hymnContentMap;
    private List<HymnVerseLine> chorus;
    private static final FrameLayout.LayoutParams cardViewLayoutParams;
    private static final FrameLayout.LayoutParams linearLayoutParams;

    static {
        // layout params for CardView
        cardViewLayoutParams = new FrameLayout.LayoutParams(MATCH_PARENT, WRAP_CONTENT);
        cardViewLayoutParams.setMargins(20, 10, 20, 10);

        // layout params for CardView LinearLayout -- must use FrameLayout.LayoutParams
        linearLayoutParams = new FrameLayout.LayoutParams(WRAP_CONTENT, WRAP_CONTENT);
        linearLayoutParams.gravity = Gravity.CENTER;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        CountDownLatch countDownLatch = new CountDownLatch(1);
        binding = FragmentDetailHymnBinding.inflate(inflater);
        binding.setLifecycleOwner(getViewLifecycleOwner());
        long hymnId = DetailHymnFragmentArgs.fromBundle(requireArguments()).getId();
        int hymnNumber = DetailHymnFragmentArgs.fromBundle(requireArguments()).getHymnNumber();
        parentActivity = (AppCompatActivity) requireActivity();
        Objects.requireNonNull(parentActivity.getSupportActionBar()).setTitle(hymnNumber + "");
        binding.setLifecycleOwner(getViewLifecycleOwner());
        application = (App) parentActivity.getApplication();
        // for keeping track of last visited page in HomeHymn List
        application.setCurrentHymnNumber(hymnNumber);
        ForkJoinPool.commonPool().execute(() -> {
            hymnContentMap = application.getRepository().getHymnContentById(hymnId);
            hymnContentMap.keySet().stream().filter(HymnVerse::isChorus).findFirst().ifPresent(hymnVerse -> chorus = hymnContentMap.get(hymnVerse));
            countDownLatch.countDown();
        });
        try {
            countDownLatch.await();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        displayHymnContent();
        return binding.getRoot();
    }

    private void displayHymnContent() {
        binding.linearLayout.removeAllViews();
        hymnContentMap.keySet()
                .stream()
                .filter(hymnVerse -> !hymnVerse.isChorus())
                .sorted(Comparator.comparingInt(HymnVerse::getVerseNumber))
                .forEach(hymnVerse -> {
                    createCardViewBox(hymnContentMap.get(hymnVerse), false);
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
        cardViewLinearLayout.setPadding(0,40,0,40);
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
}