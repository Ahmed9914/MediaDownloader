package nanodegree.udacity.media_downloader;

import android.content.Context;
import android.util.Log;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import java.util.List;

public class DataSource {
    private static final String LOG_TAG = DataSource.class.getSimpleName();

    private static final Object LOCK = new Object();
    private static DataSource sInstance;
    private final Context mContext;
    private static MutableLiveData<List<Download>> downloads;

    private DataSource(Context context, AppExecutors executors) {
        mContext = context;
        AppExecutors mExecutors = executors;
        downloads = new MutableLiveData<>();
    }

    public static DataSource getInstance(Context context, AppExecutors executors) {
        Log.d(LOG_TAG, "Getting the network data source");
        if (sInstance == null) {
            synchronized (LOCK) {
                sInstance = new DataSource(context.getApplicationContext(), executors);
                Log.d(LOG_TAG, "Made new network data source");
            }
        }
        return sInstance;
    }

    public void fetchLink(final String link){
        AppExecutors.Companion.getInstance().networkIO().execute(new Runnable() {
            @Override
            public void run() {
                if (link != "") {
                    try {
                        downloads.postValue(NetworkUtils.parseJson(
                                NetworkUtils.getResponseFromHttpUrl(
                                        NetworkUtils.buildUrl(link)
                                )
                                )
                        );
                    } catch (Exception e){
                        e.printStackTrace();
                    }
                }
            }
        });
    }

    public LiveData<List<Download>> getDownloads(){
        return downloads;
    }
}
