package com.android.asilvia.cryptoo.ui.settings;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Fragment;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Color;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.preference.SwitchPreference;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;
import android.preference.RingtonePreference;
import android.support.v7.widget.ShareActionProvider;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.support.v4.app.NavUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.android.asilvia.cryptoo.BuildConfig;
import com.android.asilvia.cryptoo.R;

import java.util.List;

import timber.log.Timber;

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

    public boolean isPrivacy = false;



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
            if(isPrivacy)
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

        SwitchPreference profitPercentagePreference;
        SwitchPreference profitAmountPreference;
        SwitchPreference indexPercentagePreference;
        SwitchPreference marketPricePreference;
        Preference rate;
        Preference share;
        Preference sendFeedback;
        Preference privacy;




        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);

            ((SettingsActivity) getActivity()).setupActionBar(R.string.pref_title_settings);
            addPreferencesFromResource(R.xml.settings);

            setSwitchPreferencesValues();
            rate = findPreference("rate");
            rate.setOnPreferenceClickListener(this);
            share = findPreference("share");
            share.setOnPreferenceClickListener(this);
            sendFeedback = findPreference("sendFeedback");
            sendFeedback.setOnPreferenceClickListener(this);
            privacy = findPreference("privacy");
            privacy.setOnPreferenceClickListener(this);



        }

        @Override
        public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
            View view = super.onCreateView(inflater, container, savedInstanceState);
            if (view != null) {
                ListView preferencesList = (ListView) view.findViewById(android.R.id.list);
                preferencesList.setPadding(50, 50, 50, 50);
            }
            return view;
        }


        private void setSwitchPreferencesValues() {
            profitPercentagePreference = (SwitchPreference) findPreference("profit_percentage");
            profitAmountPreference = (SwitchPreference) findPreference("profit_amount");
            indexPercentagePreference = (SwitchPreference) findPreference("index");
            marketPricePreference = (SwitchPreference) findPreference("market");

            profitPercentagePreference.setOnPreferenceChangeListener(this);
            profitAmountPreference.setOnPreferenceChangeListener(this);
            indexPercentagePreference.setOnPreferenceChangeListener(this);
            marketPricePreference.setOnPreferenceChangeListener(this);
        }


        @Override
        public boolean onPreferenceChange(Preference preference, Object value) {
            Boolean mValue = Boolean.valueOf(value.toString());
            if(preference.equals(profitPercentagePreference))
            {
              if(mValue == true) {
                  indexPercentagePreference.setChecked(false);
                  profitAmountPreference.setChecked(true);
                  marketPricePreference.setChecked(false);

              }
              else {
                  indexPercentagePreference.setChecked(true);
                  profitAmountPreference.setChecked(false);
                  marketPricePreference.setChecked(true);

              }
              return true;
            }
            else  if(preference.equals(profitAmountPreference))
            {
                if(mValue == true) {
                    indexPercentagePreference.setChecked(false);
                    marketPricePreference.setChecked(false);
                    profitPercentagePreference.setChecked(true);
                }
                else {
                    indexPercentagePreference.setChecked(true);
                    marketPricePreference.setChecked(true);
                    profitPercentagePreference.setChecked(false);
                }
                return true;
            }
            else  if(preference.equals(indexPercentagePreference))
            {
                if(mValue == true)
                {
                    profitAmountPreference.setChecked(false);
                    marketPricePreference.setChecked(true);
                    profitPercentagePreference.setChecked(false);
                }
                else {
                    profitAmountPreference.setChecked(true);
                    marketPricePreference.setChecked(false);
                    profitPercentagePreference.setChecked(true);
                }
                return true;
            }
            else if(preference.equals(marketPricePreference))
            {
                if(mValue == true) {
                    indexPercentagePreference.setChecked(true);
                    profitAmountPreference.setChecked(false);
                    profitPercentagePreference.setChecked(false);
                }
                else {
                    indexPercentagePreference.setChecked(false);
                    profitAmountPreference.setChecked(true);
                    profitPercentagePreference.setChecked(true);
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


            return false;
        }

        private void openPrivacy() {
            ((SettingsActivity)getActivity()).isPrivacy = true;
            getFragmentManager().beginTransaction()
                    .replace(android.R.id.content, new PrivacyFragment())
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
            ((SettingsActivity) getActivity()).isPrivacy = false;

        }
    }
}
