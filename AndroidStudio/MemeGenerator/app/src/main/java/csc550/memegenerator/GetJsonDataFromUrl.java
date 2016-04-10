package csc550.memegenerator;

import android.os.AsyncTask;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.net.URL;
import java.util.Map;

/**
 * Created by Christopher on 4/3/2016.
 */
public class GetJsonDataFromUrl extends AsyncTask<String, Void, Map<String, Object>> {

    public interface AsyncResponse {
        void onJsonDataLoaded(Map<String, Object> output);
    }

    public AsyncResponse delegate = null;

    public GetJsonDataFromUrl(AsyncResponse delegate) {
        this.delegate = delegate;
    }

    private Exception exception;

    @Override
    protected Map<String, Object> doInBackground(String... params) {
        try{
            URL url = new URL(params[0]);
            ObjectMapper mapper = new ObjectMapper();
            Map<String, Object> map = mapper.readValue(url, Map.class);
            return map;
        }
        catch (Exception e) {
            this.exception = e;
            return null;
        }
    }

    protected void onPostExecute(Map<String, Object> result) {
        delegate.onJsonDataLoaded(result);
    }
}
