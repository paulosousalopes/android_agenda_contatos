package br.com.paulo.agenda;

import android.Manifest;
import android.content.Intent;

import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.icu.text.Normalizer2;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.Button;

import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.view.View;

import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;


import br.com.paulo.agenda.R;

public class ViewContactActivity extends AppCompatActivity {
    final static String TAG = "Exercício";
    private EventBus eventBus = EventBus.getDefault();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_contact);
        getExtra();
    }

    @Override
    protected void onStart() {
        super.onStart();

        eventBus.register(this);
        final TextView textViewEmail = (TextView) findViewById(R.id.textViewEmailContact);
        final TextView textViewPhone = (TextView) findViewById(R.id.textViewPhoneContact);
        final Button buttonShare = (Button) findViewById(R.id.buttonShare);


        textViewEmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!textViewEmail.getText().toString().isEmpty()) {
                    Intent emailIntent = new Intent(Intent.ACTION_SENDTO, Uri.fromParts(
                            "mailto", textViewEmail.getText().toString(), null));
                    emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Subject");
                    emailIntent.putExtra(Intent.EXTRA_TEXT, "Body");

                    startActivity(Intent.createChooser(emailIntent, "Send email..."));
                }
            }
        });


        textViewPhone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!textViewPhone.getText().toString().isEmpty()) {
                    Intent intentCall = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + textViewPhone.getText().toString()));

                    // Verifica se existe a permissão para ligar
                    if (ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                        return;
                    }

                    startActivity(intentCall);
                } else {
                    Toast.makeText(getApplicationContext(), "Favor informar um telefone válido", Toast.LENGTH_SHORT).show();
                }
            }
        });


        buttonShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent sendItent = new Intent();
                TextView textViewName  = (TextView) findViewById(R.id.textViewNameContact);
                TextView textViewPhone = (TextView) findViewById(R.id.textViewPhoneContact);

                String name = String.valueOf(textViewName.getText());
                String phone = String.valueOf(textViewPhone.getText());
                sendItent.setAction(Intent.ACTION_SEND);
                sendItent.putExtra(Intent.EXTRA_TEXT, name + " - " + phone);
                sendItent.setType("text/plain");
                startActivity(sendItent);
            }
        });
    }

    @Override
    protected void onStop() {
        eventBus.unregister(this);
        super.onStop();
    }

    @Subscribe(sticky = true, threadMode = ThreadMode.MAIN)
    public void onPessoaEvent(Pessoa p) {
        TextView textViewName      = (TextView) findViewById(R.id.textViewNameContact);
        TextView textViewEmail     = (TextView) findViewById(R.id.textViewEmailContact);
        TextView textViewPhone     = (TextView) findViewById(R.id.textViewPhoneContact);

        // Atribui valores para o atributo Text dos componentes
        textViewName.setText(p.getName());
        textViewEmail.setText(p.getEmail());
        textViewPhone.setText(p.getPhone());

    }

    private void getExtra() {
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            //Recupera a imagem que foi adicionada ao Bundle no método onActivityResult
            Bitmap imageBitmap = (Bitmap) bundle.get("data");
            ImageView imageViewPicture = (ImageView) findViewById(R.id.imageView);
            imageViewPicture.setImageBitmap(imageBitmap);

        }
    }
}
