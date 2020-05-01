package com.example.pseudogram.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.pseudogram.R;
import com.example.pseudogram.model.Picture;
import com.google.android.material.textfield.TextInputEditText;

import java.io.File;
import java.io.FileOutputStream;
import java.util.UUID;

public class EditPictureActivity extends AppCompatActivity {

    private ImageView imageView;
    private TextView title;
    private TextView description;
    private Bitmap bitmap;

    private static final int REQUEST_IMAGE_CAPTURE = 101;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_picture);
        imageView = findViewById(R.id.imageView);
        title = findViewById(R.id.title);
        description = findViewById(R.id.description);
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

        // Salvar imagem no diretorio
        this.saveFile(this.bitmap, UUID.randomUUID().toString() + ".jpg");

        // Chamar o DAO e Salvar

    }

    private void saveFile(Bitmap imageToSave, String fileName) {

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
