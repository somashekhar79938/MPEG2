package com.example.mpeg2converter;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.arthenica.mobileffmpeg.FFmpeg;

public class MainActivity extends AppCompatActivity {
    private static final int REQUEST_VIDEO = 100;
    private Uri videoUri;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTheme(R.style.Theme_MPEG2Converter);
        setContentView(R.layout.activity_main);

        progressBar = findViewById(R.id.progressBar);
        findViewById(R.id.btnPick).setOnClickListener(v -> pickVideo());
        findViewById(R.id.btnConvert).setOnClickListener(v -> {
            if (videoUri != null) convert(videoUri);
            else Toast.makeText(this, "Select a video first", Toast.LENGTH_SHORT).show();
        });
    }

    private void pickVideo() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("video/*");
        startActivityForResult(intent, REQUEST_VIDEO);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_VIDEO && resultCode == RESULT_OK) {
            videoUri = data.getData();
        }
    }

    private void convert(Uri uri) {
        String input = FileUtils.getPath(this, uri);
        String output = getExternalFilesDir(null) + "/converted.mpg";
        String cmd = String.format("-i %s -c:v mpeg2video -qscale:v 2 -c:a mp2 %s", input, output);

        progressBar.setVisibility(View.VISIBLE);
        new Thread(() -> {
            int rc = FFmpeg.execute(cmd);
            runOnUiThread(() -> {
                progressBar.setVisibility(View.GONE);
                Toast.makeText(this,
                    rc == 0 ? "Conversion done:\n" + output : "Conversion failed (rc=" + rc + ")",
                    Toast.LENGTH_LONG).show();
            });
        }).start();
    }
}
