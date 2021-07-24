package apk.zafar.wonderpic.ui.home.adapter

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import apk.zafar.wonderpic.R

class PhotoViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    val photoImageView: ImageView = itemView.findViewById(R.id.photo)
    val authorTextView: TextView = itemView.findViewById(R.id.author)
}