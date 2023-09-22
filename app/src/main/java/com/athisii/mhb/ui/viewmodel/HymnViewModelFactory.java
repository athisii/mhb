package com.athisii.mhb.ui.viewmodel;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.athisii.mhb.App;

public class HymnViewModelFactory implements ViewModelProvider.Factory {
    private final App app;

    public HymnViewModelFactory(App app) {
        this.app = app;
    }

    @SuppressWarnings("unchecked")
    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        if (modelClass.isAssignableFrom(HymnViewModel.class)) {
            return (T) new HymnViewModel(app);
        }
        throw new IllegalArgumentException("Unknown ViewModel class");
    }
}
