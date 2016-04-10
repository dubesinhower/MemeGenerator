package csc550.memegenerator;

import android.graphics.drawable.Drawable;
import android.os.AsyncTask;

import java.io.InputStream;
import java.net.URL;

/**
 * Created by Christopher on 4/3/2016.
 */
public class GetDrawableFromUrl extends AsyncTask<String, Void, Drawable> {

    public interface AsyncResponse {
        void onDrawableLoaded(Drawable output);
    }

    public AsyncResponse delegate = null;

    public GetDrawableFromUrl(AsyncResponse delegate) {
        this.delegate = delegate;
    }

    private Exception exception;

    @Override
    protected Drawable doInBackground(String... params) {
        try {
            URL url = new URL(params[0]);
            InputStream is = (InputStream)url.getContent();
            Drawable d = Drawable.createFromStream(is, params[0]);
            is.close();
            return d;
        }
        catch (Exception e) {
            this.exception = e;
            return null;
        }
    }

    protected void onPostExecute(Drawable result) {
        delegate.onDrawableLoaded(result);
    }
}
