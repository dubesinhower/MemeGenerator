package csc550.memegenerator;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;

public class PopularGeneratorsFragment extends Fragment implements GetJsonDataFromUrl.AsyncResponse {

    public PopularGeneratorsFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        new GetJsonDataFromUrl(this).execute("http://version1.api.memegenerator.net/Generators_Select_ByPopular");

        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_popular_generators, container, false);
    }

    private void showGenerator(View v, String generator_name) {
        ShowGeneratorFragment fragment = new ShowGeneratorFragment();
        Bundle bundle = new Bundle();
        bundle.putString("GeneratorName", generator_name);
        fragment.setArguments(bundle);
        getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, fragment).addToBackStack(null).commit();
    }

    @Override
    public void onJsonDataLoaded(Map<String, Object> output) {
        Context context = getContext();

        ArrayList<LinkedHashMap> generators = (ArrayList<LinkedHashMap>)output.get("result");
        LinearLayout meme_buttons = (LinearLayout)getView().findViewById(R.id.generator_buttons);
        for(LinkedHashMap generator : generators) {
            Button generator_button = new Button(context);
            String generator_display_name = (String)generator.get("displayName");
            final String generator_name = (String)generator.get("urlName");
            generator_button.setText(generator_display_name);
            generator_button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    showGenerator(v, generator_name);
                }
            });
            meme_buttons.addView(generator_button);
        }
    }
}
