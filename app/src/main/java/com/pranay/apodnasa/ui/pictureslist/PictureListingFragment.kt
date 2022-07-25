package com.pranay.apodnasa.ui.pictureslist

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.navigation.navGraphViewModels
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import androidx.work.*
import com.pranay.apodnasa.R
import com.pranay.apodnasa.databinding.FragmentPictureListingBinding
import com.pranay.apodnasa.model.APODPictureItem
import com.pranay.apodnasa.ui.PicturesViewModel
import com.pranay.apodnasa.ui.pictureslist.adapter.NasaPicturesListAdapter
import com.pranay.apodnasa.util.ItemOffsetDecoration
import com.pranay.apodnasa.util.hide
import com.pranay.apodnasa.util.show
import com.pranay.apodnasa.util.showToast
import com.pranay.apodnasa.worker.RemotePictureWorker
import dagger.hilt.android.AndroidEntryPoint
import java.time.Duration
import java.time.LocalTime
import java.util.concurrent.TimeUnit
import kotlinx.coroutines.launch

/**
 * A simple [Fragment] subclass as the default destination in the navigation.
 */
@AndroidEntryPoint
class PictureListingFragment : Fragment() {

    private var binding: FragmentPictureListingBinding? = null
    private val picturesViewModel: PicturesViewModel by navGraphViewModels(R.id.nav_graph) {
        defaultViewModelProviderFactory
    }

    private val pictureAdapter = NasaPicturesListAdapter(this::onItemClicked)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentPictureListingBinding.inflate(inflater, container, false)
        return binding?.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUpViews()
        observePicturesPosts()
    }

    /**
     * Setup recyclerview with stagger grid layout manager and set item decoration to make same space between item
     */
    private fun setUpViews() {
        binding?.picturesRecyclerView?.apply {
            adapter = pictureAdapter
            addItemDecoration(
                ItemOffsetDecoration(
                    requireContext(), R.dimen.dimen_space_item
                )
            )
            (layoutManager as StaggeredGridLayoutManager).gapStrategy =
                StaggeredGridLayoutManager.GAP_HANDLING_MOVE_ITEMS_BETWEEN_SPANS
        }
    }

    /**
     * Add item observer to collect items from database to display in list
     */
    private fun observePicturesPosts() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.CREATED) {
                picturesViewModel.getAllMediaItems().collect { state ->
                    if (state.isEmpty()) { // check if there is no items found in database
                        startWorkerFirstTime() // start one time worker to loading initial media from api and store in database
                    } else {
                        showLoading(false) // hide loading
                        showData(state) // show data in list
                    }
                }
            }
        }
    }

    /**
     * from list of data fill ListAdapter and show items
     */
    private fun showData(data: List<APODPictureItem>) {
        pictureAdapter.submitList(data)
    }

    /**
     * Starting initial first time onetime worker to load data and observe progress to show error if any or in case of success,
     * star Periodic Worker to run daily job
     **/
    private fun startWorkerFirstTime() {
        val constraints = Constraints.Builder()
            .setRequiredNetworkType(NetworkType.CONNECTED) // required internet to run a api job
            .build()
        val request = OneTimeWorkRequestBuilder<RemotePictureWorker>()
            .addTag(RemotePictureWorker.TAG)
            .setExpedited(OutOfQuotaPolicy.RUN_AS_NON_EXPEDITED_WORK_REQUEST) // run as quickly as possible using an expedited job
            .setConstraints(constraints)
            .build()
        val workId = request.id
        WorkManager.getInstance(requireContext())
            .enqueueUniqueWork(
                RemotePictureWorker.TAG,
                ExistingWorkPolicy.REPLACE,
                request
            ) // every time enqueue unique request only

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
                            }
                            binding?.viewNoPictures?.show()
                        }
                        WorkInfo.State.SUCCEEDED -> {
                            binding?.viewNoPictures?.hide()
                            showLoading(false)
                            setUpPeriodicWorkRequest()
                        }
                        WorkInfo.State.RUNNING -> {
                            binding?.viewNoPictures?.hide()
                            showLoading(true)
                        }
                        WorkInfo.State.ENQUEUED -> { //if initial request is ENQUEUED,due to constraint not satisfied, just show no picture placeholder and stop loading
                            binding?.viewNoPictures?.show()
                            showLoading(false)
                        }
                        else -> {

                        }
                    }
                }
            }
    }

    /**
     * starting Periodic Worker to run job on everyday 6am as per initial delay time
     */
    private fun setUpPeriodicWorkRequest() {
        val constraints = Constraints.Builder()
            .setRequiredNetworkType(NetworkType.CONNECTED) // required network for worker job
            .build()
        /*val request = PeriodicWorkRequestBuilder<RemotePictureWorker>(15,TimeUnit.MINUTES)
            .setInitialDelay(3,TimeUnit.MINUTES)*/
        val request = PeriodicWorkRequestBuilder<RemotePictureWorker>(1, TimeUnit.DAYS)
            .setInitialDelay(findInitialDelay(), TimeUnit.HOURS)
            .addTag(RemotePictureWorker.TAG)
            .setConstraints(constraints)
            .build()
        WorkManager.getInstance(requireContext())
            .enqueueUniquePeriodicWork(
                RemotePictureWorker.TAG,
                ExistingPeriodicWorkPolicy.REPLACE,
                request
            ) // every time enqueue unique request
    }

    /**
     * Find initial delay as per next day 6AM time with next hours count
     */
    private fun findInitialDelay(): Long {
        val todayDate = LocalTime.now() // current Time
        val todayDateInc = LocalTime.of(6, 0) // targeted morning 6am time
        val timeDifference =
            Duration.between(todayDate, todayDateInc) // find duration between two times
        // calculate initial delay time as per current hour of day.
        // if [timeDifference]'s hours is positive simple return initial delay, if [timeDifference]'s hours is negative add 24 hour to get initial positive delay
        return if (timeDifference.toHours() <= 0) timeDifference.toHours()
            .plus(24) else timeDifference.toHours()
    }

    /**
     * Show hide loading in screen while data fetching
     */
    private fun showLoading(isLoading: Boolean) {
        binding?.groupLoading?.isVisible = isLoading
    }

    /**
     * List item click listener to navigate of media item details screen
     */
    private fun onItemClicked(aPODPictureItem: APODPictureItem) {
        picturesViewModel.setSelection(aPODPictureItem)
        findNavController().navigate(R.id.action_PictureListingFragment_to_PictureDetailsFragment)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }
}