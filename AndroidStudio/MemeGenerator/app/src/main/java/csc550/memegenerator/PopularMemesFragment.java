package csc550.memegenerator;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.ArrayList;

/**
 * Created by Christopher on 4/8/2016.
 */
public class PopularMemesFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_popular_memes, container, false);
    }

    @Override
    public void onStart() {
        super.onStart();

        MemeListRequest request = new MemeListRequest("http://version1.api.memegenerator.net/Instances_Select_ByPopular?days=30", new Response.Listener<ArrayList<Meme>>() {
            @Override
            public void onResponse(ArrayList<Meme> response) {
                try {
                    displayMemeButtonViews(response);
                } catch (JsonProcessingException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast toast = Toast.makeText(getContext(), "Sorry, there was a problem loading the list of memes.", Toast.LENGTH_LONG);
                toast.show();
            }
        });

        MyVolley.getInstance(getContext()).addToRequestQueue(request);
    }

    public void showMeme(View v, String meme_json) {
        ShowMemeFragment fragment = new ShowMemeFragment();
        Bundle bundle = new Bundle();
        bundle.putString("Meme", meme_json);
        fragment.setArguments(bundle);
        getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, fragment).addToBackStack(null).commit();
    }

    public void displayMemeButtonViews(ArrayList<Meme> memes) throws JsonProcessingException {
        Context context = getContext();

        LinearLayout meme_buttons = (LinearLayout)getView().findViewById(R.id.meme_buttons);
        for (Meme meme: memes) {
            Button button = new Button(context);
            button.setText(meme.displayName);
            ObjectMapper mapper = new ObjectMapper();
            final String jsonString = mapper.writeValueAsString(meme);
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    showMeme(v, jsonString);
                }
            });
            meme_buttons.addView(button);
        }
    }
}
