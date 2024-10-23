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
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.setFragmentResultListener
import androidx.navigation.fragment.findNavController
import com.google.firebase.analytics.FirebaseAnalytics
import com.zzang.chongdae.R
import com.zzang.chongdae.common.firebase.FirebaseAnalyticsManager
import com.zzang.chongdae.databinding.DialogDatePickerBinding
import com.zzang.chongdae.databinding.FragmentOfferingWriteEssentialBinding
import com.zzang.chongdae.presentation.util.setDebouncedOnClickListener
import com.zzang.chongdae.presentation.view.address.AddressFinderDialog
import com.zzang.chongdae.presentation.view.main.MainActivity
import dagger.hilt.android.AndroidEntryPoint
import java.util.Calendar

@AndroidEntryPoint
class OfferingWriteEssentialFragment : Fragment(), OnDateTimeButtonsClickListener {
    private var _fragmentBinding: FragmentOfferingWriteEssentialBinding? = null
    private val fragmentBinding get() = _fragmentBinding!!

    private var _dateTimePickerBinding: DialogDatePickerBinding? = null
    private val dateTimePickerBinding get() = _dateTimePickerBinding!!

    private var toast: Toast? = null
    private val dialog: Dialog by lazy { Dialog(requireActivity()) }

    private val viewModel: OfferingWriteViewModel by activityViewModels()

    private val firebaseAnalytics: FirebaseAnalytics by lazy {
        FirebaseAnalytics.getInstance(requireContext())
    }

    private val firebaseAnalyticsManager: FirebaseAnalyticsManager by lazy {
        FirebaseAnalyticsManager(firebaseAnalytics)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        initBinding(inflater, container)
        return fragmentBinding.root
    }

    override fun onViewCreated(
        view: View,
        savedInstanceState: Bundle?,
    ) {
        super.onViewCreated(view, savedInstanceState)
        (activity as MainActivity).hideBottomNavigation()
        setUpObserve()
        selectMeetingDate()
        searchPlace()
    }

    private fun setUpObserve() {
        observeNavigateToOptionalEvent()
        observeUIState()
    }

    private fun observeUIState() {
        viewModel.writeUIState.observe(viewLifecycleOwner) { state ->
            when (state) {
                is WriteUIState.Error -> {
                    showToast(state.message)
                }
                is WriteUIState.Empty -> {
                    showToast(state.message)
                }
                is WriteUIState.InvalidInput -> {
                    showToast(state.message)
                }
                else -> {}
            }
        }
    }

    private fun searchPlace() {
        fragmentBinding.tvPlaceValue.setDebouncedOnClickListener(800L) {
            AddressFinderDialog().show(parentFragmentManager, this.tag)
        }
        setFragmentResultListener(AddressFinderDialog.ADDRESS_KEY) { _, bundle ->
            fragmentBinding.tvPlaceValue.text =
                bundle.getString(AddressFinderDialog.BUNDLE_ADDRESS_KEY)
        }
    }

    private fun selectMeetingDate() {
        viewModel.meetingDateChoiceEvent.observe(viewLifecycleOwner) {
            dialog.setContentView(dateTimePickerBinding.root)
            dialog.show()
            setDateTimeText(dateTimePickerBinding)
        }
    }

    private fun setDateTimeText(dateTimeBinding: DialogDatePickerBinding) {
        val calendar = Calendar.getInstance()
        updateDate(calendar, dateTimeBinding)
    }

    private fun updateDate(
        calendar: Calendar,
        dateTimeBinding: DialogDatePickerBinding,
    ) {
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)
        updateDateTextView(dateTimeBinding.tvDate, year, month, day)
        dateTimeBinding.pickerDate.minDate = System.currentTimeMillis()
        dateTimeBinding.pickerDate.setOnDateChangedListener { _, year, monthOfYear, dayOfMonth ->
            updateDateTextView(dateTimeBinding.tvDate, year, monthOfYear, dayOfMonth)
        }
    }

    override fun onConfirmButtonClick() {
        viewModel.updateMeetingDate(
            dateTimePickerBinding.tvDate.text.toString(),
        )
        dialog.dismiss()
    }

    override fun onCancelButtonClick() {
        dialog.dismiss()
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
        val amPm = if (hourOfDay < 12) getString(R.string.all_am) else getString(R.string.all_pm)
        val hour = if (hourOfDay % 12 == 0) 12 else hourOfDay % 12
        textView.text = getString(R.string.write_selected_time, amPm, hour, minute)
    }

    private fun initBinding(
        inflater: LayoutInflater,
        container: ViewGroup?,
    ) {
        _fragmentBinding = FragmentOfferingWriteEssentialBinding.inflate(inflater, container, false)
        fragmentBinding.vm = viewModel
        fragmentBinding.lifecycleOwner = viewLifecycleOwner

        _dateTimePickerBinding = DialogDatePickerBinding.inflate(inflater, container, false)
        dateTimePickerBinding.onClickListener = this
    }

    private fun observeNavigateToOptionalEvent() {
        viewModel.navigateToOptionalEvent.observe(viewLifecycleOwner) {
            firebaseAnalyticsManager.logSelectContentEvent(
                id = "submit_offering_write_essential_event",
                name = "submit_offering_write_essential_event",
                contentType = "button",
            )
            findNavController().navigate(R.id.action_offering_write_fragment_essential_to_offering_write_fragment_optional)
        }
    }

    private fun showToast(
        @StringRes messageId: Int,
    ) {
        toast?.cancel()
        toast =
            Toast.makeText(
                requireActivity(),
                getString(messageId),
                Toast.LENGTH_SHORT,
            )
        toast?.show()
    }

    override fun onResume() {
        super.onResume()
        firebaseAnalyticsManager.logScreenView(
            screenName = "OfferingWriteEssentialFragment",
            screenClass = this::class.java.simpleName,
        )
    }

    override fun onDestroy() {
        super.onDestroy()
        _fragmentBinding = null
        viewModel.initOfferingWriteInputs()
    }
}
