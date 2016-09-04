package br.com.paulo.exercicio;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void saveContact(View view) {

        Bundle extras = new Bundle();
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
