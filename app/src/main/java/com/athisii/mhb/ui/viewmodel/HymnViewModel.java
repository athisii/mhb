package com.athisii.mhb.ui.viewmodel;

import androidx.lifecycle.ViewModel;
import androidx.paging.PagingData;

import com.athisii.mhb.App;
import com.athisii.mhb.entity.Hymn;
import com.athisii.mhb.entity.HymnVerse;
import com.athisii.mhb.entity.HymnVerseLine;

import java.util.List;
import java.util.SortedMap;

import io.reactivex.rxjava3.core.Flowable;


public class HymnViewModel extends ViewModel {
    private final App application;

    public HymnViewModel(App application) {
        this.application = application;
    }

    public Flowable<PagingData<Hymn>> getPagingDataFlow() {
        return application.getRepository().getHymnPagingDataFlow();
    }

    public Flowable<PagingData<Hymn>> searchHymn(String query) {
        return application.getRepository().searchHymn(query);
    }

    public Flowable<PagingData<Hymn>> getFavouriteHymn() {
        return application.getRepository().getFavHymns();
    }

    public boolean updateHymn(Hymn hymn) {
        return application.getRepository().updateHymn(hymn);
    }

    public SortedMap<HymnVerse, List<HymnVerseLine>> getHymnContentById(long id) {
        return application.getRepository().getHymnContentById(id);
    }


}
