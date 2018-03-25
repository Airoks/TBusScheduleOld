package ru.tblsk.owlz.busschedule.ui.routes.urban;


import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;
import ru.tblsk.owlz.busschedule.R;
import ru.tblsk.owlz.busschedule.data.db.model.Flight;
import ru.tblsk.owlz.busschedule.di.annotation.FlightType;
import ru.tblsk.owlz.busschedule.di.module.FragmentModule;
import ru.tblsk.owlz.busschedule.ui.base.BaseFragment;
import ru.tblsk.owlz.busschedule.ui.directioninfo.DirectionInfoFragment;
import ru.tblsk.owlz.busschedule.ui.main.MainActivity;
import ru.tblsk.owlz.busschedule.ui.routes.Event;
import ru.tblsk.owlz.busschedule.ui.routes.RoutesAdapter;
import ru.tblsk.owlz.busschedule.utils.RxEventBus;
import ru.tblsk.owlz.busschedule.utils.rxSchedulers.SchedulerProvider;

public class UrbanRoutesFragment extends BaseFragment
        implements UrbanRoutesMvpView, Event{

    public static final String TAG = "UrbanRoutesFragment";
    public static final String FLIGHTS = "flights";
    public static final String DIRECTION_ROUTS = "directionRouts";
    public static final String DIRECT = "direct";
    public static final String REVERSE = "reverse";

    @Inject
    UrbanRoutesMvpPresenter<UrbanRoutesMvpView> mPresenter;

    @Inject
    LinearLayoutManager mLinearLayout;

    @Inject
    @FlightType("urban")
    RoutesAdapter mAdapter;

    @Inject
    CompositeDisposable mCompositeDisposable;

    @Inject
    RxEventBus mEventBus;

    @Inject
    SchedulerProvider mSchedulerProvider;

    @BindView(R.id.urbanRouteRv)
    RecyclerView mRecyclerView;

    private List<Flight> mFlights;
    private List<String> mDirectionRoutes;
    private List<ChangeDirectionUrban.InFragment> mChangeDirectionFragment;
    private Map<Integer, String> mChangeDirectionAdapter;

    public static UrbanRoutesFragment newInstance() {
        Bundle args = new Bundle();
        UrbanRoutesFragment fragment = new UrbanRoutesFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);

        mChangeDirectionAdapter = new HashMap<>();
        mChangeDirectionFragment = new ArrayList<>();

        if(savedInstanceState != null) {
            mFlights = savedInstanceState.getParcelableArrayList(FLIGHTS);
            mDirectionRoutes = savedInstanceState.getStringArrayList(DIRECTION_ROUTS);
        }
    }

    @Override
    public void onDestroyView() {
        mPresenter.detachView();
        super.onDestroyView();
    }

    @Override
    public void onDestroy() {
        mCompositeDisposable.clear();
        super.onDestroy();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_urbanroutes, container, false);
        getBaseActivity().getActivityComponent()
                .fragmentComponent(new FragmentModule(this)).inject(this);

        if(mFlights == null) {
            subscribeOnEvents();
        }

        setUnbinder(ButterKnife.bind(this, view));
        mPresenter.attachView(this);
        return view;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        updateDirectionRouts();
        outState.putParcelableArrayList(FLIGHTS, (ArrayList<? extends Parcelable>) mFlights);
        outState.putStringArrayList(DIRECTION_ROUTS, (ArrayList<String>) mDirectionRoutes);

    }

    @Override
    protected void setUp(View view) {
        mLinearLayout.setOrientation(LinearLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(mLinearLayout);
        mRecyclerView.setAdapter(mAdapter);

        ((MainActivity)getBaseActivity()).unlockDrawer();
        ((MainActivity)getBaseActivity()).showBottomNavigationView();

        if(mFlights == null) {
            mPresenter.getUrbanFlights();
        } else {
            showSavedRouts();
        }
    }

    @Override
    public void showUrbanRoutes(List<Flight> flights) {
        mFlights = flights;
        mDirectionRoutes = new ArrayList<>();
        //при первом запуске пользователь видит прямое направление
        for(int i = 0; i < mFlights.size(); i++) {
            mDirectionRoutes.add(DIRECT);
        }
        mAdapter.addItems(flights, mDirectionRoutes);
    }

    @Override
    public void changedDirectionInFragment(ChangeDirectionUrban.InFragment direction) {
        mChangeDirectionFragment.add(direction);
    }

    @Override
    public void changedDirectionInAdapter(ChangeDirectionUrban.InAdapter direction) {
        int position = direction.getPosition();
        String directionType = direction.getDirectionType();
        mChangeDirectionAdapter.put(position, directionType);
    }

    public void showSavedRouts() {
        updateDirectionRouts();
        mAdapter.addItems(mFlights, mDirectionRoutes);
    }

    public void updateDirectionRouts() {
        //приоритет принадлежит изменениям в DirectionInfoFragment
        if(!mChangeDirectionAdapter.isEmpty()) {
            for(Map.Entry entry : mChangeDirectionAdapter.entrySet()) {
                int keyPos = (Integer) entry.getKey();
                String keyDir = entry.getValue().toString();
                mDirectionRoutes.set(keyPos, keyDir);
            }
        }
        if(!mChangeDirectionFragment.isEmpty()) {
            int position = mChangeDirectionFragment.size() - 1;
            mDirectionRoutes.set(mChangeDirectionFragment.get(position).getPosition(),
                    mChangeDirectionFragment.get(position).getDirectionType());
        }

        mChangeDirectionFragment.clear();
        mChangeDirectionAdapter.clear();
    }

    public void subscribeOnEvents() {
        mCompositeDisposable.add(mEventBus.filteredObservable(ChangeDirectionUrban.InFragment.class)
                .subscribeOn(mSchedulerProvider.io())
                .observeOn(mSchedulerProvider.ui())
                .subscribe(new Consumer<ChangeDirectionUrban.InFragment>() {
                    @Override
                    public void accept(ChangeDirectionUrban.InFragment inFragment) throws Exception {
                        changedDirectionInFragment(inFragment);
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {

                    }
                }));
        mCompositeDisposable.add(mEventBus.filteredObservable(ChangeDirectionUrban.InAdapter.class)
                .subscribeOn(mSchedulerProvider.io())
                .observeOn(mSchedulerProvider.ui())
                .subscribe(new Consumer<ChangeDirectionUrban.InAdapter>() {
                    @Override
                    public void accept(ChangeDirectionUrban.InAdapter inAdapter) throws Exception {
                        changedDirectionInAdapter(inAdapter);
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {

                    }
                }));

        mCompositeDisposable.add(mEventBus.filteredObservable(ChangeDirectionUrban.class)
                .subscribeOn(mSchedulerProvider.io())
                .observeOn(mSchedulerProvider.ui())
                .subscribe(new Consumer<ChangeDirectionUrban>() {
                    @Override
                    public void accept(ChangeDirectionUrban directionUrban) throws Exception {
                        FragmentManager fragmentManager = getBaseActivity()
                                .getSupportFragmentManager();
                        FragmentTransaction transaction = fragmentManager.beginTransaction();
                        transaction.replace(R.id.container,
                                DirectionInfoFragment.newInstance(
                                        directionUrban.getDirection()));
                        transaction.addToBackStack(DirectionInfoFragment.TAG);
                        transaction.commit();
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {

                    }
                }));
    }
}
