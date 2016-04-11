package csc550.memegenerator;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.annotation.Nullable;
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
import java.util.ArrayList;
import java.util.List;

import static csc550.memegenerator.MemeContract.*;

public class ShowMemeFragment extends Fragment{

    private MemeDbHelper memeDbHelper;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        memeDbHelper = new MemeDbHelper(getActivity());
    }

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

        setUpFavoriteButton(meme);
    }

    private void setUpFavoriteButton(Meme meme) {
        Button favoriteButton = (Button)getView().findViewById(R.id.favorite_button);
        final Meme finalMeme = meme;

        if(memeInDb(meme)) {
            favoriteButton.setText("Unfavorite");
            favoriteButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    unfavoriteMeme(finalMeme);
                }
            });
        }
        else {
            favoriteButton.setText("Favorite");
            favoriteButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    favoriteMeme(finalMeme);
                }
            });
        }
    }

    private void favoriteMeme(Meme meme) {
        Toast toast = null;
        if(saveMeme(meme) == -1) {
            toast = Toast.makeText(getContext(), "Sorry, an error occured when trying to add this meme to your favorites.", Toast.LENGTH_LONG);
        }
        else {
            toast = Toast.makeText(getContext(), "Meme was added to your favorites.", Toast.LENGTH_SHORT);
        }

        toast.show();
        setUpFavoriteButton(meme);
    }

    private void unfavoriteMeme(Meme meme) {
        Toast toast = null;
        if(!deleteMeme(meme)) {
            toast = Toast.makeText(getContext(), "Sorry, an error occured when trying to remove this meme to your favorites.", Toast.LENGTH_LONG);
        }
        else {
            toast = Toast.makeText(getContext(), "Meme was removed from your favorites.", Toast.LENGTH_SHORT);
        }
        toast.show();
        setUpFavoriteButton(meme);
    }

    private long saveMeme(Meme meme) {
        SQLiteDatabase db = memeDbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(MemeEntry.COLUMN_NAME_DISPLAY_NAME, meme.displayName);
        values.put(MemeEntry.COLUMN_NAME_GENERATOR_NAME, meme.generatorName);
        values.put(MemeEntry.COLUMN_NAME_INSTANCE_URL, meme.instanceUrl);
        values.put(MemeEntry.COLUMN_NAME_IS_MINE, meme.isMine);

        long newRowId;
        newRowId = db.insert(MemeEntry.TABLE_NAME, null, values);
        db.close();
        return newRowId;
    }

    private boolean memeInDb(Meme meme) {
        SQLiteDatabase db = memeDbHelper.getReadableDatabase();

        String[] projection = {
                MemeEntry._ID
        };

        // Define 'where' part of query.
        String selection = MemeEntry.COLUMN_NAME_INSTANCE_URL + " LIKE ?";
        // Specify arguments in placeholder order.
        String[] selectionArgs = { meme.instanceUrl };

        String sortOrder = MemeEntry._ID + " ASC";

        // Issue SQL statement.
        int numRows = db.query(MemeEntry.TABLE_NAME, projection, selection, selectionArgs, null, null, sortOrder).getCount();
        db.close();
        return numRows > 0;
    }

    private boolean deleteMeme(Meme meme) {
        SQLiteDatabase db = memeDbHelper.getWritableDatabase();
        // Define 'where' part of query.
        String selection = MemeEntry.COLUMN_NAME_INSTANCE_URL + " LIKE ?";
        // Specify arguments in placeholder order.
        String[] selectionArgs = { meme.instanceUrl };
        boolean deleted = db.delete(MemeEntry.TABLE_NAME, selection, selectionArgs) > 0;
        db.close();
        return deleted;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        memeDbHelper.close();
    }
}
