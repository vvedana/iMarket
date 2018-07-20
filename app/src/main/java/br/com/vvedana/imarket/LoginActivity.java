package br.com.vvedana.imarket;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Created by vinicius on 05/07/2017.
 */

public class LoginActivity extends AppCompatActivity {

    ImageView voltar;
    TextView login;
    EditText txtUsuario;
    EditText txtSenha;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        voltar = (ImageView)findViewById(R.id.sinb);
        login = (TextView) findViewById(R.id.login);
        txtUsuario = (EditText) findViewById(R.id.usrusr);
        txtSenha = (EditText) findViewById(R.id.pswrd);
        voltar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this,InicioActivity.class);
                startActivity(intent);
            }
        });

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (txtUsuario.getText().toString().isEmpty()){
                    Toast.makeText(LoginActivity.this, "Usuário deve ser informado!", Toast.LENGTH_LONG).show();
                    return;
                }

                if (txtSenha.getText().toString().isEmpty()){
                    Toast.makeText(LoginActivity.this, "Senha deve ser informada!", Toast.LENGTH_LONG).show();
                    return;
                }

                DBAdapter db = new DBAdapter(LoginActivity.this);
                db.open();

                Cursor usuario = db.getUsuario(txtUsuario.getText().toString(),txtSenha.getText().toString() );

                db.close();

                if (usuario.getCount() <= 0){
                    Toast.makeText(LoginActivity.this, "Usuário ou senha inválidos!", Toast.LENGTH_LONG).show();
                    return;
                } else {
                    Intent intent = new Intent(LoginActivity.this,MainActivity.class);
                    startActivity(intent);
                }

            }
        });
    }
}
