package ru.tblsk.owlz.busschedule.ui.routes.urban;


import android.os.Bundle;
import android.view.View;

import ru.tblsk.owlz.busschedule.ui.base.BaseFragment;

public class UrbanRouteFragment extends BaseFragment{

    public static UrbanRouteFragment newInstance() {
        Bundle args = new Bundle();
        UrbanRouteFragment fragment = new UrbanRouteFragment();
        fragment.setArguments(args);
        return fragment;
    }
    @Override
    protected void setUp(View view) {

    }
}
