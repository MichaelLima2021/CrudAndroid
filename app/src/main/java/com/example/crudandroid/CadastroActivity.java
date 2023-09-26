package com.example.crudandroid;

import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class CadastroActivity extends AppCompatActivity {
    EditText editTextFilme;
    EditText editTextGenero;
    EditText editTextFaixaEtaria;
    Button botao;
    SQLiteDatabase bancoDados;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro);

        editTextFilme = (EditText) findViewById(R.id.editTextFilme);
        editTextGenero = (EditText) findViewById(R.id.editTextGenero);
        editTextFaixaEtaria = (EditText) findViewById(R.id.editTextFaixaEtaria);
        botao = (Button) findViewById(R.id.buttonCadastrar);

        botao.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cadastrar();
            }
        });
    }

    public void cadastrar() {
        if (!TextUtils.isEmpty(editTextFilme.getText().toString())) {
            try {
                SharedPreferences sharedPref = getSharedPreferences("crudandroidpref", MODE_PRIVATE);
                String login = sharedPref.getString("login","");

                bancoDados = openOrCreateDatabase("crudandroid", MODE_PRIVATE, null);
                String sql = "INSERT INTO filme (nome, genero, faixaetaria, login) VALUES (?, ?, ?, ?)";
                SQLiteStatement stmt = bancoDados.compileStatement(sql);
                stmt.bindString(1,editTextFilme.getText().toString());
                stmt.bindString(2,editTextGenero.getText().toString());
                stmt.bindString(3,editTextFaixaEtaria.getText().toString());
                stmt.bindString(4, login);
                stmt.executeInsert();
                bancoDados.close();
                finish();
            }catch(Exception e){
                e.printStackTrace();
            }
        }
    }
}