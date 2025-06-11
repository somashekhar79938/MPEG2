package com.example.mpeg2converter;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;

public class FileUtils {
    public static String getPath(Context ctx, Uri uri) {
        String[] proj = { MediaStore.Video.Media.DATA };
        Cursor cursor = ctx.getContentResolver().query(uri, proj, null, null, null);
        if (cursor == null) return null;
        int idx = cursor.getColumnIndexOrThrow(MediaStore.Video.Media.DATA);
        cursor.moveToFirst();
        String path = cursor.getString(idx);
        cursor.close();
        return path;
    }
}
