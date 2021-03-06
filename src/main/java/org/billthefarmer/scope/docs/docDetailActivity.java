package org.billthefarmer.scope.docs;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.NavUtils;

import org.billthefarmer.scope.R;


public class docDetailActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doc_detail);
        Toolbar toolbar = (Toolbar) findViewById(R.id.detail_toolbar);
        setSupportActionBar(toolbar);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        if (savedInstanceState == null) {

            Bundle arguments = new Bundle();
            arguments.putString(docDetailFragment.ARG_ITEM_ID,
                    getIntent().getStringExtra(docDetailFragment.ARG_ITEM_ID));

            arguments.putString(docDetailFragment.ARG_ITEM_TEXT,
                    getIntent().getStringExtra(docDetailFragment.ARG_ITEM_TEXT));

            arguments.putString(docDetailFragment.ARG_ITEM_IMAGE,
                    getIntent().getStringExtra(docDetailFragment.ARG_ITEM_IMAGE));

            arguments.putString(docDetailFragment.ARG_ITEM_TITLE,
                    getIntent().getStringExtra(docDetailFragment.ARG_ITEM_TITLE));


            docDetailFragment fragment = new docDetailFragment();
            fragment.setArguments(arguments);
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.doc_detail_container, fragment)
                    .commit();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            NavUtils.navigateUpTo(this, new Intent(this, docListActivity.class));
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
