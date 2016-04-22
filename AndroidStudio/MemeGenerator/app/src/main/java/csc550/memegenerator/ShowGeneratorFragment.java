package csc550.memegenerator;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.Response.ErrorListener;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.w3c.dom.Text;

import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Map;

public class ShowGeneratorFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_show_generator, container, false);
    }

    @Override
    public void onStart() {
        super.onStart();

        Bundle bundle = this.getArguments();
        final String generatorJson = bundle.getString("Generator");

        Generator generator = getGeneratorFromJson(generatorJson);

        setUpTitleTextView(generator);
        setUpImageView(generator);
        setUpDescriptionTextView(generator);
    }

    private void setUpDescriptionTextView(Generator generator) {
        final TextView descriptionView = (TextView)getActivity().findViewById(R.id.generator_description);

        GeneratorDescriptionRequest request = new GeneratorDescriptionRequest("http://version1.api.memegenerator.net/Generator_Select_ByUrlNameOrGeneratorID?generatorID="+ generator.generatorId +"&urlName=" + generator.generatorName, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                descriptionView.setText(response);
            }
        }, new ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });

        MyVolley.getInstance(getContext()).addToRequestQueue(request);
    }

    private void setUpImageView(Generator generator) {
        final ImageView imageView = (ImageView)getActivity().findViewById(R.id.generator_image);
        ImageLoader imageLoader = MyVolley.getInstance(getContext()).getImageLoader();
        imageLoader.get(generator.imageUrl, new ImageLoader.ImageListener() {
            @Override
            public void onResponse(ImageLoader.ImageContainer response, boolean isImmediate) {
                imageView.setImageBitmap(response.getBitmap());
            }

            @Override
            public void onErrorResponse(VolleyError error) {
                Toast toast = Toast.makeText(getContext(), "Sorry, there was a problem displaying the image.", Toast.LENGTH_LONG);
                toast.show();
                getActivity().getSupportFragmentManager().popBackStack();
                return;
            }
        });
    }

    private void setUpTitleTextView(Generator generator) {
        TextView titleView = (TextView)getActivity().findViewById(R.id.generator_name);
        titleView.setText(generator.displayName);
    }

    private Generator getGeneratorFromJson(String json) {
        ObjectMapper mapper = new ObjectMapper();
        Generator generator = null;
        try {
            generator = mapper.readValue(json, Generator.class);
        } catch (IOException e) {
            Log.e("JSON_ERROR", "couldn't convert json into object");
        }
        return generator;
    }

}
