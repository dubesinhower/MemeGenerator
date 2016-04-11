package csc550.memegenerator;

import com.android.volley.NetworkResponse;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HttpHeaderParser;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Created by Christopher on 4/9/2016.
 */
public class MemeListRequest extends Request<ArrayList<Meme>>{
    private final Response.Listener listener;
    private final Response.ErrorListener errorListener;

    public MemeListRequest(String url, Response.Listener listener, Response.ErrorListener errorListener) {
        super(Method.GET, url, errorListener);
        this.listener = listener;
        this.errorListener = errorListener;
    }

    @Override
    protected Response<ArrayList<Meme>> parseNetworkResponse(NetworkResponse response) {
        try {
            String json = new String(response.data, HttpHeaderParser.parseCharset(response.headers));
            Map<String, Object> map = new ObjectMapper().readValue(json, Map.class);

            ArrayList<Meme> memes = new ArrayList<Meme>();
            ArrayList<LinkedHashMap> memesHash = (ArrayList<LinkedHashMap>) map.get("result");
            for (LinkedHashMap memeHash: memesHash) {
                String display_name = (String)memeHash.get("displayName");
                String generator_name = (String)memeHash.get("urlName");
                final String instance_url = (String)memeHash.get("instanceImageUrl");
                memes.add(new Meme(display_name, generator_name, instance_url, false));
            }
            return Response.success(memes, HttpHeaderParser.parseCacheHeaders(response));
        } catch (UnsupportedEncodingException e) {
            return Response.error(new ParseError(e));
        } catch (Exception e) {
            return Response.error(new ParseError(e));
        }
    }

    @Override
    protected void deliverResponse(ArrayList<Meme> response) {
        listener.onResponse(response);
    }

    @Override
    public void deliverError(VolleyError error) {
        errorListener.onErrorResponse(error);
    }
}
