package csc550.memegenerator;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.SharedPreferencesCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.ButtonBarLayout;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.security.KeyPairGenerator;

public class AccountActivity extends AppCompatActivity {
    public static final String PREFS_NAME = "MemeGeneratorPrefs";
    private SharedPreferences prefs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account);

        prefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());

        Window window = getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.setStatusBarColor(getResources().getColor(R.color.colorPrimaryDark));
        }

        Toolbar appBar = (Toolbar) findViewById(R.id.app_bar);
        setSupportActionBar(appBar);

        String username = prefs.getString("username", "");
        Button settings_button = (Button)findViewById(R.id.settings_button);
        TextView settings_message = (TextView)findViewById(R.id.account_message);
        LinearLayout settings_form = (LinearLayout)findViewById(R.id.account_settings_form);
        if(username.isEmpty()) {
            settings_button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    saveSettings();
                }
            });
        }
        else {
            settings_form.setEnabled(false);
            settings_form.setVisibility(View.GONE);
            settings_message.setText("Account settings saved for username " + username + ".");
            settings_button.setText("Delete settings");
            settings_button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    deleteSettings();
                }
            });
        }

    }

    private void deleteSettings() {
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString("username", "");
        // TODO: DON'T STORE PASSWORDS IN PLAIN TEXT
        editor.putString("password", "");
        editor.commit();

        Toast toast = Toast.makeText(getApplicationContext(), "Account settings deleted.", Toast.LENGTH_SHORT);
        toast.show();
        finish();
        Intent intent = getIntent();
        startActivity(intent);
    }

    private void saveSettings() {
        EditText usernameField = (EditText) findViewById(R.id.username_form_view);
        EditText passwordField = (EditText) findViewById(R.id.password_form_view);
        String username = usernameField.getText().toString().trim();
        String password = passwordField.getText().toString().trim();
        if(username.isEmpty() || password.isEmpty()) {
            Toast toast = Toast.makeText(getApplicationContext(), "Username and password are required.", Toast.LENGTH_LONG);
            toast.show();
            return;
        }

        SharedPreferences.Editor editor = prefs.edit();
        editor.putString("username", username);
        // TODO: DON'T STORE PASSWORDS IN PLAIN TEXT
        editor.putString("password", password);
        editor.commit();

        Toast toast = Toast.makeText(getApplicationContext(), "Account settings saved.", Toast.LENGTH_SHORT);
        toast.show();
        finish();
        Intent intent = getIntent();
        startActivity(intent);
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
            finish();
            Intent intent = new Intent(this, GeneratorActivity.class);
            startActivity(intent);
        }
        if (id == R.id.action_favorite) {
            finish();
            Intent intent = new Intent(this, FavoritesActivity.class);
            startActivity(intent);
        }
        if (id == R.id.action_account) {
            finish();
            Intent intent = getIntent();
            startActivity(intent);
        }

        return super.onOptionsItemSelected(item);
    }
}
