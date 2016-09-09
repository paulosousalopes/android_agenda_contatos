package br.com.paulo.agenda;

import android.content.Intent;
import android.graphics.Bitmap;
import android.hardware.Camera;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import br.com.paulo.agenda.R;

public class MainActivity extends AppCompatActivity {
    private Camera mCamera;
    Bundle extras = new Bundle();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    @Override
    protected void onStart() {
        super.onStart();
        TextView textViewTakePicture = (TextView) findViewById(R.id.textViewTakePicture);

        textViewTakePicture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //Intent viewCameraIntent = new Intent(getApplicationContext(), CameraActivity.class);

                //startActivity(viewCameraIntent);

                dispatchTakePictureIntent();
            }
        });
    }

    static final int REQUEST_IMAGE_CAPTURE = 1;

    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            this.extras = data.getExtras();
            //Bitmap imageBitmap = (Bitmap) extras.get("data");

            //ImageView mImageView = (ImageView) findViewById(R.id.imageView);
            //mImageView.setImageBitmap(imageBitmap);
        }
    }

    private void releaseCameraAndPreview() {
        //mPreview.setCamera(null);
        if (mCamera != null) {
            mCamera.release();
            mCamera = null;
        }
    }

    public void saveContact(View view) {


        EditText editTextName = (EditText) findViewById(R.id.editTextName);
        EditText editTextEmail = (EditText) findViewById(R.id.editTextEmail);
        EditText editTextPhone = (EditText) findViewById(R.id.editTextPhone);

        extras.putString("name", editTextName.getText().toString());
        extras.putString("email", editTextEmail.getText().toString());
        extras.putString("phone", editTextPhone.getText().toString());
        Intent viewContactIntent = new Intent(getApplicationContext(), ViewContactActivity.class);

        viewContactIntent.putExtras(extras);

        startActivity(viewContactIntent);
    }

    public void deleteContact(View view) {
        EditText editTextName = (EditText) findViewById(R.id.editTextName);
        EditText editTextEmail = (EditText) findViewById(R.id.editTextEmail);
        EditText editTextPhone = (EditText) findViewById(R.id.editTextPhone);

        // Apaga as informações do formulário
        editTextName.setText("");
        editTextEmail.setText("");
        editTextPhone.setText("");
    }
}
