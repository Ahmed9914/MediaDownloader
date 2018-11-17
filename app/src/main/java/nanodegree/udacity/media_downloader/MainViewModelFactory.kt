package nanodegree.udacity.media_downloader

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class MainViewModelFactory(private val mDataSource: DataSource) : ViewModelProvider.NewInstanceFactory() {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {

        return MainActivityViewModel(mDataSource) as T
    }
}
