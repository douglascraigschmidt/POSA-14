package vandy.mooc;

import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ListView;

/**
 * @class AcronymActivityBase
 *
 * @brief The main Activity that launches the screen users will see.
 *        This class handles the bulk of the UI operations.
 */
public class AcronymActivityBase extends Activity {
    /**
     * Used for logging purposes.
     */
    static protected String TAG = AcronymActivity.class.getCanonicalName();

    /**
     * The ListView that will display the results to the user.
     */
    protected ListView mListView;

    /**
     * A custom ArrayAdapter used to display the list of AcronymData objects.
     */
    protected AcronymDataArrayAdapter adapter;
    
    /**
     * Acronym entered by the user.
     */
    protected EditText mEditText;

    /**
     * Called when the activity is starting - this is where the GUI
     * initialization occurs.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Get references to the UI components.
        setContentView(R.layout.activity_main);

        mEditText = (EditText) findViewById(R.id.editText1);
        mListView = (ListView) findViewById(R.id.listView1);
    }

    /**
     * Display the results to the screen.
     * 
     * @param results
     *            List of Resultes to be displayed.
     */
    protected void displayResults(List<AcronymData> results) {
        // Create custom ListView Adapter and fill it with our data.
        if (adapter == null) {
            // Create a local instance of our custom Adapter for our
            // ListView.
            adapter = new AcronymDataArrayAdapter(this, results);
        } else {
            // If adapter already existed, then change data set.
            adapter.clear();
            adapter.addAll(results);
            adapter.notifyDataSetChanged();
        }

        // Set the adapter to the ListView.
        mListView.setAdapter(adapter);
    }

    /**
     * Hide the keyboard after a user has finished typing the acronym
     * they want expanded.
     */
    protected void hideKeyboard() {
        InputMethodManager mgr =
            (InputMethodManager) getSystemService
            (Context.INPUT_METHOD_SERVICE);
        mgr.hideSoftInputFromWindow(mEditText.getWindowToken(),
                                    0);
    }
}
