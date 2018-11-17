package nanodegree.udacity.media_downloader

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel

class MainActivityViewModel(private val mDataSource: DataSource) : ViewModel() {

    val availableDownloads: LiveData<List<Download>>
        get() = mDataSource.downloads

    fun retrieveLink(link: String) {
        mDataSource.fetchLink(link)
    }
}
