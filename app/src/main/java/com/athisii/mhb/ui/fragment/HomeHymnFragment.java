package com.athisii.mhb.ui.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import com.athisii.mhb.App;
import com.athisii.mhb.databinding.FragmentHomeHymnBinding;
import com.athisii.mhb.ui.adapter.HymnPagingDataAdapter;
import com.athisii.mhb.ui.viewmodel.HomeHymnViewModel;
import com.athisii.mhb.ui.viewmodel.HomeHymnViewModelFactory;

import io.reactivex.rxjava3.disposables.CompositeDisposable;

public class HomeHymnFragment extends Fragment {
    private HomeHymnViewModel viewModel;
    private HymnPagingDataAdapter hymnPagingDataAdapter;
    private final CompositeDisposable disposables = new CompositeDisposable();

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        var binding = FragmentHomeHymnBinding.inflate(inflater);
        var application = (App) requireActivity().getApplication();
        viewModel = new ViewModelProvider(this, new HomeHymnViewModelFactory(application)).get(HomeHymnViewModel.class);
        hymnPagingDataAdapter = new HymnPagingDataAdapter(hymn -> {
            if (hymn != null) {
                Navigation.findNavController(requireView()).navigate(HomeHymnFragmentDirections.actionHomeHymnFragmentToDetailHymnFragment(hymn.getId(), hymn.getHymnNumber()));
            }
        });

        disposables.add(viewModel.getPagingDataFlow().subscribe(hymnPagingData -> hymnPagingDataAdapter.submitData(getLifecycle(), hymnPagingData)));
        binding.recyclerView.setAdapter(hymnPagingDataAdapter);
        binding.setLifecycleOwner(getViewLifecycleOwner());
        return binding.getRoot();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        disposables.clear();
    }

}