package com.roadstatus.view

import android.content.Context
import android.os.Bundle
import android.view.KeyEvent
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.addTextChangedListener
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.LatLngBounds
import com.roadstatus.R
import com.roadstatus.view.model.BoundCoordinate
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.activity_road_status.*

private const val DEFAULT_LAT = 51.509865
private const val DEFAULT_LNG = -0.118092

@AndroidEntryPoint
class RoadStatusActivity : AppCompatActivity(), OnMapReadyCallback {

    private val viewModel: RoadStatusViewModel by viewModels()
    private var map: GoogleMap? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_road_status)
        setupMap()
        setupObservers()
        setupListeners()
    }

    private fun setupMap() {
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
    }

    override fun onMapReady(gmap: GoogleMap) {
        map = gmap
        setupMapToInitialPosition()
    }

    private fun setupListeners() {
        findRoadName.addTextChangedListener {
            viewModel.onTextChanged(it.toString())
        }

        findRoadName.setOnEditorActionListener { _, actionId, event ->
            if ((event != null && event.keyCode == KeyEvent.KEYCODE_ENTER) ||
                (actionId == EditorInfo.IME_ACTION_DONE)
            ) {
                fetchButton.performClick()
            }
            false
        }

        fetchButton.setOnClickListener {
            val inputMethodManager = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            inputMethodManager.hideSoftInputFromWindow(currentFocus?.windowToken, 0)

            val roadName = findRoadName.text.toString()
            if (roadName.isNotEmpty()) {
                viewModel.onFetchButtonClicked(roadName)
            }
        }
    }

    private fun setupObservers() {
        viewModel.viewState.observe(this, { handleViewState(it) })
    }

    private fun handleViewState(viewState: RoadStatusViewModel.ViewState) {
        resetView()
        if (viewState.isInProgress) progressIndicator.show() else progressIndicator.hide()
        fetchButton.isEnabled = viewState.isButtonEnabled
        when (viewState) {
            is RoadStatusViewModel.ViewState.Content -> {
                roadName.text = getString(R.string.road_name, viewState.roadStatus.roadName)
                roadSeverity.text = getString(R.string.severity, viewState.roadStatus.severity)
                severityDescription.text = getString(R.string.severity_description, viewState.roadStatus.severityDescription)
                updateMapWithBounds(viewState.roadStatus.boundCoordinates)
            }
            is RoadStatusViewModel.ViewState.Error -> {
                errorDescription.text = viewState.errorDescription
                setupMapToInitialPosition()
            }
        }
    }

    private fun resetView() {
        errorDescription.text = ""
        roadName.text = ""
        roadSeverity.text = ""
        severityDescription.text = ""
    }

    private fun updateMapWithBounds(bounds: List<BoundCoordinate>) {
        setupMapToInitialPosition()
        val latLongBuilder = LatLngBounds.builder()
        bounds.map {
            latLongBuilder.include(LatLng(it.lat, it.long))
        }
        val newBounds = latLongBuilder.build()
        map?.animateCamera(CameraUpdateFactory.newLatLngBounds(newBounds, 0))
    }

    private fun setupMapToInitialPosition() {
        val london = LatLng(DEFAULT_LAT, DEFAULT_LNG)
        map?.let { map ->
            map.setMinZoomPreference(10f)
            map.animateCamera(CameraUpdateFactory.newLatLng(london))
        }
    }
}