package com.example.pseudogram.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
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

    // View Variables
    private ImageView imageView;
    private TextView title;
    private TextView description;
    private Bitmap bitmap;

    // Object Variables
    private Picture currentPicture;
    private PictureDao pictureDao;

    // Value to request image capture
    private static final int REQUEST_IMAGE_CAPTURE = 101;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // View Variables
        setContentView(R.layout.activity_edit_picture);
        imageView = findViewById(R.id.imageView);
        title = findViewById(R.id.title);
        description = findViewById(R.id.description);
        pictureDao = new PictureDao(getApplicationContext());
        // Initialize currentPicture variable
        currentPicture = (Picture) getIntent().getSerializableExtra("EXTRA_PICTURE");
        if (currentPicture == null || currentPicture.getId() == null) {
            this.currentPicture = new Picture();
            findViewById(R.id.btnRemover).setEnabled(false);
        } else {
            title.setText(currentPicture.getTitle());
            description.setText((currentPicture.getDescription()));
            this.bitmap = BitmapFactory.decodeFile(currentPicture.getPath());
            imageView.setImageBitmap(this.bitmap);
        }
    }

    public void save(View view) {
        // Get data from screen
        currentPicture.setTitle(this.title.getText().toString());
        currentPicture.setDescription(this.description.getText().toString());

        try { // Save Picture data into Database
            // If title or description are invalid, do not allow saving
            if ((currentPicture.getTitle() == null || currentPicture.getTitle().equals("") ||
                    currentPicture.getTitle().isEmpty()) ||
                    (currentPicture.getDescription() == null || currentPicture.getDescription().equals("") ||
                    currentPicture.getDescription().isEmpty())) {
                Toast.makeText(getApplicationContext(), "Cannot Save without title or description",
                        Toast.LENGTH_SHORT).show();
                return;
            }

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
        } catch (Exception e) {
            System.out.println(e);
            Toast.makeText(getApplicationContext(), "Error", Toast.LENGTH_SHORT).show();
        }
        finish();
    }

    public void delete(View view) {
        this.deletePictureFile(this.currentPicture.getPath());
        pictureDao.delete(this.currentPicture);
        finish();
    }

    private String savePictureFile(Bitmap imageToSave, String fileName) {
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
        return file.getPath();
    }

    private void deletePictureFile(String filePath) {
        File file = new File(this.getApplicationContext().getFilesDir(), filePath);
        file.delete();
    }

    public void takePicture(View view) {
        Intent imageTakeIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (imageTakeIntent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(imageTakeIntent, REQUEST_IMAGE_CAPTURE);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            Bundle extras = data.getExtras();
            this.bitmap = (Bitmap) extras.get("data");
            imageView.setImageBitmap(this.bitmap);
        }
    }


}
