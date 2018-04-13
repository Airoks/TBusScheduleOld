package ru.tblsk.owlz.busschedule.ui.favorites;


import java.util.List;

import ru.tblsk.owlz.busschedule.ui.base.MvpPresenter;
import ru.tblsk.owlz.busschedule.ui.base.MvpView;
import ru.tblsk.owlz.busschedule.ui.mappers.viewobject.StopVO;

public interface FavoriteStopsContract {

    interface View extends MvpView {
        void showFavoriteStops(List<StopVO> stops);
        void openStopInfoFragment(StopVO stop);
        void openNavigationDrawer();
        void showEmptyScreen();
    }

    interface Presenter extends MvpPresenter<View> {
        void getFavoriteStops();
        void setClickListenerForAdapter();
        void clickedOnNavigation();
    }

}
