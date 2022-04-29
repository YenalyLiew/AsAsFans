package com.fairhr.module_support.utils;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.bitmap.CircleCrop;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;

/**
 * Description: glide 工具
 */
public class GlideUtils {

    /**
     * 加载图片到imageview
     *
     * @param context
     * @param url
     * @param imageView
     */
    public static void loadToImageView(Context context, String url, ImageView imageView) {
        Glide.with(context)
                .load(url)
                .centerCrop()
                .into(imageView);
    }

    /**
     * 加载图片到imageview
     *
     * @param fragment
     * @param url
     * @param imageView
     */
//    public static void loadToImageView(Fragment fragment, String url, ImageView imageView) {
//        Glide.with(fragment)
//                .load(url)
//                .into(imageView);
//    }

    /**
     * 加载图片到imageview
     *
     * @param activity
     * @param url
     * @param imageView
     */
    public static void loadToImageView(Activity activity, String url, ImageView imageView) {
        Glide.with(activity)
                .load(url)
                .skipMemoryCache(false)
                .into(imageView);
    }


    /**
     * 加载图片到imageview
     *
     * @param activity
     * @param url
     * @param imageView
     */
    public static void loadToImageView(Activity activity, int url, ImageView imageView) {
        Glide.with(activity)
                .load(url)
                .into(imageView);
    }

    /**
     * 加载图片到imageview
     *
     * @param context
     * @param url
     * @param imageView
     */
    public static void loadUrlToImageView(Context context, String url, ImageView imageView) {
        Glide.with(context)
                .load(url)
                .into(new SimpleTarget<Drawable>() {
                    @Override
                    public void onResourceReady(@NonNull Drawable resource, @Nullable Transition<? super Drawable> transition) {
                        imageView.setImageDrawable(resource);
                    }
                });
    }

    /**
     * 加载图片到imageview
     *
     * @param context
     * @param repaceHolderResId
     * @param errorResId
     * @param imageView
     */
    public static void loadUrlToImageView(Context context, String url, int repaceHolderResId, int errorResId, ImageView imageView) {
        RequestOptions requestOptions = new RequestOptions()
                .placeholder(repaceHolderResId)
                .error(errorResId)
                .diskCacheStrategy(DiskCacheStrategy.RESOURCE);
        Glide.with(context)
                .load(url)
                .apply(requestOptions)
                .into(new SimpleTarget<Drawable>() {
                    @Override
                    public void onResourceReady(@NonNull Drawable resource, @Nullable Transition<? super Drawable> transition) {
                        imageView.setImageDrawable(resource);
                    }
                });
    }

    /**
     * 加载图片到imageview
     *
     * @param context
     * @param repaceHolderResId
     * @param errorResId
     * @param imageView
     */
    public static void loadToImageView(Context context, String url, int repaceHolderResId, int errorResId, ImageView imageView) {
        RequestOptions requestOptions = new RequestOptions()
                .placeholder(repaceHolderResId)
                .error(errorResId)
                .diskCacheStrategy(DiskCacheStrategy.RESOURCE);
        Glide.with(context)
                .load(url)
                .apply(requestOptions)
                .into(imageView);
    }

    /**
     * 加载圆形图片到imageview
     *
     * @param context
     * @param repaceHolderResId
     * @param errorResId
     * @param imageView
     */
    public static void loadToImageViewByCircle(Context context, String url, int repaceHolderResId, int errorResId, ImageView imageView) {
//        RequestOptions requestOptions = new RequestOptions()
//                .placeholder(repaceHolderResId)
//                .error(errorResId)
//                .diskCacheStrategy(DiskCacheStrategy.RESOURCE);
//        Glide.with(context)
//                .load(url)
//                .apply(requestOptions)
//                .into(imageView);
        Glide.with(context)
                .load(url)
                .placeholder(repaceHolderResId)
                .error(errorResId)
                .diskCacheStrategy(DiskCacheStrategy.RESOURCE)
                .apply(RequestOptions.bitmapTransform(new CircleCrop()))
                .into(imageView);
    }

    /**
     * 加载圆形图片到imageview
     *
     * @param context
     * @param imageView
     */
    public static void loadToImageViewByCircle(Context context, String url, ImageView imageView) {
//        RequestOptions requestOptions = new RequestOptions()
//                .placeholder(repaceHolderResId)
//                .error(errorResId)
//                .diskCacheStrategy(DiskCacheStrategy.RESOURCE);
//        Glide.with(context)
//                .load(url)
//                .apply(requestOptions)
//                .into(imageView);
        Glide.with(context)
                .load(url)
                .diskCacheStrategy(DiskCacheStrategy.RESOURCE)
                .apply(RequestOptions.bitmapTransform(new CircleCrop()))
                .into(imageView);
    }


    /**
     * 加载图片到imageview
     *
     * @param fragment
     * @param repaceHolderResId
     * @param errorResId
     * @param imageView
     */
//    public static void loadToImageView(Fragment fragment, String url, int repaceHolderResId, int errorResId, ImageView imageView) {
//        RequestOptions requestOptions = new RequestOptions()
//                .placeholder(repaceHolderResId)
//                .error(errorResId)
//                .diskCacheStrategy(DiskCacheStrategy.RESOURCE);
//        Glide.with(fragment)
//                .load(url)
//                .apply(requestOptions)
//                .into(imageView);
//    }


    /**
     * 加载图片到imageview
     *
     * @param activity
     * @param repaceHolderResId
     * @param errorResId
     * @param imageView
     */
    public static void loadToImageView(Activity activity, String url, int repaceHolderResId, int errorResId, ImageView imageView) {
        RequestOptions requestOptions = new RequestOptions()
                .placeholder(repaceHolderResId)
                .error(errorResId)
                .diskCacheStrategy(DiskCacheStrategy.RESOURCE);
        Glide.with(activity)
                .load(url)
                .apply(requestOptions)
                .into(imageView);
    }


    /**
     * 加载图片到view 的背景
     *
     * @param context
     * @param url
     * @param view
     */
    public static void loadToViewBg(Context context, String url, View view) {
        Glide.with(context)
                .asDrawable()
                .load(url)
                .into(new CustomTarget<Drawable>() {
                    @Override
                    public void onResourceReady(@NonNull Drawable resource, @Nullable Transition<? super Drawable> transition) {
                        view.setBackground(resource);
                    }

                    @Override
                    public void onLoadCleared(@Nullable Drawable placeholder) {

                    }
                });
    }

    /**
     * 加载图片到view 的背景中
     *
     * @param fragment
     * @param url
     * @param view
     */

//    public static void loadToViewBg(Fragment fragment, String url, View view) {
//        Glide.with(fragment)
//                .asDrawable()
//                .load(url)
//                .into(new CustomTarget<Drawable>() {
//                    @Override
//                    public void onResourceReady(@NonNull Drawable resource, @Nullable Transition<? super Drawable> transition) {
//                        view.setBackground(resource);
//                    }
//
//                    @Override
//                    public void onLoadCleared(@Nullable Drawable placeholder) {
//
//                    }
//                });
//    }

    /**
     * 加载图片到view 的背景中
     *
     * @param activity
     * @param url
     * @param view
     */
    public static void loadToViewBg(Activity activity, String url, View view) {
        Glide.with(activity)
                .asDrawable()
                .load(url)
                .into(new CustomTarget<Drawable>() {
                    @Override
                    public void onResourceReady(@NonNull Drawable resource, @Nullable Transition<? super Drawable> transition) {
                        view.setBackground(resource);
                    }

                    @Override
                    public void onLoadCleared(@Nullable Drawable placeholder) {

                    }
                });
    }


    /**
     * 加载图片到view 的背景中
     *
     * @param context
     * @param repaceHolderResId
     * @param errorResId
     * @param view
     */
    public static void loadToViewBg(Context context, String url, int repaceHolderResId, int errorResId, View view) {
        RequestOptions requestOptions = new RequestOptions()
                .placeholder(repaceHolderResId)
                .error(errorResId)
                .diskCacheStrategy(DiskCacheStrategy.RESOURCE);
        Glide.with(context)
                .asDrawable()
                .load(url)
                .apply(requestOptions)
                .into(new CustomTarget<Drawable>() {
                    @Override
                    public void onResourceReady(@NonNull Drawable resource, @Nullable Transition<? super Drawable> transition) {
                        view.setBackground(resource);
                    }

                    @Override
                    public void onLoadCleared(@Nullable Drawable placeholder) {

                    }
                });
    }


    /**
     * 加载图片到view 的背景中
     *
     * @param fragment
     * @param repaceHolderResId
     * @param errorResId
     * @param view
     */
//    public static void loadToViewBg(Fragment fragment, String url, int repaceHolderResId, int errorResId, View view) {
//        RequestOptions requestOptions = new RequestOptions()
//                .placeholder(repaceHolderResId)
//                .error(errorResId)
//                .diskCacheStrategy(DiskCacheStrategy.RESOURCE);
//        Glide.with(fragment)
//                .asDrawable()
//                .load(url)
//                .apply(requestOptions)
//                .into(new CustomTarget<Drawable>() {
//                    @Override
//                    public void onResourceReady(@NonNull Drawable resource, @Nullable Transition<? super Drawable> transition) {
//                        view.setBackground(resource);
//                    }
//
//                    @Override
//                    public void onLoadCleared(@Nullable Drawable placeholder) {
//
//                    }
//                });
//    }


    /**
     * 加载图片到view 的背景中
     *
     * @param activity
     * @param repaceHolderResId
     * @param errorResId
     * @param view
     */
    public static void loadToViewBg(Activity activity, String url, int repaceHolderResId, int errorResId, View view) {
        RequestOptions requestOptions = new RequestOptions()
                .placeholder(repaceHolderResId)
                .error(errorResId)
                .diskCacheStrategy(DiskCacheStrategy.RESOURCE);
        Glide.with(activity)
                .asDrawable()
                .load(url)
                .apply(requestOptions)
                .into(new CustomTarget<Drawable>() {
                    @Override
                    public void onResourceReady(@NonNull Drawable resource, @Nullable Transition<? super Drawable> transition) {
                        view.setBackground(resource);
                    }

                    @Override
                    public void onLoadCleared(@Nullable Drawable placeholder) {

                    }
                });
    }


}
