package beacons.leto.com.letoibeacons.api;

import android.content.Context;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import beacons.leto.com.letoibeacons.core.AppPreferences;
import retrofit.RequestInterceptor;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.converter.GsonConverter;
import retrofit.mime.TypedByteArray;

/**
 * Created by Renzo on 24/11/15.
 */


final public class LetoTogglRestClient {
    public static final String API_KEY = "something";

    public  static  String BASE_URL = "https://www.toggl.com/api/v8/";
    private static final String DECODED_ERROR_IS_NULL = "error is null!";
    private static LetoTogglApi sAppRestApi;

    private LetoTogglRestClient() {
    }

    public static void prepare(Context context) {
        setupRestClient(AppPreferences.getAuthToken());
    }

    public static LetoTogglApi getPureApi() {
        return sAppRestApi;
    }

    public static String decodeError(RetrofitError error) {
        if (error == null) {
            return DECODED_ERROR_IS_NULL;
        } else if (error.getResponse() != null && error.getResponse().getBody() != null) {
            return new String(((TypedByteArray) error.getResponse().getBody()).getBytes());
        } else {
            return error.getCause().toString();
        }
    }

    public static void setupRestClient(final String token) {
        RequestInterceptor requestInterceptor = new RequestInterceptor() {
            @Override
            public void intercept(RequestFacade request) {
                request.addHeader("Content-Type", "application/json");
                request.addHeader("Authorization", token);
            }
        };

        Gson gson = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create();

        RestAdapter.Builder builder = new RestAdapter.Builder()
                .setEndpoint(BASE_URL)
                .setConverter(new GsonConverter(gson))
                .setRequestInterceptor(requestInterceptor)
                .setLogLevel(RestAdapter.LogLevel.FULL);

        RestAdapter restAdapter = builder.build();
        sAppRestApi = restAdapter.create(LetoTogglApi.class);
    }

}
