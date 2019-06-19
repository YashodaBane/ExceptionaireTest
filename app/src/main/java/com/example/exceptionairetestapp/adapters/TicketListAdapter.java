package com.example.exceptionairetestapp.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.exceptionairetestapp.R;
import com.example.exceptionairetestapp.TicketModel;
import com.example.exceptionairetestapp.listeners.OnButtonClickListener;

import java.util.ArrayList;

public class TicketListAdapter extends RecyclerView.Adapter<TicketListAdapter.CustomViewHolder> {
    private Context context;
    private OnButtonClickListener listener;
    private ArrayList<TicketModel> list;

    public TicketListAdapter(Context context, OnButtonClickListener listener, ArrayList<TicketModel> list) {
        this.context = context;
        this.listener = listener;
        this.list = list;
    }

    @NonNull
    @Override
    public CustomViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return new CustomViewHolder(LayoutInflater.from(context).inflate(R.layout.row_ticket, viewGroup, false));
    }

    @Override
    public void onBindViewHolder(@NonNull final CustomViewHolder customViewHolder, int position) {
        TicketModel ticketModel = list.get(customViewHolder.getAdapterPosition());

        customViewHolder.txtTitle.setText(String.format("%s %s", context.getString(R.string.title), ticketModel.getTitle()));
        customViewHolder.txtRatings.setText(String.format("%s %s", context.getString(R.string.ratings), ticketModel.getRating()));
        customViewHolder.txtReleaseYear.setText(String.format("%s %s", context.getString(R.string.release_year), ticketModel.getReleaseYear()));
        customViewHolder.txtGenre.setText(String.format("%s ", context.getString(R.string.genre)));
        for (int i = 0; i < ticketModel.getGenre().size(); i++) {
            customViewHolder.txtGenre.append(ticketModel.getGenre().get(i) + "\n");
        }

        Glide.with(context.getApplicationContext())
                .load(ticketModel.getImage())
                .centerCrop()
                .placeholder(R.mipmap.ic_launcher)
                .into(customViewHolder.ivMovie);

        customViewHolder.btnShowDetails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onButtonClick(customViewHolder.getAdapterPosition());
            }
        });

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class CustomViewHolder extends RecyclerView.ViewHolder {
        private TextView txtTitle, txtRatings, txtReleaseYear, txtGenre;
        private ImageView ivMovie;
        private Button btnShowDetails;

        CustomViewHolder(@NonNull View itemView) {
            super(itemView);
            ivMovie = itemView.findViewById(R.id.iv_movie);

            btnShowDetails = itemView.findViewById(R.id.btn_show);

            txtTitle = itemView.findViewById(R.id.txt_title);
            txtRatings = itemView.findViewById(R.id.txt_ratings);
            txtReleaseYear = itemView.findViewById(R.id.txt_release_year);
            txtGenre = itemView.findViewById(R.id.txt_genre);
        }
    }
}
