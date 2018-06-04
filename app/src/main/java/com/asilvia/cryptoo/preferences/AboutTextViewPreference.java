package com.asilvia.cryptoo.preferences;

import android.content.Context;
import android.preference.Preference;
import android.text.Html;
import android.util.AttributeSet;
import android.view.View;
import android.widget.TextView;

import com.asilvia.cryptoo.R;

/**
 * Created by asilvia on 13/04/2018.
 */

public class AboutTextViewPreference extends Preference {


    public AboutTextViewPreference(Context context, AttributeSet attrs) {
        super(context, attrs);
        setWidgetLayoutResource(R.layout.about);
    }

    @Override
    protected void onBindView(View view) {
        super.onBindView(view);
        TextView main = (TextView) view.findViewById(R.id.main_content);
       // main.setText( Html.fromHtml(test,Html.FROM_HTML_MODE_LEGACY));

    }
}
