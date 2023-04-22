package com.athisii.mhb.ui.fragment;

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
import com.athisii.mhb.databinding.FragmentDetailHymnBinding;
import com.athisii.mhb.entity.HymnVerse;
import com.athisii.mhb.entity.HymnVerseLine;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ForkJoinPool;

public class DetailHymnFragment extends Fragment {
    private AppCompatActivity parentActivity;
    private App application;
    private FragmentDetailHymnBinding binding;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentDetailHymnBinding.inflate(inflater);
        binding.setLifecycleOwner(getViewLifecycleOwner());


        long hymnId = DetailHymnFragmentArgs.fromBundle(requireArguments()).getId();
        long hymnNumber = DetailHymnFragmentArgs.fromBundle(requireArguments()).getHymnNumber();

        parentActivity = (AppCompatActivity) requireActivity();
        Objects.requireNonNull(parentActivity.getSupportActionBar()).setTitle(hymnNumber + "");
        binding.setLifecycleOwner(getViewLifecycleOwner());
        application = (App) parentActivity.getApplication();
        displayHymnContent(hymnId);

        return binding.getRoot();
    }

    private void displayHymnContent(long hymnId) {
        // layout params for CardView
        FrameLayout.LayoutParams cardViewLayoutParams = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        cardViewLayoutParams.setMargins(10, 15, 10, 15);

        // layout params for CardView LinearLayout
        LinearLayout.LayoutParams linearLayoutParams = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
        );

        // fetches data using worker thread
        ForkJoinPool.commonPool().execute(() -> {
            Map<HymnVerse, List<HymnVerseLine>> hymnContentMap = application.getRepository().getHymnContentById(hymnId);
            // update UI using main thread
            application.getHandler().post(() -> hymnContentMap.keySet()
                    .stream()
                    .sorted(Comparator.comparingInt(HymnVerse::getVerseNumber))
                    .forEach(hymnVerse -> {
                        if (!hymnVerse.isChorus()) {
                            CardView cardView = new CardView(parentActivity);
                            cardView.setLayoutParams(cardViewLayoutParams);
                            cardView.setRadius(20);
                            LinearLayout cardViewLinearLayout = new LinearLayout(parentActivity);
                            cardViewLinearLayout.setLayoutParams(linearLayoutParams);
                            cardViewLinearLayout.setOrientation(LinearLayout.VERTICAL);
                            setHymnVerseLine(Objects.requireNonNull(hymnContentMap.get(hymnVerse)), cardViewLinearLayout);
                            cardView.addView(cardViewLinearLayout);
                            binding.linearLayout.addView(cardView);
                        }
                    }));
        });
    }

    private void setHymnVerseLine(List<HymnVerseLine> hymnVerseLines, LinearLayout cardViewLinearLayout) {
        for (HymnVerseLine hymnVerseLine : hymnVerseLines) {
            TextView tv = new TextView(parentActivity);
            if (application.getSharedPreferences().getBoolean(App.IS_SONG_LANG_ENGLISH, false)) {
                tv.setText(hymnVerseLine.getEnglish());
            } else {
                tv.setText(hymnVerseLine.getMaola());
            }
            tv.setGravity(Gravity.CENTER_HORIZONTAL);
            tv.setPadding(20, 5, 20, 5);
            cardViewLinearLayout.addView(tv);
        }

    }
}