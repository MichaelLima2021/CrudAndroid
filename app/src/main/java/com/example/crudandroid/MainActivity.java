package com.example.crudandroid;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    private SQLiteDatabase bancoDados;
    public ListView listViewDados;
    public Button botao;
    public ArrayList<String> filmesArray;
    public ArrayList<Integer> idsArray;
    TextView textViewSaudacao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        listViewDados = (ListView) findViewById(R.id.listViewDados);
        botao = (Button) findViewById(R.id.buttonInserir);

        registerForContextMenu(listViewDados);

        botao.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                abrirTelaCadastro();
            }
        });

        criarBancoDados();
        //inserirDadosTemp();
        listarDados();

        textViewSaudacao = (TextView) findViewById(R.id.textViewSaudacao);
        SharedPreferences sharedPref = getSharedPreferences("crudandroidpref", MODE_PRIVATE);
        String login = sharedPref.getString("login","");
        textViewSaudacao.setText("Olá "+login+"!");
    }

    @Override
    protected void onResume() {
        super.onResume();
        listarDados();
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v,
                                    ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menuzinho, menu);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item){
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo)item.getMenuInfo();
        switch (item.getItemId()) {
            case R.id.editar:
                abrirEditar(idsArray.get(info.position));
                return true;
            case R.id.excluir:
                excluir(info.position);
                return true;
        }
        return super.onContextItemSelected(item);
    }

    public void criarBancoDados() {
        try {
            bancoDados = openOrCreateDatabase("crudandroid", MODE_PRIVATE, null);
            bancoDados.execSQL("CREATE TABLE IF NOT EXISTS filme(" +
                    "id INTEGER PRIMARY KEY AUTOINCREMENT" +
                    " , nome VARCHAR " +
                    " , genero VARCHAR " +
                    " , faixaetaria INTEGER " +
                    " , login VARCHAR)");
            bancoDados.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void abrirEditar(Integer id){
        Intent intent = new Intent(this,EditActivity.class);
        intent.putExtra("id",id);
        startActivity(intent);
    }

    public void excluir(final Integer position){
        AlertDialog alerta;
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Excluir");
        builder.setMessage("Deseja realmente excluir?");
        builder.setPositiveButton("Sim", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface arg0, int arg1) {
                excluirDB(idsArray.get(position));
                listarDados();
            }
        });
        builder.setNegativeButton("Não", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface arg0, int arg1) {
            }
        });
        alerta = builder.create();
        alerta.show();
    }


    @SuppressLint("Range")
    public void listarDados() {
        try {
            SharedPreferences sharedPref = getSharedPreferences("crudandroidpref", MODE_PRIVATE);
            String login = sharedPref.getString("login","");

            bancoDados = openOrCreateDatabase("crudandroid", MODE_PRIVATE, null);
            Cursor meuCursor = bancoDados.rawQuery("SELECT id, nome, genero, faixaetaria FROM filme WHERE login='" + login + "'", null);
            filmesArray = new ArrayList<String>();
            idsArray = new ArrayList<Integer>();
            ArrayAdapter adapter =
                    new ArrayAdapter(this,
                            android.R.layout.simple_list_item_1, filmesArray);
            if(meuCursor.moveToFirst()) {
                do {
                    filmesArray.add(meuCursor.getString(meuCursor.getColumnIndex("nome"))+ ", "
                            + meuCursor.getString(meuCursor.getColumnIndex("genero"))+ ", "
                            + meuCursor.getString(meuCursor.getColumnIndex("faixaetaria")));
                    idsArray.add(meuCursor.getInt(meuCursor.getColumnIndex("id")));
                } while (meuCursor.moveToNext());
            }
            listViewDados.setAdapter(adapter);
            bancoDados.close();
        }catch (Exception e) {
            e.printStackTrace();
        }
    }

    /*public void inserirDadosTemp(){
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

    }*/

    public void excluirDB(Integer id){
        try {
            bancoDados = openOrCreateDatabase("crudandroid",MODE_PRIVATE ,null);
            String sql = "DELETE FROM filme WHERE id = ?";
            SQLiteStatement stmt = bancoDados.compileStatement(sql);
            stmt.bindLong(1, id);
            stmt.executeUpdateDelete();
            bancoDados.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void abrirTelaCadastro() {
        Intent intent = new Intent(this,CadastroActivity.class);
        startActivity(intent);
    }
}