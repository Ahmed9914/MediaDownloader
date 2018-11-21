package nanodegree.udacity.media_downloader;

import android.net.Uri;
import android.util.Log;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * Created by Ahmd on 17/02/2018.
 */

public class NetworkUtils {
    private static final String TAG = NetworkUtils.class.getSimpleName();

    // Base url building
    public static final String ENDPOINT = "";

    public static URL buildUrl(String requiredMediaLink) {
        Uri builtUri = Uri.parse(ENDPOINT + requiredMediaLink);
        URL url = null;
        Log.v(TAG,"url is:" + builtUri.toString());
        try {
            url = new URL(builtUri.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        return url;
    }

    public static String getResponseFromHttpUrl(URL url) throws IOException {
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url(url)
                .build();
        try (Response response = client.newCall(request).execute()) {
            return response.body().string();
        }

    }

    public static List<Download> parseJson(String JsonResponse){
        List<Download> downloads = new ArrayList<>();
        try {
            JSONObject responseJSONObject = new JSONObject(JsonResponse);
            if (responseJSONObject.has("formats")) {
                JSONArray formatsArray = responseJSONObject.getJSONArray("formats");
                for (int i = 0; i < formatsArray.length(); i++) {
                    JSONObject downloadObject = formatsArray.getJSONObject(i);

                    if (downloadObject.has("ext")) {
                        if (downloadObject.get("ext").equals("mp4")
                                || downloadObject.get("ext").equals("3gp")
                                || downloadObject.get("ext").equals("mp3")
                                || downloadObject.get("ext").equals("webm")) {
                            String url = null;
                            String quality = null;
                            int size = 0;
                            if (downloadObject.has("url")) {
                                url = downloadObject.getString("url");
                            }
                            if (downloadObject.has("format")) {
                                quality = downloadObject.getString("format");
                            }
                            if (downloadObject.has("filesize")) {
                                size = (int) (downloadObject.getInt("filesize") / 1e6);
                            }
                            downloads.add(new Download(quality, size, url));
                        }
                    }
                }

            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        return downloads;
    }

}
