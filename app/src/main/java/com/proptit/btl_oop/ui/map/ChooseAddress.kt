package com.proptit.btl_oop.ui.map
import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.proptit.btl_oop.response.AutoCompleteResponse
import com.proptit.btl_oop.response.GoongApiService
import com.proptit.btl_oop.adapter.AddressAdapter
import com.proptit.btl_oop.api.APIKey
import com.proptit.btl_oop.databinding.FragmentChoseMapBinding
import com.proptit.btl_oop.model.Address
import com.proptit.btl_oop.utils.KeyBoard
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

import android.os.Handler
import android.os.Looper
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.proptit.btl_oop.viewmodel.ChoseAddressViewModel

class ChooseAddress : Fragment() {

    private var _binding: FragmentChoseMapBinding? = null
    private val binding get() = _binding!!
    private val goongApiService by lazy {
        val retrofit = Retrofit.Builder()
            .baseUrl("https://rsapi.goong.io/") // Base URL của Goong API
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        retrofit.create(GoongApiService::class.java)
    }
    private lateinit var choseAddressViewModel: ChoseAddressViewModel

    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private lateinit var currentLocation: android.location.Location

    private var addressSuggestions = mutableListOf<Address>()
    private lateinit var addressAdapter: AddressAdapter

    private val handler = Handler(Looper.getMainLooper())
    private var searchRunnable: Runnable? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentChoseMapBinding.inflate(inflater, container, false)
        choseAddressViewModel = ViewModelProvider(requireActivity()).get(ChoseAddressViewModel::class.java)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireActivity())

        if (ActivityCompat.checkSelfPermission(
                requireContext(), Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            getCurrentLocation()
        } else {
            requestPermissions(
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                1
            )
        }
        binding.icBack.setOnClickListener {
            findNavController().popBackStack()
        }

        addressAdapter = AddressAdapter(addressSuggestions) { address ->
            binding.etAddress.setText(address.fullAddress)
            choseAddressViewModel.setAddress(address.fullAddress)
            binding.rvAddressSuggestions.visibility = View.GONE
            KeyBoard.hideKeyboard(requireActivity() as AppCompatActivity)
            binding.etAddress.clearFocus()
            findNavController().popBackStack()
        }
        binding.rvAddressSuggestions.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        binding.rvAddressSuggestions.adapter = addressAdapter

        binding.etAddress.addTextChangedListener { editable ->
            val query = editable.toString()
            searchRunnable?.let { handler.removeCallbacks(it) }

            if (query.isNotEmpty() && ::currentLocation.isInitialized) {
                searchRunnable = Runnable {
                    fetchAddressSuggestions(query)
                    binding.rvAddressSuggestions.visibility = View.VISIBLE
                }
                handler.postDelayed(searchRunnable!!, 1000)
            } else {
                binding.rvAddressSuggestions.visibility = View.GONE
            }
        }
    }
    //Lấy current location
    private fun getCurrentLocation() {
        if (ActivityCompat.checkSelfPermission(
                requireContext(), Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            return
        }
        fusedLocationClient.lastLocation
            .addOnSuccessListener { location ->
                if (location != null) {
                    currentLocation = location
                } else {
                    Toast.makeText(context, "Location not available", Toast.LENGTH_SHORT).show()
                }
            }
            .addOnFailureListener { exception ->
                Log.e("Location", "Failed to get location", exception)
            }
    }

    //query địa chỉ
    private fun fetchAddressSuggestions(query: String) {
        val api = APIKey.GOONG_API_KEY
        val location = "${currentLocation.latitude},${currentLocation.longitude}"
        val input = query
        val call = goongApiService.getAutoCompleteSuggestions(api, location, input)

        call.enqueue(object : Callback<AutoCompleteResponse> {
            override fun onResponse(
                call: Call<AutoCompleteResponse>,
                response: Response<AutoCompleteResponse>
            ) {
                if (response.isSuccessful) {
                    val predictions = response.body()?.predictions ?: emptyList()
                    addressSuggestions.clear()
                    predictions.forEach {
                        val firstChar = getFirstPartOfAddress(it.description)
                        addressSuggestions.add(Address(firstChar, it.description))
                    }
                    addressAdapter.notifyDataSetChanged()
                } else {
                    Log.e("Retrofit", "Failed to fetch suggestions: ${response.message()}")
                }
            }

            override fun onFailure(call: Call<AutoCompleteResponse>, t: Throwable) {
                Log.e("Retrofit", "Error fetching data", t)
            }
        })
    }

    fun getFirstPartOfAddress(address: String): String {
        val parts = address.split(",", limit = 2)
        return parts.firstOrNull()?.trim() ?: ""
    }
}


