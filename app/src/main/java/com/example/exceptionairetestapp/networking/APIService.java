package com.example.exceptionairetestapp.networking;

import com.example.exceptionairetestapp.TicketModel;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface APIService {
    @GET("movies.json")
    Call<ArrayList<TicketModel>> getMovieTicketData();
}
