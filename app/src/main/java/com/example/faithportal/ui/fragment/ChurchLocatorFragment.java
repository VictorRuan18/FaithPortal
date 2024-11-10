package com.example.faithportal.ui.fragment;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.faithportal.R;
import com.example.faithportal.data.repository.GooglePlacesService;
import com.example.faithportal.data.repository.PlaceResult;
import com.example.faithportal.data.repository.PlacesResponse;
import com.example.faithportal.ui.ChurchListAdapter;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import android.os.Looper;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.net.PlacesClient;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ChurchLocatorFragment extends Fragment implements OnMapReadyCallback {

    private static final String TAG = "ChurchLocatorFragment";
    private MapView mapView;
    private GoogleMap googleMap;
    private FusedLocationProviderClient fusedLocationClient;
    private ActivityResultLauncher<String> requestPermissionLauncher;
    private RecyclerView recyclerView;
    private ChurchListAdapter adapter;
    private boolean isInitialLocationUpdate = true;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestPermissionLauncher = registerForActivityResult(new ActivityResultContracts.RequestPermission(), isGranted -> {
            if (isGranted) {
                onMapReady(googleMap);
            } else {
                Log.e(TAG, "Location permission denied");
            }
        });

        if (!Places.isInitialized()) {
            Places.initialize(requireContext(), getString(R.string.google_maps_key));
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_church_locator, container, false);
        mapView = view.findViewById(R.id.map_view);
        mapView.onCreate(savedInstanceState);
        mapView.getMapAsync(this);
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireActivity());

        recyclerView = view.findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
        return view;
    }

    @Override
    public void onMapReady(@NonNull GoogleMap map) {
        googleMap = map;
        if (ActivityCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            requestLocationPermissions();
            return;
        }
        googleMap.setMyLocationEnabled(true);
        googleMap.setMinZoomPreference(1.0f);
        googleMap.setMaxZoomPreference(21.0f);
        startLocationUpdates();
    }

    private void requestLocationPermissions() {
        requestPermissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION);
    }

    private void startLocationUpdates() {
        LocationRequest locationRequest = LocationRequest.create()
                .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
                .setInterval(10000)
                .setFastestInterval(5000);

        if (ActivityCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        Log.d(TAG, "Requesting location updates");
        fusedLocationClient.requestLocationUpdates(locationRequest, locationCallback, Looper.getMainLooper());
    }

    private final LocationCallback locationCallback = new LocationCallback() {
        @Override
        public void onLocationResult(@NonNull LocationResult locationResult) {
            for (Location location : locationResult.getLocations()) {
                if (location != null) {
                    Log.d(TAG, "Location received: " + location);
                    LatLng currentLocation = new LatLng(location.getLatitude(), location.getLongitude());
                    if (isInitialLocationUpdate) {
                        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(currentLocation, 15));
                        isInitialLocationUpdate = false;
                    }
                    findNearbyChurches(currentLocation);
                    break;
                } else {
                    Log.e(TAG, "Location is null");
                }
            }
        }
    };

    private void findNearbyChurches(LatLng currentLocation) {
        ExecutorService executor = Executors.newSingleThreadExecutor();
        executor.execute(() -> {
            List<PlaceResult> results = fetchNearbyChurches(currentLocation);
            requireActivity().runOnUiThread(() -> {
                if (results != null && !results.isEmpty()) {
                    for (PlaceResult result : results) {
                        LatLng placeLatLng = new LatLng(result.getGeometry().getLocation().getLat(), result.getGeometry().getLocation().getLng());
                        result.setDistance(calculateDistance(currentLocation, placeLatLng)); // Set distance
                        googleMap.addMarker(new MarkerOptions()
                                .position(placeLatLng)
                                .title(result.getName())
                                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED))); // Red color
                    }
                    if (adapter == null) {
                        adapter = new ChurchListAdapter(requireContext(), results);
                        recyclerView.setAdapter(adapter);
                    } else {
                        adapter.updateData(results);
                    }
                } else {
                    Log.e(TAG, "No results found or results are empty");
                }
            });
        });
    }

    private List<PlaceResult> fetchNearbyChurches(LatLng currentLocation) {
        String location = currentLocation.latitude + "," + currentLocation.longitude;
        String apiKey = getString(R.string.google_maps_key);
        String url = "https://maps.googleapis.com/maps/api/place/nearbysearch/json" +
                "?location=" + location +
                "&radius=5000" +  // 5000 meters
                "&type=church" +
                "&key=" + apiKey;

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://maps.googleapis.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        GooglePlacesService service = retrofit.create(GooglePlacesService.class);
        Call<PlacesResponse> call = service.getNearbyPlaces(url);

        try {
            Response<PlacesResponse> response = call.execute();
            if (response.isSuccessful() && response.body() != null) {
                return response.body().getResults();
            } else {
                Log.e(TAG, "Response not successful or body is null");
            }
        } catch (Exception e) {
            Log.e(TAG, "Request failed", e);
        }
        return null;
    }

    private double calculateDistance(LatLng start, LatLng end) {
        float[] results = new float[1];
        Location.distanceBetween(start.latitude, start.longitude, end.latitude, end.longitude, results);
        return results[0] / 1000; // Convert meters to kilometers
    }

    @Override
    public void onResume() {
        super.onResume();
        mapView.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        mapView.onPause();
        fusedLocationClient.removeLocationUpdates(locationCallback);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mapView.onLowMemory();
    }
}