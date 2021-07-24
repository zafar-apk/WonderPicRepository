package apk.zafar.wonderpic.ui.home.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import apk.zafar.wonderpic.R
import apk.zafar.wonderpic.model.pojo.Photo
import com.bumptech.glide.Glide

class PhotoAdapter : PagingDataAdapter<Photo, PhotoViewHolder>(diffUtil) {

    override fun onBindViewHolder(holder: PhotoViewHolder, position: Int) {

        val photo = getItem(position)

        holder.apply {
            Glide
                .with(holder.itemView.context)
                .load(photo?.src?.medium)
                .placeholder(R.drawable.placeholder)
                .into(photoImageView)

            authorTextView.text = photo?.photographer
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        PhotoViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.item_photo, parent, false)
        )


    companion object {
        val diffUtil = object : DiffUtil.ItemCallback<Photo>() {
            override fun areItemsTheSame(oldItem: Photo, newItem: Photo) = oldItem.id == newItem.id
            override fun areContentsTheSame(oldItem: Photo, newItem: Photo) = oldItem == newItem
        }
    }

}