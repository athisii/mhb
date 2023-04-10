package com.athisii.mhb.ui.viewmodel;

import androidx.lifecycle.ViewModel;
import androidx.paging.PagingData;

import com.athisii.mhb.App;
import com.athisii.mhb.entity.Hymn;

import io.reactivex.rxjava3.core.Flowable;


public class HomeHymnViewModel extends ViewModel {
    private final App application;

    public HomeHymnViewModel(App application) {
        this.application = application;
    }

    public Flowable<PagingData<Hymn>> getPagingDataFlow() {
        return application.getRepository().getPagingDataFlow();
    }


}
