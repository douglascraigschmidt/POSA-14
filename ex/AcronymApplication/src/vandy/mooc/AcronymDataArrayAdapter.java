package vandy.mooc;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

/**
 * Custom ArrayAdapter for the AcronymData class, which makes each row
 * of the ListView have a more complex layout than just a single
 * textview (which is the default for ListViews).
 */
public class AcronymDataArrayAdapter extends ArrayAdapter<AcronymData> {
    /**
     * Construtor that declares which layout file is used as the
     * layout for each row.
     */
    public AcronymDataArrayAdapter(Context context,
                                   List<AcronymData> objects) {
        super(context, R.layout.acronym_data_row, objects);
    }

    /**
     * Method used by the ListView to "get" the "view" for each row of
     * data in the ListView.
     * 
     * @param position
     *            The position of the item within the adapter's data set of the
     *            item whose view we want. convertView The old view to reuse, if
     *            possible. Note: You should check that this view is non-null
     *            and of an appropriate type before using. If it is not possible
     *            to convert this view to display the correct data, this method
     *            can create a new view. Heterogeneous lists can specify their
     *            number of view types, so that this View is always of the right
     *            type (see getViewTypeCount() and getItemViewType(int)).
     * @param parent
     *            The parent that this view will eventually be attached to
     * @return A View corresponding to the data at the specified position.
     */
    @Override
    public View getView(int position,
                        View convertView,
                        ViewGroup parent) {
        AcronymData data = getItem(position);
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.acronym_data_row,
                                                                    parent,
                                                                    false);
        }

        TextView resultTV =
            (TextView) convertView.findViewById(R.id.name);
        TextView dbRefsTV = 
            (TextView) convertView.findViewById(R.id.db_refs);
        TextView yearAddedTV =
            (TextView) convertView.findViewById(R.id.year_added_to_db);

        resultTV.setText(data.mLongForm);
        dbRefsTV.setText("" + data.mFreq);
        yearAddedTV.setText("" + data.mSince);

        return convertView;
    }
}
