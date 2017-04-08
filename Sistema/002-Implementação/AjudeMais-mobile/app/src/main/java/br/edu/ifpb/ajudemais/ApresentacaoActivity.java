package br.edu.ifpb.ajudemais;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import java.util.Timer;
import java.util.TimerTask;

public class ApresentacaoActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_apresentacao);

        //Time to next Activity
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            public void run() {

                SharedPreferences sharedPref = getSharedPreferences("login", Context.MODE_PRIVATE);
                String nomeUsuario = sharedPref.getString("nomeUsuario", null);
                String senhaUsuario = sharedPref.getString("emailUsuario", null);

                if (nomeUsuario != null && senhaUsuario != null) {
                    Intent i = new Intent();
                    i.setClass(ApresentacaoActivity.this, MainActivity.class);
                    i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(i);

                } else {

                    Intent i = new Intent();
                    i.setClass(ApresentacaoActivity.this, LoginActivity.class);
                    i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(i);
                }

            }//end TimerTask

        }, 1500);//end schedule
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.apresentacao_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        return id == R.id.action_about || super.onOptionsItemSelected(item);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}
