package org.billthefarmer.scope.docs;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.NavUtils;
import androidx.recyclerview.widget.RecyclerView;

import org.billthefarmer.scope.AppExecutors;
import org.billthefarmer.scope.R;
import org.billthefarmer.scope.database.AppDatabase;
import org.billthefarmer.scope.docs.dummy.DummyContent;
import org.billthefarmer.scope.models.Post;

import java.util.ArrayList;
import java.util.List;

/**
 * An activity representing a list of docs. This activity
 * has different presentations for handset and tablet-size devices. On
 * handsets, the activity presents a list of items, which when touched,
 * lead to a {@link docDetailActivity} representing
 * item details. On tablets, the activity presents the list of items and
 * item details side-by-side using two vertical panes.
 */
public class docListActivity extends AppCompatActivity {

    /**
     * Whether or not the activity is in two-pane mode, i.e. running on a tablet
     * device.
     */
    private boolean mTwoPane;
    public static final List<DummyContent.DummyItem> ITEMS = new ArrayList<DummyContent.DummyItem>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doc_list);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitle(getTitle());

        // Show the Up button in the action bar.
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        if (findViewById(R.id.doc_detail_container) != null) {
            mTwoPane = true;
        }

        View recyclerView = findViewById(R.id.doc_list);
        assert recyclerView != null;

        AppDatabase mDb = AppDatabase.getInstance(getApplicationContext());
        ITEMS.clear();
        AppExecutors.getInstance().diskIO().execute(new Runnable() {
            @Override
            public void run() {

                final List<Post> posts = mDb.postDao().getAll();

                for (int i = 0; i < posts.size(); i++) {
                    String id = String.valueOf(posts.get(i).order);
                    DummyContent.DummyItem item = new DummyContent.DummyItem(id,
                                                                        posts.get(i).title,
                                                                        posts.get(i).text,
                                                                 "images/texto-01.png");
                    ITEMS.add(item);
                }

                //mDb.userDao().insertUser(user);
                //Log.d("ADD user", user.toString());
            }


        });

        setupRecyclerView((RecyclerView) recyclerView, ITEMS);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            NavUtils.navigateUpFromSameTask(this);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void setupRecyclerView(@NonNull RecyclerView recyclerView, List<DummyContent.DummyItem> items) {
        recyclerView.setAdapter(new SimpleItemRecyclerViewAdapter(this, items, mTwoPane));
    }

    public static class SimpleItemRecyclerViewAdapter
            extends RecyclerView.Adapter<SimpleItemRecyclerViewAdapter.ViewHolder> {

        private final docListActivity mParentActivity;
        private final List<DummyContent.DummyItem> mValues;
        private final boolean mTwoPane;
        private final View.OnClickListener mOnClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DummyContent.DummyItem item = (DummyContent.DummyItem) view.getTag();

                //Log.d("DummyItem", item.id+" --  "+item.toString()+item.text.substring(0,100));

                Context context = view.getContext();
                Intent intent = new Intent(context, docDetailActivity.class);
                intent.putExtra(docDetailFragment.ARG_ITEM_ID, item.id);
                intent.putExtra(docDetailFragment.ARG_ITEM_TITLE, item.content);
                intent.putExtra(docDetailFragment.ARG_ITEM_TEXT, item.text);
                intent.putExtra(docDetailFragment.ARG_ITEM_IMAGE, item.image);
                context.startActivity(intent);
            }
        };

        SimpleItemRecyclerViewAdapter(docListActivity parent,
                                      List<DummyContent.DummyItem> items,
                                      boolean twoPane) {
            mValues = items;
            mParentActivity = parent;
            mTwoPane = twoPane;
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.doc_list_content, parent, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(final ViewHolder holder, int position) {
            holder.mIdView.setText(mValues.get(position).id);
            holder.mContentView.setText(mValues.get(position).content);

            holder.itemView.setTag(mValues.get(position));
            holder.itemView.setOnClickListener(mOnClickListener);
        }

        @Override
        public int getItemCount() {
            return mValues.size();
        }

        class ViewHolder extends RecyclerView.ViewHolder {
            final TextView mIdView;
            final TextView mContentView;

            ViewHolder(View view) {
                super(view);
                mIdView = (TextView) view.findViewById(R.id.id_text);
                mContentView = (TextView) view.findViewById(R.id.content);
            }
        }
    }
}
