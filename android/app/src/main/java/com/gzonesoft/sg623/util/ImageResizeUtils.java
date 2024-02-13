package com.gzonesoft.sg623.util;


import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.media.ExifInterface;
import android.util.Log;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class ImageResizeUtils {

    /**
     * 이미지의 너비를 변경한다.
     *
     * @param file
     * @param newFile
     * @param newWidth
     * @param isCamera
     */
    public static void resizeFile(File file, File newFile, int newWidth, Boolean isCamera) {

        String TAG = "blackjin";

        Bitmap originalBm = null;
        Bitmap resizedBitmap = null;

        try {

            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inPurgeable = true;
            options.inDither = true;

            originalBm = BitmapFactory.decodeFile(file.getAbsolutePath(), options);

            if (isCamera) {

                // 카메라인 경우 이미지를 상황에 맞게 회전시킨다
                try {

                    ExifInterface exif = new ExifInterface(file.getAbsolutePath());
                    int exifOrientation = exif.getAttributeInt(
                            ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);
                    int exifDegree = exifOrientationToDegrees(exifOrientation);
                    Log.d(TAG, "exifDegree : " + exifDegree);

                    originalBm = rotate(originalBm, exifDegree);

                } catch (IOException e) {
                    e.printStackTrace();
                }

            }

            if (originalBm == null) {
                Log.e(TAG, ("파일 에러"));
                return;
            }

            int width = originalBm.getWidth();
            int height = originalBm.getHeight();

            float aspect, scaleWidth, scaleHeight;
            if (width > height) {
                if (width <= newWidth) return;

                aspect = (float) width / height;

                scaleWidth = newWidth;
                scaleHeight = scaleWidth / aspect;

            } else {

                if (height <= newWidth) return;

                aspect = (float) height / width;

                scaleHeight = newWidth;
                scaleWidth = scaleHeight / aspect;

            }

            // create a matrix for the manipulation
            Matrix matrix = new Matrix();

            // resize the bitmap
            matrix.postScale(scaleWidth / width, scaleHeight / height);

            // recreate the new Bitmap
            resizedBitmap = Bitmap.createBitmap(originalBm, 0, 0, width, height, matrix, true);

            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {

                resizedBitmap.compress(Bitmap.CompressFormat.JPEG, 80, new FileOutputStream(newFile));

            } else {

                resizedBitmap.compress(Bitmap.CompressFormat.PNG, 80, new FileOutputStream(newFile));

            }


        } catch (FileNotFoundException e) {

            e.printStackTrace();

        } finally {

            if (originalBm != null) {
                originalBm.recycle();
            }

            if (resizedBitmap != null) {
                resizedBitmap.recycle();
            }
        }

    }

    public static Bitmap mergeTwoBitmapWithOverlapping(Bitmap up, Bitmap down) {
        Bitmap cs = null;
        int width, height = 0;

        final Bitmap second = Bitmap.createScaledBitmap(down,
                (int) (down.getWidth() / 1.5),
                (int) (down.getHeight() / 1.5), true);

        final Bitmap first = Bitmap.createScaledBitmap(up,
                up.getWidth() / 2, up.getHeight() / 2, true);

        width = up.getWidth();
        height = up.getHeight();
        cs = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);

        Canvas twiseImage = new Canvas(cs);

        twiseImage.drawBitmap(first, width - (int) (first.getWidth() * 2),
                height - (int) (first.getHeight()), null);
        twiseImage.drawBitmap(second, width - second.getWidth(), 0f, null);

        return cs;
    }

    private Bitmap overlay(Bitmap bmp1, Bitmap bmp2) {
        Bitmap bmOverlay = Bitmap.createBitmap(bmp1.getWidth(), bmp1.getHeight(), bmp1.getConfig());
        Canvas canvas = new Canvas(bmOverlay);
        canvas.drawBitmap(bmp1, new Matrix(), null);
        canvas.drawBitmap(bmp2, new Matrix(), null);
        return bmOverlay;
    }


    public static Bitmap mergeMultiple(Bitmap[] parts){
        Bitmap result = Bitmap.createBitmap(parts[0].getWidth(), parts[0].getHeight()+parts[1].getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(result);
        Paint paint = new Paint();
//        for (int i = 0; i < parts.length; i++) {
//            canvas.drawBitmap(parts[i], parts[i].getWidth() * (i % 2), parts[i].getHeight() * (i / 2), paint);
//        }
        int posY = parts[0].getHeight(); // - parts[1].getHeight(); // 하단에 위치하기 위함..
        canvas.drawBitmap(parts[0], 0, 0, paint);
        canvas.drawBitmap(parts[1], 0, posY, paint);
        return result;
    }
    /**
     * 기존이미지에 이미지를 추가한다.
     * @param file
     * @param addBitmap
     */
    public static void attachImage(File file, File newFile, int newWidth, Boolean isCamera, Bitmap addBitmap) {

        String TAG = "blackjin";

        Bitmap originalBm = null, originalBmEdit = null;
        Bitmap resizedBitmap = null;

        try {

            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inPurgeable = true;
            options.inDither = true;

            originalBmEdit = BitmapFactory.decodeFile(file.getAbsolutePath(), options);

//
//            Canvas canvas = new Canvas(originalBmEdit.copy(Bitmap.Config.ARGB_8888, true));
//            Paint paint = new Paint(Paint.FILTER_BITMAP_FLAG);
//            canvas.drawBitmap(addBitmap, 0, 0, paint);
//
//            originalBm = originalBmEdit;

            //originalBm = mergeTwoBitmapWithOverlapping(addBitmap, originalBm.copy(Bitmap.Config.ARGB_8888, true));

            if (isCamera) {

                // 카메라인 경우 이미지를 상황에 맞게 회전시킨다
                try {

                    ExifInterface exif = new ExifInterface(file.getAbsolutePath());
                    int exifOrientation = exif.getAttributeInt(
                            ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);
                    int exifDegree = exifOrientationToDegrees(exifOrientation);
                    Log.d(TAG, "exifDegree : " + exifDegree);

                    originalBmEdit = rotate(originalBmEdit, exifDegree);

                } catch (IOException e) {
                    e.printStackTrace();
                }

            }

            // 회전보정이후 추가한다.
            Bitmap[] listBitmap = new Bitmap[2];
            listBitmap[0] = originalBmEdit;
            listBitmap[1] = addBitmap;
            originalBm = mergeMultiple(listBitmap);

            if (originalBm == null) {
                Log.e(TAG, ("파일 에러"));
                return;
            }

            int width = originalBm.getWidth();
            int height = originalBm.getHeight();

            float aspect, scaleWidth, scaleHeight;
            if (width > height) {
                if (width <= newWidth) return;

                aspect = (float) width / height;

                scaleWidth = newWidth;
                scaleHeight = scaleWidth / aspect;

            } else {

                if (height <= newWidth) return;

                aspect = (float) height / width;

                scaleHeight = newWidth;
                scaleWidth = scaleHeight / aspect;

            }

            // create a matrix for the manipulation
            Matrix matrix = new Matrix();

            // resize the bitmap
            matrix.postScale(scaleWidth / width, scaleHeight / height);

            // recreate the new Bitmap
            resizedBitmap = Bitmap.createBitmap(originalBm, 0, 0, width, height, matrix, true);

            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {

                resizedBitmap.compress(Bitmap.CompressFormat.JPEG, 80, new FileOutputStream(newFile));

            } else {

                resizedBitmap.compress(Bitmap.CompressFormat.PNG, 80, new FileOutputStream(newFile));

            }


        } catch (FileNotFoundException e) {

            e.printStackTrace();

        } finally {

            if (originalBm != null) {
                originalBm.recycle();
            }

            if (resizedBitmap != null) {
                resizedBitmap.recycle();
            }
        }

    }

    /**
     * EXIF 정보를 회전각도로 변환하는 메서드
     *
     * @param exifOrientation EXIF 회전각
     * @return 실제 각도
     */
    public static int exifOrientationToDegrees(int exifOrientation) {
        if (exifOrientation == ExifInterface.ORIENTATION_ROTATE_90) {
            return 90;
        } else if (exifOrientation == ExifInterface.ORIENTATION_ROTATE_180) {
            return 180;
        } else if (exifOrientation == ExifInterface.ORIENTATION_ROTATE_270) {
            return 270;
        }
        return 0;
    }

    /**
     * 이미지를 회전시킵니다.
     *
     * @param bitmap  비트맵 이미지
     * @param degrees 회전 각도
     * @return 회전된 이미지
     */
    public static Bitmap rotate(Bitmap bitmap, int degrees) {
        if (degrees != 0 && bitmap != null) {
            Matrix m = new Matrix();
            m.setRotate(degrees, (float) bitmap.getWidth() / 2,
                    (float) bitmap.getHeight() / 2);

            try {
                Bitmap converted = Bitmap.createBitmap(bitmap, 0, 0,
                        bitmap.getWidth(), bitmap.getHeight(), m, true);
                if (bitmap != converted) {
                    bitmap.recycle();
                    bitmap = converted;
                }
            } catch (OutOfMemoryError ex) {
                // 메모리가 부족하여 회전을 시키지 못할 경우 그냥 원본을 반환합니다.
            }
        }
        return bitmap;
    }
}
