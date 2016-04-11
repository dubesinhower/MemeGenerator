package csc550.memegenerator;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.annotation.Nullable;
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

import static csc550.memegenerator.MemeContract.*;

public class FavoriteMemesFragment extends Fragment {

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
        return inflater.inflate(R.layout.fragment_favorite_memes, container, false);
    }
    @Override
    public void onStart() {
        super.onStart();
        ArrayList<Meme> memes = getFavoriteMemes();
        try {
            displayMemeButtonViews(memes);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
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

        LinearLayout meme_buttons = (LinearLayout)getView().findViewById(R.id.favorite_meme_buttons);
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

    public ArrayList<Meme> getFavoriteMemes() {
        SQLiteDatabase db = memeDbHelper.getReadableDatabase();

        ArrayList<Meme> memes = new ArrayList<Meme>();

        Cursor  cursor = db.rawQuery("select * from " + MemeEntry.TABLE_NAME, null);
        if (cursor .moveToFirst()) {
            while (cursor.isAfterLast() == false) {
                String displayName = cursor.getString(cursor.getColumnIndex(MemeEntry.COLUMN_NAME_DISPLAY_NAME));
                String generatorName = cursor.getString(cursor.getColumnIndex(MemeEntry.COLUMN_NAME_GENERATOR_NAME));
                String instanceUrl = cursor.getString(cursor.getColumnIndex(MemeEntry.COLUMN_NAME_INSTANCE_URL));
                boolean isMine = cursor.getInt(cursor.getColumnIndex(MemeEntry.COLUMN_NAME_IS_MINE)) > 0;

                memes.add(new Meme(displayName, generatorName, instanceUrl, isMine));
                cursor.moveToNext();
            }
        }
        db.close();
        return memes;
    }

}
