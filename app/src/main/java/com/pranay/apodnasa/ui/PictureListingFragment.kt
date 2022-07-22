package com.pranay.apodnasa.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.work.*
import com.pranay.apodnasa.databinding.FragmentPictureListingBinding
import com.pranay.apodnasa.model.APODPictureItem
import com.pranay.apodnasa.model.State
import com.pranay.apodnasa.util.Logger
import com.pranay.apodnasa.util.showToast
import com.pranay.apodnasa.worker.RemotePictureWorker
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

/**
 * A simple [Fragment] subclass as the default destination in the navigation.
 */
@AndroidEntryPoint
class PictureListingFragment : Fragment() {

    private var _binding: FragmentPictureListingBinding? = null
    private val binding get() = _binding!!
    private val picturesViewModel: PicturesViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPictureListingBinding.inflate(inflater, container, false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        observePosts()
    }

    private fun observePosts() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                picturesViewModel.pictures.collect { state ->
                    when (state) {
                        is State.Loading -> showLoading(true)
                        is State.Success -> {
                            showLoading(false)
                            if (state.data.isNotEmpty()) {
                                showData(state.data)
                            } else {
                                // assuming for the first time start worker to fetch remote images
                                startWorkerFirstTime()
                            }
                        }
                        is State.Error -> {
                            showToast(state.message)
                            showLoading(false)
                        }
                    }
                }
            }
        }
    }

    private fun showData(data: List<APODPictureItem>) {
        Logger.log("AAAData--->", data.toString())
    }

    private fun startWorkerFirstTime() {
        Logger.log("AAAWorkManage--->", "Started")
        val request = OneTimeWorkRequestBuilder<RemotePictureWorker>()
            .addTag(RemotePictureWorker.TAG)
            .setExpedited(OutOfQuotaPolicy.RUN_AS_NON_EXPEDITED_WORK_REQUEST)
            .build()
        val workId = request.id
        WorkManager.getInstance(requireContext())
            .enqueueUniqueWork(RemotePictureWorker.TAG, ExistingWorkPolicy.REPLACE, request)

        WorkManager.getInstance(requireContext())
            .getWorkInfoByIdLiveData(workId)
            .observe(viewLifecycleOwner) { workInfo: WorkInfo? ->
                if (workInfo != null) {
                    when (workInfo.state) {
                        WorkInfo.State.FAILED, WorkInfo.State.CANCELLED -> {
                            showLoading(false)
                            val errorValue =
                                workInfo.outputData.getString(RemotePictureWorker.Error)
                            if (errorValue != null) // error case
                            {
                                showToast(errorValue)
                                //TOdo show error page if any issue with first time loading
                            }
                        }
                        WorkInfo.State.SUCCEEDED -> {
                            showLoading(false)
                            picturesViewModel.getPictures()
                        }
                        WorkInfo.State.RUNNING -> {
                            showLoading(true)
                        }
                        else -> {
                            showLoading(false)
                        }
                    }
                }
            }
    }

    private fun showLoading(isLoading: Boolean) {
        binding.groupLoading.isVisible = isLoading
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}