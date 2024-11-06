package com.example.faithportal.data.repository;

import java.util.List;

public class PlacesResponse {
    private List<PlaceResult> results;

    public List<PlaceResult> getResults() {
        return results;
    }

    public void setResults(List<PlaceResult> results) {
        this.results = results;
    }
}