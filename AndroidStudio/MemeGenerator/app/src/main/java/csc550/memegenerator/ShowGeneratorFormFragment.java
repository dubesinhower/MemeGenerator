package csc550.memegenerator;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.Response.ErrorListener;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.Charset;

public class ShowGeneratorFormFragment extends Fragment {
    private SharedPreferences prefs;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        prefs = PreferenceManager.getDefaultSharedPreferences(getContext());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_show_generator_form, container, false);
    }

    @Override
    public void onStart() {
        super.onStart();

        Bundle bundle = this.getArguments();
        final String generatorJson = bundle.getString("Generator");

        Generator generator = getGeneratorFromJson(generatorJson);

        setUpGeneratorFormTitle(generator);
        setUpGeneratorFormSubmit(generator);
    }

    private void setUpGeneratorFormSubmit(final Generator generator) {
        Button submit = (Button) getActivity().findViewById(R.id.generator_form_submit);
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                generateMeme(generator);
            }
        });
    }

    private void generateMeme(Generator generator) {
        EditText topTextField = (EditText) getActivity().findViewById(R.id.generator_top_text);
        EditText bottomTextField = (EditText) getActivity().findViewById(R.id.generator_bottom_text);
        String topText = topTextField.getText().toString().trim();
        String bottomText = bottomTextField.getText().toString().trim();
        if(topText.isEmpty() || bottomText.isEmpty()) {
            Toast toast = Toast.makeText(getContext(), "Both text fields are required.", Toast.LENGTH_LONG);
            toast.show();
            return;
        }
        String username = prefs.getString("username", "");
        String password = prefs.getString("password", "");

        topText = TextUtils.htmlEncode(topText);
        bottomText = TextUtils.htmlEncode(bottomText);

        /*try {
            topText = URLEncoder.encode(topText, "UTF-8");
            bottomText = URLEncoder.encode(bottomText, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }*/

        GeneratorGenerateRequest request = new GeneratorGenerateRequest("http://version1.api.memegenerator.net/Instance_Create?username="+ username +"&password="+ password +"&languageCode=en&generatorID="+ generator.generatorId +"&imageID=" + generator.imageId + "&text0="+ topText +"&text1="+ bottomText , new Response.Listener<Meme>() {
            @Override
            public void onResponse(Meme response) {

            }
        }, new ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast toast = Toast.makeText(getContext(), "Sorry, there was a problem generating the meme.", Toast.LENGTH_LONG);
                toast.show();
            }
        });

        MyVolley.getInstance(getContext()).addToRequestQueue(request);
    }

    private void setUpGeneratorFormTitle(Generator generator) {
        TextView title = (TextView) getActivity().findViewById(R.id.generator_form_title);
        title.setText(generator.displayName + " Generator");
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
