package nanodegree.udacity.media_downloader

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.provider.Browser
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class DownloadAdapter(private val mContext: Context) :
    RecyclerView.Adapter<DownloadAdapter.DownloadsAdapterViewHolder>() {
    var downloads: List<Download>? = null
        set(downloads) {
            field = downloads
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DownloadsAdapterViewHolder {
        val view = LayoutInflater.from(mContext)
            .inflate(R.layout.download_item, parent, false)

        return DownloadsAdapterViewHolder(view)
    }

    @SuppressLint("StringFormatInvalid")
    override fun onBindViewHolder(holder: DownloadsAdapterViewHolder, position: Int) {
        val (quality, size, url) = this.downloads!![position]
        holder.qualityTv.text = mContext.getString(R.string.quality_text, quality)
        holder.sizeTv.text = mContext.getString(R.string.size_text, size)
        holder.downloadButton.setOnClickListener {
            val uri = Uri.parse(url)
            val intent = Intent(Intent.ACTION_VIEW, uri)
            intent.putExtra(Browser.EXTRA_APPLICATION_ID, mContext.packageName)
            try {
                mContext.startActivity(intent)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    override fun getItemCount(): Int {
        return if (this.downloads == null) {
            0
        } else this.downloads!!.size
    }

    inner class DownloadsAdapterViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        internal val qualityTv: TextView
        internal val sizeTv: TextView
        internal val downloadButton: Button

        init {
            qualityTv = itemView.findViewById(R.id.quality_tv)
            sizeTv = itemView.findViewById(R.id.size_tv)
            downloadButton = itemView.findViewById(R.id.download_button)
        }

    }
}
