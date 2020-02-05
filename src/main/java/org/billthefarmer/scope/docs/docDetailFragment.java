package org.billthefarmer.scope.docs;
import android.app.Activity;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.app.Fragment;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import org.billthefarmer.scope.R;
import org.billthefarmer.scope.docs.dummy.DummyContent;

/**
 * A fragment representing a single doc detail screen.
 * This fragment is either contained in a {@link docListActivity}
 * in two-pane mode (on tablets) or a {@link docDetailActivity}
 * on handsets.
 */
public class docDetailFragment extends Fragment {
    /**
     * The fragment argument representing the item ID that this fragment
     * represents.
     */
    public static final String ARG_ITEM_ID = "item_id";

    /**
     * The dummy content this fragment is presenting.
     */
    private DummyContent.DummyItem mItem;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public docDetailFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments().containsKey(ARG_ITEM_ID)) {
            // Load the dummy content specified by the fragment
            // arguments. In a real-world scenario, use a Loader
            // to load content from a content provider.
            mItem = DummyContent.ITEM_MAP.get(getArguments().getString(ARG_ITEM_ID));

            Activity activity = this.getActivity();
            CollapsingToolbarLayout appBarLayout = (CollapsingToolbarLayout) activity.findViewById(R.id.toolbar_layout);
            if (appBarLayout != null) {
                appBarLayout.setTitle(mItem.content);
            }
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.doc_detail, container, false);


        String html_ = "<h2>Title</h2><br><p>Description here</p>";

        if (mItem != null) {

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                ((TextView) rootView.findViewById(R.id.doc_detail)).setText(Html.fromHtml(html_, Html.FROM_HTML_MODE_COMPACT));
            } else {
                ((TextView) rootView.findViewById(R.id.doc_detail)).setText(Html.fromHtml(html_));
            }


        }

        return rootView;
    }
}
