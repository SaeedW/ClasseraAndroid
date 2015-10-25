package com.classera1.newtaskwithapi.util.imageutil;

import android.content.Context;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

/**
 * Created by Saeed on 9/21/2015.
 * Image Load class contain function to load the image by passing the activity ,  url , imageView object
 */
public class ImageLoad {

    private static String url;
    private static ImageView image;
    private static Context context;

    public ImageLoad(Context context, String url, ImageView image){
        this.context = context;
        this.url = url;
        this.image = image;
    }

    public static void loadImageByUrlAndPicasso(){
        Picasso.with(context)//context
                .load(url)//image url
//                .placeholder(R.drawable.loading)//change
//                .error(R.drawable.loading)//change
                .into(image);//image view object
    }
}
