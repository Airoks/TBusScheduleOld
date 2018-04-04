package ru.tblsk.owlz.busschedule.ui.stopinfo;


import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import ru.tblsk.owlz.busschedule.R;
import ru.tblsk.owlz.busschedule.di.module.FragmentModule;
import ru.tblsk.owlz.busschedule.ui.base.BaseFragment;
import ru.tblsk.owlz.busschedule.ui.base.SetupToolbar;
import ru.tblsk.owlz.busschedule.ui.main.MainActivity;
import ru.tblsk.owlz.busschedule.ui.viewobject.DirectionVO;
import ru.tblsk.owlz.busschedule.ui.viewobject.StopVO;

public class StopInfoFragment extends BaseFragment
        implements StopInfoMvpView, SetupToolbar{

    public static final String STOP = "stop";
    public static final String DIRECTIONS = "directions";

    @Inject
    StopInfoMvpPresenter<StopInfoMvpView> mPresenter;

    @Inject
    StopInfoAdapter mAdapter;

    @Inject
    LinearLayoutManager mLinearLayout;

    @BindView(R.id.toolbar_stopinfo)
    Toolbar mToolbar;

    @BindView(R.id.recyclerview_stopinfo)
    RecyclerView mRecyclerView;

    private List<DirectionVO> mDirections;
    private StopVO mStop;

    public static  StopInfoFragment newInstance(StopVO stop) {
        Bundle args = new Bundle();
        args.putParcelable(STOP, stop);
        StopInfoFragment fragment = new StopInfoFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if(savedInstanceState != null) {
            mDirections = savedInstanceState.getParcelableArrayList(DIRECTIONS);
        }
        mStop = getArguments().getParcelable(STOP);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        View view = getView() != null ? getView() :
                inflater.inflate(R.layout.fragment_stopinfo, container, false);

        getBaseActivity().getActivityComponent().fragmentComponent(new FragmentModule(this))
                .inject(this);

        setUnbinder(ButterKnife.bind(this, view));
        mPresenter.attachView(this);
        return view;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelableArrayList(DIRECTIONS, (ArrayList<? extends Parcelable>) mDirections);
    }

    @Override
    public void onDestroyView() {
        mPresenter.detachView();
        super.onDestroyView();
    }

    @Override
    public void onDestroy() {
        mPresenter.unsubscribeFromEvents();
        super.onDestroy();
    }

    @Override
    protected void setUp(View view) {
        setupToolbar();

        mLinearLayout.setOrientation(LinearLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(mLinearLayout);
        mRecyclerView.setAdapter(mAdapter);

        if(mDirections == null) {
            mPresenter.getDirectionsByStop(mStop.getId());
        } else {
            mPresenter.getSavedDirectionsByStop();
        }

        ((MainActivity)getBaseActivity()).lockDrawer();
        ((MainActivity)getBaseActivity()).hideBottomNavigationView();
    }

    @Override
    public void setupToolbar() {
        mToolbar.setNavigationIcon(R.drawable.all_arrowbackblack_24dp);
        mToolbar.setTitle(mStop.getStopName());

        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mPresenter.clickedOnBackButton();
            }
        });
    }

    @Override
    public void showDirectionsByStop(List<DirectionVO> directions) {
        mDirections = directions;
        mAdapter.addItems(mDirections);
    }

    @Override
    public void showSavedDirectionsByStop() {
        mAdapter.addItems(mDirections);
    }

    @Override
    public void openPreviousFragment() {
        FragmentManager fragmentManager = getBaseActivity().getSupportFragmentManager();
        fragmentManager.popBackStack();
    }
}