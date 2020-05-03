package com.example.pseudogram.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.pseudogram.R;
import com.example.pseudogram.model.Picture;
import com.example.pseudogram.repository.PictureDao;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.UUID;

public class EditPictureActivity extends AppCompatActivity {
    // Value to request image capture
    private static final int REQUEST_IMAGE_CAPTURE = 101;

    // View Variables
    private ImageView imageView;
    private TextView title;
    private TextView description;
    private Bitmap bitmap;

    // buttons
    private Button btnSave;
    private Button btnDelete;
    private Button btnTakePicture;

    // Object Variables
    private Picture currentPicture;
    private PictureDao pictureDao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // View Variables
        setContentView(R.layout.activity_edit_picture);
        pictureDao = new PictureDao(getApplicationContext());

        imageView = findViewById(R.id.imageView);
        title = findViewById(R.id.title);
        description = findViewById(R.id.description);

        btnSave = findViewById(R.id.btnSave);
        btnDelete = findViewById(R.id.btnDelete);
        btnTakePicture = findViewById(R.id.btnTakePicture);

        // Initialize currentPicture variable
        currentPicture = (Picture) getIntent().getSerializableExtra("EXTRA_PICTURE");
        if (currentPicture == null || currentPicture.getId() == null) {
            this.currentPicture = new Picture();
            btnDelete.setEnabled(false);
        } else {
            title.setText(currentPicture.getTitle());
            description.setText((currentPicture.getDescription()));
            this.bitmap = BitmapFactory.decodeFile(currentPicture.getPath());
            imageView.setImageBitmap(this.bitmap);
        }
    }

//    ------------------------------------------------------
//    button actions

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            Bundle extras = data.getExtras();
            this.bitmap = (Bitmap) extras.get("data");
            imageView.setImageBitmap(this.bitmap);
        }
    }


    public void save(View view) {
        // Get data from screen
        currentPicture.setTitle(this.title.getText().toString());
        currentPicture.setDescription(this.description.getText().toString());

        // If title or description are invalid, do not allow saving
        if (currentPicture.getTitle().trim().isEmpty() ||
                currentPicture.getDescription().trim().isEmpty() ||
                bitmap == null) {
            Toast.makeText(getApplicationContext(), "Cannot save! Enter the title, description and image",
                    Toast.LENGTH_SHORT).show();
            return;
        }

        try { // Save Picture data into Database
            if (currentPicture.getId() == null) { // New Picture
                currentPicture.setPath(this.savePictureFile(this.bitmap, UUID.randomUUID()
                        .toString() + ".jpg"));
                pictureDao.insert(currentPicture);
            } else { // Already Existent Picture
                this.deletePictureFile(currentPicture.getPath()); // Deletes Previous Image
                currentPicture.setPath(this.savePictureFile(this.bitmap, UUID.randomUUID()
                        .toString() + ".jpg"));
                pictureDao.update(currentPicture);
            }

            Toast.makeText(getApplicationContext(), "Success", Toast.LENGTH_SHORT).show();
            finish();
        } catch (IOException e) {
            Log.wtf("An error ocurred while saving the picture", e);
            Toast.makeText(getApplicationContext(), "Error", Toast.LENGTH_SHORT).show();
        }
    }

    public void delete(View view) {
        this.deletePictureFile(this.currentPicture.getPath());
        pictureDao.delete(this.currentPicture);
        finish();
    }

    public void takePicture(View view) {
        Intent imageTakeIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (imageTakeIntent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(imageTakeIntent, REQUEST_IMAGE_CAPTURE);
        }
    }

//    --------------------------------------------------------
//    util

    private String savePictureFile(Bitmap imageToSave, String fileName) throws IOException {
        try {
            File file = new File(this.getApplicationContext().getFilesDir(), fileName);
            if (file.exists()) {
                file.delete();
            }

            FileOutputStream out = new FileOutputStream(file);
            imageToSave.compress(Bitmap.CompressFormat.JPEG, 100, out);
            out.flush();
            out.close();
            return file.getPath();
        } catch (IOException e) {
            Log.wtf("Cannot save the picture", e);
            throw e;
        }
    }

    private void deletePictureFile(String filePath) {
        File file = new File(filePath);
        file.delete();
    }

}
