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

    private int pageNumber = 1;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_popular_memes, container, false);
    }

    @Override
    public void onStart() {
        super.onStart();

        showPopularMemes();
        Button nextButton = (Button)getActivity().findViewById(R.id.next_button);
        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewNextPage();
            }
        });
        Button lastButton = (Button)getActivity().findViewById(R.id.last_button);
        lastButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewLastPage();
            }
        });
    }

    public void showPopularMemes() {
        MemeListRequest request = new MemeListRequest("http://version1.api.memegenerator.net/Instances_Select_ByPopular?pageIndex="+ pageNumber +"&pageSize=20&days=30", new Response.Listener<ArrayList<Meme>>() {
            @Override
            public void onResponse(ArrayList<Meme> response) {
                displayMemeButtonViews(response);
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

    public void showMeme(Meme meme) {
        String memeJson = null;
        ObjectMapper mapper = new ObjectMapper();
        try {
            memeJson = mapper.writeValueAsString(meme);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        ShowMemeFragment fragment = new ShowMemeFragment();
        Bundle bundle = new Bundle();
        bundle.putString("Meme", memeJson);
        fragment.setArguments(bundle);
        getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, fragment).addToBackStack(null).commit();
    }

    public void displayMemeButtonViews(ArrayList<Meme> memes) {
        Context context = getContext();
        LinearLayout memeButtons = (LinearLayout)getView().findViewById(R.id.meme_buttons);
        memeButtons.removeAllViews();

        for (final Meme meme: memes) {
            Button button = new Button(context);
            button.setText(meme.displayName);
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    showMeme(meme);
                }
            });
            memeButtons.addView(button);
        }
    }

    public void viewNextPage() {
        pageNumber++;
        showPopularMemes();
        Button lastButton = (Button)getActivity().findViewById(R.id.last_button);
        if(pageNumber>1)
            lastButton.setEnabled(true);
        else
            lastButton.setEnabled(false);
    }

    public void viewLastPage() {
        if(pageNumber - 1 >= 1)
            pageNumber--;
        showPopularMemes();
        Button lastButton = (Button)getActivity().findViewById(R.id.last_button);
        if(pageNumber>1)
            lastButton.setEnabled(true);
        else
            lastButton.setEnabled(false);
    }
}
