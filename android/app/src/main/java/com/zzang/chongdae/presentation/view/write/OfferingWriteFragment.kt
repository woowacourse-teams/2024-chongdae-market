package com.zzang.chongdae.presentation.view.write

import android.app.Dialog
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts.PickVisualMedia
import androidx.annotation.StringRes
import androidx.fragment.app.Fragment
import androidx.fragment.app.setFragmentResultListener
import androidx.fragment.app.viewModels
import com.zzang.chongdae.ChongdaeApp
import com.zzang.chongdae.R
import com.zzang.chongdae.databinding.DialogDateTimePickerBinding
import com.zzang.chongdae.databinding.FragmentOfferingWriteBinding
import com.zzang.chongdae.presentation.util.FileUtils
import com.zzang.chongdae.presentation.util.PermissionManager
import com.zzang.chongdae.presentation.view.MainActivity
import com.zzang.chongdae.presentation.view.address.AddressFinderDialog
import java.util.Calendar

class OfferingWriteFragment : Fragment(), OnOfferingWriteClickListener {
    private var _fragmentBinding: FragmentOfferingWriteBinding? = null
    private val fragmentBinding get() = _fragmentBinding!!

    private var _dateTimePickerBinding: DialogDateTimePickerBinding? = null
    private val dateTimePickerBinding get() = _dateTimePickerBinding!!
    private var toast: Toast? = null
    private val dialog: Dialog by lazy { Dialog(requireActivity()) }
    private lateinit var permissionManager: PermissionManager
    private lateinit var pickMediaLauncher: ActivityResultLauncher<PickVisualMediaRequest>

    private val viewModel: OfferingWriteViewModel by viewModels {
        OfferingWriteViewModel.getFactory(
            offeringRepository = (requireActivity().application as ChongdaeApp).offeringRepository,
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setUpPermissionManager()
        initializePhotoPicker()
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
        observeInvalidInputEvent()
        observeFinishEvent()
        observeImageUploadEvent()
        selectDeadline()
        searchPlace()
    }

    private fun initializePhotoPicker() {
        pickMediaLauncher =
            registerForActivityResult(PickVisualMedia()) { uri: Uri? ->
                handleMediaResult(uri)
            }
    }

    private fun launchPhotoPicker() {
        pickMediaLauncher.launch(PickVisualMediaRequest(PickVisualMedia.ImageOnly))
    }

    private fun handleMediaResult(uri: Uri?) {
        if (uri != null) {
            val multipartBodyPart = FileUtils.getMultipartBodyPart(requireContext(), uri, "image")
            if (multipartBodyPart != null) {
                viewModel.uploadImageFile(multipartBodyPart)
            } else {
                showToast(R.string.all_error_file_conversion)
            }
        }
    }

    private fun setUpPermissionManager() {
        permissionManager =
            PermissionManager(
                fragment = this,
                onPermissionGranted = { onPermissionsGranted() },
                onPermissionDenied = { onPermissionsDenied() },
            )
    }

    private fun observeImageUploadEvent() {
        viewModel.imageUploadEvent.observe(viewLifecycleOwner) {
            if (permissionManager.isAndroid13OrAbove()) {
                launchPhotoPicker()
            } else {
                permissionManager.requestPermissions()
            }
        }
    }

    private fun onPermissionsGranted() {
        showToast(R.string.all_permission_granted)
        launchPhotoPicker()
    }

    private fun onPermissionsDenied() {
        showToast(R.string.all_permission_denied)
    }

    private fun searchPlace() {
        fragmentBinding.tvPlaceValue.setOnClickListener {
            AddressFinderDialog().show(parentFragmentManager, this.tag)
        }
        setFragmentResultListener(AddressFinderDialog.ADDRESS_KEY) { _, bundle ->
            fragmentBinding.tvPlaceValue.text =
                bundle.getString(AddressFinderDialog.BUNDLE_ADDRESS_KEY)
        }
    }

    private fun selectDeadline() {
        viewModel.deadlineChoiceEvent.observe(viewLifecycleOwner) {
            dialog.setContentView(dateTimePickerBinding.root)
            dialog.show()
            setDateTimeText(dateTimePickerBinding)
        }
    }

    override fun onDateTimeSubmitButtonClick() {
        viewModel.updateDeadline(
            dateTimePickerBinding.tvDate.text.toString(),
            dateTimePickerBinding.tvTime.text.toString(),
        )
        dialog.dismiss()
    }

    override fun onDateTimeCancelButtonClick() {
        dialog.dismiss()
    }

    private fun setDateTimeText(dateTimeBinding: DialogDateTimePickerBinding) {
        val calendar = Calendar.getInstance()
        updateDate(calendar, dateTimeBinding)
        updateTime(calendar, dateTimeBinding)
    }

    private fun updateTime(
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

    private fun updateDate(
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
        val amPm = if (hourOfDay < 12) getString(R.string.all_am) else getString(R.string.all_pm)
        val hour = if (hourOfDay % 12 == 0) 12 else hourOfDay % 12
        textView.text = getString(R.string.write_selected_time, amPm, hour, minute)
    }

    private fun initBinding(
        inflater: LayoutInflater,
        container: ViewGroup?,
    ) {
        _fragmentBinding = FragmentOfferingWriteBinding.inflate(inflater, container, false)
        fragmentBinding.vm = viewModel
        fragmentBinding.lifecycleOwner = viewLifecycleOwner

        _dateTimePickerBinding = DialogDateTimePickerBinding.inflate(inflater, container, false)
        dateTimePickerBinding.vm = viewModel
        dateTimePickerBinding.onClickListener = this
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
        viewModel.originPriceCheaperThanSplitPriceEvent.observe(viewLifecycleOwner) {
            showToast(R.string.write_origin_price_cheaper_than_total_price)
        }
        viewModel.errorEvent.observe(viewLifecycleOwner) {
            showToast(it)
        }
    }

    private fun observeFinishEvent() {
        viewModel.finishEvent.observe(viewLifecycleOwner) {
            showToast(R.string.write_success_writing)
            parentFragmentManager.popBackStack()
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
        _fragmentBinding = null
    }
}
