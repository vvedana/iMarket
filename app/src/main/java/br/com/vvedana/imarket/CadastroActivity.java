package br.com.vvedana.imarket;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Created by vinicius on 05/07/2017.
 */

public class CadastroActivity extends AppCompatActivity
{
    ImageView voltar;
    TextView criar;
    EditText txtNome;
    EditText txtEmail;
    EditText txtSenha;
    EditText txtEndereco;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro);
        voltar = (ImageView) findViewById(R.id.sback);
        criar = (TextView) findViewById(R.id.sin);
        txtNome = (EditText) findViewById(R.id.fname);
        txtEmail = (EditText) findViewById(R.id.mail);
        txtSenha = (EditText) findViewById(R.id.pswrd);
        txtEndereco = (EditText) findViewById(R.id.endereco);
        voltar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                Intent it = new Intent(CadastroActivity.this, InicioActivity.class);
                startActivity(it);

            }
        });

        criar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (txtNome.getText().toString().isEmpty()){
                    Toast.makeText(CadastroActivity.this, "Nome deve ser informado!", Toast.LENGTH_LONG).show();
                    return;
                }
                if (txtEmail.getText().toString().isEmpty()){
                    Toast.makeText(CadastroActivity.this, "E-mail deve ser informado!", Toast.LENGTH_LONG).show();
                    return;
                }
                if (txtSenha.getText().toString().isEmpty()){
                    Toast.makeText(CadastroActivity.this, "Senha deve ser informada!", Toast.LENGTH_LONG).show();
                    return;
                }
                if (txtEndereco.getText().toString().isEmpty()){
                    Toast.makeText(CadastroActivity.this, "Endereço deve ser informado!", Toast.LENGTH_LONG).show();
                    return;
                }

                DBAdapter db = new DBAdapter(CadastroActivity.this);
                db.open();
                db.insereUsuario(txtNome.getText().toString(),txtEmail.getText().toString(),txtSenha.getText().toString(),txtEndereco.getText().toString());
                db.close();

                Toast.makeText(CadastroActivity.this, "Usuário cadastrado com sucesso! ;)", Toast.LENGTH_LONG).show();

                Intent it = new Intent(CadastroActivity.this, MainActivity.class);
                startActivity(it);
            }
        });
    }
}
