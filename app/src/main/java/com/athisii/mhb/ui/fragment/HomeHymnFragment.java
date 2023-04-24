package com.athisii.mhb.ui.fragment;

import android.os.Bundle;
import android.util.Log;
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
import com.athisii.mhb.ui.viewmodel.HomeHymnViewModel;
import com.athisii.mhb.ui.viewmodel.HomeHymnViewModelFactory;

import io.reactivex.rxjava3.disposables.CompositeDisposable;

public class HomeHymnFragment extends Fragment {
    private MainActivity parentActivity;
    private App application;
    private HymnPagingDataAdapter hymnPagingDataAdapter;
    private HomeHymnViewModel viewModel;
    private final CompositeDisposable disposables = new CompositeDisposable();

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        var binding = FragmentHomeHymnBinding.inflate(inflater);
        parentActivity = (MainActivity) requireActivity();
        application = (App) parentActivity.getApplication();

        viewModel = new ViewModelProvider(this, new HomeHymnViewModelFactory(application)).get(HomeHymnViewModel.class);
        hymnPagingDataAdapter = new HymnPagingDataAdapter(hymn -> {
            if (hymn != null) {
                Navigation.findNavController(requireView()).navigate(HomeHymnFragmentDirections.actionHomeHymnFragmentToDetailHymnFragment(hymn.getId(), hymn.getHymnNumber()));
            }
        }, application);

        disposables.add(viewModel.getPagingDataFlow().subscribe(hymnPagingData -> hymnPagingDataAdapter.submitData(getLifecycle(), hymnPagingData)));
        binding.recyclerView.setAdapter(hymnPagingDataAdapter);
        binding.setLifecycleOwner(getViewLifecycleOwner());
        addMenuItems();
        return binding.getRoot();
    }

    private void addMenuItems() {
        parentActivity.addMenuProvider(new MenuProvider() {
            @Override
            public void onCreateMenu(@NonNull Menu menu, @NonNull MenuInflater menuInflater) {
                menuInflater.inflate(R.menu.toolbar_menu, menu);
                MenuItem menuItem = menu.findItem(R.id.app_bar_search);
                setListenerOnMenuItem(menuItem);
                SearchView searchView = (SearchView) menuItem.getActionView();
                setListenerOnSearchView(searchView);
            }

            @Override
            public boolean onMenuItemSelected(@NonNull MenuItem menuItem) {
                if (menuItem.getItemId() == R.id.app_bar_search) {
                    return true;
                }
                // else other menu items
                return false;
            }
        }, getViewLifecycleOwner());
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
            disposables.add(viewModel.getPagingDataFlow().subscribe(hymnPagingData -> hymnPagingDataAdapter.submitData(getLifecycle(), hymnPagingData)));
            return;
        }
        disposables.add(viewModel.searchHymn("%" + query + "%").subscribe(hymnPagingData -> hymnPagingDataAdapter.submitData(getLifecycle(), hymnPagingData)));
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        disposables.clear();
    }

}