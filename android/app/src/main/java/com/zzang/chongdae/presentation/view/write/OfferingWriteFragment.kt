package com.zzang.chongdae.presentation.view.write

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.StringRes
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.zzang.chongdae.ChongdaeApp
import com.zzang.chongdae.R
import com.zzang.chongdae.databinding.DialogDateTimePickerBinding
import com.zzang.chongdae.databinding.FragmentOfferingWriteBinding
import com.zzang.chongdae.presentation.view.MainActivity
import java.util.Calendar

class OfferingWriteFragment : Fragment() {
    private var _binding: FragmentOfferingWriteBinding? = null
    private val binding get() = _binding!!
    private var toast: Toast? = null

    private val viewModel: OfferingWriteViewModel by viewModels {
        OfferingWriteViewModel.getFactory(
            offeringsRepository = (requireActivity().application as ChongdaeApp).offeringRepository,
        )
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        initBinding(inflater, container)
        return binding.root
    }

    override fun onViewCreated(
        view: View,
        savedInstanceState: Bundle?,
    ) {
        super.onViewCreated(view, savedInstanceState)
        (activity as MainActivity).hideBottomNavigation()
        observeInvalidInputEvent()
        showDateTimePickerDialog()
    }

    private fun showDateTimePickerDialog() {
        viewModel.deadlineChoiceEvent.observe(viewLifecycleOwner) {
            val dialog = Dialog(requireActivity())
            val dateTimeBinding = DialogDateTimePickerBinding.inflate(layoutInflater)
            dateTimeBinding.apply {
                vm = viewModel
                offeringWriteFragment = this@OfferingWriteFragment
            }
            dialog.setContentView(dateTimeBinding.root)
            dialog.show()
            setDateTimeText(dateTimeBinding)
            setSubmitButtonClickListener(dateTimeBinding, dialog)
            setCancleButtonClickListener(dateTimeBinding, dialog)
        }
    }

    private fun setCancleButtonClickListener(dateTimeBinding: DialogDateTimePickerBinding, dialog: Dialog) {
        dateTimeBinding.btnCancel.setOnClickListener {
            dialog.dismiss()
        }
    }

    private fun setSubmitButtonClickListener(dateTimeBinding: DialogDateTimePickerBinding, dialog: Dialog) {
        dateTimeBinding.btnSubmit.setOnClickListener {
            viewModel.updateDeadline(
                dateTimeBinding.tvDate.text.toString(),
                dateTimeBinding.tvTime.text.toString(),
            )
            dialog.dismiss()
        }
    }

    private fun setDateTimeText(dateTimeBinding: DialogDateTimePickerBinding) {
        val calendar = Calendar.getInstance()
        setDate(calendar, dateTimeBinding)
        setTime(calendar, dateTimeBinding)
    }

    private fun setTime(
        calendar: Calendar,
        dateTimeBinding: DialogDateTimePickerBinding,
    ) {
        val hourOfDay = calendar.get(Calendar.HOUR_OF_DAY)
        val minute = calendar.get(Calendar.MINUTE)
        updateTimeTextView(dateTimeBinding.tvTime, hourOfDay, minute)
        dateTimeBinding.pickerTime.setOnTimeChangedListener { _, hourOfDay, minute ->
            updateTimeTextView(dateTimeBinding.tvTime, hourOfDay, minute)
        }
    }

    private fun setDate(
        calendar: Calendar,
        dateTimeBinding: DialogDateTimePickerBinding,
    ) {
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)
        updateDateTextView(dateTimeBinding.tvDate, year, month, day)
        dateTimeBinding.pickerDate.setOnDateChangedListener { _, year, monthOfYear, dayOfMonth ->
            updateDateTextView(dateTimeBinding.tvDate, year, monthOfYear, dayOfMonth)
        }
    }

    private fun updateDateTextView(
        textView: TextView,
        year: Int,
        monthOfYear: Int,
        dayOfMonth: Int,
    ) {
        textView.text =
            getString(R.string.write_selected_date).format(
                year,
                monthOfYear + 1,
                dayOfMonth,
            )
    }

    private fun updateTimeTextView(
        textView: TextView,
        hourOfDay: Int,
        minute: Int,
    ) {
        val amPm = if (hourOfDay < 12) "오전" else "오후"
        val hour = if (hourOfDay % 12 == 0) 12 else hourOfDay % 12
        textView.text = getString(R.string.write_selected_time, amPm, hour, minute)
    }

    private fun initBinding(
        inflater: LayoutInflater,
        container: ViewGroup?,
    ) {
        _binding = FragmentOfferingWriteBinding.inflate(inflater, container, false)
        binding.vm = viewModel
        binding.lifecycleOwner = viewLifecycleOwner
    }

    private fun observeInvalidInputEvent() {
        viewModel.invalidTotalCountEvent.observe(viewLifecycleOwner) {
            showToast(R.string.write_invalid_total_count)
        }
        viewModel.invalidTotalPriceEvent.observe(viewLifecycleOwner) {
            showToast(R.string.write_invalid_total_price)
        }
        viewModel.invalidEachPriceEvent.observe(viewLifecycleOwner) {
            showToast(R.string.write_invalid_each_price)
        }
    }

    private fun showToast(
        @StringRes message: Int,
    ) {
        toast?.cancel()
        toast =
            Toast.makeText(
                requireActivity(),
                message,
                Toast.LENGTH_SHORT,
            )
        toast?.show()
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}
