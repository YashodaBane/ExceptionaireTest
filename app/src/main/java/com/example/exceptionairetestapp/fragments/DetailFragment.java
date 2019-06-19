package com.example.exceptionairetestapp.fragments;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.exceptionairetestapp.R;
import com.example.exceptionairetestapp.TicketModel;
import com.example.exceptionairetestapp.databinding.FragmentDetailBinding;

/**
 * A simple {@link Fragment} subclass.
 */
public class DetailFragment extends Fragment {
    private FragmentDetailBinding binding;
    private TicketModel ticketModel;


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if (binding == null)
            binding = FragmentDetailBinding.inflate(inflater, container, false);

        return binding.getRoot();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
    }

    public void setDataToView(TicketModel model) {
        ticketModel = model;
        if (ticketModel != null) {
            binding.ivMovie.setVisibility(View.VISIBLE);
            binding.layout.setVisibility(View.VISIBLE);
            binding.txtNoData.setVisibility(View.GONE);

            setDataCommonMethod(ticketModel);
        }
    }


    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putSerializable("model", ticketModel);
    }

    @Override
    public void onViewStateRestored(@Nullable Bundle savedInstanceState) {
        super.onViewStateRestored(savedInstanceState);
        if (savedInstanceState != null) {

            ticketModel = (TicketModel) savedInstanceState.getSerializable("model");
            setData(ticketModel);
        }
    }

    private void setData(TicketModel ticketModel) {
        if (ticketModel != null && binding != null) {
            binding.ivMovie.setVisibility(View.VISIBLE);
            binding.layout.setVisibility(View.VISIBLE);
            binding.txtNoData.setVisibility(View.GONE);

            setDataCommonMethod(ticketModel);
        } else if (ticketModel == null) {
            binding.ivMovie.setVisibility(View.GONE);
            binding.layout.setVisibility(View.GONE);
            binding.txtNoData.setVisibility(View.VISIBLE);
        }
    }

    private void setDataCommonMethod(TicketModel ticketModel) {
        binding.txtTitle.setText(String.format("%s %s", getString(R.string.title), ticketModel.getTitle()));
        binding.txtRatings.setText(String.format("%s %s", getString(R.string.ratings), ticketModel.getRating()));
        binding.txtReleaseYear.setText(String.format("%s %s", getString(R.string.release_year), ticketModel.getReleaseYear()));
        binding.txtGenre.setText(String.format("%s ", getString(R.string.genre)));

        for (int i = 0; i < ticketModel.getGenre().size(); i++) {
            binding.txtGenre.append(ticketModel.getGenre().get(i) + "\n");
        }

        Glide.with(this)
                .load(ticketModel.getImage())
                .centerCrop()
                .placeholder(R.mipmap.ic_launcher)
                .into(binding.ivMovie);
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
}
