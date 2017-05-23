package br.edu.ifpb.ajudemais.activities;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBar;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import br.edu.ifpb.ajudemais.R;
import br.edu.ifpb.ajudemais.TabFragment;
import br.edu.ifpb.ajudemais.domain.Conta;
import br.edu.ifpb.ajudemais.storage.SharedPrefManager;

/**
 * <p>
 * <b>MainActivity</b>
 * </p>
 * <p>
 * <p>
 * Activity inicial para Mensageiro
 * </p>
 *
 * @author <a href="https://github.com/JoseRafael97">Rafael Feitosa</a>
 */
public class MainActivity extends AbstractActivity implements NavigationView.OnNavigationItemSelectedListener {

    private FragmentManager mFragmentManager;
    private FragmentTransaction mFragmentTransaction;
    private FloatingActionButton fab;
    private RelativeLayout componentHeader;


    /**
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        findViewById(R.id.loadingPanel).setVisibility(View.VISIBLE);
        findViewById(R.id.containerView).setVisibility(View.GONE);

        init();
        setUpAccount();
        setUpToggle();
        setupNavDrawer();

        mFragmentManager = getSupportFragmentManager();
        mFragmentTransaction = mFragmentManager.beginTransaction();
        mFragmentTransaction.replace(R.id.containerView, new TabFragment()).commit();

        fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        new LoadingColetasTask().execute();

    }


    /**
     * Set as informações do usuário logado no app
     */
    protected void setUpAccount() {
        View hView = mNavigationView.getHeaderView(0);
        componentHeader = (RelativeLayout) hView.findViewById(R.id.background_header);
        profilePhoto = (ImageView) hView.findViewById(R.id.photoProfile);
        tvUserName = (TextView) hView.findViewById(R.id.tvUserNameProfile);
        tvEmail = (TextView) hView.findViewById(R.id.tvEmailProfile);

        conta = (Conta) getIntent().getSerializableExtra("Conta");

        if (conta == null){
            conta = SharedPrefManager.getInstance(this).getUser();
        }

        if (conta != null) {
            tvUserName.setText(conta.getUsername());
            tvEmail.setText(conta.getEmail());
        }

        if (getIntent().hasExtra("ImageByteArray") && getIntent().getByteArrayExtra("ImageByteArray") != null) {
            if (isStoragePermissionGranted()){
                bitmap = androidUtil.convertBytesInBitmap(getIntent().getByteArrayExtra("ImageByteArray"));
                capturePhotoUtils.saveToInternalStorage(bitmap);
            }

        } else {
            bitmap = capturePhotoUtils.loadImageFromStorage();
        }

        if (bitmap != null) {
            profilePhoto.setImageBitmap(bitmap);
        }

        componentHeader.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(MainActivity.this, ProfileSettingsActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        });
    }


    /**
     * @param menuItem
     */
    private void onNavDrawerItemSelected(MenuItem menuItem) {
        switch (menuItem.getItemId()) {
            case R.id.nav_my_addresses:
                Intent intent = new Intent();
                intent.setClass(MainActivity.this, MyEnderecosActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);

            case R.id.nav_config_conta:

            case R.id.nav_notificacoes:
                break;
            case R.id.nav_sair:
                SharedPrefManager.getInstance(this).clearSharedPrefs();
                capturePhotoUtils.deleteImageProfile();
                Intent intent2 = new Intent();
                intent2.setClass(MainActivity.this, LoginActivity.class);
                intent2.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                finish();
                startActivity(intent2);
        }

    }

    /**
     * Set Configuração para Navegation Drawer
     */
    protected void setupNavDrawer() {
        final ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            if (mNavigationView != null && mDrawerLayout != null) {

                mNavigationView.setNavigationItemSelectedListener(
                        new NavigationView.OnNavigationItemSelectedListener() {
                            @Override
                            public boolean onNavigationItemSelected(MenuItem menuItem) {
                                menuItem.setChecked(true);
                                mDrawerLayout.closeDrawers();
                                onNavDrawerItemSelected(menuItem);
                                return true;
                            }
                        });
            }
        }
    }




    /**
     * Classe auxiliar para acessar Serviços
     */
    private class LoadingColetasTask extends AsyncTask<Void, Void, String> {

        @Override
        protected String doInBackground(Void... params) {
            return null;
        }

        @Override
        protected void onPostExecute(String message) {
            findViewById(R.id.loadingPanel).setVisibility(View.GONE);
            findViewById(R.id.containerView).setVisibility(View.VISIBLE);
        }

    }
}
