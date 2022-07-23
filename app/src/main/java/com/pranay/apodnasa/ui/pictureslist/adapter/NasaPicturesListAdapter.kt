package com.pranay.apodnasa.ui.pictureslist.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.pranay.apodnasa.databinding.ListItemNasaPhotoBinding
import com.pranay.apodnasa.model.APODPictureItem
import com.pranay.apodnasa.ui.pictureslist.viewholder.NasaPicturesItemViewHolder

/**
 * Adapter class [RecyclerView.Adapter] for [RecyclerView] which binds [APODPictureItem] along with [NasaPicturesItemViewHolder]
 * @param onItemClicked which will receive callback when item is clicked.
 */
class NasaPicturesListAdapter(
    private val onItemClicked: (APODPictureItem, ImageView) -> Unit
) : ListAdapter<APODPictureItem, NasaPicturesItemViewHolder>(DIFF_CALLBACK) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = NasaPicturesItemViewHolder(
        ListItemNasaPhotoBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
    )

    override fun onBindViewHolder(holder: NasaPicturesItemViewHolder, position: Int) =
        holder.bind(getItem(position), onItemClicked)

    companion object {
        private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<APODPictureItem>() {
            override fun areItemsTheSame(
                oldItem: APODPictureItem,
                newItem: APODPictureItem
            ): Boolean =
                oldItem.url == newItem.url

            override fun areContentsTheSame(
                oldItem: APODPictureItem,
                newItem: APODPictureItem
            ): Boolean =
                oldItem == newItem
        }
    }
}
