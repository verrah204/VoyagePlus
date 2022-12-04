package io.network.voyageplus.ui.maps;

import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.SearchView;
import androidx.core.view.WindowCompat;
import androidx.fragment.app.Fragment;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.material.elevation.SurfaceColors;

import java.io.IOException;
import java.util.List;

import io.network.voyageplus.R;
import io.network.voyageplus.databinding.FragmentMapsBinding;


public class MapsFragment extends Fragment implements OnMapReadyCallback {

    private FragmentMapsBinding binding;
    private GoogleMap mMap;
    private SupportMapFragment mapFragment;
    private SearchView searchView;
    private Context context ;
    private LinearLayout searchCont;


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentMapsBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        context= container.getContext();

      //  WindowCompat.setDecorFitsSystemWindows(requireActivity().getWindow(), false);

        mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
        searchView =  root.findViewById(R.id.idSearchView);
        searchCont =  root.findViewById(R.id.searchCont);

        searchCont.setBackgroundColor(SurfaceColors.SURFACE_0.getColor(context));
      //  searchView.setBackgroundColor(SurfaceColors.SURFACE_2.getColor(context));



        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                // on below line we are getting the
                // location name from search view.
                String location = searchView.getQuery().toString();

                // below line is to create a list of address
                // where we will store the list of all address.
                List<Address> addressList = null;

                // checking if the entered location is null or not.
                if (location != null || location.equals("")) {
                    // on below line we are creating and initializing a geo coder.
                    Geocoder geocoder = new Geocoder(context);
                    try {
                        // on below line we are getting location from the
                        // location name and adding that location to address list.
                        addressList = geocoder.getFromLocationName(location, 1);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    // on below line we are getting the location
                    // from our list a first position.
                    Address address = addressList.get(0);

                    // on below line we are creating a variable for our location
                    // where we will add our locations latitude and longitude.
                    LatLng latLng = new LatLng(address.getLatitude(), address.getLongitude());

                    // on below line we are adding marker to that position.
                    mMap.addMarker(new MarkerOptions().position(latLng).title(location));

                    // below line is to animate camera to that position.
                    mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 10));
                }
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });

        mapFragment.getMapAsync(this);
    
     return root;
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // Add a marker in Sydney and move the camera
        LatLng benin = new LatLng(6.36536, 2.41833);
        mMap.addMarker(new MarkerOptions().position(benin).title("Marker in Benin"));
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(benin, 12.0f));

       // mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(benin,12.0f));
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}