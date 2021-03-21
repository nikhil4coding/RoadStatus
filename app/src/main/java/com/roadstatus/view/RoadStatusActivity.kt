package com.roadstatus.view

import android.content.Context
import android.os.Bundle
import android.view.KeyEvent
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
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

@AndroidEntryPoint
class RoadStatusActivity : AppCompatActivity(), OnMapReadyCallback {

    private val viewModel: RoadStatusViewModel by viewModels()
    private var map: GoogleMap? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_road_status)
        setupObservers()
        setupListeners()
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
    }

    override fun onMapReady(gmap: GoogleMap) {
        map = gmap
        setupMapToInitialPosition()
    }

    private fun setupListeners() {
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

            if (findRoadName.text.toString().isNotEmpty()) {
                viewModel.onFetchButtonClicked(findRoadName.text.toString())
            }
        }
    }

    private fun setupObservers() {
        viewModel.viewState.observe(this, { handleViewState(it) })
        viewModel.viewEvent.observe(this, { handleViewEvent(it) })
    }

    private fun handleViewState(viewState: RoadStatusViewModel.ViewState) {
        when (viewState) {
            is RoadStatusViewModel.ViewState.Content -> handleContent(viewState)
        }
    }

    private fun handleViewEvent(viewEvent: RoadStatusViewModel.ViewEvent) {
        when (viewEvent) {
            is RoadStatusViewModel.ViewEvent.InProgress -> if (viewEvent.showProgress) progressIndicator.show() else progressIndicator.hide()
        }
    }

    private fun handleContent(content: RoadStatusViewModel.ViewState.Content) {
        resetView()
        when (content) {
            is RoadStatusViewModel.ViewState.Content.Error -> {
                errorDescription.text = content.errorDescription
                setupMapToInitialPosition()
            }
            is RoadStatusViewModel.ViewState.Content.Success -> {
                roadName.text = getString(R.string.road_name, content.roadStatus.roadName)
                roadSeverity.text = getString(R.string.severity, content.roadStatus.severity)
                severityDescription.text = getString(R.string.severity_description, content.roadStatus.severityDescription)
                updateMapWithBounds(content.roadStatus.boundCoordinates)
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
            latLongBuilder.include(LatLng(it.lat.toDouble(), it.long.toDouble()))
        }
        val newBounds = latLongBuilder.build()
        map?.animateCamera(CameraUpdateFactory.newLatLngBounds(newBounds, 0))
    }

    private fun setupMapToInitialPosition() {
        val london = LatLng(51.509865, -0.118092)
        map?.let {
            it.setMinZoomPreference(10f)
            it.animateCamera(CameraUpdateFactory.newLatLng(london))
        }
    }
}