package org.billthefarmer.scope.docs;

import android.app.Activity;
import android.content.res.AssetManager;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.google.android.material.appbar.CollapsingToolbarLayout;

import org.billthefarmer.scope.R;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;


public class docDetailFragment extends Fragment {

    public static final String ARG_ITEM_ID = "item_id";
    public static final String ARG_ITEM_TITLE = "item_title";
    public static final String ARG_ITEM_TEXT = "item_text";
    public static final String ARG_ITEM_IMAGE = "item_image";
    private static String mItem = "";
    private static String textItem = "";
    private static String imagetItem = "";
    private static String titletItem = "";

    String contents = "";
    String ImageUri = "";
    InputStream ims = null;
    BufferedReader reader = null;

    public docDetailFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments().containsKey(ARG_ITEM_ID)) {

            mItem       = getArguments().getString(ARG_ITEM_ID);
            titletItem  = getArguments().getString(ARG_ITEM_TITLE);
            textItem    = getArguments().getString(ARG_ITEM_TEXT);
            imagetItem  = getArguments().getString(ARG_ITEM_IMAGE);
            //Log.d("DummyItem-->", mItem+" -- "+titletItem+"--"+imagetItem);

            Activity activity = this.getActivity();
            ImageView ImageHeader = activity.findViewById(R.id.img_header);
            AssetManager mngr = getContext().getAssets();

            try {
                ims = mngr.open(imagetItem);
            } catch (IOException e) {
                e.printStackTrace();
            }
            Drawable d = Drawable.createFromStream(ims, null);
            ImageHeader.setImageDrawable(d);

            CollapsingToolbarLayout appBarLayout = (CollapsingToolbarLayout) activity.findViewById(R.id.toolbar_layout);
            if (appBarLayout != null) {
                appBarLayout.setTitle(titletItem);
            }
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.doc_detail, container, false);
        String html_ = textItem;

        if (mItem != null) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                ((TextView) rootView.findViewById(R.id.doc_detail))
                        .setText(Html.fromHtml(html_, Html.FROM_HTML_MODE_LEGACY, new ImageGetter(), null));
            } else {
                ((TextView) rootView.findViewById(R.id.doc_detail))
                        .setText(Html.fromHtml(html_, new ImageGetter(),null));
            }
        }

        return rootView;
    }

    private class ImageGetter implements Html.ImageGetter {

        public Drawable getDrawable(String source) {
            int id;
            //Log.e("source -->>: ",source);
            try{
                id = getResources().getIdentifier(source, "drawable", getContext().getPackageName());
                Drawable d = getResources().getDrawable(id);
                d.setBounds(0,0,d.getIntrinsicWidth(),d.getIntrinsicHeight());
                return d;

            }catch (Exception e){
                Log.e("ImageGetter:Exception ",e.toString());
                return null;
            }

        }
    };


}
