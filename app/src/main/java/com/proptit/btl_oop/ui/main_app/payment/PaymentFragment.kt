package com.proptit.btl_oop.ui.main_app.payment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.core.content.ContextCompat
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.proptit.btl_oop.api.APIKey
/*import com.proptit.btl_oop.AutoCompleteResponse
import com.proptit.btl_oop.GoongApiService*/
import com.proptit.btl_oop.utils.Payment
import com.proptit.btl_oop.R
import com.proptit.btl_oop.utils.SaveToDB
import com.proptit.btl_oop.databinding.FragmentPaymentBinding
import com.proptit.btl_oop.model.OrderHistory
import com.proptit.btl_oop.ui.main_app.dialog.SuccessDialogFragment
import com.proptit.btl_oop.viewmodel.CartViewModel
import com.proptit.btl_oop.viewmodel.OrderHistoryViewModel
/*import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory*/

class PaymentFragment : Fragment() {
    private var _binding: FragmentPaymentBinding? = null
    private val binding get() = _binding!!
    private val cartViewModel: CartViewModel by activityViewModels(){
        CartViewModel.CartViewModelFactory(requireActivity().application)
    }
    private val orderHistoryViewModel: OrderHistoryViewModel by activityViewModels(){
        OrderHistoryViewModel.OrderHistoryViewModelFactory(requireActivity().application)
    }

    /*private val goongApiService by lazy {
        val retrofit = Retrofit.Builder()
            .baseUrl("https://rsapi.goong.io/") // Base URL của Goong API
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        retrofit.create(GoongApiService::class.java)
    }*/
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentPaymentBinding.inflate(inflater, container, false)
        setupUI()
        return binding.root
    }

    private fun setupUI() {
        binding.apply {
            btnBack.setOnClickListener { findNavController().popBackStack() }

            paymentMethodGroup.setOnCheckedChangeListener { _, _ ->
                // Nếu có radio button được chọn thì btnPay mới được enable
                val isEnabled = paymentMethodGroup.checkedRadioButtonId != -1
                btnPay.isEnabled = isEnabled

                val backgroundColor = if (isEnabled) R.color.orange else R.color.light_orange
                val textColor = if (isEnabled) R.color.white else R.color.white

                btnPay.backgroundTintList = ContextCompat.getColorStateList(requireContext(), backgroundColor)
                btnPay.setTextColor(ContextCompat.getColor(requireContext(), textColor))
            }
            btnPay.setOnClickListener {
                val orderItem = cartViewModel.cartItem.value
                val orderHistory = OrderHistory(
                    System.currentTimeMillis(),
                    orderItem,
                    orderItem.sumBy { it.price * it.quantity },
                    checkPayment()
                )
                SaveToDB.saveOrderHistoryToDB(orderHistory)
                SaveToDB.clearCart()
//                dismiss()
                SuccessDialogFragment().show(
                    parentFragmentManager,
                    "SuccessDialog"
                )
            }
            // disable btnPay neu khong duoc chon
            val isEnabled = paymentMethodGroup.checkedRadioButtonId != -1
            btnPay.isEnabled = isEnabled
            val backgroundColor = if (isEnabled) R.color.orange else R.color.light_orange
            val textColor = if (isEnabled) R.color.white else R.color.white

            btnPay.backgroundTintList = ContextCompat.getColorStateList(requireContext(), backgroundColor)
            btnPay.setTextColor(ContextCompat.getColor(requireContext(), textColor))

           /* binding.etAddress.setOnClickListener {

            }*/

        }
        /*binding.etAddress.addTextChangedListener {
            val query = it.toString()
            if (query.isNotEmpty()){
                fetchAddressSuggestions(query)
            }
        }
        binding.etAddress.setOnItemClickListener { parent, _, position, _ ->
            val selectedAddress = parent.getItemAtPosition(position).toString()
            binding.etAddress.setText(selectedAddress)
        }*/
    }

    /*private fun fetchAddressSuggestions(query: String) {
        val call = goongApiService.getAutoCompleteSuggestions(query, APIKey.GOONG_API_KEY)

        call.enqueue(object : Callback<AutoCompleteResponse> {
            override fun onResponse(call: Call<AutoCompleteResponse>, response: Response<AutoCompleteResponse>) {
                if (response.isSuccessful) {
                    val predictions = response.body()?.predictions ?: emptyList()
                    val addressList = predictions.map { it.description }

                    val adapter = ArrayAdapter(requireContext(), android.R.layout.simple_dropdown_item_1line, addressList)
                     binding.etAddress.setAdapter(adapter)
                } else {
                    Log.e("Retrofit", "Failed to fetch suggestions: ${response.message()}")
                }
            }

            override fun onFailure(call: Call<AutoCompleteResponse>, t: Throwable) {
                Log.e("Retrofit", "Error fetching data", t)
            }
        })
    }*/
    private fun checkPayment(): String{
        return when(binding.paymentMethodGroup.checkedRadioButtonId){
            R.id.cashRadioButton -> Payment.Cash.toString()
            R.id.vnPayRadioButton -> Payment.VNPAY.toString()
            R.id.momoRadioButton -> Payment.MoMo.toString()
            R.id.zaloPayRadioButton -> Payment.ZaloPay.toString()
            R.id.cardsRadioButton -> Payment.Cards.toString()
            else -> ""
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}