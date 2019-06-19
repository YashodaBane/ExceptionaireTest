package com.example.exceptionairetestapp.fragments;

import android.app.ProgressDialog;
import android.content.Context;
import android.databinding.DataBindingUtil;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.exceptionairetestapp.MainActivity;
import com.example.exceptionairetestapp.R;
import com.example.exceptionairetestapp.TicketModel;
import com.example.exceptionairetestapp.adapters.TicketListAdapter;
import com.example.exceptionairetestapp.databinding.FragmentTab1Binding;
import com.example.exceptionairetestapp.listeners.OnButtonClickListener;
import com.example.exceptionairetestapp.networking.APIService;
import com.example.exceptionairetestapp.networking.RetrofitClient;

import java.io.Serializable;
import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.support.constraint.Constraints.TAG;

public class TabFragment1 extends Fragment implements OnButtonClickListener {

    private ArrayList<TicketModel> list = new ArrayList<>();
    private ArrayList<TicketModel> movieListInstance = new ArrayList<>();
    private ProgressDialog mProgressDialog;
    private APIService apiService;
    private MainActivity activity;
    private TicketListAdapter adapter;
    private SendDataListener sendDataListener;
    private int selectedItemPosition;
    private TicketModel ticketModel = new TicketModel();
    private Parcelable layoutManagerState;
    private LinearLayoutManager mLayoutManager;
    private FragmentTab1Binding binding;


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        if (binding == null)
            binding = FragmentTab1Binding.inflate(inflater, container, false);
        return binding.getRoot();
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        activity = (MainActivity) getActivity();

        apiService = RetrofitClient.getClient().create(APIService.class);
        hitTicketDataApi();
    }

    private void setListAdapter(ArrayList<TicketModel> list) {
        adapter = new TicketListAdapter(getContext(), this, list);
        mLayoutManager = new LinearLayoutManager(getActivity());
        binding.rvFragmentTab1.setLayoutManager(mLayoutManager);
        binding.rvFragmentTab1.setAdapter(adapter);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
    }


    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putSerializable("movieList", movieListInstance);
//        outState.putParcelable("layoutManagerState", r.onSaveInstanceState());
        outState.putInt("selectedItemPosition", selectedItemPosition);
        outState.putSerializable("model", ticketModel);
    }

    @Override
    public void onViewStateRestored(@Nullable Bundle savedInstanceState) {
        super.onViewStateRestored(savedInstanceState);

        if (savedInstanceState != null) {
            selectedItemPosition = savedInstanceState.getInt("selectedItemPosition", 0);
            ticketModel = (TicketModel) savedInstanceState.getSerializable("model");
            movieListInstance = (ArrayList<TicketModel>) savedInstanceState.getSerializable("movieList");

//            layoutManagerState = savedInstanceState.getParcelable("layoutManagerState");
//            if (layoutManagerState != null && binding.rvFragmentTab1.getLayoutManager() != null) {
//                binding.rvFragmentTab1.getLayoutManager().onRestoreInstanceState(layoutManagerState);
//            }
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    @Override
    public void onButtonClick(int position) {
        selectedItemPosition = position;
        updateDetail(list.get(selectedItemPosition));
        sendDataListener.sendData(list.get(selectedItemPosition), selectedItemPosition);
    }


    public interface SendDataListener {
        void sendData(TicketModel ticketModel, int selectedItemPosition);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (binding != null) {
            ViewGroup parent = (ViewGroup) binding.getRoot().getParent();
            if (parent != null) {
                parent.removeAllViews();
            }
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        try {
            sendDataListener = (SendDataListener) getActivity();
        } catch (ClassCastException e) {
            throw new ClassCastException("Error in retrieving data. Please try again");
        }
    }

    public void updateDetail(TicketModel model) {
        ticketModel = model;
        DetailFragment detailFragment = null;
        if (getFragmentManager() != null) {
            detailFragment = (DetailFragment) getFragmentManager().findFragmentById(R.id.fl_detail);
            if (detailFragment != null) {
                detailFragment.setDataToView(ticketModel);
            }
        }

    }

    public boolean isOnline() {
        ConnectivityManager cm = (ConnectivityManager) activity.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        if (netInfo != null && netInfo.isConnectedOrConnecting()) {
            return true;
        } else {
            Toast.makeText(activity, "" + getString(R.string.please_check_internet_connection), Toast.LENGTH_SHORT).show();
            return false;
        }
    }

    public void hitTicketDataApi() {
        if (activity != null && isOnline()) {

            mProgressDialog = new ProgressDialog(activity);
            mProgressDialog.setIndeterminate(true);
            mProgressDialog.setMessage("Loading...");
            mProgressDialog.show();

            Call<ArrayList<TicketModel>> call = apiService.getMovieTicketData();
            call.enqueue(new Callback<ArrayList<TicketModel>>() {
                @Override
                public void onResponse(Call<ArrayList<TicketModel>> call, Response<ArrayList<TicketModel>> response) {
                    if (mProgressDialog.isShowing())
                        mProgressDialog.dismiss();

                    if (response.isSuccessful()) {
                        list = response.body();
                        movieListInstance.addAll(list);
                        setListAdapter(list);
                        Log.i(TAG, "post submitted to API." + list.size());
                    }
                }

                @Override
                public void onFailure(Call<ArrayList<TicketModel>> call, Throwable t) {
                    if (mProgressDialog.isShowing())
                        mProgressDialog.dismiss();
                    Toast.makeText(getActivity(), "Failed: " + t.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        }

    }

}