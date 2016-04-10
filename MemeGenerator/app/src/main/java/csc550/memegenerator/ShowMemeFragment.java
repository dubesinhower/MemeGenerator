package csc550.memegenerator;

import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.media.ImageReader;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.ImageRequest;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;

public class ShowMemeFragment extends Fragment{

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_show_meme, container, false);
    }

    @Override
    public void onStart() {
        super.onStart();

        Bundle bundle = this.getArguments();
        final String memeJson = bundle.getString("Meme");
        ObjectMapper mapper = new ObjectMapper();

        Meme meme = null;
        try {
            meme = mapper.readValue(memeJson, Meme.class);
        } catch (IOException e) {
            Log.e("JSON_ERROR", "couldn't convert json into object");
        }
        final ImageView memeView = (ImageView)getView().findViewById(R.id.meme);
        ImageLoader imageLoader = MyVolley.getInstance(getContext()).getImageLoader();
        imageLoader.get(meme.instanceUrl, new ImageLoader.ImageListener() {
            @Override
            public void onResponse(ImageLoader.ImageContainer response, boolean isImmediate) {
                memeView.setImageBitmap(response.getBitmap());
            }

            @Override
            public void onErrorResponse(VolleyError error) {
                Toast toast = Toast.makeText(getContext(), "Sorry, there was a problem displaying the image.", Toast.LENGTH_LONG);
                toast.show();
                getActivity().getSupportFragmentManager().popBackStack();
                return;
            }
        });

        Button favoriteButton = (Button)getView().findViewById(R.id.favorite_button);
        favoriteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toggleFavorite(v, memeJson);
            }
        });
    }

    public void toggleFavorite(View v, String memeJson) {
        Toast toast = Toast.makeText(getContext(), "Meme was added to your favorites.", Toast.LENGTH_LONG);
        toast.show();
    }
}
