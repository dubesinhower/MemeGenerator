package csc550.memegenerator;

import android.os.AsyncTask;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.net.URL;
import java.util.Map;

/**
 * Created by Christopher on 4/3/2016.
 */
public class RetrieveJsonDataFromUrlTask extends AsyncTask<String, Void, Map<String, Object>> {

    public interface AsyncResponse {
        void processFinish(Map<String, Object> output);
    }

    public AsyncResponse delegate = null;

    public RetrieveJsonDataFromUrlTask(AsyncResponse delegate) {
        this.delegate = delegate;
    }

    private Exception exception;

    @Override
    protected Map<String, Object> doInBackground(String... params) {
        try{
            URL url = new URL(params[0]);
            ObjectMapper mapper = new ObjectMapper();
            return mapper.readValue(url, Map.class);
        }
        catch (Exception e) {
            this.exception = e;
            return null;
        }
    }

    protected void onPostExecute(Map<String, Object> result) {
        delegate.processFinish(result);
    }
}
