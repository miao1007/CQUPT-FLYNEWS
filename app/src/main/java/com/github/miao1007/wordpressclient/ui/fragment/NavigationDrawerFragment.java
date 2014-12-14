package com.github.miao1007.wordpressclient.ui.fragment;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.github.miao1007.wordpressclient.R;
import com.github.miao1007.wordpressclient.ui.adapter.MenuAdapter;

public class NavigationDrawerFragment extends Fragment {

    private NavigationDrawerCallbacks mCallback;

    private ListView mDrawerListView;
    private int mCurrentSelectedPosition = 1;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View mView = inflater.inflate(R.layout.fragment_navigation_drawer, container, false);
        mDrawerListView = (ListView) mView.findViewById(R.id.listview);
        MenuAdapter adapter = new MenuAdapter(getActivity(), getActivity().getSharedPreferences("sharesdk", 0));
        if (mDrawerListView != null) {
            mDrawerListView.setAdapter(adapter);
            mDrawerListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                    mCallback.onNavigationDrawerItemSelected(i);

                }
            });
            //initial setelcted item
            selectItem(mCurrentSelectedPosition);
        } else {

        }
        return mView;
    }

    private void selectItem(int position) {
        mCurrentSelectedPosition = position;
        if (mDrawerListView != null) {
            mDrawerListView.setItemChecked(position, true);
        }
        if (mCallback != null) {
            mCallback.onNavigationDrawerItemSelected(position);
        }
    }


    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mCallback = (NavigationDrawerCallbacks) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mCallback = null;
    }

    public interface NavigationDrawerCallbacks {
        public void onNavigationDrawerItemSelected(int position);
    }




}
