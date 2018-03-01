package ru.tblsk.owlz.busschedule.data.db;


import java.util.List;

import io.reactivex.Completable;
import io.reactivex.Observable;
import io.reactivex.Single;
import ru.tblsk.owlz.busschedule.data.db.model.Direction;
import ru.tblsk.owlz.busschedule.data.db.model.DirectionType;
import ru.tblsk.owlz.busschedule.data.db.model.Flight;
import ru.tblsk.owlz.busschedule.data.db.model.FlightType;
import ru.tblsk.owlz.busschedule.data.db.model.Schedule;
import ru.tblsk.owlz.busschedule.data.db.model.ScheduleType;
import ru.tblsk.owlz.busschedule.data.db.model.Stop;
import ru.tblsk.owlz.busschedule.data.db.model.StopsOnRouts;

public interface DbHelper {

    Completable saveFlightTypeList(List<FlightType> flightTypeList);
    Completable saveFlightList(List<Flight> flightList);
    Completable saveDirectionTypeList(List<DirectionType> directionTypeList);
    Completable saveDirectionList(List<Direction> directionList);
    Completable saveStopsOnRoutsList(List<StopsOnRouts> stopsOnRoutsList);
    Completable saveStopList(List<Stop> stopList);
    Completable saveScheduleList(List<Schedule> scheduleList);
    Completable saveScheduleTypeList(List<ScheduleType> scheduleTypeList);

    Single<Boolean> isEmptyDirection();
    Single<Boolean> isEmptyDirectionType();
    Single<Boolean> isEmptyFlight();
    Single<Boolean> isEmptyFlightType();
    Single<Boolean> isEmptySchedule();
    Single<Boolean> isEmptyScheduleType();
    Single<Boolean> isEmptyStop();
    Single<Boolean> isEmptyStopsOnRouts();

    Observable<List<Stop>> getAllStops();
    Observable<List<Flight>> getFlightByType(String flightType);
    Observable<List<Direction>> getDirectionByStop(long stopId);
    Observable<List<Schedule>> getSchedule(long stopId, long directionId);
    Observable<List<Stop>> getAllStopsOnRouts(long directionId);

    Single<Long> insertFavoriteStops(long stopId, long directionId);
    Observable<List<Stop>> getFavoriteStop();
    Observable<List<Direction>> getFavoriteDirection(long stopId);
    Single<String> getFlightNumber(long flightId);
}
