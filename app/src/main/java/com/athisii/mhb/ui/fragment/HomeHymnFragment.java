package com.athisii.mhb.ui.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.SearchView;
import androidx.core.view.MenuProvider;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import com.athisii.mhb.App;
import com.athisii.mhb.MainActivity;
import com.athisii.mhb.R;
import com.athisii.mhb.databinding.FragmentHomeHymnBinding;
import com.athisii.mhb.ui.adapter.HymnPagingDataAdapter;
import com.athisii.mhb.ui.viewmodel.HymnViewModelFactory;
import com.athisii.mhb.ui.viewmodel.HymnViewModel;

import io.reactivex.rxjava3.disposables.CompositeDisposable;

public class HomeHymnFragment extends Fragment {
    private MainActivity parentActivity;
    private boolean fetchForEmptyString;
    private App application;
    private HymnPagingDataAdapter hymnPagingDataAdapter;
    private HymnViewModel viewModel;
    private final CompositeDisposable disposables = new CompositeDisposable();

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        fetchForEmptyString = false;
        var binding = FragmentHomeHymnBinding.inflate(inflater);
        parentActivity = (MainActivity) requireActivity();
        application = (App) parentActivity.getApplication();

        viewModel = new ViewModelProvider(this, new HymnViewModelFactory(application)).get(HymnViewModel.class);
        hymnPagingDataAdapter = new HymnPagingDataAdapter(hymn -> {
            if (hymn != null) {
                Navigation.findNavController(requireView()).navigate(HomeHymnFragmentDirections.actionHomeHymnFragmentToDetailHymnFragment(hymn));
            }
        }, application);

        disposables.add(viewModel.getPagingDataFlow().subscribe(hymnPagingData -> hymnPagingDataAdapter.submitData(getLifecycle(), hymnPagingData)));
        binding.homeHymnRcv.recyclerView.setAdapter(hymnPagingDataAdapter);
        binding.setLifecycleOwner(getViewLifecycleOwner());
        addToolbarMenuItems();
        return binding.getRoot();
    }

    private void addToolbarMenuItems() {
        parentActivity.addMenuProvider(new MenuProvider() {
            @Override
            public void onCreateMenu(@NonNull Menu menu, @NonNull MenuInflater menuInflater) {
                menuInflater.inflate(R.menu.toolbar_home_hymn_menu, menu);
                MenuItem menuItem = menu.findItem(R.id.toolbar_home_hymn_search);
                setListenerOnMenuItem(menuItem);
                SearchView searchView = (SearchView) menuItem.getActionView();
                setListenerOnSearchView(searchView);
            }

            @Override
            public boolean onMenuItemSelected(@NonNull MenuItem menuItem) {
                return menuItem.getItemId() == R.id.toolbar_home_hymn_search;
            }
        }, getViewLifecycleOwner()); //life cycle aware
    }

    private void setListenerOnSearchView(SearchView searchView) {
        searchView.setQueryHint("Enter a hymn number or title.");
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                searchQuery(query);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                searchQuery(newText);
                return true;
            }
        });
    }

    private void setListenerOnMenuItem(MenuItem menuItem) {
        menuItem.setOnActionExpandListener(new MenuItem.OnActionExpandListener() {
            @Override
            public boolean onMenuItemActionExpand(@NonNull MenuItem item) {
                parentActivity.getBinding().appBarMain.babMain.setVisibility(View.GONE);
                return true;
            }

            @Override
            public boolean onMenuItemActionCollapse(@NonNull MenuItem item) {
                parentActivity.getBinding().appBarMain.babMain.setVisibility(View.VISIBLE);
                return true;
            }
        });
    }

    private void searchQuery(String query) {
        if (query.isBlank()) {
            // don't fetch again when user click the search icon for the first time.
            if (fetchForEmptyString) {
                disposables.add(viewModel.getPagingDataFlow().subscribe(hymnPagingData -> hymnPagingDataAdapter.submitData(getLifecycle(), hymnPagingData)));
            }
            return;
        }
        fetchForEmptyString = true;
        disposables.add(viewModel.searchHymn("%" + query + "%").subscribe(hymnPagingData -> hymnPagingDataAdapter.submitData(getLifecycle(), hymnPagingData)));
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        disposables.clear();
    }
}