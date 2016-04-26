package csc550.memegenerator;

import com.android.volley.NetworkResponse;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HttpHeaderParser;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.UnsupportedEncodingException;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Created by Christopher on 4/11/2016.
 */
public class GeneratorGenerateRequest extends Request<Meme> {
    private final Response.Listener listener;
    private final Response.ErrorListener errorListener;

    public GeneratorGenerateRequest(String url, Response.Listener listener, Response.ErrorListener errorListener) {
        super(Method.GET, url, errorListener);
        this.listener = listener;
        this.errorListener = errorListener;
    }

    @Override
    protected Response<Meme> parseNetworkResponse(NetworkResponse response) {
        try {
            String json = new String(response.data, HttpHeaderParser.parseCharset(response.headers));
            Map<String, Object> map = new ObjectMapper().readValue(json, Map.class);

            Meme generatedMeme = null;
            LinkedHashMap generatorHash = (LinkedHashMap) map.get("result");

            return Response.success(generatedMeme, HttpHeaderParser.parseCacheHeaders(response));
        } catch (UnsupportedEncodingException e) {
            return Response.error(new ParseError(e));
        } catch (Exception e) {
            return Response.error(new ParseError(e));
        }
    }

    @Override
    protected void deliverResponse(Meme response) {
        listener.onResponse(response);
    }

    @Override
    public void deliverError(VolleyError error) {
        errorListener.onErrorResponse(error);
    }
}
