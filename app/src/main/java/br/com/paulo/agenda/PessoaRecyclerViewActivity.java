package br.com.paulo.agenda;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import io.realm.RealmResults;

public class PessoaRecyclerViewActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private PessoaAdapter pessoaAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pessoa_recycler_view);
        init();
    }

    private void init() {
        recyclerView = (RecyclerView) findViewById(R.id.pessoa_recycler_view);
        recyclerView.setHasFixedSize(true);

        layoutManager = new LinearLayoutManager(this);

        recyclerView.setLayoutManager(layoutManager);
        final Pessoa pessoa = new Pessoa(getApplicationContext());
        final RealmResults<Pessoa> pessoas = pessoa.getAllPessoas();
        pessoaAdapter = new PessoaAdapter(getApplicationContext(), pessoa.getAllPessoas(), new PessoaAdapter.PessoaOnClickListener() {
            @Override
            public void onClickEvent(View view, int index) {
                Intent intent = new Intent(getApplicationContext(), PessoaRecyclerViewActivity.class);
                intent.putExtra("id", pessoas.get(index).getId());
                startActivity(intent);
            }
        });
        recyclerView.setAdapter(pessoaAdapter);
    }
}
