package nanodegree.udacity.media_downloader

import android.content.Context

object InjectorUtils {

    fun provideDataSource(context: Context): DataSource {
        val executors = AppExecutors.instance
        return DataSource.getInstance(context, executors)
    }

    fun provideMainActivityViewModelFactory(context: Context): MainViewModelFactory {
        val dataSource = provideDataSource(context.applicationContext)
        return MainViewModelFactory(dataSource)
    }
}
