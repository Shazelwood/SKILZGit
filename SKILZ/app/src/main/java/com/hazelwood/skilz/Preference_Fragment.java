package com.hazelwood.skilz;

import android.os.Bundle;
import android.preference.PreferenceFragment;

/**
 * Created by Hazelwood on 4/29/15.
 */
public class Preference_Fragment extends PreferenceFragment {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Load the preferences from an XML resource
        addPreferencesFromResource(R.xml.preference);

    }
}
