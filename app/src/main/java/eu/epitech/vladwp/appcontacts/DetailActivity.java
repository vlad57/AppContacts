package eu.epitech.vladwp.appcontacts;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.AppCompatButton;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;


public class DetailActivity extends AppCompatActivity {
    List<Model> ListModel;
    Model model;
    ImageView imageDetail;
    byte[]getImage;
    Bitmap mybitmap;
    final int REQUEST_CODE_GALLERY = 999;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        final DBHandler db = new DBHandler(this);
        model = new Model();
        final EditText NameDetail = (EditText)findViewById(R.id.nameDetail);
        final EditText NumberDetail = (EditText)findViewById(R.id.numberDetail);
        final EditText EmailDetail = (EditText)findViewById(R.id.emailDetail);
        AppCompatButton ButtonEdit = (AppCompatButton)findViewById(R.id.edit);
        AppCompatButton ButtonCancel = (AppCompatButton)findViewById(R.id.cancel);
        imageDetail = (ImageView)findViewById(R.id.imageDetail);

        ListModel = db.getSpecificContact(getIntent().getStringExtra(Constantes.ID_KEY));

        for (Model model : ListModel) {
            NameDetail.setText(model.getName());
            NumberDetail.setText(model.getNumber());
            EmailDetail.setText(model.getEmail());
            if (model.getImage() != null){
                getImage = model.getImage();
                mybitmap = BitmapFactory.decodeByteArray(getImage, 0, getImage.length);
                imageDetail.setImageBitmap(mybitmap);
            }
        }

        ButtonEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                db.updateContact(getIntent().getStringExtra(Constantes.ID_KEY), NameDetail.getText().toString(), NumberDetail.getText().toString(), EmailDetail.getText().toString(), imageViewToByte(imageDetail));
                DetailActivity.this.finish();
            }
        });

        ButtonCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DetailActivity.this.finish();
            }
        });

        imageDetail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ActivityCompat.requestPermissions(
                        DetailActivity.this,
                        new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                        REQUEST_CODE_GALLERY);
            }
        });

    }

    public static byte[] imageViewToByte(ImageView image) {
        Bitmap bitmap = ((BitmapDrawable)image.getDrawable()).getBitmap();
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 50, stream);
        byte[] byteArray = stream.toByteArray();
        return byteArray;
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        if(requestCode == REQUEST_CODE_GALLERY){
            if(grantResults.length >0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType("image/*");
                startActivityForResult(intent, REQUEST_CODE_GALLERY);
            }
            else {
                Toast.makeText(getApplicationContext(), "You don't have permission to access file location!", Toast.LENGTH_SHORT).show();
            }
            return;
        }

        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if(requestCode == REQUEST_CODE_GALLERY && resultCode == RESULT_OK && data != null){
            Uri uri = data.getData();

            try {
                InputStream inputStream = getContentResolver().openInputStream(uri);

                Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
                imageDetail.setImageBitmap(bitmap);

            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }

        super.onActivityResult(requestCode, resultCode, data);
    }


}
