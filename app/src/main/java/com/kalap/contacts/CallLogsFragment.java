package com.kalap.contacts;

import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.kalap.contacts.adapters.CallLogAdapter;
import com.kalap.contacts.database.CallLogLoadListener;
import com.kalap.contacts.executors.CallLogExecutor;
import com.kalap.contacts.object.PhoneLog;

import java.util.ArrayList;

public class CallLogsFragment extends Fragment implements CallLogLoadListener {

    private RecyclerView callLogs;
    private ArrayList<PhoneLog> phoneLogs;

    public static CallLogsFragment newInstance() {

        Bundle args = new Bundle();

        CallLogsFragment fragment = new CallLogsFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.call_logs_fragment,container,false);
        callLogs = (RecyclerView) view.findViewById(R.id.call_logs);
        phoneLogs = new ArrayList<>();
        resetAdapter();
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        CallLogExecutor executor = new CallLogExecutor();
        executor.setListener(this);
        executor.loadCallLogs(getActivity());
    }

    private void resetAdapter() {
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                CallLogAdapter callLogAdapter = new CallLogAdapter(getActivity(), phoneLogs);
                callLogs.setAdapter(callLogAdapter);
                callLogs.setLayoutManager(new LinearLayoutManager(getActivity()));
            }
        });
    }

    @Override
    public void onCallLogLoaded(final ArrayList<PhoneLog> phoneLogs) {
        this.phoneLogs = phoneLogs;
        resetAdapter();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case 103: {
                if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    CallLogExecutor executor = new CallLogExecutor();
                    executor.setListener(this);
                    executor.loadCallLogs(getActivity());
                }
            }
        }
    }
}
