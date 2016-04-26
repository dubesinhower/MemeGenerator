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

    private int pageNumber = 1;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_popular_generators, container, false);
    }

    @Override
    public void onStart() {
        super.onStart();
        showPopularGenerators();
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

    private void showPopularGenerators() {
        GeneratorListRequest request = new GeneratorListRequest("http://version1.api.memegenerator.net/Generators_Select_ByPopular?pageIndex="+ pageNumber +"&pageSize=20", new Response.Listener<ArrayList<Generator>>() {

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
        LinearLayout memeButtons = (LinearLayout)getView().findViewById(R.id.generator_buttons);
        memeButtons.removeAllViews();

        for(final Generator generator : generators) {
            Button generator_button = new Button(context);
            generator_button.setText(generator.displayName);
            generator_button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    showGenerator(generator);
                }
            });
            memeButtons.addView(generator_button);
        }
    }

    public void viewNextPage() {
        pageNumber++;
        showPopularGenerators();
        Button lastButton = (Button)getActivity().findViewById(R.id.last_button);
        if(pageNumber>1)
            lastButton.setEnabled(true);
        else
            lastButton.setEnabled(false);
    }

    public void viewLastPage() {
        if(pageNumber - 1 >= 1)
            pageNumber--;
        showPopularGenerators();
        Button lastButton = (Button)getActivity().findViewById(R.id.last_button);
        if(pageNumber>1)
            lastButton.setEnabled(true);
        else
            lastButton.setEnabled(false);
    }
}
