package csc550.memegenerator;


import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;


/**
 * A simple {@link Fragment} subclass.
 */
public class ShowImageFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        String imageUrl = null;

        if(savedInstanceState != null) {
            imageUrl=savedInstanceState.getString("ImageUrl");
        }
        else {
            Bundle bundle = this.getArguments();
            imageUrl=bundle.getString("ImageUrl");
        }

        new RetrieveDrawableFromUrlTask( new RetrieveDrawableFromUrlTask.AsyncResponse() {

            @Override
            public void processFinish(Drawable output) {
                if(output == null) {
                    Toast toast = Toast.makeText(getActivity().getApplicationContext(), "Sorry, there was a problem displaying the image.", Toast.LENGTH_LONG);
                    toast.show();
                    getActivity().getSupportFragmentManager().popBackStack();
                    return;
                }

                ImageView meme = (ImageView)getView().findViewById(R.id.meme);
                meme.setImageDrawable(output);
            }
        }).execute(imageUrl);
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_show_image, container, false);
    }
}
