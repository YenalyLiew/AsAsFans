package com.fairhr.module_support.base;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;

import androidx.annotation.Nullable;
import androidx.databinding.ViewDataBinding;


import com.fairhr.module_support.rxpermissions.RxPermissions;
import com.fairhr.module_support.utils.AndroidFileUtils;
import com.fairhr.module_support.utils.BitmapUtils;
import com.fairhr.module_support.utils.FileUtils;
import com.fairhr.module_support.utils.LogUtil;
import com.fairhr.module_support.utils.SystemAppUtil;
import com.fairhr.module_support.utils.SystemUtil;

import java.io.File;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.Observer;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.functions.Consumer;
import io.reactivex.rxjava3.schedulers.Schedulers;


public abstract class BaseSelectPictureActivity<DB extends ViewDataBinding, VM extends BaseViewModel> extends MvvmActivity<DB,VM> {
    private static final long MAX_SIZE = 1024 * 1024;
    private static final int MY_CAMERA_REQUEST = 0x12;
    private static final int MY_PHOTO_ALUM_REQUEST = 0x13;
    private static final int MY_CROP_PICTURE_REQUEST = 0x14;
    private RxPermissions mRxPermissions;
    private Uri mCameraUri;
    private Uri mCropUri;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mRxPermissions = new RxPermissions(this);
    }

    /**
     * 选择的图片地址和来源
     *
     * @param filePath
     * @param pictureFromType
     */
    public abstract void selectPictureFilePath(String filePath, String pictureFromType);

    /**
     * 取消和取消来源
     *
     * @param pictureFromType
     */
    public abstract void cancleSelectPicture(String pictureFromType);


    @SuppressLint("CheckResult")
    public void selectPictureFromCamera() {
        mRxPermissions.request(Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE)
                .subscribe(new Consumer<Boolean>() {
                    @Override
                    public void accept(Boolean granted) throws Exception {
                        if (granted) {
                            String fileName = FileUtils.creatImageFileName("png");
                            String sdCardPublicDir = AndroidFileUtils.getSDCardPublicDir(Environment.DIRECTORY_PICTURES);
                            mCameraUri = SystemAppUtil.openCamera(BaseSelectPictureActivity.this,
                                    sdCardPublicDir,
                                    fileName, MY_CAMERA_REQUEST);
                        }
                    }
                });
    }

    /**
     * 从相册选择图片
     */
    @SuppressLint("CheckResult")
    public void selectPictureFromAlum() {
        mRxPermissions.request(Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE)
                .subscribe(new Consumer<Boolean>() {
                    @Override
                    public void accept(Boolean granted) throws Exception {
                        if (granted) {
                            SystemAppUtil.openPhotoAlum(BaseSelectPictureActivity.this, MY_PHOTO_ALUM_REQUEST);
                        }
                    }
                });
    }


    /**
     * 从相册选择图片
     */
    @SuppressLint("CheckResult")
    public void cropPicture(final String filePath) {
        mRxPermissions.request(Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE)
                .subscribe(new Consumer<Boolean>() {
                    @Override
                    public void accept(Boolean granted) throws Exception {
                        if (granted) {
                            int canCropSize = BitmapUtils.pictureCanCropSize(getApplicationContext(), filePath);
                            File tarFile = new File(AndroidFileUtils.getSDAppFileDir(getApplicationContext(), Environment.DIRECTORY_DCIM), FileUtils.creatImageFileName(FileUtils.getFileFormat(filePath)));
                            mCropUri = SystemAppUtil.cropPhoto(BaseSelectPictureActivity.this, canCropSize, new File(filePath), tarFile, MY_CROP_PICTURE_REQUEST);
                        }
                    }
                });
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //获取来自相册的信息
        if (requestCode == MY_PHOTO_ALUM_REQUEST && resultCode == Activity.RESULT_OK && data != null) {
            Uri fileUri = data.getData();
            if (fileUri == null)
                return;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                FileUtils.sdkQUriToFilePath(getApplicationContext(), fileUri, Environment.DIRECTORY_PICTURES, MAX_SIZE).subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new Observer<String>() {
                            @Override
                            public void onSubscribe(Disposable d) {

                            }

                            @Override
                            public void onNext(String s) {
                                selectPictureFilePath(s, "Alum");
                            }

                            @Override
                            public void onError(Throwable e) {
                                LogUtil.d(e.toString());
                            }

                            @Override
                            public void onComplete() {

                            }
                        });
            } else {
                File file = FileUtils.getFileByUri(BaseSelectPictureActivity.this, fileUri);
                if (file != null && file.exists() && file.length() > 0) {
                    if (file.length() > MAX_SIZE) {
                        File tarFile = new File(AndroidFileUtils.getSDAppFileDir(getApplicationContext(), Environment.DIRECTORY_DCIM), FileUtils.getFileName(file.getAbsolutePath()));
                        BitmapUtils.compressToAssignSize(getApplicationContext(), file.getAbsolutePath(), tarFile.getAbsolutePath(),
                                MAX_SIZE, new Observer<String>() {
                                    @Override
                                    public void onSubscribe(Disposable d) {

                                    }

                                    @Override
                                    public void onNext(String s) {
                                        selectPictureFilePath(s, "Alum");
                                    }

                                    @Override
                                    public void onError(Throwable e) {

                                    }

                                    @Override
                                    public void onComplete() {

                                    }
                                });
                    } else {
                        selectPictureFilePath(file.getAbsolutePath(), "Alum");
                    }
                }
            }
            return;
        }
        //信息来自相机
        if (requestCode == MY_CAMERA_REQUEST && resultCode == Activity.RESULT_OK) {
            if (mCameraUri == null)
                return;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                FileUtils.sdkQUriToFilePath(getApplicationContext(), mCameraUri, Environment.DIRECTORY_PICTURES, MAX_SIZE).subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new Observer<String>() {
                            @Override
                            public void onSubscribe(Disposable d) {

                            }

                            @Override
                            public void onNext(String s) {
                                selectPictureFilePath(s, "Camera");
                            }

                            @Override
                            public void onError(Throwable e) {
                                LogUtil.d(e.toString());
                            }

                            @Override
                            public void onComplete() {

                            }
                        });
            } else {
                File file = FileUtils.getFileByUri(BaseSelectPictureActivity.this, mCameraUri);
                if (file.exists() && file.length() > 0) {
                    if (file.length() > MAX_SIZE) {
                        File tarFile = new File(AndroidFileUtils.getSDAppFileDir(getApplicationContext(), Environment.DIRECTORY_DCIM), FileUtils.getFileName(file.getAbsolutePath()));
                        BitmapUtils.compressToAssignSize(getApplicationContext(), file.getAbsolutePath(), tarFile.getAbsolutePath(),
                                MAX_SIZE, new Observer<String>() {
                                    @Override
                                    public void onSubscribe(Disposable d) {

                                    }

                                    @Override
                                    public void onNext(String s) {
                                        selectPictureFilePath(s, "Camera");
                                    }

                                    @Override
                                    public void onError(Throwable e) {

                                    }

                                    @Override
                                    public void onComplete() {

                                    }
                                });
                    } else {
                        selectPictureFilePath(file.getAbsolutePath(), "Camera");
                        SystemUtil.scanFile(getApplicationContext(), file.getAbsolutePath());
                    }
                }
            }
            return;
        }

        //信息来自剪切图片
        if (requestCode == MY_CROP_PICTURE_REQUEST && resultCode == Activity.RESULT_OK) {
            Uri fileUri = data.getData();
            if (fileUri == null) {
                fileUri = mCropUri;
            }
            if (fileUri == null)
                return;
            File file = FileUtils.getFileByUri(BaseSelectPictureActivity.this, fileUri);
            if (file != null && file.exists() && file.length() > 0) {
                if (file.length() > MAX_SIZE) {
                    BitmapUtils.compressToAssignSize(getApplicationContext(), file.getAbsolutePath(), MAX_SIZE, new Observer<String>() {
                        @Override
                        public void onSubscribe(Disposable d) {

                        }

                        @Override
                        public void onNext(String s) {
                            selectPictureFilePath(s, "CropImage");
                        }

                        @Override
                        public void onError(Throwable e) {

                        }

                        @Override
                        public void onComplete() {

                        }
                    });
                } else {
                    selectPictureFilePath(file.getAbsolutePath(), "CropImage");
                }
            }
            return;
        }


        if (requestCode == MY_PHOTO_ALUM_REQUEST && Activity.RESULT_CANCELED == resultCode) {
            cancleSelectPicture("Alum");
            return;
        }
        if (requestCode == MY_CAMERA_REQUEST && Activity.RESULT_CANCELED == resultCode) {
            cancleSelectPicture("Camera");
            return;
        }
        if (requestCode == MY_CROP_PICTURE_REQUEST && Activity.RESULT_CANCELED == resultCode) {
            cancleSelectPicture("CropImage");
        }
    }
}
