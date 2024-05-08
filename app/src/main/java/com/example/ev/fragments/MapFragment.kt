package com.example.ev.fragments

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.location.Criteria
import android.location.LocationManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.example.ev.R
import com.example.ev.bottom_sheet_dialog.EventDetailBottomSheet
import com.example.ev.data.Events
import com.example.ev.databinding.FragmentMapBinding
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions


class MapFragment : Fragment() {
    companion object {
        private const val LOCATION_PERMISSION_REQUEST_CODE = 1
    }
    private var _binding: FragmentMapBinding? = null
    private val binding get() = _binding!!
    private lateinit var mMap: GoogleMap
    private var mapReady = false

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentMapBinding.inflate(inflater, container, false)
        return binding.root
    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val mapFragment = childFragmentManager.findFragmentById(R.id.map_Fragment) as SupportMapFragment
        mapFragment.getMapAsync { googleMap ->
            mMap = googleMap
            mapReady = true
            requestLocationPermission()
        }
    }

    private fun requestLocationPermission() {
        if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), LOCATION_PERMISSION_REQUEST_CODE)
        } else {
            setupMap()
        }
    }
    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == LOCATION_PERMISSION_REQUEST_CODE && grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            setupMap()
        } else {
            Toast.makeText(context, "Permission denied", Toast.LENGTH_LONG).show()
        }
    }
    private fun setupMap() {
        if (ActivityCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            mMap.isMyLocationEnabled = true
            mMap.uiSettings.isMyLocationButtonEnabled = true

            val locationManager = activity?.getSystemService(Context.LOCATION_SERVICE) as LocationManager
            val provider = locationManager.getBestProvider(Criteria(), true)
            val location = locationManager.getLastKnownLocation(provider!!)
            if (location != null) {
                val userLatLng = LatLng(location.latitude, location.longitude)
                mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(userLatLng, 12f))
            }
            addEventMarkers()
            updateMap()
        }
    }

    private fun addEventMarkers() {
        val events = generateSampleEvents()
        events.forEach { event ->
            val latLng = LatLng(event.latitude.toDouble(), event.longitude.toDouble())
            val marker = mMap.addMarker(MarkerOptions()
                .position(latLng)
                .title(event.name))
            marker?.tag = event
        }
    }

    private fun updateMap() {
        if (mapReady) {
            mMap.setOnMarkerClickListener { marker ->
                val event = marker.tag as? Events
                event?.let {
                    openEventDetailsFragment(it)
                }
                true
            }
        }
    }

    private fun openEventDetailsFragment(event: Events) {
        val bottomSheet = EventDetailBottomSheet.newInstance(event)
        bottomSheet.show(parentFragmentManager, "EventDetailBottomSheet")
    }

    private fun generateSampleEvents(): List<Events> {
        return listOf(
            Events("33.8930", "35.5279","Poiema", "Art", "Painting", "2023-05-07", "16:00", "18:00", "$15", "Tote Bag Painting.\nContact +961 81 123 123 for more details"),
            Events("33.9064", "35.5095","Promoteam", "Education", "Expo", "2023-05-16", "16:00", "22:00", "Free", "Lebanon international solar week - expo and conference."),
            Events("33.8902", "35.5705","Vamos Todos", "Nature", "Hike", "2023-05-30", "07:30", "17:00", "$50", "Bazhel - Nahr Ibrahim hike.\nPayment fee includes transport, guides, insurance and lunch. 30$ without lunch. For reservation contact: +961 81 123 123.")
        )
    }

}