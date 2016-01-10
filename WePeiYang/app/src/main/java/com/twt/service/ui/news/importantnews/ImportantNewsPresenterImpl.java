package com.twt.service.ui.news.importantnews;

import com.twt.service.bean.NewsList;
import com.twt.service.interactor.ImportantNewsInteractor;

/**
 * Created by sunjuntao on 15/11/14.
 */
public class ImportantNewsPresenterImpl implements ImportantNewsPresenter, OnGetImportantNewsCallback {
    private ImportantNewsView view;
    private ImportantNewsInteractor interactor;
    private boolean isRefreshing = false;
    private boolean isLoadingMore = false;
    private int page;

    public ImportantNewsPresenterImpl(ImportantNewsView view, ImportantNewsInteractor interactor) {
        this.view = view;
        this.interactor = interactor;
    }

    @Override
    public void onSuccess(NewsList newsList) {
        view.setRefreshEnable(false);
        if (newsList.data.size() == 0) {
            isLoadingMore = false;
            isRefreshing = false;
            view.showToast("没有新闻了orz");
            return;
        }
        if (isLoadingMore) {
            view.loadMoreItems(newsList.data);
            isLoadingMore = false;
        } else {
            isRefreshing = false;
            view.refreshItems(newsList.data);
        }
    }

    @Override
    public void onFailure(String errorMsg) {
        view.setRefreshEnable(false);
        isLoadingMore = false;
        isRefreshing = false;
        view.showToast(errorMsg);
    }

    @Override
    public void refreshNewsItems() {
        if (isRefreshing){
            return;
        }
        page = 1;
        isRefreshing = true;
        interactor.getImportantNews(page, this);
    }

    @Override
    public void loadMoreNewsItems() {
        if (isLoadingMore) {
            return;
        }
        page += 1;
        isLoadingMore = true;
        interactor.getImportantNews(page, this);
    }
}