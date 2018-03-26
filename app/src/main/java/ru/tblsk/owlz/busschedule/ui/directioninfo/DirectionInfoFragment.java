package ru.tblsk.owlz.busschedule.ui.directioninfo;


import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import ru.tblsk.owlz.busschedule.R;
import ru.tblsk.owlz.busschedule.data.db.model.Direction;
import ru.tblsk.owlz.busschedule.data.db.model.Flight;
import ru.tblsk.owlz.busschedule.data.db.model.Schedule;
import ru.tblsk.owlz.busschedule.data.db.model.Stop;
import ru.tblsk.owlz.busschedule.di.module.FragmentModule;
import ru.tblsk.owlz.busschedule.ui.base.BaseFragment;
import ru.tblsk.owlz.busschedule.ui.base.SetupToolbar;
import ru.tblsk.owlz.busschedule.ui.main.MainActivity;
import ru.tblsk.owlz.busschedule.ui.routes.suburban.ChangeDirectionSuburban;
import ru.tblsk.owlz.busschedule.ui.routes.urban.ChangeDirectionUrban;
import ru.tblsk.owlz.busschedule.utils.RxEventBus;

public class DirectionInfoFragment extends BaseFragment
        implements DirectionInfoMvpView, SetupToolbar{

    public static final String TAG = "DirectionInfoFragment";
    public static final String DIRECTION = "direction";
    public static final String DIRECTIONS = "directions";
    public static final String FLIGHT = "flight";
    public static final String POSITION = "position";

    @Inject
    RxEventBus mEventBus;

    @BindView(R.id.toolbar_directioninfo)
    Toolbar mToolbar;

    @BindView(R.id.textview_directioninfo_directionname)
    TextView mDirectionName;

    @BindView(R.id.recyclerview_directioninfo_stops)
    RecyclerView mRecyclerView;

    private Direction mDirection;
    private Flight mFlight;
    private int mPosition;
    private List<Stop> mStops;


    public static DirectionInfoFragment newInstance(Direction direction,
                                                    Flight flight, int mPosition) {
        Bundle bundle = new Bundle();
        bundle.putInt(POSITION, mPosition);
        bundle.putParcelable(DIRECTION, direction);
        bundle.putParcelable(FLIGHT, flight);
        DirectionInfoFragment newInstance = new DirectionInfoFragment();
        newInstance.setArguments(bundle);
        return newInstance;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if(savedInstanceState != null) {
            mStops = savedInstanceState.getParcelableArrayList(DIRECTIONS);
        }
        Bundle bundle = this.getArguments();
        mDirection = bundle.getParcelable(DIRECTION);
        mFlight = bundle.getParcelable(FLIGHT);
        mPosition = bundle.getInt(POSITION);
    }

    @Override
    protected void setUp(View view) {
        setupToolbar();
        mDirectionName.setText(mDirection.getDirectionName());
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_directioninfo, container, false);
        getBaseActivity().getActivityComponent().fragmentComponent(new FragmentModule(this))
                .inject(this);
        setUnbinder(ButterKnife.bind(this, view));
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        String s = getResources().getString(R.string.directioninfo_rout);
        mToolbar.setTitle(mFlight.getFlightNumber() + " " + s);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putParcelableArrayList(DIRECTIONS, (ArrayList<? extends Parcelable>) mStops);
    }

    @Override
    public void showStopsOnDirection(List<Stop> stops) {

    }

    @Override
    public void setupToolbar() {
        mToolbar.setNavigationIcon(R.drawable.all_arrowbackblack_24dp);
        mToolbar.inflateMenu(R.menu.menu_directioninfo);
        mToolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.item_directioninfo_change:
                        //оповещение в DirectionInfoAdapter
                        /*Collections.reverse(mStops);
                        ChangeDirectionInfo directionInfo = new ChangeDirectionInfo(mStops);
                        mEventBus.post(directionInfo);*/

                        //оповещение в Urban/SuburbanRoutesFragment
                        if(mFlight.getFlightTypeId() == 0) {
                            ChangeDirectionUrban.InFragment inFragment =
                                    new ChangeDirectionUrban.InFragment(mPosition, "urban");
                            mEventBus.post(inFragment);
                        }
                        if(mFlight.getFlightTypeId() == 1) {
                            ChangeDirectionSuburban.InFragment inFragment =
                                    new ChangeDirectionSuburban.InFragment(mPosition, "suburban");
                            mEventBus.post(inFragment);
                        }

                        return true;
                }
                return false;
            }
        });

        ((MainActivity)getBaseActivity()).lockDrawer();
        ((MainActivity)getBaseActivity()).hideBottomNavigationView();
    }
}
