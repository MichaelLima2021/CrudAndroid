package com.example.crudandroid;

import androidx.appcompat.app.AppCompatActivity;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    private SQLiteDatabase bancoDados;
    public ListView listViewDados;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        listViewDados = (ListView) findViewById(R.id.listViewDados);

        criarBancoDados();
        inserirDadosTemp();
        listarDados();
    }

    public void criarBancoDados() {
        try {
            bancoDados = openOrCreateDatabase("crudandroid", MODE_PRIVATE, null);
            bancoDados.execSQL("CREATE TABLE IF NOT EXISTS filme(" +
                    "id INTEGER PRIMARY KEY AUTOINCREMENT" +
                    " , nome VARCHAR " +
                    " , genero VARCHAR " +
                    " , faixaetaria INTEGER)");
            bancoDados.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void listarDados() {
        try {
            bancoDados = openOrCreateDatabase("crudandroid", MODE_PRIVATE, null);
            Cursor meuCursor = bancoDados.rawQuery("SELECT id, nome, genero, faixaetaria FROM filme", null);
            ArrayList<String> linhas = new ArrayList<String>();
            ArrayAdapter meuAdapter = new ArrayAdapter<String>(
                    this,
                    android.R.layout.simple_list_item_1,
                    android.R.id.text1,
                    linhas
            );
            listViewDados.setAdapter(meuAdapter);
            meuCursor.moveToFirst();
            while (meuCursor!=null){
                linhas.add(meuCursor.getString(1)+ " , " + meuCursor.getString(2) + " , " +
                        meuCursor.getString(3));
                meuCursor.moveToNext();
            }
        }catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void inserirDadosTemp(){
        try {
            bancoDados = openOrCreateDatabase("crudandroid", MODE_PRIVATE, null);
            String sql = "INSERT INTO filme (nome, genero, faixaetaria) VALUES (?, ?, ?)";
            SQLiteStatement stmt = bancoDados.compileStatement(sql);

            stmt.bindString(1, "Divergente");
            stmt.bindString(2, "Ação");
            stmt.bindLong(3, 12);
            stmt.executeInsert();

            bancoDados.close();
        }catch (Exception e) {
            e.printStackTrace();
        }

    }
}