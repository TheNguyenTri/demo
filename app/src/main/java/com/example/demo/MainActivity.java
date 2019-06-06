package com.example.demo;

import android.app.ProgressDialog;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.demo.callback.ResponseCallbackListener;
import com.example.demo.manager.DownloadVideoManager;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import okhttp3.ResponseBody;

public class MainActivity extends AppCompatActivity {
private Button button;
    private ProgressDialog pDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        pDialog = new ProgressDialog(this);
        button = findViewById(R.id.btnDownLoad);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                downloadVideo();
            }
        });
    }

    private void showProcessDialog() {
        pDialog.setMessage("Please wait...");
        pDialog.setCancelable(false);
        pDialog.show();
    }

    private void disProcessDialog() {
        pDialog.isShowing();
        pDialog.dismiss();
    }

    private void downloadVideo(){

        showProcessDialog();
        DownloadVideoManager downloadVideoManager = new DownloadVideoManager(new ResponseCallbackListener<ResponseBody>() {
            @Override
            public void onObjectComplete(String TAG, ResponseBody data) {
                disProcessDialog();
                Toast.makeText(MainActivity.this,"Done",Toast.LENGTH_SHORT).show();
                boolean writtenToDisk = writeResponseBodyToDisk(data);
                Toast.makeText(MainActivity.this,"Ok không ? " + writtenToDisk,Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onResponseFailed(String TAG, String message) {

            }
        });
        downloadVideoManager.startGetData();
    }

    private boolean writeResponseBodyToDisk(ResponseBody body) {
        try {
            // todo change the file location/name according to your needs
            File futureStudioIconFile = new File(getExternalFilesDir(null) + File.separator + "file_master.zip");

            InputStream inputStream = null;
            OutputStream outputStream = null;

            try {
                byte[] fileReader = new byte[4096];

                long fileSize = body.contentLength();
                long fileSizeDownloaded = 0;

                inputStream = body.byteStream();
                outputStream = new FileOutputStream(futureStudioIconFile);

                while (true) {
                    int read = inputStream.read(fileReader);

                    if (read == -1) {
                        break;
                    }

                    outputStream.write(fileReader, 0, read);

                    fileSizeDownloaded += read;
                }

                outputStream.flush();

                return true;
            } catch (IOException e) {
                return false;
            } finally {
                if (inputStream != null) {
                    inputStream.close();
                }

                if (outputStream != null) {
                    outputStream.close();
                }
            }
        } catch (IOException e) {
            return false;
        }
    }

}
