package br.com.paulo.agenda;

import android.content.Intent;

import android.content.SharedPreferences;
import android.hardware.Camera;
import android.provider.MediaStore;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import io.realm.Realm;
import io.realm.RealmConfiguration;

public class MainActivity extends AppCompatActivity {
    private EventBus eventBus = EventBus.getDefault();
    private Camera mCamera;
    private String TAG = "Agenda";
    private static final String PREF_NAME = "preferencia";
    private SharedPreferences sharedPreferences;
    static final int REQUEST_IMAGE_CAPTURE = 1;
    private Realm realm;
    private RealmConfiguration realmConfig;
    private Button btnRecyclerView;

    Bundle extras = new Bundle();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        realmConfig = new RealmConfiguration.Builder(this).build();
        realm       = Realm.getInstance(realmConfig);

        // Busca uma instancia para SharedPreferences para persistir dados no dispositivo
        sharedPreferences = getSharedPreferences(PREF_NAME, MODE_PRIVATE);

        // Cria um editor para modificar os dados da variável sharedPreferences
        SharedPreferences.Editor editor = sharedPreferences.edit();

        // Busca o inteiro armazenado para a chave "acessos"
        int i = sharedPreferences.getInt("acessos", 0);
        Log.i(TAG, String.valueOf(i));

        // Armazena um novo valor para a chave "acessos" (quantidade de acessos anteriores + o acesso atual)
        editor.putInt("acessos", i + 1);
        editor.commit();

        if (i % 3 == 0) {
            // A cada três acessos exibe uma mensagem de agradecimento
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

        btnRecyclerView  = (Button) findViewById(R.id.buttonRecycler);

        btnRecyclerView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Toast.makeText(getApplicationContext(), "Clicked", Toast.LENGTH_SHORT).show();

                Intent recyclerViewActivity = new Intent(getApplicationContext(), PessoaRecyclerViewActivity.class);
                startActivity(recyclerViewActivity);
            }
        });


        TextView textViewTakePicture = (TextView) findViewById(R.id.textViewTakePicture);

        textViewTakePicture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                dispatchTakePictureIntent();
            }
        });
    }

    @Override
    protected void onStop() {
        eventBus.unregister(this);
        super.onStop();
    }


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

        // Busca os elementos EditText pelos respectivos ids
        EditText editTextName = (EditText) findViewById(R.id.editTextName);
        EditText editTextEmail = (EditText) findViewById(R.id.editTextEmail);
        EditText editTextPhone = (EditText) findViewById(R.id.editTextPhone);

        // Armazenamos os valores em variáveis locais
        String name = editTextName.getText().toString();
        String email = editTextEmail.getText().toString();
        String phone = editTextPhone.getText().toString();

        // Estamos adicionando as informações no Bundle para que possam ser acessadas na ViewContactActivity
        // porém, vamos utilizar o eventBus para compartilhar os dados da pessoa, conforme o código abaixo
        extras.putString("name", name);
        extras.putString("email", email);
        extras.putString("phone", phone);

        // Instancia um objeto da classe Pessoa passando o contexto atual para o contrutor
        Pessoa pessoa = new Pessoa(getApplicationContext());

        pessoa.setEmail(email);
        pessoa.setName(name);
        pessoa.setPhone(phone);

        pessoa.save();
        long count = pessoa.count();
        Toast.makeText(getApplicationContext(), "You have " + count + " pessoa" + (count > 1 ? "s" : ""), Toast.LENGTH_SHORT).show();

        // Compartilhando um objeto do tipo Pessoa atravez de um eventBus para utilizar o recurso
        eventBus.postSticky(pessoa);




        // inicia o ViewContactActivity
        Intent viewContactIntent = new Intent(getApplicationContext(), ViewContactActivity.class);
        startActivity(viewContactIntent);
    }

    public void deleteContact(View view) {

        // Busca os elementos EditText pelos respectivos ids
        EditText editTextName = (EditText) findViewById(R.id.editTextName);
        EditText editTextEmail = (EditText) findViewById(R.id.editTextEmail);
        EditText editTextPhone = (EditText) findViewById(R.id.editTextPhone);

        // Apaga as informações do formulário
        editTextName.setText("");
        editTextEmail.setText("");
        editTextPhone.setText("");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //  Vamos fechar a instancia do Realm para evitar um "leak memory"
        realm.close();
    }
}
