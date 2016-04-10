package csc550.memegenerator;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.LinkedHashMap;
import java.util.Map;

public class ShowGeneratorFragment extends Fragment implements GetJsonDataFromUrl.AsyncResponse, GetDrawableFromUrl.AsyncResponse {

    public ShowGeneratorFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_show_generator, container, false);
    }

    @Override
    public void onStart() {
        super.onStart();
        String generator_name = null;

        Bundle bundle = this.getArguments();
        generator_name=bundle.getString("GeneratorName");

        new GetJsonDataFromUrl(this).execute("http://version1.api.memegenerator.net/Generator_Select_ByUrlNameOrGeneratorID?urlName=" + generator_name);
    }

    @Override
    public void onJsonDataLoaded(Map<String, Object> output) {
        LinkedHashMap generatorDetails = (LinkedHashMap)output.get("result");
        int id = (int)generatorDetails.get("generatorID");
        String name = (String)generatorDetails.get("displayName");
        String imageUrl = (String)generatorDetails.get("imageUrl");
        String description = (String)generatorDetails.get("description");

        TextView generator_text = (TextView)getView().findViewById(R.id.generator_name);
        TextView generator_description = (TextView)getView().findViewById(R.id.generator_description);
        generator_text.setText(name);
        if(description != null && !description.trim().isEmpty()) {
            generator_description.setText(description.trim());
        }
        else {
            generator_description.setText("No description.");
        }

        new GetDrawableFromUrl(this).execute(imageUrl);
    }

    @Override
    public void onDrawableLoaded(Drawable output) {
        ImageView generator_image = (ImageView)getView().findViewById(R.id.generator_image);
        generator_image.setImageDrawable(output);
    }
}
