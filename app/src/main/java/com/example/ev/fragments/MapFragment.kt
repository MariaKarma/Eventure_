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
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import com.example.ev.R
import com.example.ev.bottom_sheet_dialog.EventDetailBottomSheet
import com.example.ev.data.DateRange
import com.example.ev.data.Events
import com.example.ev.databinding.FragmentMapBinding
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import java.text.SimpleDateFormat
import java.util.*

class MapFragment : Fragment() {
    companion object {
        private const val LOCATION_PERMISSION_REQUEST_CODE = 1
    }

    private var _binding: FragmentMapBinding? = null
    private val binding get() = _binding!!
    private lateinit var mMap: GoogleMap
    private var mapReady = false
    private lateinit var events: List<Events>
    private var currentCategory: String? = "All"
    private var currentDateRange: DateRange = DateRange.TODAY

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _binding = FragmentMapBinding.inflate(inflater, container, false)
        events = generateSampleEvents()
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initializeMap()
    }

    private fun initializeMap() {
        val mapFragment = childFragmentManager.findFragmentById(R.id.map_Fragment) as SupportMapFragment?
        mapFragment?.getMapAsync { googleMap ->
            mMap = googleMap
            mapReady = true
            setupMap()
            setupFilterSpinners()
            displayEvents(filterByDate(DateRange.TODAY))
        }
    }

    private fun setupMap() {
        if (ActivityCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), LOCATION_PERMISSION_REQUEST_CODE)
            return
        }

        mMap.isMyLocationEnabled = true
        mMap.uiSettings.isMyLocationButtonEnabled = true
        setupMarkerClickListener()
    }

    private fun setupMarkerClickListener() {
        mMap.setOnMarkerClickListener { marker ->
            val event = marker.tag as? Events
            event?.let {
                openEventDetailsFragment(it)
            }
            true
        }
    }

    private fun addEventMarkers(events: List<Events>) {
        events.forEach { event ->
            val latLng = LatLng(event.latitude.toDouble(), event.longitude.toDouble())
            val marker = mMap.addMarker(MarkerOptions().position(latLng).title(event.name))
            marker?.tag = event
        }
    }



    private fun openEventDetailsFragment(event: Events) {
        val bottomSheet = EventDetailBottomSheet.newInstance(event)
        bottomSheet.show(parentFragmentManager, "EventDetailBottomSheet")
    }

    private fun generateSampleEvents(): List<Events> {
        return listOf(
            Events("33.8930", "35.5279", "Poiema", "Art", "Painting", "2024-05-09", "16:00", "18:00", "$15", "Tote Bag Painting. Contact +961 81 123 123 for more details"),
            Events("33.9064", "35.5095", "Promoteam", "Education", "Expo", "2024-05-15", "16:00", "22:00", "Free", "Lebanon international solar week - expo and conference."),
            Events("33.8902", "35.5705", "Vamos Todos", "Nature", "Hike", "2024-05-30", "07:30", "17:00", "$50", "Bazhel - Nahr Ibrahim hike. Payment fee includes transport, guides, insurance and lunch. $30 without lunch. For reservation contact: +961 81 123 123.")
        )
    }

    private fun setupFilterSpinners() {
        setupDateFilterSpinner()
        setupCategoryFilterSpinner()
    }

    private fun setupDateFilterSpinner() {
        val spinner: Spinner = binding.dateFilterSpinner
        val adapter = ArrayAdapter.createFromResource(
            requireContext(),
            R.array.date_filter_options,
            android.R.layout.simple_spinner_item
        )
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinner.adapter = adapter

        spinner.setSelection(0)

        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View, position: Int, id: Long) {
                currentDateRange = DateRange.values()[position]
                updateFilteredEvents()
            }
            override fun onNothingSelected(parent: AdapterView<*>) {}
        }
    }

    private fun setupCategoryFilterSpinner() {
        val spinner: Spinner = binding.categoryFilterSpinner

        val adapter = ArrayAdapter.createFromResource(
            requireContext(),
            R.array.event_categories,
            android.R.layout.simple_spinner_item
        )

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinner.adapter = adapter

        spinner.setSelection(if (currentCategory == "All") 0 else adapter.getPosition(currentCategory))

        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View, position: Int, id: Long) {
                currentCategory = parent.getItemAtPosition(position).toString()
                updateFilteredEvents()
            }

            override fun onNothingSelected(parent: AdapterView<*>) {}
        }
    }

    private fun updateFilteredEvents() {
        val dateFilteredEvents = filterByDate(currentDateRange)
        val categoryFilteredEvents = filterByCategory(dateFilteredEvents)
        displayEvents(categoryFilteredEvents)
    }

    private fun filterByDate(range: DateRange): List<Events> {
        val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        val today = Calendar.getInstance().apply {
            set(Calendar.HOUR_OF_DAY, 0)
            set(Calendar.MINUTE, 0)
            set(Calendar.SECOND, 0)
            set(Calendar.MILLISECOND, 0)
        }

        val endOfPeriod = Calendar.getInstance().apply {
            when (range) {
                DateRange.TODAY -> add(Calendar.DATE, 1)
                DateRange.THIS_WEEK -> add(Calendar.DATE, 7)
                DateRange.THIS_MONTH -> add(Calendar.MONTH, 1)
            }
        }


        return events.filter {
            val eventDate = dateFormat.parse(it.date)?.let { date ->
                Calendar.getInstance().apply {
                    time = date
                    set(Calendar.HOUR_OF_DAY, 0)
                    set(Calendar.MINUTE, 0)
                    set(Calendar.SECOND, 0)
                    set(Calendar.MILLISECOND, 0)
                }
            }
            eventDate != null && !eventDate.before(today) && eventDate.before(endOfPeriod)
        }
    }
    private fun filterByCategory(events: List<Events>): List<Events> {
        return if (currentCategory == "All") events else events.filter { it.category == currentCategory }
    }


    private fun displayEvents(events: List<Events>) {
        mMap.clear()
        events.forEach { event ->
            addEventMarkers(events)
        }    }
}
