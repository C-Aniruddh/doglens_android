package com.achandratre.doglens;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.widget.Toast;

import com.achandratre.doglens.helper.AboutHelper;
import com.danielstone.materialaboutlibrary.ConvenienceBuilder;
import com.danielstone.materialaboutlibrary.MaterialAboutActivity;
import com.danielstone.materialaboutlibrary.items.MaterialAboutActionItem;
import com.danielstone.materialaboutlibrary.items.MaterialAboutItemOnClickAction;
import com.danielstone.materialaboutlibrary.items.MaterialAboutTitleItem;
import com.danielstone.materialaboutlibrary.model.MaterialAboutCard;
import com.danielstone.materialaboutlibrary.model.MaterialAboutList;
import com.mikepenz.community_material_typeface_library.CommunityMaterial;
import com.mikepenz.iconics.IconicsDrawable;

public class AboutActivity extends MaterialAboutActivity {

    public static final String THEME_EXTRA = "";
    public static final int THEME_LIGHT_LIGHTBAR = 0;
    public static final int THEME_LIGHT_DARKBAR = 1;
    public static final int THEME_DARK_LIGHTBAR = 2;
    public static final int THEME_DARK_DARKBAR = 3;
    protected int colorIcon = R.color.colorAccent;

    @Override
    protected MaterialAboutList getMaterialAboutList(Context context) {

        //return new MaterialAboutList.Builder()
          //      .build();
        return AboutHelper.createMaterialAboutList(context, R.color.colorAccent, getIntent().getIntExtra(THEME_EXTRA, THEME_DARK_DARKBAR));
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        switch (getIntent().getIntExtra(THEME_EXTRA, THEME_DARK_DARKBAR)) {
            case THEME_LIGHT_LIGHTBAR:
                setTheme(R.style.Theme_Mal_Light_LightActionBar);
                colorIcon = R.color.colorAccent;
                break;
            case THEME_DARK_LIGHTBAR:
                setTheme(R.style.Theme_Mal_Dark_LightActionBar);
                colorIcon = R.color.colorAccent;
                break;
            case THEME_LIGHT_DARKBAR:
                setTheme(R.style.Theme_Mal_Light_DarkActionBar);
                colorIcon = R.color.colorAccent;
                break;
            case THEME_DARK_DARKBAR:
                setTheme(R.style.Theme_Mal_Dark_DarkActionBar);
                colorIcon = R.color.colorAccent;
                break;
        }
        super.onCreate(savedInstanceState);
    }

    @Override
    protected CharSequence getActivityTitle() {
        return getString(R.string.mal_title_about);
    }

}
