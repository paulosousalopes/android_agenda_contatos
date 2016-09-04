package br.com.paulo.exercicio;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

public class ViewContactActivity extends AppCompatActivity {
    final static String TAG = "Exercício";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_contact);
        getExtra();
    }

    @Override
    protected void onStart() {
        super.onStart();

        final TextView textViewEmail = (TextView) findViewById(R.id.textViewEmailContact);
        final TextView textViewPhone = (TextView) findViewById(R.id.textViewPhoneContact);

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


    }

    private void getExtra() {
        Bundle bundle = getIntent().getExtras();

        if (bundle != null) {
            // Atribui os valores recebidos pelo bundle para variáveis locais
            String name  = bundle.getString("name");
            String email = bundle.getString("email");
            String phone = bundle.getString("phone");

            TextView textViewName  = (TextView) findViewById(R.id.textViewNameContact);
            TextView textViewEmail = (TextView) findViewById(R.id.textViewEmailContact);
            TextView textViewPhone = (TextView) findViewById(R.id.textViewPhoneContact);

            // Atribui valores para o atributo Text dos componentes
            textViewName.setText(name);
            textViewEmail.setText(email);
            textViewPhone.setText(phone);
        }
    }


}
