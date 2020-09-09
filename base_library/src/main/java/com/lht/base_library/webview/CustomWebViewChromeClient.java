package com.lht.base_library.webview;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.database.Cursor;
import android.net.Uri;
import android.os.Environment;
import android.os.Message;
import android.provider.MediaStore;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.widget.FrameLayout;

import androidx.annotation.Nullable;

import java.io.File;
import java.lang.ref.SoftReference;

public class CustomWebViewChromeClient extends WebChromeClient {

    public static final int FILE_CHOOSER_RESULT_CODE = 5173;
    public static final int FILE_CHOOSER_RESULTS_CODE = 5174;

    private final SoftReference<CustomWebFragment> fragment;

    private WebChromeClient.CustomViewCallback customViewCallback;
    private CustomWebVideoView videoView;
    private View viewHolder;

    public CustomWebViewChromeClient(CustomWebFragment fragment) {
        this.fragment = new SoftReference<>(fragment);
    }

    private ValueCallback<Uri> uploadMsg;
    private ValueCallback<Uri[]> uploadMsgs;
    private static String mCameraFilePath = "";

    @Override
    public void onProgressChanged(WebView view, int newProgress) {
        progress(newProgress);
        super.onProgressChanged(view, newProgress);
    }

    //Android > 4.1.1
    @Override
    public boolean onShowFileChooser(WebView webView, ValueCallback<Uri[]> filePathCallback, FileChooserParams fileChooserParams) {
        uploadMsgs = filePathCallback;
        if (fragment.get() != null && fragment.get().getActivity() != null) {
            fragment.get().getActivity().startActivityForResult(createDefaultOpenableIntent(), FILE_CHOOSER_RESULTS_CODE);
        }
        return true;
    }

    public void openFileChooser(ValueCallback<Uri> uploadMsg, String acceptType, String capture) {
        this.uploadMsg = uploadMsg;
        if (fragment.get() != null && fragment.get().getActivity() != null) {
            fragment.get().getActivity().startActivityForResult(createDefaultOpenableIntent(), FILE_CHOOSER_RESULT_CODE);
        }
    }

    public void openFileChooser(ValueCallback<Uri> uploadMsg, String acceptType) {
        this.uploadMsg = uploadMsg;
        if (fragment.get() != null && fragment.get().getActivity() != null) {
            fragment.get().getActivity().startActivityForResult(createDefaultOpenableIntent(), FILE_CHOOSER_RESULT_CODE);
        }
    }

    // Android < 3.0
    public void openFileChooser(ValueCallback<Uri> uploadMsg) {
        this.uploadMsg = uploadMsg;
        if (fragment.get() != null && fragment.get().getActivity() != null) {
            fragment.get().getActivity().startActivityForResult(createDefaultOpenableIntent(), FILE_CHOOSER_RESULT_CODE);
        }
    }

    private Intent createDefaultOpenableIntent() {
        Intent i = new Intent(Intent.ACTION_GET_CONTENT);
        i.addCategory(Intent.CATEGORY_OPENABLE);
        i.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
        Intent chooser = createChooserIntent(createCameraIntent());
//            Intent chooser = createChooserIntent(createCameraIntent(),createCamcorderIntent(),createSoundRecorderIntent());
        chooser.putExtra(Intent.EXTRA_INTENT, i);
        return chooser;
    }

    private Intent createChooserIntent(Intent... intents) {
        Intent chooser = new Intent(Intent.ACTION_CHOOSER);
        chooser.putExtra(Intent.EXTRA_INITIAL_INTENTS, intents);
        chooser.putExtra(Intent.EXTRA_TITLE, "选择图片");
        return chooser;
    }

    private Intent createCameraIntent() {
        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        File externalDataDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM);
        File cameraDataDir = new File(externalDataDir.getAbsolutePath() + File.separator + "515aaa");
        cameraDataDir.mkdirs();
        String mCameraFilePath = cameraDataDir.getAbsolutePath() + File.separator + System.currentTimeMillis() + ".jpg";
        this.mCameraFilePath = mCameraFilePath;
        cameraIntent.putExtra(MediaStore.Images.Media.ORIENTATION, 0);
        cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(new File(mCameraFilePath)));
        return cameraIntent;
    }

    public static Uri getImageContentUri(Context context, File imageFile) {
        String filePath = imageFile.getAbsolutePath();
        Cursor cursor = context.getContentResolver().query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, new String[]{MediaStore.Images.Media._ID}, MediaStore.Images.Media.DATA + "=? ", new String[]{filePath}, null);
        if (cursor != null && cursor.moveToFirst()) {
            int id = cursor.getInt(cursor.getColumnIndex(MediaStore.MediaColumns._ID));
            Uri baseUri = Uri.parse("content://media/external/images/media");
            return Uri.withAppendedPath(baseUri, "" + id);
        } else {
            if (imageFile.exists()) {
                ContentValues values = new ContentValues();
                values.put(MediaStore.Images.Media.DATA, filePath);
                return context.getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
            } else {
                return null;
            }
        }
    }

    public ValueCallback<Uri> getUploadMsg() {
        return uploadMsg;
    }

    public ValueCallback<Uri[]> getUploadMsgs() {
        return uploadMsgs;
    }

    public String getImageUrl() {
        return mCameraFilePath;
    }

//        private Intent createCamcorderIntent() {
//            return new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
//        }
//
//        private Intent createSoundRecorderIntent() {
//            return new Intent(MediaStore.Audio.Media.RECORD_SOUND_ACTION);
//        }

    @Nullable
    @Override
    public View getVideoLoadingProgressView() {
        if (fragment.get() != null && fragment.get().getActivity() != null) {
            FrameLayout frameLayout = new FrameLayout(fragment.get().getActivity());
            frameLayout.setLayoutParams(new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT));
            return frameLayout;
        } else {
            return super.getVideoLoadingProgressView();
        }
    }

    @Override
    public void onShowCustomView(View view, CustomViewCallback callback) {
        if (fragment.get() != null && fragment.get().getActivity() != null) {
            if (viewHolder != null) {
                callback.onCustomViewHidden();
                return;
            }
            FrameLayout decore = (FrameLayout) fragment.get().getActivity().getWindow().getDecorView();
            videoView = new CustomWebVideoView(fragment.get().getActivity());
            videoView.addView(view, new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
            decore.addView(videoView, new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
            fragment.get().getActivity().getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
            fragment.get().getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
            viewHolder = view;
            customViewCallback = callback;
            fragment.get().setVideoFull(true);
        } else {
            super.onShowCustomView(view, callback);
        }
    }

    @Override
    public void onHideCustomView() {
        if (fragment.get() != null && fragment.get().getActivity() != null) {
            if (videoView == null) {
                return;
            }
            fragment.get().getActivity().getWindow().setFlags(0, WindowManager.LayoutParams.FLAG_FULLSCREEN);
            fragment.get().getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
            FrameLayout decor = (FrameLayout) fragment.get().getActivity().getWindow().getDecorView();
            if (videoView != null) {
                decor.removeView(videoView);
            }
            videoView = null;
            viewHolder = null;
            customViewCallback.onCustomViewHidden();
            if (fragment.get().getWebView() != null) {
                fragment.get().getWebView().setVisibility(View.VISIBLE);
            }
            fragment.get().setVideoFull(false);
        } else {
            super.onHideCustomView();
        }
    }

    private void progress(int progress) {
        if (fragment.get() != null && fragment.get().getWebView() != null && fragment.get().getWebView().getHandler() != null) {
            Message message = Message.obtain(fragment.get().getWebView().getHandler(), new Runnable() {
                @Override
                public void run() {
                    fragment.get().updateProgress(progress);
                }
            });
            fragment.get().getWebView().getHandler().sendMessage(message);
        }
    }

}
