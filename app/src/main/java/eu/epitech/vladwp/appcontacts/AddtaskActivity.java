package eu.epitech.vladwp.appcontacts;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatButton;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

public class AddtaskActivity extends AppCompatActivity {

    final int REQUEST_CODE_GALLERY = 999;
    ImageView ImageNContact;
    public DBHandler myDB;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_addtask);

        AppCompatButton Add_task = (AppCompatButton) findViewById(R.id.add);
        AppCompatButton Cancel_task = (AppCompatButton)findViewById(R.id.cancelAddtask);
        final EditText Name = (EditText)findViewById(R.id.newName);
        final EditText Number = (EditText)findViewById(R.id.newNumber);
        final EditText Email = (EditText)findViewById(R.id.email);
        ImageNContact = (ImageView)findViewById(R.id.NewContactImage);

        ImageNContact.setImageResource(R.drawable.android);

        myDB = new DBHandler(this);

        ImageNContact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ActivityCompat.requestPermissions(
                        AddtaskActivity.this,
                        new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                        REQUEST_CODE_GALLERY);
            }
        });



        Add_task.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ImageNContact.getDrawable() != null){
                    myDB.addContact(Name.getText().toString(),Number.getText().toString(), Email.getText().toString(), imageViewToByte(ImageNContact));
                }
                else{
                    final Resources myRes = getResources();
                    final Drawable zeroImage = myRes.getDrawable(R.drawable.android);
                    ImageNContact.setImageDrawable(zeroImage);
                    myDB.addContact(Name.getText().toString(),Number.getText().toString(), Email.getText().toString(), imageViewToByte(ImageNContact));
                }
                AddtaskActivity.this.finish();
            }
        });

        Cancel_task.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AddtaskActivity.this.finish();
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
                ImageNContact.setImageBitmap(bitmap);

            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }

        super.onActivityResult(requestCode, resultCode, data);
    }

}
