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
    public EditText editTextTitulo;
    public EditText editTextGenero;
    public EditText editTextAutor;
    public Button button;
    public Integer id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);

        Intent intent = getIntent();
        id = intent.getIntExtra("id",0);

        editTextTitulo = (EditText) findViewById(R.id.editTextTitulo);
        editTextGenero = (EditText) findViewById(R.id.editTextGenero);
        editTextAutor = (EditText) findViewById(R.id.editTextAutor);
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
            Cursor cursor = bancoDados.rawQuery("SELECT id,titulo, genero, autor FROM livro WHERE id = " + id.toString(), null);
            if(cursor.moveToFirst()) {
                editTextTitulo.setText(cursor.getString(cursor.getColumnIndex("titulo")));
                editTextGenero.setText(cursor.getString(cursor.getColumnIndex("genero")));
                editTextAutor.setText(cursor.getString(cursor.getColumnIndex("autor")));
            }

            bancoDados.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void alterar(){
        if(TextUtils.isEmpty(editTextTitulo.getText().toString())){
            editTextTitulo.setError("Campo obrigatório!");
        } else {
            try {
                bancoDados = openOrCreateDatabase("crudandroid", MODE_PRIVATE, null);
                String sql = "UPDATE livro SET titulo = ?, genero = ?, autor = ? WHERE id = ?";
                SQLiteStatement stmt = bancoDados.compileStatement(sql);
                stmt.bindString(1, editTextTitulo.getText().toString());
                stmt.bindString(2, editTextGenero.getText().toString());
                stmt.bindString(3, editTextAutor.getText().toString());
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