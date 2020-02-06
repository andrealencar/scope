package org.billthefarmer.scope.docs;
import android.app.Activity;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.DrawableRes;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import org.billthefarmer.scope.R;
import org.billthefarmer.scope.docs.dummy.DummyContent;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;


public class docDetailFragment extends Fragment {

    public static final String ARG_ITEM_ID = "item_id";
    private DummyContent.DummyItem mItem;

    String contents = "";
    String ImageUri = "";
    InputStream ims = null;

    public docDetailFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments().containsKey(ARG_ITEM_ID)) {

            mItem = DummyContent.ITEM_MAP.get(getArguments().getString(ARG_ITEM_ID));

            String file_name = mItem.id+".html";
            AssetManager mngr = getContext().getAssets();
            ImageUri = mItem.image;

            BufferedReader reader = null;
            String mLine;

            try {
                reader = new BufferedReader(new InputStreamReader(mngr.open(file_name)));
                ims = mngr.open("wave_audio.png");

                while ((mLine = reader.readLine()) != null) {
                        contents += '\n' + mLine;

                }
            } catch (IOException e) {
                            Log.e("ERROR message: ",e.getMessage());
            } finally {
                if (reader != null) {
                    try {
                            reader.close();
                    } catch (IOException e) {
                            Log.e("ERROR message: ",e.getMessage());
                    }
                }
            }

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
        View rootTool = inflater.inflate(R.layout.activity_doc_detail, container, false);


        Log.e("ImageUri:-->>>> ",ImageUri);
        Drawable d = Drawable.createFromStream(ims, null);

        ImageView ImageHeader = rootTool.findViewById(R.id.img_header);

        //Bitmap bitmap = BitmapFactory.decodeStream(ims);
        //mToolbar.setTitle("Teste");
        // set image to ImageView "ic_action_about.png"

        ImageHeader.setImageDrawable(d);


        String html_ = contents;

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
