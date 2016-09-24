package br.com.paulo.agenda;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.hardware.Camera;
import android.provider.MediaStore;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import br.com.paulo.agenda.R;

public class MainActivity extends AppCompatActivity {
    private EventBus eventBus = EventBus.getDefault();
    private Camera mCamera;
    private String TAG = "Agenda";
    private static final String PREF_NAME = "preferencia";
    private SharedPreferences sharedPreferences;


    Bundle extras = new Bundle();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        sharedPreferences = getSharedPreferences(PREF_NAME, MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        int i = sharedPreferences.getInt("acessos", 0);
        Log.i(TAG, ""+i);
        editor.putInt("acessos", i + 1);
        editor.commit();

        if (i % 3 == 0) {
            new AlertDialog.Builder(this)
                    .setTitle("Hello")
                    .setMessage("Thanks for using this app")
                    .show();
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        eventBus.register(this);

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

    @Override
    protected void onStop() {
        eventBus.unregister(this);
        super.onStop();
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
            releaseCameraAndPreview();
            //Bitmap imageBitmap = (Bitmap) extras.get("data");

            //ImageView mImageView = (ImageView) findViewById(R.id.imageView);
            //mImageView.setImageBitmap(imageBitmap);
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    protected void onMessageEvent(Pessoa event) {
        Log.i(TAG, "onMessageEvent: " + event.getName());
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

        String name = editTextName.getText().toString();
        String email = editTextEmail.getText().toString();
        String phone = editTextPhone.getText().toString();
        extras.putString("name", name);
        extras.putString("email", email);
        extras.putString("phone", phone);
        Intent viewContactIntent = new Intent(getApplicationContext(), ViewContactActivity.class);

        eventBus.postSticky(new Pessoa(name, null, email, phone, 20));
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
