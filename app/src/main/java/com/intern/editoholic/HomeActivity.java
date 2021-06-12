package com.intern.editoholic;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.dsphotoeditor.sdk.activity.DsPhotoEditorActivity;
import com.dsphotoeditor.sdk.utils.DsPhotoEditorConstants;

public class HomeActivity extends AppCompatActivity {

    Button b1;
    ImageView imageView1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        b1 = findViewById(R.id.bt_pick);
        imageView1 = findViewById(R.id.image_view);
        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkPermission();
            }
        });
    }

    private void checkPermission() {
        int permission = ActivityCompat.checkSelfPermission(HomeActivity.this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE);
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q){
            pickImage();
        }else{
            if(permission != PackageManager.PERMISSION_GRANTED){
                ActivityCompat.requestPermissions(HomeActivity.this,
                        new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                        100);
            }else {
                pickImage();
            }
        }
    }

    private void pickImage() {
        Intent i1 = new Intent(Intent.ACTION_PICK);
        i1.setType("image/*");
        startActivityForResult(i1,100);
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull @org.jetbrains.annotations.NotNull String[] permissions, @NonNull @org.jetbrains.annotations.NotNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode == 100 && grantResults.length>0 && grantResults[0] ==
        PackageManager.PERMISSION_GRANTED){
            pickImage();
        }else{
            Toast.makeText(this, "Permission Denied...", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable @org.jetbrains.annotations.Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK){
            Uri uri = data.getData();
            switch (requestCode){
                case 100:
                    Intent i1 = new Intent(HomeActivity.this, DsPhotoEditorActivity.class);
                    i1.setData(uri);
                    i1.putExtra(DsPhotoEditorConstants.DS_PHOTO_EDITOR_OUTPUT_DIRECTORY,
                            "Images");
//                    i1.putExtra(DsPhotoEditorConstants.DS_TOOL_BAR_BACKGROUND_COLOR,
//                            Color.parseColor("FFFFFF"));
//                    i1.putExtra(DsPhotoEditorConstants.DS_MAIN_BACKGROUND_COLOR,
//                            Color.parseColor("FFFFFF"));
                    i1.putExtra(DsPhotoEditorConstants.DS_PHOTO_EDITOR_TOOLS_TO_HIDE,
                            new int[]{DsPhotoEditorActivity.TOOL_WARMTH,
                            DsPhotoEditorActivity.TOOL_PIXELATE});
                    startActivityForResult(i1,101);
                    break;
                case 101:
                    imageView1.setImageURI(uri);
                    Toast.makeText(this, "Photo Saved...", Toast.LENGTH_SHORT).show();
                    break;
            }
        }
    }
}