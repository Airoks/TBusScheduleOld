package ru.tblsk.owlz.busschedule.ui.directioninfo;


import java.util.List;

import ru.tblsk.owlz.busschedule.ui.base.MvpView;
import ru.tblsk.owlz.busschedule.ui.mappers.viewobject.FlightVO;
import ru.tblsk.owlz.busschedule.ui.mappers.viewobject.StopVO;

public interface DirectionInfoMvpView extends MvpView{
    void showStopsOnDirection(List<StopVO> stops);
    void openPreviousFragment();
    void setDirectionTitle(String directionName);
    void setFlightNumber(String flightNumber);
    void showChangeButton(boolean flag);
}
