package com.imastudio.permissonapp;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

public class MainActivity extends AppCompatActivity {

    private static final int REQUEST_CAMERA = 1;
    private static final int REQUEST_GALLERY = 2;
    //1
    ImageView gambar;
    Button btnPilih;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //2
        gambar = findViewById(R.id.imageView);
        btnPilih = findViewById(R.id.button);

        //3
        btnPilih.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //cek permission
                boolean permissionresult = Utility.cekPermission(MainActivity.this);
                if (permissionresult){
                    showAlertDialog();
                }
            }
        });

    }

    private void showAlertDialog() {
        AlertDialog.Builder popup = new AlertDialog.Builder(this);
        popup.setTitle("Pilih Foto");
        String[] pilihan = {"Camera", "Gallery"};
        popup.setItems(pilihan, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which){
                    case 0:
                        bukaCamera();
                        break;
                    case 1:
                        bukaGallery();
                        break;
                }
            }
        });
        popup.show();
    }

    private void bukaGallery() {
        Intent gallery = new Intent();
        gallery.setType("image/*");
        gallery.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(gallery, "Pilih File"), REQUEST_GALLERY);
    }

    private void bukaCamera() {
        Intent camera = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(camera, REQUEST_CAMERA);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode){
            case REQUEST_CAMERA:
                Bitmap thumbnail = (Bitmap) data.getExtras().get("data");
                gambar.setImageBitmap(thumbnail);
                break;
            case REQUEST_GALLERY:
                Bitmap gal = (Bitmap) data.getExtras().get("data");
                gambar.setImageBitmap(gal);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode){
            case Utility.REQUEST_CODE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    showAlertDialog();
                }
        }
    }
}
