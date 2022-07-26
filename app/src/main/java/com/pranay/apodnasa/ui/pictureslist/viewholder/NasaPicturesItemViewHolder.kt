package com.pranay.apodnasa.ui.pictureslist.viewholder

import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.pranay.apodnasa.R
import com.pranay.apodnasa.databinding.ListItemNasaPhotoBinding
import com.pranay.apodnasa.model.APODPictureItem
import com.pranay.apodnasa.ui.pictureslist.adapter.NasaPicturesListAdapter
import com.pranay.apodnasa.util.formatDate
import com.pranay.apodnasa.util.hide
import com.pranay.apodnasa.util.show
import com.pranay.apodnasa.util.toDate

/**
 * [RecyclerView.ViewHolder] implementation to inflate View for RecyclerView.
 * See [NasaPicturesListAdapter]]
 */
class NasaPicturesItemViewHolder(private val binding: ListItemNasaPhotoBinding) :
    RecyclerView.ViewHolder(binding.root) {

    fun bind(
        apodPictureItem: APODPictureItem,
        onItemClicked: (APODPictureItem) -> Unit
    ) {
        binding.apply {
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
            ivVideoItemPlay.isVisible = apodPictureItem.isVideo()
            val urlToLoad =
                if (apodPictureItem.isVideo()) apodPictureItem.thumbnailUrl else apodPictureItem.url
            urlToLoad?.let {
                Glide.with(imageViewPlanet.context).load(it)
                    .apply(RequestOptions.noTransformation())
                    .placeholder(R.drawable.ic_baseline_image_24)
                    .error(R.drawable.ic_baseline_image_24)
                    .into(imageViewPlanet)
            }

            root.setOnClickListener {
                onItemClicked(apodPictureItem)
            }
        }
    }
}
