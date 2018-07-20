package br.com.vvedana.imarket;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * Created by vinicius on 05/07/2017.
 */

public class InicioActivity extends AppCompatActivity {
    TextView login;
    LinearLayout cadastro;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inicio);

        cadastro = (LinearLayout) findViewById(R.id.circle);
        login = (TextView) findViewById(R.id.sin);

        cadastro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intentt = new Intent(InicioActivity.this, CadastroActivity.class);
                startActivity(intentt);

            }
        });
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentt = new Intent(InicioActivity.this, LoginActivity.class);
                startActivity(intentt);
            }
        });

    }
}
