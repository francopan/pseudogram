package com.example.pseudogram.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.AlertDialog;
import android.content.DialogInterface;
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

    // Buttons
    private Button btnSave;
    private Button btnDelete;
    private Button btnTakePicture;
    private Button btnEdit;
    private Button btnChangePicture;

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
        btnEdit = findViewById(R.id.btnEdit);
        btnTakePicture = findViewById(R.id.btnTakePicture);
        btnChangePicture = findViewById(R.id.btnTakePicture);
        currentPicture = (Picture) getIntent().getSerializableExtra("EXTRA_PICTURE");

        // Adjusts data and display elements
        this.toggleEdition(null);
        if (currentPicture == null || currentPicture.getId() == null) {
            this.currentPicture = new Picture();
            getSupportActionBar().setTitle("New Picture");
        } else {
            title.setText(currentPicture.getTitle());
            getSupportActionBar().setTitle("Edit " + currentPicture.getTitle());
            description.setText((currentPicture.getDescription()));
            this.bitmap = BitmapFactory.decodeFile(currentPicture.getPath());
            imageView.setImageBitmap(this.bitmap);
        }
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
    }

    /**
     * This method is executed when the user takes a picture with the camera.
     * It displays the picture taken in the bitmap square in the view.
     * @param requestCode
     * @param resultCode
     * @param data
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) { // Image Captured
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            Bundle extras = data.getExtras();
            this.bitmap = (Bitmap) extras.get("data");
            imageView.setImageBitmap(this.bitmap);
        }
    }

    /**
     * This method is executed when the left arrow in the toolbar is clicked, sending the user
     * back to the previous page.
     * @return
     */
    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    /**
     * This method saves the picture into the local directory and saves data into database
     * @param view
     */
    public void save(View view) {
        // Get data from screen
        currentPicture.setTitle(this.title.getText().toString());
        currentPicture.setDescription(this.description.getText().toString());

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

    /**
     * This method toggles the edition by enabling and disabling the  visibility.
     * @param view
     */
    public void toggleEdition(View view) {
        if (currentPicture == null || currentPicture.getId() == null) {
            this.btnEdit.setVisibility(View.GONE);
            this.btnDelete.setVisibility(View.GONE);
        } else {
            this.btnEdit.setEnabled(this.btnSave.isEnabled());
            this.btnSave.setEnabled(!this.btnSave.isEnabled());
            this.title.setEnabled(!this.title.isEnabled());
            this.btnChangePicture.setEnabled(this.title.isEnabled());
            this.description.setEnabled(!this.description.isEnabled());
        }
    }

    /**
     * Prompts the user for delete confirmation. If true, then deletes the image from the device and
     * the data from the database.
     * @param view
     */
    public void delete(View view) {
        new AlertDialog.Builder(this)
                .setTitle("Delete Confirmation")
                .setMessage("Do you really want to delete?")
                .setIcon(android.R.drawable.ic_delete)
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        deletePictureFile(currentPicture.getPath());
                        pictureDao.delete(currentPicture);
                        finish();
                    }})
                .setNegativeButton(android.R.string.no, null).show();
    }

    /**
     * Requests an intent of taking a picture from the camera
     * @param view
     */
    public void takePicture(View view) {
        Intent imageTakeIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (imageTakeIntent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(imageTakeIntent, REQUEST_IMAGE_CAPTURE);
        }
    }

    /**
     * Saves the picture file in the device
     * @param imageToSave
     * @param fileName
     * @return
     * @throws IOException
     */
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

    /**
     * Deletes the picture file in the device
     * @param filePath
     */
    private void deletePictureFile(String filePath) {
        File file = new File(filePath);
        file.delete();
    }

}
