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
 * Created by Christopher on 4/11/2016.
 */
public class GeneratorListRequest extends Request<ArrayList<Generator>> {
    private final Response.Listener listener;
    private final Response.ErrorListener errorListener;

    public GeneratorListRequest(String url, Response.Listener listener, Response.ErrorListener errorListener) {
        super(Method.GET, url, errorListener);
        this.listener = listener;
        this.errorListener = errorListener;
    }

    @Override
    protected Response<ArrayList<Generator>> parseNetworkResponse(NetworkResponse response) {
        try {
            String json = new String(response.data, HttpHeaderParser.parseCharset(response.headers));
            Map<String, Object> map = new ObjectMapper().readValue(json, Map.class);

            ArrayList<Generator> list = new ArrayList<>();
            ArrayList<LinkedHashMap> memesHash = (ArrayList<LinkedHashMap>) map.get("result");
            for (LinkedHashMap memeHash: memesHash) {
                String displayName = (String)memeHash.get("displayName");
                String generatorName = (String)memeHash.get("urlName");
                int generatorId = (int)memeHash.get("generatorID");
                String imageUrl = (String)memeHash.get("imageUrl");
                String fileName = imageUrl.substring( imageUrl.lastIndexOf('/')+1, imageUrl.length() );

                // http://stackoverflow.com/questions/605696/get-file-name-from-url
                String fileNameWithoutExtn = fileName.substring(0, fileName.lastIndexOf('.'));
                int imageId = Integer.parseInt(fileNameWithoutExtn);

                list.add(new Generator(displayName, generatorName, generatorId, imageUrl, imageId));
            }
            return Response.success(list, HttpHeaderParser.parseCacheHeaders(response));
        } catch (UnsupportedEncodingException e) {
            return Response.error(new ParseError(e));
        } catch (Exception e) {
            return Response.error(new ParseError(e));
        }
    }

    @Override
    protected void deliverResponse(ArrayList<Generator> response) {
        listener.onResponse(response);
    }

    @Override
    public void deliverError(VolleyError error) {
        errorListener.onErrorResponse(error);
    }
}
