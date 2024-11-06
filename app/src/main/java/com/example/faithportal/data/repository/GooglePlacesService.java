package com.example.faithportal.data.repository;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Url;

public interface GooglePlacesService {
    @GET
    Call<PlacesResponse> getNearbyPlaces(@Url String url);
}