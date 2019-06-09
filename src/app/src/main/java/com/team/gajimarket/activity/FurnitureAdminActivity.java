package com.team.gajimarket.activity;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.Image;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.gun0912.tedpermission.PermissionListener;
import com.gun0912.tedpermission.TedPermission;
import com.team.gajimarket.R;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class FurnitureAdminActivity extends AppCompatActivity implements View.OnClickListener {

    private static final int REQUEST_IMAGE_CAPTURE = 1;
    private static final int PICK_FROM_ALBUM = 2;
    String currentPhotoPath;

    private Uri photoURI;
    private File photoFile;
    ImageView imgCamera, imgGallery, imgConfirm;
    EditText edtName, edtPrice, edtWidth, edtDepth, edtHeight;
    ImageView photozone;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_furniture_admin);

        tedPermission();

        imgCamera = (ImageView)findViewById(R.id.imgCamera);
        imgGallery = (ImageView)findViewById(R.id.imgGallery);
        imgConfirm = (ImageView)findViewById(R.id.imgConfirm);
        photozone = (ImageView)findViewById(R.id.photozone);

        edtName = (EditText)findViewById(R.id.edtName);
        edtPrice = (EditText)findViewById(R.id.edtPrice);
        edtWidth = (EditText)findViewById(R.id.edtWidth);
        edtDepth = (EditText)findViewById(R.id.edtDepth);
        edtHeight = (EditText)findViewById(R.id.edtHeight);

        imgCamera.setOnClickListener(this);
        imgGallery.setOnClickListener(this);
        imgConfirm.setOnClickListener(this);
    }

    private void tedPermission() {
        PermissionListener permissionListener = new PermissionListener() {
            @Override
            public void onPermissionGranted() {

            }

            @Override
            public void onPermissionDenied(ArrayList<String> deniedPermissions) {

            }
        };

        TedPermission.with(this)
                .setPermissionListener(permissionListener)
                .setRationaleMessage(getResources().getString(R.string.permission_2))
                .setDeniedMessage(getResources().getString(R.string.permission_1))
                .setPermissions(Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA)
                .check();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode != Activity.RESULT_OK) {
            Toast.makeText(this, "취소되었습니다.", Toast.LENGTH_SHORT).show();
            if (photoFile != null) {
                if (photoFile.exists()) {
                    if (photoFile.delete()) {
                        photoFile = null;
                    }
                }
            }
        }

        if (requestCode == REQUEST_IMAGE_CAPTURE) {
            ((ImageView)findViewById(R.id.photozone)).setImageURI(photoURI);
        } else if (requestCode == PICK_FROM_ALBUM) {
            photoURI = data.getData();
            Cursor cursor = null;
            try {
                String[] proj = {MediaStore.Images.Media.DATA };
                assert photoURI != null;
                cursor = getContentResolver().query(photoURI, proj, null, null, null);
                assert cursor != null;
                int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
                cursor.moveToFirst();
                photoFile = new File(cursor.getString(column_index));
            } finally {
                if (cursor != null) {
                    cursor.close();
                }
            }
            setImage();
        }
    }

    private void setImage() {
        BitmapFactory.Options options = new BitmapFactory.Options();
        Bitmap originalBm = BitmapFactory.decodeFile(photoFile.getAbsolutePath(), options);

        ((ImageView)findViewById(R.id.photozone)).setImageBitmap(originalBm);
    }

    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException ex) {
                Toast.makeText(this, "ERROR: CAMERA", Toast.LENGTH_SHORT).show();
            }

            if (photoFile != null) {
                photoURI = FileProvider.getUriForFile(this, getPackageName(), photoFile);
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
            }
        }
    }

    private File createImageFile() throws IOException {
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",  /* suffix */
                storageDir      /* directory */
        );

        currentPhotoPath = image.getAbsolutePath();
        return image;
    }

    private void goToAlbum() {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType(MediaStore.Images.Media.CONTENT_TYPE);
        startActivityForResult(intent, PICK_FROM_ALBUM);
    }

    @Override
    public void onClick(View view) {
        if (view == imgCamera) {
            dispatchTakePictureIntent();
        } else if (view == imgGallery) {
            goToAlbum();
        } else if (view == imgConfirm) {
            if (edtName.getText().toString().length() == 0 || edtPrice.getText().toString().length() == 0
                    || edtWidth.getText().toString().length() == 0 || edtDepth.getText().toString().length() == 0 || edtHeight.getText().toString().length() == 0) {
                Toast.makeText(this, "빠진 양식이 없는지 확인해 주십시오.", Toast.LENGTH_SHORT).show();
            } else if (photozone.getDrawable() == null) {
                Toast.makeText(this, "가구 사진을 등록해주세요.", Toast.LENGTH_SHORT).show();
            } else {
                finish();
                Toast.makeText(this, "등록 요청 되었습니다.", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
