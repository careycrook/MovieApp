package edu.gatech.movierecommender;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.design.widget.TabLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import android.widget.EditText;
import android.widget.Toast;

public class Dashboard extends AppCompatActivity {

    private static final String UNUSED = "unused";
    private Context c = this;

    /**
     * Runs on inception of activity
     *
     * @param savedInstanceState default argument
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        final Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        /*
      The {@link android.support.v4.view.PagerAdapter} that will provide
      fragments for each of the sections. We use a
      {@link FragmentPagerAdapter} derivative, which will keep every
      loaded fragment in memory. If this becomes too memory intensive, it
      may be best to switch to a
      {@link android.support.v4.app.FragmentStatePagerAdapter}.
     */
        final SectionsPagerAdapter mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.
        /*
      The {@link ViewPager} that will host the section contents.
     */
        final ViewPager mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        final TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(mViewPager);

        final FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                profileButton(view);
            }
        });

    }

    /**
     * Logout button dropdown
     *
     * @param menu item on action bar
     * @return success boolean
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_dashboard, menu);
        return true;
    }

    /**
     * On logout
     *
     * @param item item on action bar
     * @return success boolean
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        final int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * Disable back button
     */
    @Override
    public void onBackPressed() {
    }

    /**
     * Opens main page when activated.
     *
     * @param  item  MenuItem corresponding to this method.
     */
    @SuppressWarnings(UNUSED)
    public void logout(MenuItem item) {
        //Launch main activity.
        final Intent homeIntent = new Intent(getApplicationContext(), MainActivity.class);
        startActivity(homeIntent);
    }

    /**
     * Creates and launches dialog.
     *
     * @param v View for layout
     */
    private void dialog(View v) {
        //Create dialog.
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("You don't have a profile. Do you want to create a profile?");
        final View vi = v;
        //Create profile case.
        builder.setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                World.getCurrentUser().setProfile(new Profile());
                launchProfile(vi);
                dialog.dismiss();
            }
        });
        //Don't create profile case.
        builder.setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                dialog.dismiss();
            }
        });
        final AlertDialog dialog = builder.create();
        //Show the dialog that was created.
        dialog.show();
    }

    /**
     * Dialog branching.
     *
     * @param  v  View for layout
     */
    private void profileButton(View v) {
        //If currentUser does not have a profile, show the prompt.
        if (World.getCurrentUser().getProfile() == null) {
            dialog(v);
        //Else just launch the profile.
        } else {
            launchProfile(v);
        }
    }

    /**
     * Opens profile activity
     *
     * @param  v  View for layout
     */
    private void launchProfile(View v) {
        //Launch profile activity.
        final Intent profileIntent = new Intent(v.getContext(), ProfileActivity.class);
        startActivity(profileIntent);
    }

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment {
        /**
         * The fragment argument representing the section number for this
         * fragment.
         */
        private static final String ARG_SECTION_NUMBER = "section_number";

        /**
         * Returns a new instance of this fragment for the given section
         * number.
         *
         * @param sectionNumber the section number
         * @return PlaceholderFragment
         */
        public static PlaceholderFragment newInstance(int sectionNumber) {
            final PlaceholderFragment fragment = new PlaceholderFragment();
            final Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            fragment.setArguments(args);
            return fragment;
        }

        /**
         * Create view listener
         *
         * @param inflater item to grow
         * @param container holds the controls
         * @param savedInstanceState the bundle
         * @return view
         */
        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            return inflater.inflate(R.layout.fragment_dashboard, container, false);
        }
    }

    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        /**
         * Gets item position
         *
         * @param fm Fragment manager
         */
        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        /**
         * Gets item position
         *
         * @param position current page
         * @return Fragment
         */
        @Override
        public Fragment getItem(int position) {
            // getItem is called to instantiate the fragment for the given page.
            // Return a PlaceholderFragment (defined as a static inner class below).
            return PlaceholderFragment.newInstance(position + 1);
        }

        /**
         * Gets number of pages
         * @return num pages
         */
        @Override
        public int getCount() {
            // Show 3 total pages.
            return 1;
        }

        /**
         * Gets title of specified page
         *
         * @param position the page number
         * @return CharSequence page titles
         */
        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                    return "Search for Movies";
                case 1:
                    return "SECTION 2";
                case 2:
                    return "SECTION 3";
            }
            return null;
        }
    }

    private Intent results;
    private EditText searchBox;
    private String query;

    /**
     * Get results intent
     *
     * @return intent
     */
    private Intent getResults() {
        return results;
    }

    /**
     * Get search intent
     *
     * @return intent
     */
    private EditText getSearchBox() {
        return searchBox;
    }

    /**
     * Get query intent
     *
     * @return intent
     */
    private String getQuery() {
        return query;
    }

    /**
     * Search function
     *
     * @param v View for layout
     */
    @SuppressWarnings(UNUSED)
    public void buttonSearchClick(View v) {
        results = new Intent(c, SearchActivity.class);
        searchBox = (EditText) findViewById(R.id.search_box);
        query = getSearchBox().getText().toString();
        results.putExtra("QUERY", getQuery());
        results.putExtra("TITLE", true);

        if (query.length() < 2) {
            Toast.makeText(this, "Searches must be at least 2 characters in length.", Toast.LENGTH_LONG).show();
        } else {
            startActivity(getResults());
        }
    }

    /**
     * Top recommendation function
     *
     * @param v View for layout
     */
    @SuppressWarnings(UNUSED)
    public void buttonSearchClick2(View v) {
        results = new Intent(getApplicationContext(), SearchActivity.class);
        results.putExtra("QUERY", "TOP");
        results.putExtra("TYPE", false);
        startActivity(getResults());
    }

    /**
     * Major recommendation function
     *
     * @param v View for layout
     */
    @SuppressWarnings(UNUSED)
    public void buttonSearchClick3(View v) {
        results = new Intent(getApplicationContext(), SearchActivity.class);
        results.putExtra("QUERY", "MAJOR");
        results.putExtra("TYPE", false);

        if (World.getCurrentUser().getProfile() == null) {
            Toast.makeText(this, "You must have a profile to get a recommendation.", Toast.LENGTH_LONG).show();
        } else {
            startActivity(getResults());
        }
    }

}
