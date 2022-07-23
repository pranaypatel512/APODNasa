package com.pranay.apodnasa.ui.pictureslist.viewholder

import android.widget.ImageView
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
        onItemClicked: (APODPictureItem, ImageView) -> Unit
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
            apodPictureItem.url.let {
                Glide.with(imageViewPlanet.context).load(it)
                    .apply(RequestOptions.noTransformation())
                    .placeholder(R.drawable.ic_launcher_background)
                    .error(R.drawable.ic_launcher_background)
                    .into(imageViewPlanet)
            }

            root.setOnClickListener {
                onItemClicked(apodPictureItem, binding.imageViewPlanet)
            }
        }
    }
}
