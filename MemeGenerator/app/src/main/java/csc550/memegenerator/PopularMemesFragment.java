package csc550.memegenerator;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Created by Christopher on 4/8/2016.
 */
public class PopularMemesFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        new RetrieveJsonDataFromUrlTask(new RetrieveJsonDataFromUrlTask.AsyncResponse() {
            Context context = getContext();

            @Override
            public void processFinish(Map<String, Object> output) {
                // Display memes
                ArrayList<LinkedHashMap> memes = (ArrayList<LinkedHashMap>)output.get("result");
                LinearLayout meme_buttons = (LinearLayout)getView().findViewById(R.id.meme_buttons);
                for(LinkedHashMap meme : memes) {
                    Button meme_button = new Button(context);
                    String meme_display_name = (String)meme.get("displayName");
                    final String meme_url = (String)meme.get("instanceImageUrl");
                    meme_button.setText(meme_display_name);
                    meme_button.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            showMeme(v, meme_url);
                        }
                    });
                    meme_buttons.addView(meme_button);
                }
            }
        }).execute("http://version1.api.memegenerator.net/Instances_Select_ByPopular");

        return inflater.inflate(R.layout.fragment_popular_memes, container, false);
    }

    private void showMeme(View v, String meme_url) {
        ShowImageFragment fragment = new ShowImageFragment();
        Bundle bundle = new Bundle();
        bundle.putString("ImageUrl", meme_url);
        fragment.setArguments(bundle);
        getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, fragment).addToBackStack(null).commit();
    }
}
