package com.achandratre.doglens.helper;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.support.v4.content.ContextCompat;

import com.achandratre.doglens.AboutActivity;
import com.achandratre.doglens.LicensesActivity;
import com.achandratre.doglens.R;
import com.danielstone.materialaboutlibrary.ConvenienceBuilder;
import com.danielstone.materialaboutlibrary.items.MaterialAboutActionItem;
import com.danielstone.materialaboutlibrary.items.MaterialAboutItemOnClickAction;
import com.danielstone.materialaboutlibrary.items.MaterialAboutTitleItem;
import com.danielstone.materialaboutlibrary.model.MaterialAboutCard;
import com.danielstone.materialaboutlibrary.model.MaterialAboutList;
import com.danielstone.materialaboutlibrary.util.OpenSourceLicense;
import com.mikepenz.community_material_typeface_library.CommunityMaterial;
import com.mikepenz.google_material_typeface_library.GoogleMaterial;
import com.mikepenz.iconics.IconicsDrawable;

/**
 * Created by achandratre on 1/6/17.
 */

public class AboutHelper {
    public static MaterialAboutList createMaterialAboutList(final Context c, final int colorIcon,  final int theme) {
        MaterialAboutCard.Builder appCardBuilder = new MaterialAboutCard.Builder();

        // Add items to card

        appCardBuilder.addItem(new MaterialAboutTitleItem.Builder()
                .text("Dog Lens")
                .icon(R.mipmap.ic_launcher)
                .build());

        try {

            appCardBuilder.addItem(ConvenienceBuilder.createVersionActionItem(c,
                    new IconicsDrawable(c)
                            .icon(GoogleMaterial.Icon.gmd_info_outline)
                            .color(ContextCompat.getColor(c, colorIcon))
                            .sizeDp(18),
                    "Version",
                    false));

        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

        appCardBuilder.addItem(new MaterialAboutActionItem.Builder()
                .text("Changelog")
                .icon(new IconicsDrawable(c)
                        .icon(CommunityMaterial.Icon.cmd_history)
                        .color(ContextCompat.getColor(c, colorIcon))
                        .sizeDp(18))
                .setOnClickAction(ConvenienceBuilder.createWebViewDialogOnClickAction(c, "Releases", "1. Initial Release", false, true))
                .build());

        appCardBuilder.addItem(new MaterialAboutActionItem.Builder()
                .text("Licenses")
                .icon(new IconicsDrawable(c)
                        .icon(GoogleMaterial.Icon.gmd_book)
                        .color(ContextCompat.getColor(c, colorIcon))
                        .sizeDp(18))
                .setOnClickAction(new MaterialAboutItemOnClickAction() {
                    @Override
                    public void onClick() {
                        Intent intent = new Intent(c, LicensesActivity.class);
                        intent.putExtra(AboutActivity.THEME_EXTRA, theme);
                        c.startActivity(intent);
                        c.startActivity(intent);
                    }
                })
                .build());

        MaterialAboutCard.Builder authorCardBuilder = new MaterialAboutCard.Builder();
        authorCardBuilder.title("Author");
//        authorCardBuilder.titleColor(ContextCompat.getColor(c, R.color.colorAccent));

        authorCardBuilder.addItem(new MaterialAboutActionItem.Builder()
                .text("Aniruddh Chandratre")
                .subText("India")
                .icon(new IconicsDrawable(c)
                        .icon(GoogleMaterial.Icon.gmd_person)
                        .color(ContextCompat.getColor(c, colorIcon))
                        .sizeDp(18))
                .build());

        authorCardBuilder.addItem(new MaterialAboutActionItem.Builder()
                .text("Follow on GitHub")
                .icon(new IconicsDrawable(c)
                        .icon(CommunityMaterial.Icon.cmd_github_circle)
                        .color(ContextCompat.getColor(c, colorIcon))
                        .sizeDp(18))
                .setOnClickAction(ConvenienceBuilder.createWebsiteOnClickAction(c, Uri.parse("https://github.com/C-Aniruddh")))
                .build());

        authorCardBuilder.addItem(new MaterialAboutActionItem.Builder()
                .text("Follow on Twitter")
                .icon(new IconicsDrawable(c)
                        .icon(CommunityMaterial.Icon.cmd_twitter_circle)
                        .color(ContextCompat.getColor(c, colorIcon))
                        .sizeDp(18))
                .setOnClickAction(ConvenienceBuilder.createWebsiteOnClickAction(c, Uri.parse("https://twitter.com/Aniruddhc")))
                .build());

        MaterialAboutCard.Builder convenienceCardBuilder = new MaterialAboutCard.Builder();

        convenienceCardBuilder.title("Contact / Support Me");

        convenienceCardBuilder.addItem(ConvenienceBuilder.createWebsiteActionItem(c,
                new IconicsDrawable(c)
                        .icon(CommunityMaterial.Icon.cmd_earth)
                        .color(ContextCompat.getColor(c, colorIcon))
                        .sizeDp(18),
                "Visit Website",
                true,
                Uri.parse("http://github.com/C-Aniruddh")));

        convenienceCardBuilder.addItem(ConvenienceBuilder.createRateActionItem(c,
                new IconicsDrawable(c)
                        .icon(CommunityMaterial.Icon.cmd_star)
                        .color(ContextCompat.getColor(c, colorIcon))
                        .sizeDp(18),
                "Rate this app",
                null
        ));

        convenienceCardBuilder.addItem(ConvenienceBuilder.createEmailItem(c,
                new IconicsDrawable(c)
                        .icon(CommunityMaterial.Icon.cmd_email)
                        .color(ContextCompat.getColor(c, colorIcon))
                        .sizeDp(18),
                "Send an email",
                true,
                "c.aniruddh98@gmail.com",
                "Question concerning Dog Lens"));
        return new MaterialAboutList(appCardBuilder.build(), authorCardBuilder.build(), convenienceCardBuilder.build());
    }

    public static MaterialAboutList createMaterialAboutLicenseList(final Context c, int colorIcon) {

        MaterialAboutCard supportv7LicenseCard = ConvenienceBuilder.createLicenseCard(c,
                new IconicsDrawable(c)
                        .icon(GoogleMaterial.Icon.gmd_book)
                        .color(ContextCompat.getColor(c, colorIcon))
                        .sizeDp(18),
                "Support AppCompat V7", "2017", "Google",
                OpenSourceLicense.APACHE_2);

        MaterialAboutCard designLicenseCard = ConvenienceBuilder.createLicenseCard(c,
                new IconicsDrawable(c)
                        .icon(GoogleMaterial.Icon.gmd_book)
                        .color(ContextCompat.getColor(c, colorIcon))
                        .sizeDp(18),
                "Support Design", "2017", "Google",
                OpenSourceLicense.APACHE_2);

        MaterialAboutCard constraintLicenseCard = ConvenienceBuilder.createLicenseCard(c,
                new IconicsDrawable(c)
                        .icon(GoogleMaterial.Icon.gmd_book)
                        .color(ContextCompat.getColor(c, colorIcon))
                        .sizeDp(18),
                "Support Constraint Layout", "2017", "Google",
                OpenSourceLicense.APACHE_2);

        MaterialAboutCard lottieLicenseCard = ConvenienceBuilder.createLicenseCard(c,
                new IconicsDrawable(c)
                        .icon(GoogleMaterial.Icon.gmd_book)
                        .color(ContextCompat.getColor(c, colorIcon))
                        .sizeDp(18),
                "Lottie Android", "2017", "Airbnb",
                OpenSourceLicense.APACHE_2);

        MaterialAboutCard cameraLicenseCard = ConvenienceBuilder.createLicenseCard(c,
                new IconicsDrawable(c)
                        .icon(GoogleMaterial.Icon.gmd_book)
                        .color(ContextCompat.getColor(c, colorIcon))
                        .sizeDp(18),
                "Material Camera", "2017", "Aidan Follestad",
                OpenSourceLicense.APACHE_2);

        MaterialAboutCard bridgeLicenseCard = ConvenienceBuilder.createLicenseCard(c,
                new IconicsDrawable(c)
                        .icon(GoogleMaterial.Icon.gmd_book)
                        .color(ContextCompat.getColor(c, colorIcon))
                        .sizeDp(18),
                "Bridge", "2017", "Aidan Follestad",
                OpenSourceLicense.APACHE_2);

        MaterialAboutCard toastyLicenseCard = ConvenienceBuilder.createLicenseCard(c,
                new IconicsDrawable(c)
                        .icon(GoogleMaterial.Icon.gmd_book)
                        .color(ContextCompat.getColor(c, colorIcon))
                        .sizeDp(18),
                "Toasty", "2017", "GrenderG",
                OpenSourceLicense.GNU_GPL_3);

        MaterialAboutCard dialogLicenseCard = ConvenienceBuilder.createLicenseCard(c,
                new IconicsDrawable(c)
                        .icon(GoogleMaterial.Icon.gmd_book)
                        .color(ContextCompat.getColor(c, colorIcon))
                        .sizeDp(18),
                "Material Dialogs", "2017", "Aidan Follestad",
                OpenSourceLicense.APACHE_2);

        MaterialAboutCard slidingLicenseCard = ConvenienceBuilder.createLicenseCard(c,
                new IconicsDrawable(c)
                        .icon(GoogleMaterial.Icon.gmd_book)
                        .color(ContextCompat.getColor(c, colorIcon))
                        .sizeDp(18),
                "Sliding Activity", "2017", "Jacob Klinker",
                OpenSourceLicense.APACHE_2);

        MaterialAboutCard glideLicenseCard = ConvenienceBuilder.createLicenseCard(c,
                new IconicsDrawable(c)
                        .icon(GoogleMaterial.Icon.gmd_book)
                        .color(ContextCompat.getColor(c, colorIcon))
                        .sizeDp(18),
                "Glide", "2017", "Bumptech",
                OpenSourceLicense.MIT);

        MaterialAboutCard appIntroLicenseCard = ConvenienceBuilder.createLicenseCard(c,
                new IconicsDrawable(c)
                        .icon(GoogleMaterial.Icon.gmd_book)
                        .color(ContextCompat.getColor(c, colorIcon))
                        .sizeDp(18),
                "AppIntro", "2017", "Paolo Rotolo, Maximilian Narr",
                OpenSourceLicense.APACHE_2);


        MaterialAboutCard materialAboutLibraryLicenseCard = ConvenienceBuilder.createLicenseCard(c,
                new IconicsDrawable(c)
                        .icon(GoogleMaterial.Icon.gmd_book)
                        .color(ContextCompat.getColor(c, colorIcon))
                        .sizeDp(18),
                "material-about-library", "2016", "Daniel Stone",
                OpenSourceLicense.APACHE_2);

        MaterialAboutCard androidIconicsLicenseCard = ConvenienceBuilder.createLicenseCard(c,
                new IconicsDrawable(c)
                        .icon(GoogleMaterial.Icon.gmd_book)
                        .color(ContextCompat.getColor(c, colorIcon))
                        .sizeDp(18),
                "Android Iconics", "2016", "Mike Penz",
                OpenSourceLicense.APACHE_2);


        return new MaterialAboutList(
                supportv7LicenseCard,
                designLicenseCard,
                constraintLicenseCard,
                lottieLicenseCard,
                cameraLicenseCard,
                bridgeLicenseCard,
                toastyLicenseCard,
                dialogLicenseCard,
                slidingLicenseCard,
                glideLicenseCard,
                appIntroLicenseCard,
                materialAboutLibraryLicenseCard,
                androidIconicsLicenseCard);
    }
}
