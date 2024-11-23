package com.proptit.btl_oop.ui.main_app.order_history

import android.os.Bundle
import android.util.Log
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewTreeObserver
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.proptit.btl_oop.utils.Payment
import com.proptit.btl_oop.R
import com.proptit.btl_oop.adapter.OrderDetailAdapter
import com.proptit.btl_oop.databinding.FragmentDetailsOrderHistoryBinding
import com.proptit.btl_oop.viewmodel.HomeViewModel
import com.proptit.btl_oop.viewmodel.OrderHistoryViewModel
import kotlinx.coroutines.launch

class DetailsOrderHistoryFragment : Fragment() {
    private var _binding: FragmentDetailsOrderHistoryBinding? = null
    private val binding get() = _binding!!
    private lateinit var detailAdapter: OrderDetailAdapter
    private val homeViewModel: HomeViewModel by activityViewModels {
        HomeViewModel.HomeViewModelFactory(requireActivity().application)
    }
    private val orderHistoryViewModel: OrderHistoryViewModel by activityViewModels {
        OrderHistoryViewModel.OrderHistoryViewModelFactory(requireActivity().application)
    }
    private val args: DetailsOrderHistoryFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentDetailsOrderHistoryBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupUI()
        setUpAdapter()
    }


    private fun setupUI() {
        binding.apply {
            icBack.setOnClickListener { findNavController().popBackStack() }
            rvOrderItems.viewTreeObserver.addOnGlobalLayoutListener(object :
                ViewTreeObserver.OnGlobalLayoutListener {
                override fun onGlobalLayout() {
                    val recyclerViewHeight = rvOrderItems.height
                    val thresholdHeightPx = TypedValue.applyDimension(
                        TypedValue.COMPLEX_UNIT_DIP,
                        400f,
                        resources.displayMetrics
                    ).toInt()

                    if (recyclerViewHeight > thresholdHeightPx) {
                        btnExpand.visibility = View.VISIBLE
                    } else {
                        btnExpand.visibility = View.GONE
                    }
                    rvOrderItems.viewTreeObserver.removeOnGlobalLayoutListener(this)
                }
            })

            var isExpanded = false

            btnExpand.setOnClickListener {
                if (isExpanded) {
                    rvOrderItems.layoutParams.height = TypedValue.applyDimension(
                        TypedValue.COMPLEX_UNIT_DIP,
                        400f,
                        resources.displayMetrics
                    ).toInt()
                    btnExpand.setImageResource(R.drawable.ic_expand_more)
                } else {
                    rvOrderItems.layoutParams.height = ViewGroup.LayoutParams.WRAP_CONTENT
                    btnExpand.setImageResource(R.drawable.ic_expand_less)
                }
                rvOrderItems.requestLayout()
                isExpanded = !isExpanded
            }
        }
        binding.apply {
            lifecycleScope.launch {
                orderHistoryViewModel.orderHistory.collect { orders ->
                    val order = orders.find { it.date == args.orderID }
                    order?.let {
                        tvPaymentAmount.text = "${"%,d".format(it.totalPrice)}đ"
                        tvPriceAmount.text = "${"%,d".format(it.totalPrice)}đ"
                        checkPayment(it.payment)
                    }
                }
            }
        }
    }

    private fun setUpAdapter() {
        detailAdapter =
            OrderDetailAdapter(homeViewModel.coffees.value!!, homeViewModel.beans.value!!)
        binding.rvOrderItems.apply {
            adapter = detailAdapter
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        }
        lifecycleScope.launch {
            orderHistoryViewModel.orderHistory.collect { orders ->
                detailAdapter.submitList(orders.find { it.date == args.orderID }?.cart)
            }
        }
    }
    private fun checkPayment(payment: String){
        when (payment) {
            Payment.Cash.toString() -> {
                binding.apply {
                    tvPaymentMethod.text = Payment.Cash.toString()
                    ivPaymentMethod.setImageResource(R.drawable.ic_cash)
                }
            }
            Payment.VNPAY.toString() -> {
                binding.apply {
                    tvPaymentMethod.text = Payment.VNPAY.toString()
                    ivPaymentMethod.setImageResource(R.drawable.ic_vnpay)
                }
            }
            Payment.MoMo.toString() -> {
                binding.apply {
                    tvPaymentMethod.text = Payment.MoMo.toString()
                    ivPaymentMethod.setImageResource(R.drawable.ic_momo)
                }
            }
            Payment.ZaloPay.toString() -> {
                binding.apply {
                    tvPaymentMethod.text = Payment.ZaloPay.toString()
                    ivPaymentMethod.setImageResource(R.drawable.ic_zalopay)
                }
            }
            Payment.Cards.toString() -> {
                binding.apply {
                    tvPaymentMethod.text = Payment.Cards.toString()
                    ivPaymentMethod.setImageResource(R.drawable.ic_card)
                }
            }}
    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}