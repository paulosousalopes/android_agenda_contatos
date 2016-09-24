package br.com.paulo.agenda;

/**
 * Created by catolica on 24/09/16.
 */
public class Pessoa {
    private String name, sobrenome, email, telefone;
    private int age;

    public Pessoa(String name, String sobrenome, String email, String telefone, int age) {
        this.name = name;
        this.sobrenome = sobrenome;
        this.email = email;
        this.telefone = telefone;
        this.age = age;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSobrenome() {
        return sobrenome;
    }

    public void setSobrenome(String sobrenome) {
        this.sobrenome = sobrenome;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getTelefone() {
        return telefone;
    }

    public void setTelefone(String telefone) {
        this.telefone = telefone;
    }
}
