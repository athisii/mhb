package com.athisii.mhb.ui.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.athisii.mhb.App;
import com.athisii.mhb.databinding.FragmentDetailHymnBinding;
import com.athisii.mhb.entity.HymnVerse;
import com.athisii.mhb.entity.HymnVerseLine;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ForkJoinPool;

public class DetailHymnFragment extends Fragment {

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        var binding = FragmentDetailHymnBinding.inflate(inflater);

        long hymnId = DetailHymnFragmentArgs.fromBundle(requireArguments()).getId();
        long hymnNumber = DetailHymnFragmentArgs.fromBundle(requireArguments()).getHymnNumber();
        Objects.requireNonNull(((AppCompatActivity) requireActivity()).getSupportActionBar()).setTitle(hymnNumber + "");
        binding.setLifecycleOwner(getViewLifecycleOwner());
        App application = (App) requireActivity().getApplication();
        ForkJoinPool.commonPool().execute(() -> {
            Map<HymnVerse, List<HymnVerseLine>> hymnContentMap = application.getRepository().getHymnContentById(hymnId);
            application.getHandler().post(() ->
                    binding.hymnData.setText(hymnContentMap.values().stream().findFirst().get().get(0).getMaola()));
        });
        binding.setLifecycleOwner(getViewLifecycleOwner());
        return binding.getRoot();
    }
}