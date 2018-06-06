package com.interview.crowdfireinterview;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.LinearLayoutCompat;
import android.view.View;

import com.interview.crowdfireinterview.db.DbHelper;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import butterknife.BindView;
import butterknife.ButterKnife;
import pl.aprilapps.easyphotopicker.DefaultCallback;
import pl.aprilapps.easyphotopicker.EasyImage;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    @BindView(R.id.fabAddShirt)
    FloatingActionButton fabAddShirt;
    @BindView(R.id.fabAddPant)
    FloatingActionButton fabAddPant;
    @BindView(R.id.fabShuffle)
    FloatingActionButton fabShuffle;
    @BindView(R.id.fabFav)
    FloatingActionButton fabFav;
    @BindView(R.id.viewPagerShirts)
    ViewPager viewPagerShirts;
    @BindView(R.id.viewPagerPants)
    ViewPager viewPagerPants;
    private PagerAdapter shirtPagerAdapter, pantPagerAdapter;
    private DbHelper dbHelper;
    private ArrayList<Cloth> shirts;
    private ArrayList<Cloth> pants;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        dbHelper = new DbHelper(getApplicationContext());

        EasyImage.configuration(this)
                .setImagesFolderName(getString(R.string.app_name))
                .setCopyTakenPhotosToPublicGalleryAppFolder(false)
                .setCopyPickedImagesToPublicGalleryAppFolder(false)
                .setAllowMultiplePickInGallery(true);

        shirtPagerAdapter = new PagerAdapter();
        viewPagerShirts.setAdapter(shirtPagerAdapter);
        viewPagerShirts.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {


            }

            @Override
            public void onPageSelected(int position) {
                if (dbHelper.checkFav(shirts.get(position).getId(), pants.get(viewPagerPants.getCurrentItem()).getId()) == -1) {
                    fabFav.setImageResource(R.drawable.ic_favorite_border_white_24dp);
                } else {
                    fabFav.setImageResource(R.drawable.ic_favorite_white_24dp);
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        pantPagerAdapter = new PagerAdapter();
        viewPagerPants.setAdapter(pantPagerAdapter);
        viewPagerPants.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {


            }

            @Override
            public void onPageSelected(int position) {
                if (dbHelper.checkFav(shirts.get(viewPagerShirts.getCurrentItem()).getId(), pants.get(position).getId()) == -1) {
                    fabFav.setImageResource(R.drawable.ic_favorite_border_white_24dp);
                } else {
                    fabFav.setImageResource(R.drawable.ic_favorite_white_24dp);
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        fabAddShirt.setOnClickListener(this);
        fabAddPant.setOnClickListener(this);
        fabShuffle.setOnClickListener(this);
        fabFav.setOnClickListener(this);

        ArrayList<Cloth> cloths = dbHelper.getCloths();
        shirts = new ArrayList<>();
        pants = new ArrayList<>();

        if (cloths != null && cloths.size() > 0) {
            for (int i = 0; i < cloths.size(); i++) {
                if (cloths.get(i).getClothType() == 0) {
                    shirts.add(cloths.get(i));
                } else {
                    pants.add(cloths.get(i));
                }
                onPhotosReturned(cloths.get(i).getClothImageUrl(), cloths.get(i).getClothType(), true);
            }

            if (dbHelper.checkFav(shirts.get(viewPagerShirts.getCurrentItem()).getId(), pants.get(viewPagerPants.getCurrentItem()).getId()) == -1) {
                fabFav.setImageResource(R.drawable.ic_favorite_border_white_24dp);
            } else {
                fabFav.setImageResource(R.drawable.ic_favorite_white_24dp);
            }
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.fabAddShirt:
                EasyImage.openChooserWithGallery(MainActivity.this, "Pick source", 0);
                break;
            case R.id.fabAddPant:
                EasyImage.openChooserWithGallery(MainActivity.this, "Pick source", 1);
                break;
            case R.id.fabShuffle:
                if (shirts.size() > 0 && pants.size() > 0) {
                    Cloth shirt = shirts.get(new Random().nextInt(shirts.size()));
                    viewPagerShirts.setCurrentItem(shirts.indexOf(shirt), true);

                    Cloth pant = pants.get(new Random().nextInt(pants.size()));
                    viewPagerPants.setCurrentItem(pants.indexOf(pant), true);
                }
                break;
            case R.id.fabFav:
                if (shirts.size() > 0 && pants.size() > 0) {
                    int id = dbHelper.checkFav(shirts.get(viewPagerShirts.getCurrentItem()).getId(), pants.get(viewPagerPants.getCurrentItem()).getId());
                    if (id == -1) {
                        dbHelper.insertFav(shirts.get(viewPagerShirts.getCurrentItem()).getId(), pants.get(viewPagerPants.getCurrentItem()).getId());
                        fabFav.setImageResource(R.drawable.ic_favorite_white_24dp);
                    } else {
                        dbHelper.deleteFav(id);
                        fabFav.setImageResource(R.drawable.ic_favorite_border_white_24dp);
                    }
                }
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        EasyImage.handleActivityResult(requestCode, resultCode, data, this, new DefaultCallback() {
            @Override
            public void onImagePickerError(Exception e, EasyImage.ImageSource source, int type) {
                //Some error handling
                e.printStackTrace();
            }

            @Override
            public void onImagesPicked(@NonNull List<File> imageFiles, EasyImage.ImageSource source, int type) {
                if (imageFiles.size() > 0) {
                    for (int i = 0; i < imageFiles.size(); i++) {
                        long id = dbHelper.insertCloth(type, imageFiles.get(i).getPath());
                        Cloth cloth = dbHelper.getCloth(id);
                        if (cloth != null) {
                            if (cloth.getClothType() == 0) {
                                shirts.add(cloth);
                            } else {
                                pants.add(cloth);
                            }
                            onPhotosReturned(cloth.getClothImageUrl(), cloth.getClothType(), false);
                        }
                    }
                }
            }

            @Override
            public void onCanceled(EasyImage.ImageSource source, int type) {
                if (source == EasyImage.ImageSource.CAMERA) {
                    File photoFile = EasyImage.lastlyTakenButCanceledPhoto(MainActivity.this);
                    if (photoFile != null) photoFile.delete();
                }
            }
        });
    }

    private void onPhotosReturned(String path, int type, boolean fromDb) {

        LinearLayoutCompat linearLayoutParent = (LinearLayoutCompat) getLayoutInflater().inflate(R.layout.parent_layout, null);
        AppCompatImageView childView = (AppCompatImageView) getLayoutInflater().inflate(R.layout.child_layout, linearLayoutParent, false);
        Picasso.with(MainActivity.this)
                .load(new File(path))
                .fit()
                .centerInside()
                .into(childView);
        linearLayoutParent.addView(childView);
        addView(linearLayoutParent, type, fromDb);
    }

    public void addView(View newPage, int type, boolean fromDb) {
        if (type == 0) {
            int pageIndex = shirtPagerAdapter.addView(newPage);
            shirtPagerAdapter.notifyDataSetChanged();
            if (!fromDb) {
                viewPagerShirts.setCurrentItem(pageIndex, true);
            }
        } else {
            int pageIndex = pantPagerAdapter.addView(newPage);
            pantPagerAdapter.notifyDataSetChanged();
            if (!fromDb) {
                viewPagerPants.setCurrentItem(pageIndex, true);
            }
        }
    }

    @Override
    protected void onDestroy() {
        // Clear any configuration that was done!
        EasyImage.clearConfiguration(this);
        super.onDestroy();
    }
}
