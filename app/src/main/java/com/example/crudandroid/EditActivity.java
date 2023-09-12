package com.example.crudandroid;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class EditActivity extends AppCompatActivity {
    public SQLiteDatabase bancoDados;
    public EditText editTextFilme;
    public EditText editTextGenero;
    public EditText editTextFaixaEtaria;
    public Button button;
    public Integer id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);

        Intent intent = getIntent();
        id = intent.getIntExtra("id",0);

        editTextFilme = (EditText) findViewById(R.id.editTextFilme);
        editTextGenero = (EditText) findViewById(R.id.editTextGenero);
        editTextFaixaEtaria = (EditText) findViewById(R.id.editTextFaixaEtaria);
        button = (Button) findViewById(R.id.button);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alterar();
            }
        });

        carregarDados();
    }

    @SuppressLint("Range")
    public void carregarDados(){
        try {
            bancoDados = openOrCreateDatabase("crudandroid", MODE_PRIVATE, null);
            Cursor cursor = bancoDados.rawQuery("SELECT id,nome, genero, faixaetaria FROM filme WHERE id = " + id.toString(), null);
            if(cursor.moveToFirst()) {
                editTextFilme.setText(cursor.getString(cursor.getColumnIndex("nome")));
                editTextGenero.setText(cursor.getString(cursor.getColumnIndex("genero")));
                editTextFaixaEtaria.setText(cursor.getString(cursor.getColumnIndex("faixaetaria")));
            }

            bancoDados.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void alterar(){
        if(TextUtils.isEmpty(editTextFilme.getText().toString())){
            editTextFilme.setError("Campo obrigatório!");
        } else {
            try {
                bancoDados = openOrCreateDatabase("crudandroid", MODE_PRIVATE, null);
                String sql = "UPDATE filme SET nome = ?, genero = ?, faixaetaria = ? WHERE id = ?";
                SQLiteStatement stmt = bancoDados.compileStatement(sql);
                stmt.bindString(1, editTextFilme.getText().toString());
                stmt.bindString(2, editTextGenero.getText().toString());
                stmt.bindString(3, editTextFaixaEtaria.getText().toString());
                stmt.bindLong(4, id );
                stmt.executeInsert();
                bancoDados.close();
                finish();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}