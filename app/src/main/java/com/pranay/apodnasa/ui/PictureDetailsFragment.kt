package com.pranay.apodnasa.ui

import android.graphics.drawable.Drawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.navGraphViewModels
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.request.target.Target
import com.pranay.apodnasa.R
import com.pranay.apodnasa.databinding.FragmentPictureDetailsBinding
import com.pranay.apodnasa.model.APODPictureItem
import com.pranay.apodnasa.model.State
import com.pranay.apodnasa.util.*
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

/**
 * A simple [Fragment] subclass as the second destination in the navigation.
 */
@AndroidEntryPoint
class PictureDetailsFragment : Fragment() {

    private var _binding: FragmentPictureDetailsBinding? = null
    private val binding get() = _binding!!
    private val picturesViewModel: PicturesViewModel by navGraphViewModels(R.id.nav_graph) {
        defaultViewModelProviderFactory
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPictureDetailsBinding.inflate(inflater, container, false)
        return binding.root

    }

    private fun observePicturesPosts() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                picturesViewModel.selectedItem.collect { state ->
                    when (state) {
                        is State.Loading -> {}
                        is State.Success -> {
                            setUpUi(state.data)
                        }
                        is State.Error -> {
                            showToast(state.message)
                        }
                    }
                }
            }
        }
    }

    private fun setUpUi(apodPictureItem: APODPictureItem) {
        binding.apply {
            progressBarView.progressBarView.show()
            textViewPhotoTitle.text = apodPictureItem.title
            val formattedDate = apodPictureItem.date?.toDate()?.formatDate()
            textViewPhotoDate.text = formattedDate ?: apodPictureItem.date
            apodPictureItem.copyright?.let {
                textViewPhotoCopyright.show()
                textViewPhotoCopyright.text =
                    textViewPhotoCopyright.context.getString(R.string.str_copyright, it)
            } ?: kotlin.run {
                textViewPhotoCopyright.hide()
            }
            apodPictureItem.url.let {
                Glide.with(imageViewPlanet.context).load(it)
                    .apply(RequestOptions.noTransformation())
                    .error(R.drawable.ic_launcher_background)
                    .listener(object : RequestListener<Drawable> {
                        override fun onLoadFailed(
                            e: GlideException?,
                            model: Any?,
                            target: Target<Drawable>?,
                            isFirstResource: Boolean
                        ): Boolean {
                            progressBarView.progressBarView.hide()
                            return false
                        }

                        override fun onResourceReady(
                            resource: Drawable?,
                            model: Any?,
                            target: Target<Drawable>?,
                            dataSource: DataSource?,
                            isFirstResource: Boolean
                        ): Boolean {
                            progressBarView.progressBarView.hide()
                            return false
                        }
                    })
                    .into(imageViewPlanet)

            }
            textViewPhotoDetails.text = apodPictureItem.explanation
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        observePicturesPosts()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}