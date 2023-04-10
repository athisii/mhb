package com.athisii.mhb.ui.viewmodel;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.athisii.mhb.App;
import com.athisii.mhb.ui.viewmodel.HomeHymnViewModel;

public class HomeHymnViewModelFactory implements ViewModelProvider.Factory {
    private final App app;

    public HomeHymnViewModelFactory(App app) {
        this.app = app;
    }

    @SuppressWarnings("unchecked")
    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        if (modelClass.isAssignableFrom(HomeHymnViewModel.class)) {
            return (T) new HomeHymnViewModel(app);
        }
        throw new IllegalArgumentException("Unknown ViewModel class");
    }
}
