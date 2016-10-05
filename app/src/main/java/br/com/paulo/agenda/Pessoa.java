package br.com.paulo.agenda;

import android.content.Context;

import io.realm.Realm;
import io.realm.RealmConfiguration;
import io.realm.RealmObject;
import io.realm.RealmResults;
import io.realm.Sort;
import io.realm.annotations.PrimaryKey;


/**
 * Created by catolica on 24/09/16.
 */
public class Pessoa extends RealmObject {
    @PrimaryKey
    private int id;
    private String name, sobrenome, email, phone;

    private static Context contextPessoa;
    private static RealmConfiguration realmConfiguration;

    public Pessoa(){

    };

    public Pessoa(int id, String name, String email, String phone) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.phone = phone;
    }

    public Pessoa(Context context) {
        this.contextPessoa = context;
        this.realmConfiguration = new RealmConfiguration.Builder(this.contextPessoa).build();
    }

    private int autoIncrementId() {
        int key = 1;

        Realm realm = Realm.getInstance(this.realmConfiguration);
        try {
            // Consulta todos os registros de pessoa
            RealmResults<Pessoa> items = realm.where(Pessoa.class).findAll();
            items = items.sort("id", Sort.DESCENDING);
            // Obtem o valor do próximo id, caso não tenha nenhum registro o id será 1
            key = items.size() == 0? 1 : items.get(0).getId() + 1;
        }
        catch(NullPointerException e) {
            e.printStackTrace();
        }
        return key;
    }

    public void save() {
        Realm realm = Realm.getInstance(this.realmConfiguration);

        // Executa uma transação para criar um objeto no realm
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                Pessoa person = realm.createObject(Pessoa.class);
                person.setId(autoIncrementId());
                person.setEmail(getEmail());
                person.setName(getName());
                person.setPhone(getPhone());

            }
        });
    }

    public RealmResults<Pessoa> getAllPessoas() {
        Realm realm = Realm.getInstance(this.realmConfiguration);
        RealmResults<Pessoa> pessoas = realm.where(Pessoa.class).findAll();
        return pessoas;
    }

    public long count() {
        Realm realm = Realm.getInstance(this.realmConfiguration);
        return realm.where(Pessoa.class).count();
    }

    public Pessoa getPessoa(int id) {
        Realm realm = Realm.getInstance(this.realmConfiguration);
        return realm.where(Pessoa.class).equalTo("id", id).findFirst();
    }

    public void delete() {
        Realm realm = Realm.getInstance(this.realmConfiguration);
        final Pessoa pessoa = getPessoa(getId());

        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                pessoa.deleteFromRealm();
            }
        });
    }

    public void update(int id, Pessoa updatePessoa) {
        Realm realm = Realm.getInstance(this.realmConfiguration);
        realm.beginTransaction();
        Pessoa pessoa = getPessoa(id);

        pessoa.setName(updatePessoa.getName());
        pessoa.setPhone(updatePessoa.getPhone());
        pessoa.setEmail(updatePessoa.getEmail());
        // Persiste o objeto caso ele não exista, ou atualiza um objeto existente
        realm.copyToRealmOrUpdate(pessoa);
        realm.commitTransaction();
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public static Context getContextPessoa() {
        return contextPessoa;
    }

    public static void setContextPessoat(Context contextPessoa) {
        Pessoa.contextPessoa = contextPessoa;
    }

    public static RealmConfiguration getRealmConfiguration() {
        return realmConfiguration;
    }

    public static void setRealmConfiguration(RealmConfiguration realmConfiguration) {
        Pessoa.realmConfiguration = realmConfiguration;
    }
}
