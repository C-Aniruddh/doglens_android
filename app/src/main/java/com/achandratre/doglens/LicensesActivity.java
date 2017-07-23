package com.achandratre.doglens;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;

import com.achandratre.doglens.helper.AboutHelper;
import com.danielstone.materialaboutlibrary.model.MaterialAboutList;

public class LicensesActivity extends AboutActivity {

    @NonNull
    @Override
    protected MaterialAboutList getMaterialAboutList(@NonNull final Context c) {
        return AboutHelper.createMaterialAboutLicenseList(c, R.color.colorAccent);
    }

    @Override
    protected CharSequence getActivityTitle() {
        return getString(R.string.mal_title_licenses);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
            default:
                return false;
        }
    }
}
