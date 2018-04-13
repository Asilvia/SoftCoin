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
import android.support.v7.app.ActionBar;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;
import android.preference.RingtonePreference;
import android.support.v7.widget.ShareActionProvider;
import android.text.TextUtils;
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

    /**
     * A preference value change listener that updates the preference's summary
     * to reflect its new value.
     */
    private static Preference.OnPreferenceChangeListener sBindPreferenceSummaryToValueListener = new Preference.OnPreferenceChangeListener() {
        @Override
        public boolean onPreferenceChange(Preference preference, Object value) {
            String stringValue = value.toString();


            if (preference instanceof ListPreference) {
                // For list preferences, look up the correct display value in
                // the preference's 'entries' list.
                ListPreference listPreference = (ListPreference) preference;
                int index = listPreference.findIndexOfValue(stringValue);

                // Set the summary to reflect the new value.
                preference.setSummary(
                        index >= 0
                                ? listPreference.getEntries()[index]
                                : null);

            } else if (preference instanceof RingtonePreference) {
                // For ringtone preferences, look up the correct display value
                // using RingtoneManager.
                if (TextUtils.isEmpty(stringValue)) {
                    // Empty values correspond to 'silent' (no ringtone).
                    preference.setSummary(R.string.pref_ringtone_silent);

                } else {
                    Ringtone ringtone = RingtoneManager.getRingtone(
                            preference.getContext(), Uri.parse(stringValue));

                    if (ringtone == null) {
                        // Clear the summary if there was a lookup error.
                        preference.setSummary(null);
                    } else {
                        // Set the summary to reflect the new ringtone display
                        // name.
                        String name = ringtone.getTitle(preference.getContext());
                        preference.setSummary(name);
                    }
                }

            } else {
                // For all other preferences, set the summary to the value's
                // simple string representation.
                preference.setSummary(stringValue);
            }
            return true;
        }
    };

    /**
     * Helper method to determine if the device has an extra-large screen. For
     * example, 10" tablets are extra-large.
     */
    private static boolean isXLargeTablet(Context context) {
        return (context.getResources().getConfiguration().screenLayout
                & Configuration.SCREENLAYOUT_SIZE_MASK) >= Configuration.SCREENLAYOUT_SIZE_XLARGE;
    }

    /**
     * Binds a preference's summary to its value. More specifically, when the
     * preference's value is changed, its summary (line of text below the
     * preference title) is updated to reflect the value. The summary is also
     * immediately updated upon calling this method. The exact display format is
     * dependent on the type of preference.
     *
     * @see #sBindPreferenceSummaryToValueListener
     */
    private static void bindPreferenceSummaryToValue(Preference preference) {
        // Set the listener to watch for value changes.
        preference.setOnPreferenceChangeListener(sBindPreferenceSummaryToValueListener);

        // Trigger the listener immediately with the preference's
        // current value.
        sBindPreferenceSummaryToValueListener.onPreferenceChange(preference,
                PreferenceManager
                        .getDefaultSharedPreferences(preference.getContext())
                        .getString(preference.getKey(), ""));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setupActionBar();
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
    private void setupActionBar() {
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            // Show the Up button in the action bar.
            actionBar.setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle(R.string.title_activity_settings);
            getSupportActionBar().setDisplayShowTitleEnabled(true);
        }
    }

    @Override
    public boolean onMenuItemSelected(int featureId, MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            if (!super.onMenuItemSelected(featureId, item)) {
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
                || GeneralPreferenceFragment.class.getName().equals(fragmentName)
                || DataSyncPreferenceFragment.class.getName().equals(fragmentName)
                || NotificationPreferenceFragment.class.getName().equals(fragmentName);
    }

    /**
     * This fragment shows general preferences only. It is used when the
     * activity is showing a two-pane settings UI.
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    public static class GeneralPreferenceFragment extends PreferenceFragment {
        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            addPreferencesFromResource(R.xml.pref_general);
            setHasOptionsMenu(true);

            // Bind the summaries of EditText/List/Dialog/Ringtone preferences
            // to their values. When their values change, their summaries are
            // updated to reflect the new value, per the Android Design
            // guidelines.
            bindPreferenceSummaryToValue(findPreference("example_text"));
            bindPreferenceSummaryToValue(findPreference("example_list"));
        }

        @Override
        public boolean onOptionsItemSelected(MenuItem item) {
            int id = item.getItemId();
            if (id == android.R.id.home) {
                startActivity(new Intent(getActivity(), SettingsActivity.class));
                return true;
            }
            return super.onOptionsItemSelected(item);
        }
    }

    /**
     * This fragment shows notification preferences only. It is used when the
     * activity is showing a two-pane settings UI.
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    public static class NotificationPreferenceFragment extends PreferenceFragment {
        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            addPreferencesFromResource(R.xml.pref_notification);
            setHasOptionsMenu(true);

            // Bind the summaries of EditText/List/Dialog/Ringtone preferences
            // to their values. When their values change, their summaries are
            // updated to reflect the new value, per the Android Design
            // guidelines.
            bindPreferenceSummaryToValue(findPreference("notifications_new_message_ringtone"));
        }

        @Override
        public boolean onOptionsItemSelected(MenuItem item) {
            int id = item.getItemId();
            if (id == android.R.id.home) {
                startActivity(new Intent(getActivity(), SettingsActivity.class));
                return true;
            }
            return super.onOptionsItemSelected(item);
        }
    }

    /**
     * This fragment shows data and sync preferences only. It is used when the
     * activity is showing a two-pane settings UI.
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    public static class DataSyncPreferenceFragment extends PreferenceFragment {
        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            addPreferencesFromResource(R.xml.pref_data_sync);
            setHasOptionsMenu(true);

            // Bind the summaries of EditText/List/Dialog/Ringtone preferences
            // to their values. When their values change, their summaries are
            // updated to reflect the new value, per the Android Design
            // guidelines.
            bindPreferenceSummaryToValue(findPreference("sync_frequency"));
        }

        @Override
        public boolean onOptionsItemSelected(MenuItem item) {
            int id = item.getItemId();
            if (id == android.R.id.home) {
                startActivity(new Intent(getActivity(), SettingsActivity.class));
                return true;
            }
            return super.onOptionsItemSelected(item);
        }
    }


    public static class SettingsFragment extends PreferenceFragment implements Preference.OnPreferenceChangeListener, Preference.OnPreferenceClickListener {

        SwitchPreference profitPercentagePreference;
        SwitchPreference profitAmountPreference;
        SwitchPreference indexPercentagePreference;
        SwitchPreference marketPricePreference;
        Preference rate;
        Preference share;
        Preference sendFeedback;




        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
       //     PreferenceManager.setDefaultValues(getActivity().getBaseContext(), R.xml.settings, false);
            addPreferencesFromResource(R.xml.settings);

            setSwitchPreferencesValues();
            rate = findPreference("rate");
            rate.setOnPreferenceClickListener(this);
            share = findPreference("share");
            share.setOnPreferenceClickListener(this);
            sendFeedback = findPreference("sendFeedback");
            sendFeedback.setOnPreferenceClickListener(this);




        //    ListPreference dataPref = (ListPreference) findPreference("default_currency");

        /*    if(dataPref.getValue() == null){
                dataPref.setValueIndex(0); //set to index of your deafult value
            }

            String preferencesName = this.getPreferenceManager().getSharedPreferencesName();

            Timber.d("===========settings===========" + preferencesName);*/



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
              if(mValue == true)
                  indexPercentagePreference.setChecked(false);
              else
                  indexPercentagePreference.setChecked(true);
              return true;
            }
            else  if(preference.equals(profitAmountPreference))
            {
                if(mValue == true)
                    marketPricePreference.setChecked(false);
                else
                    marketPricePreference.setChecked(true);
                return true;
            }
            else  if(preference.equals(indexPercentagePreference))
            {
                if(mValue == true)
                    profitPercentagePreference.setChecked(false);
                else
                    profitPercentagePreference.setChecked(true);
                return true;
            }
            else if(preference.equals(marketPricePreference))
            {
                if(mValue == true)
                    profitAmountPreference.setChecked(false);
                else
                    profitAmountPreference.setChecked(true);
                return true;
            }
            return false;
        }

        @Override
        public boolean onPreferenceClick(Preference preference) {

            Timber.d("===========settings on CLick===========" );
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


            return false;
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
}
