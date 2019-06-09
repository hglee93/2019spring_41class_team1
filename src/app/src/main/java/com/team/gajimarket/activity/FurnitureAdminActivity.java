package com.team.gajimarket.activity;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.Image;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.gun0912.tedpermission.PermissionListener;
import com.gun0912.tedpermission.TedPermission;
import com.team.gajimarket.R;
import com.team.gajimarket.item.FurnitureData;

import java.io.File;
import java.io.IOException;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class FurnitureAdminActivity extends AppCompatActivity implements View.OnClickListener {

    private static final int REQUEST_IMAGE_CAPTURE = 1;
    private static final int PICK_FROM_ALBUM = 2;
    String currentPhotoPath;

    int i;

    private Uri photoURI;
    private File photoFile;
    private DecimalFormat decimalFormat = new DecimalFormat("#,###");
    private String resPrice = "", resWidth = "", resDepth = "", resHeight = "";
    ImageView imgCamera, imgGallery, imgConfirm;
    EditText edtName, edtPrice, edtWidth, edtDepth, edtHeight;
    ImageView photozone;

    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_furniture_admin);

        tedPermission();

        i = 0;

        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference();

        imgCamera = (ImageView)findViewById(R.id.imgCamera);
        imgGallery = (ImageView)findViewById(R.id.imgGallery);
        imgConfirm = (ImageView)findViewById(R.id.imgConfirm);
        photozone = (ImageView)findViewById(R.id.photozone);

        edtName = (EditText)findViewById(R.id.edtName);
        edtPrice = (EditText)findViewById(R.id.edtPrice);
        edtPrice.addTextChangedListener(watcherPrice);
        edtWidth = (EditText)findViewById(R.id.edtWidth);
        edtWidth.addTextChangedListener(watcherWidth);
        edtDepth = (EditText)findViewById(R.id.edtDepth);
        edtDepth.addTextChangedListener(watcherDepth);
        edtHeight = (EditText)findViewById(R.id.edtHeight);
        edtHeight.addTextChangedListener(watcherHeight);

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

    TextWatcher watcherPrice = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            if (!TextUtils.isEmpty(s.toString()) && !s.toString().equals(resPrice)) {
                resPrice = decimalFormat.format(Double.parseDouble(s.toString().replaceAll(",","")));
                edtPrice.setText(resPrice);
                edtPrice.setSelection(resPrice.length());
            }
        }

        @Override
        public void afterTextChanged(Editable s) {

        }
    };
    TextWatcher watcherWidth = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            if (!TextUtils.isEmpty(s.toString()) && !s.toString().equals(resWidth)) {
                resWidth = decimalFormat.format(Double.parseDouble(s.toString().replaceAll(",","")));
                edtWidth.setText(resWidth);
                edtWidth.setSelection(resWidth.length());
            }
        }

        @Override
        public void afterTextChanged(Editable s) {

        }
    };
    TextWatcher watcherDepth = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            if (!TextUtils.isEmpty(s.toString()) && !s.toString().equals(resDepth)) {
                resDepth = decimalFormat.format(Double.parseDouble(s.toString().replaceAll(",", "")));
                edtDepth.setText(resDepth);
                edtDepth.setSelection(resDepth.length());
            }
        }

        @Override
        public void afterTextChanged(Editable s) {

        }
    };
    TextWatcher watcherHeight = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            if (!TextUtils.isEmpty(s.toString()) && !s.toString().equals(resHeight)) {
                resHeight = decimalFormat.format(Double.parseDouble(s.toString().replaceAll(",","")));
                edtHeight.setText(resHeight);
                edtHeight.setSelection(resHeight.length());
            }
        }

        @Override
        public void afterTextChanged(Editable s) {

        }
    };

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

    private void uploadFile() {
        if (photoURI != null) {
            final ProgressDialog progressDialog = new ProgressDialog(this);
            progressDialog.setTitle("등록 요청 중...");
            progressDialog.show();

            FirebaseUser user = mAuth.getCurrentUser();
            String uid = mAuth.getUid();

            FirebaseStorage storage = FirebaseStorage.getInstance();
            SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMHH_mmss");
            Date now = new Date();
            String filename = uid + "_" + formatter.format(now) + ".png";
            StorageReference storageReference = storage.getReferenceFromUrl("gs://eggplant-market.appspot.com")
                    .child("/furnitures/" + filename);
            storageReference.putFile(photoURI)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            progressDialog.dismiss();
                            Toast.makeText(getApplicationContext(), "등록 요청되었습니다", Toast.LENGTH_SHORT).show();
                            finish();
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            progressDialog.dismiss();
                            Toast.makeText(getApplicationContext(), "등록에 실패했습니다.", Toast.LENGTH_SHORT).show();
                        }
                    })
                    .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                            double progress = (100 * taskSnapshot.getBytesTransferred()) / taskSnapshot.getTotalByteCount();
                            progressDialog.setMessage("Uploaded " + ((int) progress) + "% ...");
                        }
                    });
        } else {
            Toast.makeText(getApplicationContext(), "등록된 사진 파일이 없습니다. 다시 한 번 확인해주세요.", Toast.LENGTH_SHORT).show();
        }
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
                FirebaseUser user = mAuth.getCurrentUser();
                String uid = mAuth.getUid();
                String name = user.getDisplayName();
                String email = user.getEmail();

                // Firebase Realtime DB에 Furniture 데이터 저장
                Query query = mDatabase.child("furnitures").orderByChild("sellerName");
                query.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMHH_mmss");
                        Date now = new Date();
                        String fileName = uid + "_" + formatter.format(now);

                        FurnitureData furnituredata = new FurnitureData(uid, name, email, edtName.getText().toString(),
                                edtPrice.getText().toString(), edtWidth.getText().toString(), edtDepth.getText().toString(),
                                edtHeight.getText().toString());
                        mDatabase.child("furnitures").child(fileName).setValue(furnituredata);

                        uploadFile();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
            }
        }
    }
}
