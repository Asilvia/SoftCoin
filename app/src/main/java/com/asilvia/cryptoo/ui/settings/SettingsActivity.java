package com.asilvia.cryptoo.ui.settings;

import android.annotation.TargetApi;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.preference.SwitchPreference;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.preference.PreferenceFragment;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.support.v4.app.NavUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.asilvia.cryptoo.BuildConfig;
import com.asilvia.cryptoo.R;

/**
 * A {@link PreferenceActivity} that presents a set of application settings. On
 * handset devices, settings are presented as a single list. On tablets,
 * settings are split by category, with category headers shown to the left of
 * the list of settings.
 * <p>
 * See <a href="http://developer.android.com/design/patterns/settings.html">
 * Android Design: Settings</a> for design guidelines and the <a
 * href="http://developer.android.com/guide/topics/ui/settings.html">Settings
 * API Guide</a> for more information on developing a Settings UI.
 */
public class SettingsActivity extends AppCompatPreferenceActivity {

    public boolean isSecond = false;




    /**
     * Helper method to determine if the device has an extra-large screen. For
     * example, 10" tablets are extra-large.
     */
    private static boolean isXLargeTablet(Context context) {
        return (context.getResources().getConfiguration().screenLayout
                & Configuration.SCREENLAYOUT_SIZE_MASK) >= Configuration.SCREENLAYOUT_SIZE_XLARGE;
    }



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getListView().setBackgroundColor(getResources().getColor(R.color.colorPrimary));
        View v = getLayoutInflater().inflate(R.layout.layout_line, null);
        getListView().addHeaderView(v);
        getFragmentManager().beginTransaction()
                .replace(android.R.id.content, new SettingsFragment())
                .commit();
        getListView().setDivider(null);
        getListView().setPadding(0, 0, 0, 0);

    }

    /**
     * Set up the {@link android.app.ActionBar}, if the API is available.
     */
    private void setupActionBar(int value) {
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            // Show the Up button in the action bar.
            actionBar.setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle(value);
            getSupportActionBar().setDisplayShowTitleEnabled(true);
        }
    }

    @Override
    public boolean onMenuItemSelected(int featureId, MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            if(isSecond)
            {
                onBackPressed();
                return true;
            }
            if (!super.onMenuItemSelected(featureId, item))
            {
                NavUtils.navigateUpFromSameTask(this);
            }
            return true;
        }
        return super.onMenuItemSelected(featureId, item);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean onIsMultiPane() {
        return isXLargeTablet(this);
    }


    /**
     * This method stops fragment injection in malicious applications.
     * Make sure to deny any unknown fragments here.
     */
    protected boolean isValidFragment(String fragmentName) {
        return PreferenceFragment.class.getName().equals(fragmentName)
                || SettingsFragment.class.getName().equals(fragmentName)
                || PrivacyFragment.class.getName().equals(fragmentName);
    }








    public static class SettingsFragment extends PreferenceFragment implements Preference.OnPreferenceChangeListener, Preference.OnPreferenceClickListener {

        SwitchPreference market;
        SwitchPreference profit;
        Preference about;
        Preference rate;
        Preference share;
        Preference sendFeedback;
        Preference privacy;




        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);

            ((SettingsActivity) getActivity()).setupActionBar(R.string.pref_title_settings);
            addPreferencesFromResource(R.xml.settings);

            profit = (SwitchPreference) findPreference("profit");
            market = (SwitchPreference) findPreference("market");
            rate = findPreference("rate");
            share = findPreference("share");
            sendFeedback = findPreference("sendFeedback");
            privacy = findPreference("privacy");
            about = findPreference("about");

            share.setOnPreferenceClickListener(this);
            privacy.setOnPreferenceClickListener(this);
            profit.setOnPreferenceChangeListener(this);
            market.setOnPreferenceChangeListener(this);
            sendFeedback.setOnPreferenceClickListener(this);
            rate.setOnPreferenceClickListener(this);
            about.setOnPreferenceClickListener(this);



        }

        @Override
        public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
            View view = super.onCreateView(inflater, container, savedInstanceState);
            if (view != null) {
                ListView preferencesList = (ListView) view.findViewById(android.R.id.list);
                float scale = getResources().getDisplayMetrics().density;
                int dpAsPixels = (int) (24 *scale + 0.5f);
                preferencesList.setPadding(dpAsPixels, 0, dpAsPixels, 0);
                ColorDrawable divider = new ColorDrawable(this.getResources().getColor(R.color.cards_background));
                preferencesList.setDivider(divider);
                preferencesList.setDividerHeight(1);
            }
            return view;
        }





        @Override
        public boolean onPreferenceChange(Preference preference, Object value) {
            Boolean mValue = Boolean.valueOf(value.toString());
            if(preference.equals(market))
            {
              if(mValue == true) {
                  profit.setChecked(false);
              }
              else {
                  profit.setChecked(true);
              }
              return true;
            }
            else  if(preference.equals(profit))
            {
                if(mValue == true) {
                    market.setChecked(false);
                }
                else {
                    market.setChecked(true);
                }
                return true;
            }
            return false;
        }

        @Override
        public boolean onPreferenceClick(Preference preference) {
            if(preference.equals(rate))
            {
                rate();
            }
            else if(preference.equals(share))
            {
                share();
            }
            else if(preference.equals(sendFeedback))
            {
                sendFeedback();
            }
            else if(preference.equals(privacy))
            {

                openPrivacy();
            }
            else if(preference.equals(about))
            {

                openAbout();
            }


            return false;
        }

        private void openPrivacy() {
            ((SettingsActivity)getActivity()).isSecond = true;
            getFragmentManager().beginTransaction()
                    .replace(android.R.id.content, new PrivacyFragment())
                    .addToBackStack(null)
                    .commit();
        }

        private void openAbout() {
            ((SettingsActivity)getActivity()).isSecond = true;
            getFragmentManager().beginTransaction()
                    .replace(android.R.id.content, new AboutFragment())
                    .addToBackStack(null)
                    .commit();
        }

        private void sendFeedback() {
            Intent emailIntent = new Intent(Intent.ACTION_SENDTO, Uri.fromParts(
                    "mailto", BuildConfig.MAINEMAIL, null));
            startActivity(Intent.createChooser(emailIntent, "Send email..."));
        }

        private void share() {
            Intent sendIntent = new Intent();
            sendIntent.setAction(Intent.ACTION_SEND);
            sendIntent.putExtra(Intent.EXTRA_TEXT, getActivity().getResources().getString(R.string.store)+getActivity().getPackageName());
            sendIntent.setType("text/plain");
            startActivity(sendIntent);
        }

        private void rate() {
            try {
                Uri marketUri = Uri.parse("market://details?id=" + getActivity().getPackageName());
                Intent marketIntent = new Intent(Intent.ACTION_VIEW, marketUri);
                startActivity(marketIntent);
            }catch(ActivityNotFoundException e) {
                Uri marketUri = Uri.parse("https://play.google.com/store/apps/details?id=" + getActivity().getPackageName());
                Intent marketIntent = new Intent(Intent.ACTION_VIEW, marketUri);
                startActivity(marketIntent);
            }
        }
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    public static class PrivacyFragment extends PreferenceFragment {
        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            addPreferencesFromResource(R.xml.privacy);
            setHasOptionsMenu(true);
            ((SettingsActivity) getActivity()).setupActionBar(R.string.pref_title_privacy);


        }

        @Override
        public void onDetach() {
            super.onDetach();
            ((SettingsActivity) getActivity()).setupActionBar(R.string.pref_title_settings);
            ((SettingsActivity) getActivity()).isSecond = false;

        }
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    public static class AboutFragment extends PreferenceFragment {
        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            addPreferencesFromResource(R.xml.about);
            setHasOptionsMenu(true);
            ((SettingsActivity) getActivity()).setupActionBar(R.string.pref_title_about);


        }

        @Override
        public void onDetach() {
            super.onDetach();
            ((SettingsActivity) getActivity()).setupActionBar(R.string.pref_title_settings);
            ((SettingsActivity) getActivity()).isSecond = false;

        }
    }
}
