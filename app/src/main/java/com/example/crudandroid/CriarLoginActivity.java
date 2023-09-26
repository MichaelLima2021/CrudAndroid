package com.example.crudandroid;

import androidx.appcompat.app.AppCompatActivity;

import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class CriarLoginActivity extends AppCompatActivity {
    SQLiteDatabase bancoDados;
    EditText editTextLoginCadastro, editTextSenhaCadastro;
    Button buttonCadastroLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_criar_login);

        editTextLoginCadastro = (EditText) findViewById(R.id.editTextLoginCadastro);
        editTextSenhaCadastro = (EditText) findViewById(R.id.editTextSenhaCadastro);
        buttonCadastroLogin = (Button) findViewById(R.id.buttonCadastroLogin);
        buttonCadastroLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cadastrar();
            }
        });
    }

    public void cadastrar() {
        if (!TextUtils.isEmpty(editTextLoginCadastro.getText().toString()) && !TextUtils.isEmpty(editTextSenhaCadastro.getText().toString())) {
            try {
                bancoDados = openOrCreateDatabase("crudandroid", MODE_PRIVATE, null);
                String sql = "INSERT INTO login (login, senha) VALUES (?, ?)";
                SQLiteStatement stmt = bancoDados.compileStatement(sql);
                stmt.bindString(1,editTextLoginCadastro.getText().toString());
                stmt.bindString(2,editTextSenhaCadastro.getText().toString());
                stmt.executeInsert();
                bancoDados.close();
                finish();
            }catch(Exception e){
                e.printStackTrace();
            }
        }
    }
}