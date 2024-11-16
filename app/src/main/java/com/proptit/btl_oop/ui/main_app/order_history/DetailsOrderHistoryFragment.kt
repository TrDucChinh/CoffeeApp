package com.proptit.btl_oop.ui.main_app.order_history

import android.os.Bundle
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewTreeObserver
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.proptit.btl_oop.R
import com.proptit.btl_oop.databinding.FragmentDetailsOrderHistoryBinding

class DetailsOrderHistoryFragment : Fragment() {
    private var _binding: FragmentDetailsOrderHistoryBinding? = null
    private val binding get() = _binding!!
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentDetailsOrderHistoryBinding.inflate(inflater, container, false)
        setupUI()
        return binding.root
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
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}