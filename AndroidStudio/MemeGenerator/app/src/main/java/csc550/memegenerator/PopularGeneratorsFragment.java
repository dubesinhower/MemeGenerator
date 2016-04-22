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
import java.util.LinkedHashMap;
import java.util.Map;

public class PopularGeneratorsFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_popular_generators, container, false);
    }

    @Override
    public void onStart() {
        super.onStart();
        GeneratorListRequest request = new GeneratorListRequest("http://version1.api.memegenerator.net/Generators_Select_ByPopular", new Response.Listener<ArrayList<Generator>>() {

            public void onResponse(ArrayList<Generator> response) {
                displayGeneratorButtonViews(response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast toast = Toast.makeText(getContext(), "Sorry, there was a problem loading the list of generators.", Toast.LENGTH_LONG);
                toast.show();
            }
        });

        MyVolley.getInstance(getContext()).addToRequestQueue(request);
    }

    private void showGenerator(Generator generator) {
        String generatorJson = null;
        ObjectMapper mapper = new ObjectMapper();
        try {
            generatorJson = mapper.writeValueAsString(generator);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        ShowGeneratorFragment fragment = new ShowGeneratorFragment();
        Bundle bundle = new Bundle();
        bundle.putString("Generator", generatorJson);
        fragment.setArguments(bundle);
        getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, fragment).addToBackStack(null).commit();
    }


    public void displayGeneratorButtonViews(ArrayList<Generator> generators)  {
        Context context = getContext();

        LinearLayout meme_buttons = (LinearLayout)getView().findViewById(R.id.generator_buttons);
        for(final Generator generator : generators) {
            Button generator_button = new Button(context);
            generator_button.setText(generator.displayName);
            generator_button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    showGenerator(generator);
                }
            });
            meme_buttons.addView(generator_button);
        }
    }
}
