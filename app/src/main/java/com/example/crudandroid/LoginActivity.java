package com.example.crudandroid;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class LoginActivity extends AppCompatActivity {
    Button buttonCadastrarLogin1, buttonEntrar;
    EditText editTextLoginIndex, editTextSenhaIndex;
    SQLiteDatabase bancoDados;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        buttonCadastrarLogin1 = (Button) findViewById(R.id.buttonCadastrarLogin1);
        buttonEntrar = (Button) findViewById(R.id.buttonEntrar);
        editTextLoginIndex = (EditText) findViewById(R.id.editTextLoginIndex);
        editTextSenhaIndex = (EditText) findViewById(R.id.editTextSenhaIndex);
        buttonCadastrarLogin1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                abrirCadastroLogin();
            }
        });

        buttonEntrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                validarLogin();
            }
        });

        criarBancoDados();
    }

    public void abrirCadastroLogin() {
        Intent intent = new Intent(this, CriarLoginActivity.class);
        startActivity(intent);
    }

    public void criarBancoDados() {
        try {
            bancoDados = openOrCreateDatabase("crudandroid", MODE_PRIVATE, null);
            bancoDados.execSQL("CREATE TABLE IF NOT EXISTS login(" +
                    " login VARCHAR PRIMARY KEY" +
                    " , senha VARCHAR )");
            bancoDados.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void validarLogin() {
        String login = editTextLoginIndex.getText().toString();
        String senha = editTextLoginIndex.getText().toString();
        bancoDados = openOrCreateDatabase("crudandroid", MODE_PRIVATE, null);
        Cursor meuCursor = bancoDados.rawQuery("SELECT login, senha FROM login WHERE login='" + login + "' AND senha='" + senha + "'", null);
        if(meuCursor.moveToFirst()) {
            SharedPreferences sharedPref = getSharedPreferences("crudandroidpref", MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPref.edit();
            editor.putString("login", login);
            editor.commit();

            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
        }
        else{
            Toast.makeText(this, "Não logou", Toast.LENGTH_SHORT).show();
        }
    }
}