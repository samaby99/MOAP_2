package at.technikum_wien.sommerbauer.newsreader.settings;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.preference.CheckBoxPreference;
import android.support.v7.preference.Preference;
import android.support.v7.preference.PreferenceFragmentCompat;
import android.support.v7.preference.PreferenceScreen;

import at.technikum_wien.sommerbauer.newsreader.R;

public class SettingsFragment extends PreferenceFragmentCompat implements SharedPreferences.OnSharedPreferenceChangeListener {
  public SettingsFragment() {
    //Required!
  }

  @Override
  public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
    addPreferencesFromResource(R.xml.pref_general);

    PreferenceScreen preferenceScreen = getPreferenceScreen();
    SharedPreferences sharedPreferences = preferenceScreen.getSharedPreferences();
    int count = preferenceScreen.getPreferenceCount();
    for (int i = 0; i < count; i++) {
      Preference p = preferenceScreen.getPreference(i);
      if (p instanceof CheckBoxPreference) {
      }
      else {
        String value = sharedPreferences.getString(p.getKey(), "");
        setPreferenceSummary(p, value);
      }
    }
  }

  private void setPreferenceSummary(Preference preference, String value) {
    preference.setSummary(value);
  }

  @Override
  public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
    Preference p = findPreference(key);

    if (p != null) {
      if (p instanceof CheckBoxPreference) {
      }
      else {
        String value = sharedPreferences.getString(p.getKey(), "");
        setPreferenceSummary(p, value);
      }
    }
  }

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    getPreferenceScreen().getSharedPreferences().registerOnSharedPreferenceChangeListener(this);
  }

  @Override
  public void onDestroy() {
    super.onDestroy();
    getPreferenceScreen().getSharedPreferences().unregisterOnSharedPreferenceChangeListener(this);
  }
}
