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
import com.athisii.mhb.MainActivity;
import com.athisii.mhb.databinding.FragmentFavouriteHymnBinding;
import com.athisii.mhb.ui.adapter.HymnPagingDataAdapter;
import com.athisii.mhb.ui.viewmodel.HymnViewModelFactory;
import com.athisii.mhb.ui.viewmodel.HymnViewModel;

import io.reactivex.rxjava3.disposables.CompositeDisposable;

public class FavouriteHymnFragment extends Fragment {
    private final CompositeDisposable disposables = new CompositeDisposable();

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        var binding = FragmentFavouriteHymnBinding.inflate(inflater);
        App application = (App) ((MainActivity) requireActivity()).getApplication();
        var viewModel = new ViewModelProvider(this, new HymnViewModelFactory(application)).get(HymnViewModel.class);

        var hymnPagingDataAdapter = new HymnPagingDataAdapter(hymn -> {
            if (hymn != null) {
                Navigation.findNavController(requireView()).navigate(FavouriteHymnFragmentDirections.actionFavouriteHymnFragmentToDetailHymnFragment(hymn));
            }
        }, application);

        disposables.add(viewModel.getFavouriteHymn().subscribe(favHymnPagingData -> hymnPagingDataAdapter.submitData(getLifecycle(), favHymnPagingData)));
        binding.favouriteHymnRcv.recyclerView.setAdapter(hymnPagingDataAdapter);
        binding.setLifecycleOwner(getViewLifecycleOwner());
        return binding.getRoot();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        disposables.clear();
    }
}