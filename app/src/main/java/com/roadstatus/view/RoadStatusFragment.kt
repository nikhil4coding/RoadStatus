package com.roadstatus.view

import android.content.Context
import android.os.Bundle
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.LatLngBounds
import com.roadstatus.R
import com.roadstatus.view.model.BoundCoordinate
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_road_status.*

private const val DEFAULT_LAT = 51.509865
private const val DEFAULT_LNG = -0.118092

@AndroidEntryPoint
class RoadStatusFragment : Fragment(), OnMapReadyCallback {
    private val viewModel: RoadStatusViewModel by viewModels()
    private var gMap: GoogleMap? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? =
        inflater.inflate(R.layout.fragment_road_status, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupMap()
        setupObservers()
        setupListeners()
    }

    private fun setupMap() {
        map.onCreate(null)
        map.onResume()
        map.getMapAsync(this)
    }

    override fun onMapReady(gmap: GoogleMap) {
        gMap = gmap
        setupMapToInitialPosition()
    }

    private fun setupListeners() {
        findRoadName.addTextChangedListener { viewModel.onTextChanged(it.toString()) }

        findRoadName.setOnEditorActionListener { _, actionId, event ->
            if ((event != null && event.keyCode == KeyEvent.KEYCODE_ENTER) ||
                (actionId == EditorInfo.IME_ACTION_DONE)
            ) {
                fetchButton.performClick()
            }
            false
        }

        fetchButton.setOnClickListener {

            val inputMethodManager = activity?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            inputMethodManager.hideSoftInputFromWindow(requireView().windowToken, 0)

            val roadName = findRoadName.text.toString()
            if (roadName.isNotEmpty()) {
                viewModel.onFetchButtonClicked(roadName)
            }
        }
    }

    private fun setupObservers() {
        viewModel.viewState.observe(requireActivity(), { handleViewState(it) })
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
        gMap?.animateCamera(CameraUpdateFactory.newLatLngBounds(newBounds, 0))
    }

    private fun setupMapToInitialPosition() {
        val london = LatLng(DEFAULT_LAT, DEFAULT_LNG)
        gMap?.let { map ->
            map.setMinZoomPreference(10f)
            map.animateCamera(CameraUpdateFactory.newLatLng(london))
        }
    }

    companion object {
        @JvmStatic
        fun newInstance() = RoadStatusFragment()

        val TAG: String = RoadStatusFragment::class.java.name
    }
}