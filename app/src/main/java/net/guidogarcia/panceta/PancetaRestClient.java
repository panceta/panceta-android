package net.guidogarcia.panceta;

import com.loopj.android.http.*;

public class PancetaRestClient {
    private static final String BASE_URL = "http://music-guido.rhcloud.com";

    private static AsyncHttpClient client = new AsyncHttpClient();

    public static void post(String url, RequestParams params, AsyncHttpResponseHandler responseHandler) {
        client.post(getAbsoluteUrl(url), params, responseHandler);
    }

    private static String getAbsoluteUrl(String relativeUrl) {
        return BASE_URL + relativeUrl;
    }
}
