package com.example.pseudogram.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.pseudogram.R;
import com.example.pseudogram.model.Picture;
import com.example.pseudogram.repository.PictureDao;

import java.io.File;
import java.io.FileOutputStream;
import java.util.UUID;

public class EditPictureActivity extends AppCompatActivity {

    private ImageView imageView;
    private TextView title;
    private TextView description;
    private Bitmap bitmap;

    private PictureDao pictureDao;

    private static final int REQUEST_IMAGE_CAPTURE = 101;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_picture);
        imageView = findViewById(R.id.imageView);
        title = findViewById(R.id.title);
        description = findViewById(R.id.description);
        pictureDao = new PictureDao(getApplicationContext());
    }

    public void takePicture(View view) {
        Intent imageTakeIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (imageTakeIntent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(imageTakeIntent, REQUEST_IMAGE_CAPTURE);
        }
    }

    public void save(View view) {
        Picture newPicture = new Picture();
        newPicture.setTitle(this.title.getText().toString());
        newPicture.setDescription(this.description.getText().toString());

        try {
            // Save Picture File into local Directory
            newPicture.setPath(this.saveFile(this.bitmap, UUID.randomUUID().toString() + ".jpg"));
            // Save Picture data into Database
            if (newPicture.getId() == null) {
                pictureDao.insert(newPicture);
            } else {
                pictureDao.update(newPicture);
            }
            Toast.makeText(getApplicationContext(), "Success", Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            System.out.println(e);
            Toast.makeText(getApplicationContext(), "Error", Toast.LENGTH_SHORT).show();
        }

    }

    private String saveFile(Bitmap imageToSave, String fileName) {

        File file = new File(this.getApplicationContext().getFilesDir(), fileName);
        if (file.exists()) {
            file.delete();
        }
        try {
            FileOutputStream out = new FileOutputStream(file);
            imageToSave.compress(Bitmap.CompressFormat.JPEG, 100, out);
            out.flush();
            out.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return file.getAbsolutePath();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            Bundle extras = data.getExtras();
            this.bitmap = (Bitmap) extras.get("data");
            imageView.setImageBitmap(this.bitmap);
        }

//        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_CANCELED)
//            finish();
    }
}
