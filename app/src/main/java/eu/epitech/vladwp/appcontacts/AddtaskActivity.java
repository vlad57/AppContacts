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
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.regex.Pattern;

public class AddtaskActivity extends AppCompatActivity {

    final int REQUEST_CODE_GALLERY = 999;
    ImageView ImageNContact;
    public DBHandler myDB;
    public static final Pattern EMAIL_ADDRESS_PATTERN = Pattern.compile(
            "[a-zA-Z0-9\\+\\.\\_\\%\\-\\+]{1,256}" +
                    "\\@" +
                    "[a-zA-Z0-9][a-zA-Z0-9\\-]{0,64}" +
                    "(" +
                    "\\." +
                    "[a-zA-Z0-9][a-zA-Z0-9\\-]{0,25}" +
                    ")+"
    );


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_addtask);

        final AppCompatButton Add_task = (AppCompatButton) findViewById(R.id.add);
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
                if (TextUtils.isEmpty(Name.getText()) || TextUtils.isEmpty(Number.getText()) || TextUtils.isEmpty(Email.getText())) {
                    Toast.makeText(AddtaskActivity.this, "Complete all fields.", Toast.LENGTH_LONG).show();
                }
                else {
                    Long retourDB = myDB.addContact(Name.getText().toString(), Number.getText().toString(), Email.getText().toString(), imageViewToByte(ImageNContact));
                    if (!checkEmail(Email.getText().toString())){
                        Toast.makeText(AddtaskActivity.this, "Invalid email address.", Toast.LENGTH_LONG).show();
                    }
                    else if (retourDB == -1){
                        Toast.makeText(AddtaskActivity.this, "Contact already exist.", Toast.LENGTH_LONG).show();
                    }
                    else{
                        Intent intent = new Intent(AddtaskActivity.this, MainActivity.class);
                        Log.e("OKLM", "IDRETOURDB : " + Integer.valueOf(retourDB.intValue()));
                        intent.putExtra("IDRETOURDB", Integer.valueOf(retourDB.intValue()));
                        intent.putExtra(Constantes.NAME_KEY, Name.getText().toString());
                        intent.putExtra(Constantes.NUMBER_KEY, Number.getText().toString());
                        intent.putExtra(Constantes.EMAIL_KEY, Email.getText().toString());
                        intent.putExtra(Constantes.IMAGE_KEY,imageViewToByte(ImageNContact));
                        setResult(RESULT_OK, intent);
                        AddtaskActivity.this.finish();
                    }
                }
            }
        });

        Cancel_task.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AddtaskActivity.this.finish();
            }
        });
    }

    private boolean checkEmail(String email) {
        return EMAIL_ADDRESS_PATTERN.matcher(email).matches();
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
