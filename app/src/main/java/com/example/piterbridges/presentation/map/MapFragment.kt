package com.example.piterbridges.presentation.map

import android.content.Context
import android.os.Bundle
import android.view.View
import androidx.appcompat.content.res.AppCompatResources
import androidx.core.graphics.drawable.toBitmap
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.isVisible
import androidx.core.view.updatePadding
import androidx.navigation.fragment.findNavController
import by.kirich1409.viewbindingdelegate.viewBinding
import com.example.piterbridges.R
import com.example.piterbridges.databinding.FragmentMapBinding
import com.example.piterbridges.databinding.ViewMapPinBinding
import com.example.piterbridges.presentation.BaseFragment
import com.example.piterbridges.presentation.LoadState
import com.example.piterbridges.presentation.bridges.model.Bridge
import com.example.piterbridges.presentation.bridges.model.Divorces
import com.example.piterbridges.presentation.bridges.model.stateBridge
import com.example.piterbridges.presentation.map.extensions.toBitmap
import com.yandex.mapkit.MapKitFactory
import com.yandex.mapkit.geometry.Geometry
import com.yandex.mapkit.geometry.Point
import com.yandex.mapkit.map.CameraPosition
import com.yandex.mapkit.map.ClusterListener
import com.yandex.mapkit.map.ClusterizedPlacemarkCollection
import com.yandex.mapkit.map.InputListener
import com.yandex.mapkit.map.Map
import com.yandex.mapkit.map.MapObjectTapListener
import com.yandex.mapkit.map.PlacemarkMapObject
import com.yandex.runtime.image.ImageProvider

private const val YANDEX_ZOOM_REDUCTION_COEFFICIENT = 0.8f
private const val CLUSTER_RADIUS = 40.0
private const val CLUSTER_MIN_ZOOM = 15

class MapFragment : BaseFragment(R.layout.fragment_map) {
    private val binding by viewBinding(FragmentMapBinding::bind)
    private val viewModel: MapViewModel by appViewModels()

    private val mapPinSize by lazy { resources.getDimensionPixelSize(R.dimen.map_pin_size) }
    private val mapObjects = mutableMapOf<PlacemarkMapObject, Bridge>()
    private val mapPinViewBinding by lazy { ViewMapPinBinding.inflate(layoutInflater) }
    private var bridgeId = 0
    private var bridgeDivorces = listOf<Divorces>()
    private val inputListener = (object : InputListener {
        override fun onMapTap(p0: Map, p1: Point) {
            binding.bridgeRowViewInfo.visibility = View.GONE
        }

        override fun onMapLongTap(p0: Map, p1: Point) {
        }
    })
    private val tapListener = (MapObjectTapListener { mapObject, _ ->
        mapObjects[mapObject]?.let { bridge ->
            bridgeId = bridge.id
            bridgeDivorces = bridge.divorces
            binding.bridgeRowViewInfo.visibility = View.VISIBLE
            when (stateBridge(bridge.divorces)) {
                0 -> binding.bridgeRowViewInfo.setImage(R.drawable.ic_brige_soon)
                1 -> binding.bridgeRowViewInfo.setImage(R.drawable.ic_brige_normal)
                2 -> binding.bridgeRowViewInfo.setImage(R.drawable.ic_brige_late)
            }
            binding.bridgeRowViewInfo.setTitleText(bridge.title)
            val stringBuilderTime = StringBuilder()
            bridge.divorces.forEach { position ->
                stringBuilderTime.append(position.startTime).append(" - ").append(position.endTime)
                    .append("    ")
            }
            binding.bridgeRowViewInfo.setTimeText(stringBuilderTime)
        }
        true
    })
    private val clusterListener = ClusterListener { cluster ->
        mapPinViewBinding.textViewName.text = cluster.size.toString()
        cluster.addClusterTapListener { selectedCluster ->
            val points = selectedCluster.placemarks.map { it.geometry }
            val boundingBoxBuilder = BoundingBoxBuilder().apply {
                points.forEach { point ->
                    include(point)
                }
            }
            val boundingBoxCameraPosition = binding.mapView.mapWindow.map.cameraPosition(
                Geometry.fromBoundingBox(boundingBoxBuilder.build()),
                0f,
                0f,
                binding.mapView.mapWindow.focusRect
            )
            val targetCameraPosition = CameraPosition(
                boundingBoxCameraPosition.target,
                boundingBoxCameraPosition.zoom - YANDEX_ZOOM_REDUCTION_COEFFICIENT,
                boundingBoxCameraPosition.azimuth,
                boundingBoxCameraPosition.tilt,
            )
            binding.mapView.mapWindow.map.move(targetCameraPosition)
            true
        }
        cluster.appearance.setIcon(
            ImageProvider.fromBitmap(mapPinViewBinding.root.toBitmap(mapPinSize))
        )
    }
    private var collection: ClusterizedPlacemarkCollection? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (savedInstanceState == null) {
            viewModel.loadBridges()
        }
        MapKitFactory.initialize(context)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.bridgeRowViewInfo.setOnClickListener {
            val action = MapFragmentDirections.actionMapFragmentToInfoBridgeFragment(
                bridgeId,
                bridgeDivorces.toTypedArray()
            )
            findNavController().navigate(action)
        }
        ViewCompat.setOnApplyWindowInsetsListener(binding.appBarLayout) { view, windowInsets ->
            val insets = windowInsets.getInsets(WindowInsetsCompat.Type.statusBars())
            view.updatePadding(top = insets.top)
            windowInsets
        }
        binding.toolbar.menu.findItem(R.id.itemList).setOnMenuItemClickListener {
            val action = MapFragmentDirections.actionMapFragmentToListBridgeFragment()
            findNavController().navigate(action)
            true
        }
        binding.mapView.mapWindow.map.move(
            CameraPosition(Point(59.94623925, 30.34531475), 12f, 0f, 0f)
        )
        binding.buttonRepeat.setOnClickListener {
            viewModel.loadBridges()
            observerBridges()
        }
        binding.mapView.mapWindow.map.addInputListener(inputListener)
        collection = binding.mapView.mapWindow.map.mapObjects.addClusterizedPlacemarkCollection(
            clusterListener
        )
        observerBridges()
    }

    private fun observerBridges() {
        viewModel.bridgesLiveData.observe(viewLifecycleOwner) { state ->
            when (state) {
                is LoadState.Data -> {
                    binding.progressBar.isVisible = false
                    if (state.data.isNotEmpty()) {
                        state.data.forEach { bridge ->
                            collection?.addPlacemark()?.apply {
                                geometry = Point(bridge.lat, bridge.lng)
                                when (stateBridge(bridge.divorces)) {
                                    0 -> setIcon(
                                        showIcon(binding.root.context, R.drawable.ic_brige_soon)
                                    )

                                    1 -> setIcon(
                                        showIcon(binding.root.context, R.drawable.ic_brige_normal)
                                    )

                                    2 -> setIcon(
                                        showIcon(binding.root.context, R.drawable.ic_brige_late)
                                    )
                                }
                                addTapListener(tapListener)
                                mapObjects[this] = bridge
                            }
                        }
                        collection?.clusterPlacemarks(CLUSTER_RADIUS, CLUSTER_MIN_ZOOM)
                    } else {
                        binding.progressBar.isVisible = false
                        binding.mapView.visibility = View.GONE
                        binding.textViewError.visibility = View.VISIBLE
                        binding.textViewError.text = getString(R.string.data_not_download)
                        binding.buttonRepeat.visibility = View.VISIBLE
                    }
                }

                is LoadState.Error -> {
                    binding.progressBar.isVisible = false
                    binding.mapView.visibility = View.GONE
                    binding.textViewError.visibility = View.VISIBLE
                    binding.textViewError.text = state.toString()
                }

                is LoadState.Loading -> {
                    binding.progressBar.isVisible = true
                }
            }
        }
    }

    private fun showIcon(context: Context, resId: Int): ImageProvider {
        return ImageProvider.fromBitmap(
            AppCompatResources.getDrawable(
                context, resId
            )?.toBitmap()
        )
    }

    override fun onStart() {
        super.onStart()
        MapKitFactory.getInstance().onStart()
        binding.mapView.onStart()
    }

    override fun onStop() {
        binding.mapView.onStop()
        MapKitFactory.getInstance().onStop()
        super.onStop()
    }

    override fun onDestroyView() {
        binding.mapView.mapWindow.map.mapObjects.clear()
        super.onDestroyView()
    }
}