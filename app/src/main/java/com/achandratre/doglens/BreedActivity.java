package com.achandratre.doglens;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.bridge.Bridge;
import com.afollestad.bridge.BridgeException;
import com.afollestad.bridge.Request;
import com.afollestad.materialdialogs.MaterialDialog;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.klinker.android.sliding.MultiShrinkScroller;
import com.klinker.android.sliding.SlidingActivity;

import org.w3c.dom.Text;

import java.util.Arrays;

import es.dmoral.toasty.Toasty;

public class BreedActivity extends SlidingActivity {

    public String dog_breed = null;
    public String file_name = null;
    @Override
    public void init(Bundle savedInstanceState) {
        Bundle extras = getIntent().getExtras();
        String file_path = extras.getString("file_path");
        dog_breed = extras.getString("dog_breed");
        String predict_one = extras.getString("predict_one");
        String predict_two = extras.getString("predict_two");
        file_name = extras.getString("filename");

        String could_be = predict_one + " / " + predict_two;

        setTitle(dog_breed);
        setDogImage(file_path);

        setPrimaryColors(
                getResources().getColor(R.color.colorPrimary),
                getResources().getColor(R.color.colorPrimaryDark)
        );

        setContent(R.layout.content_breed);

        setFab(getResources().getColor(R.color.colorAccent), R.drawable.check_all, verifyBreed);

        TextView dogBreed = (TextView) findViewById(R.id.breedView);
        dogBreed.setText(dog_breed);

        TextView predictText = (TextView) findViewById(R.id.predictView);
        predictText.setText(could_be);

        Toasty.success(BreedActivity.this, "Recognition Complete", Toast.LENGTH_SHORT, true).show();
    }

    private View.OnClickListener verifyBreed = new View.OnClickListener() {
        public void onClick(View v) {
            try {
                Request request = Bridge
                        .get(Constants.SERVER+ "verify/%s&%s", dog_breed, file_name)
                        .request();
                Toasty.success(BreedActivity.this, "Breed verified as " + dog_breed, Toast.LENGTH_SHORT, true).show();
            } catch (BridgeException e) {
                e.printStackTrace();
                Toasty.error(BreedActivity.this, "Error reaching server", Toast.LENGTH_SHORT, true).show();
            }
        }
    };


    public void setDogImage(String url) {
        Glide.with(this)
                .load(url)
                .asBitmap()
                .into(new SimpleTarget<Bitmap>() {
                    @Override
                    public void onResourceReady(Bitmap resource, GlideAnimation glideAnimation) {
                        setImage(resource);
                    }
                });
    }

    public void markAsIncorrect(View v) {
        String[] list = getResources().getStringArray(R.array.breeds);
        Arrays.sort(list);

        new MaterialDialog.Builder(this)
                .title("Mark as incorrect")
                .items(list)
                .itemsCallbackSingleChoice(-1, new MaterialDialog.ListCallbackSingleChoice() {
                    @Override
                    public boolean onSelection(MaterialDialog dialog, View view, int which, CharSequence text) {
                        /**
                         * If you use alwaysCallSingleChoiceCallback(), which is discussed below,
                         * returning false here won't allow the newly selected radio button to actually be selected.
                         **/
                        if (text != null){
                            try {
                                String new_breed = text.toString();
                                Request request = Bridge
                                        .get(Constants.SERVER+ "new_breed/%s&%s", new_breed, file_name)
                                        .request();
                                Toasty.success(BreedActivity.this, "Breed marked as " + new_breed, Toast.LENGTH_SHORT, true).show();
                            } catch (BridgeException e) {
                                e.printStackTrace();
                                Toasty.error(BreedActivity.this, "Error communicating with server", Toast.LENGTH_SHORT, true).show();
                            }

                        } else{
                            Toasty.error(BreedActivity.this, "Choose one of the options first.", Toast.LENGTH_SHORT, true).show();
                        }
                        return true;
                    }
                })
                .positiveText("Choose")
                .show();
    }

    @Override
    protected void configureScroller(MultiShrinkScroller scroller) {
        super.configureScroller(scroller);
        scroller.setIntermediateHeaderHeightRatio(1);
    }
}