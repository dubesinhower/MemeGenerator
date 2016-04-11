package csc550.memegenerator;

import android.content.Intent;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

public class FavoritesActivity extends AppCompatActivity implements FragmentManager.OnBackStackChangedListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favorites);

        Toolbar appBar = (Toolbar) findViewById(R.id.app_bar);
        setSupportActionBar(appBar);

        // Configure up button
        getSupportFragmentManager().addOnBackStackChangedListener(this);
        shouldDisplayHomeUp();

        // http://developer.android.com/training/basics/fragments/fragment-ui.html
        if (findViewById(R.id.fragment_container) != null) {
            if (savedInstanceState != null) {
                return;
            }

            FavoriteMemesFragment fragment = new FavoriteMemesFragment();
            getSupportFragmentManager().beginTransaction().add(R.id.fragment_container, fragment).commit();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_home) {
            finish();
        }
        if (id == R.id.action_generate) {
            getSupportFragmentManager().popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
            Intent intent = new Intent(this, GeneratorActivity.class);
            startActivity(intent);
        }
        if (id == R.id.action_favorite) {
            Intent intent = getIntent();
            finish();
            startActivity(intent);
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackStackChanged() {
        shouldDisplayHomeUp();
    }

    public void shouldDisplayHomeUp() {
        //Enable Up button only  if there are entries in the back stack
        boolean canback = getSupportFragmentManager().getBackStackEntryCount()>0;
        getSupportActionBar().setDisplayHomeAsUpEnabled(canback);
    }

    @Override
    public boolean onSupportNavigateUp() {
        //This method is called when the up button is pressed. Just the pop back stack.
        getSupportFragmentManager().popBackStack();
        return true;
    }
}
