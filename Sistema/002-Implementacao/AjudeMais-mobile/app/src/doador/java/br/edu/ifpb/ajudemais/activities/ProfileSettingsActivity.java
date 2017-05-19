package br.edu.ifpb.ajudemais.activities;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TextInputEditText;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import org.springframework.web.client.RestClientException;

import java.io.ByteArrayOutputStream;
import java.io.File;

import br.edu.ifpb.ajudemais.R;
import br.edu.ifpb.ajudemais.asyncTasks.ChangePasswordTask;
import br.edu.ifpb.ajudemais.domain.Doador;
import br.edu.ifpb.ajudemais.domain.Imagem;
import br.edu.ifpb.ajudemais.fragments.ProfileSettingsFragment;
import br.edu.ifpb.ajudemais.remoteServices.DoadorRemoteService;
import br.edu.ifpb.ajudemais.remoteServices.ImagemStorageRemoteService;
import br.edu.ifpb.ajudemais.storage.SharedPrefManager;
import br.edu.ifpb.ajudemais.utils.AndroidUtil;
import br.edu.ifpb.ajudemais.utils.CapturePhotoUtils;

public class ProfileSettingsActivity extends AbstractAsyncActivity implements View.OnClickListener {

    private Toolbar mToolbar;
    private CollapsingToolbarLayout collapsingToolbarLayout;
    private FloatingActionButton fab;
    private AndroidUtil androidUtil;
    private SharedPrefManager sharedPrefManager;
    private Doador doador;
    private Button btnChangePassword;
    private ImageView imageView;
    private Context context;
    private NestedScrollView nestedScrollView;
    private static final int PICK_IMAGE_ID = 104;
    private static final int MY_PERMISSIONS_REQUEST_CAMERA = 11;
    private static final int MY_PERMISSIONS_GRANTED_CAMERA = 12;
    private static final int REQUEST_CAMERA = 1;
    private static final int SELECT_FILE = 13;
    private int PROFILE_PIC_COUNT = 0;
    protected CapturePhotoUtils capturePhotoUtils;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_settings);
        context = this;
        capturePhotoUtils = new CapturePhotoUtils(this);
        androidUtil = new AndroidUtil(this);
        sharedPrefManager = new SharedPrefManager(this);

        btnChangePassword = (Button) findViewById(R.id.btnChangePassword);
        mToolbar = (Toolbar) findViewById(R.id.nav_action);
        nestedScrollView = (NestedScrollView) findViewById(R.id.netScroll);

        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        collapsingToolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar);
        collapsingToolbarLayout.setTitle(getString(R.string.loading));

        imageView = (ImageView) findViewById(R.id.image_profile);
        Bitmap bitmap = capturePhotoUtils.loadImageFromStorage();

        if (bitmap != null) {
            imageView.setImageBitmap(bitmap);
        }

        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openDialog();
            }
        });

        fab = (FloatingActionButton) findViewById(R.id.fabEditAccount);
        new ProfileLoading().execute();

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(ProfileSettingsActivity.this, CreateAccountActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.putExtra("Doador", doador);
                startActivity(intent);
            }
        });

        btnChangePassword.setOnClickListener(this);

    }

    /**
     * Checa se o device pertence ao SDK versão 23 para exibir permissão
     */
    private void checkPermissions() {
        if (Build.VERSION.SDK_INT >= 23) {
            if (ContextCompat.checkSelfPermission(this,
                    Manifest.permission.CAMERA)
                    != PackageManager.PERMISSION_GRANTED) {
                requestPermission();

            }
        }
    }

    /**
     * Requisita permissão para realizar um ligação no device.
     */
    private void requestPermission() {
        int checkPermission = ContextCompat.checkSelfPermission(context, Manifest.permission.CAMERA);
        if (checkPermission != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(
                    this,
                    new String[]{Manifest.permission.CAMERA},
                    MY_PERMISSIONS_REQUEST_CAMERA);
        } else {
            customDialog();
        }
    }

    /**
     * @param
     */
    public void customDialog() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA}, MY_PERMISSIONS_REQUEST_CAMERA);
        }
    }


    private void openDialog() {
        final CharSequence[] items = {getString(R.string.TakePhoto), getString(R.string.gallery), getString(R.string.cancelar)};
        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(this);
        builder.setTitle(getString(R.string.selectPhoto));
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {

                if (items[item].equals(getString(R.string.TakePhoto))) {
                    checkPermissions();
                    PROFILE_PIC_COUNT = 1;
                    int checkPermission = ContextCompat.checkSelfPermission(context, Manifest.permission.CAMERA);
                    if (checkPermission == PackageManager.PERMISSION_GRANTED) {
                        ActivityCompat.requestPermissions(
                                ProfileSettingsActivity.this,
                                new String[]{Manifest.permission.CAMERA},
                                MY_PERMISSIONS_GRANTED_CAMERA);

                        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                        startActivityForResult(intent, REQUEST_CAMERA);
                    }

                } else if (items[item].equals(getString(R.string.gallery))) {
                    PROFILE_PIC_COUNT = 1;
                    Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
                    photoPickerIntent.setType("image/*");
                    startActivityForResult(photoPickerIntent, SELECT_FILE);

                } else if (items[item].equals(getString(R.string.cancelar))) {
                    PROFILE_PIC_COUNT = 0;
                    dialog.dismiss();
                }
            }
        });
        builder.show();
    }

    /**
     * @param requestCode
     * @param permissions
     * @param grantResults
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {

        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_CAMERA: {
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    openDialog();
                } else {
                    Toast.makeText(ProfileSettingsActivity.this, getString(R.string.permissionDeniedAccessCamera), Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

    /**
     * @param item
     * @return
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                Intent intent = new Intent(this, MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                finish();
                return true;
            case R.id.action_seletec_image:
                openDialog();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }



    /**
     * @param requestCode
     * @param resultCode
     * @param data
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        Bitmap photo = null;

        if (requestCode == REQUEST_CAMERA && resultCode == Activity.RESULT_OK) {
            photo = (Bitmap) data.getExtras().get("data");

        } else if (requestCode == SELECT_FILE && resultCode == Activity.RESULT_OK) {
            Uri selectedImage;
            File imageFile = capturePhotoUtils.getTempFile(this);
            selectedImage = data.getData();
            photo = capturePhotoUtils.getImageResized(this, selectedImage);


        }

        if (photo != null){

            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            photo.compress(Bitmap.CompressFormat.JPEG, 100, baos);
            byte[] imageBytes = baos.toByteArray();
            String encodedImage = Base64.encodeToString(imageBytes, Base64.DEFAULT);

            new ImageUploadTask(imageBytes).execute();
            imageView.setImageBitmap(photo);
            capturePhotoUtils.saveToInternalStorage(photo);

        }
    }


    /**
     * @param menu
     * @return
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.edit_profile_menu, menu);
        return true;
    }

    /**
     * @param v
     */
    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btnChangePassword) {
            LayoutInflater layoutInflaterAndroid = LayoutInflater.from(context);
            View mView = layoutInflaterAndroid.inflate(R.layout.dialog_change_password, null);
            AlertDialog.Builder alertDialogBuilderUserInput = new AlertDialog.Builder(context);
            alertDialogBuilderUserInput.setView(mView);

            final TextInputEditText password = (TextInputEditText) mView.findViewById(R.id.edtPassword);
            final TextInputEditText newPassword = (TextInputEditText) mView.findViewById(R.id.edtNewPassword);

            alertDialogBuilderUserInput
                    .setCancelable(false)
                    .setPositiveButton(R.string.btn_change_password, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialogBox, int id) {

                            if (newPassword.getText().toString().trim().length() > 5) {
                                new ChangePasswordTask(ProfileSettingsActivity.this, password.getText().toString().trim(), newPassword.getText().toString().trim()).execute();
                            } else {
                                Toast.makeText(getApplication(), "A nova senha informada deve contém no mínimo 6 caracteres", Toast.LENGTH_LONG).show();

                            }
                        }
                    })

                    .setNegativeButton(R.string.cancelar,
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialogBox, int id) {
                                    dialogBox.cancel();
                                }
                            });

            AlertDialog alertDialogAndroid = alertDialogBuilderUserInput.create();
            alertDialogAndroid.show();
        }
    }

    /**
     *
     */
    private class ProfileLoading extends AsyncTask<Void, Void, Doador> {

        private DoadorRemoteService doadorRemoteService;
        private String message = null;

        /**
         *
         */
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            doadorRemoteService = new DoadorRemoteService(getApplication());
        }

        /**
         * @param params
         * @return
         */
        @Override
        protected Doador doInBackground(Void... params) {
            try {

                if (androidUtil.isOnline()) {
                    doador = doadorRemoteService.getDoador(sharedPrefManager.getUser().getUsername());

                } else {
                }
            } catch (RestClientException e) {
                message = e.getMessage();
                e.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            }
            return doador;
        }

        @Override
        protected void onPostExecute(Doador doador) {
            if (doador != null) {

                collapsingToolbarLayout.setTitle(doador.getConta().getUsername());
                ProfileSettingsFragment fragment = new ProfileSettingsFragment();
                Bundle bundle = new Bundle();
                bundle.putSerializable("doador", doador);
                fragment.setArguments(bundle);
                android.support.v4.app.FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
                fragmentTransaction.add(R.id.editprofile_fragment, fragment);
                fragmentTransaction.commit();
                nestedScrollView.setVisibility(View.VISIBLE);
            }
        }
    }

    /**
     *
     */
    private class ImageUploadTask extends AsyncTask<Void, Void, Imagem> {

        private ImagemStorageRemoteService imagemStorageRemoteService;
        private String message = null;
        private Imagem imagem;
        private DoadorRemoteService doadorRemoteService;
        private byte[] array;

        public ImageUploadTask(byte[] array) {
            this.array = array;
        }

        /**
         *
         */
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            showLoadingProgressDialog();
            doadorRemoteService = new DoadorRemoteService(getApplication());
            imagemStorageRemoteService = new ImagemStorageRemoteService(getApplication());
        }

        /**
         * @param params
         * @return
         */
        @Override
        protected Imagem doInBackground(Void... params) {
            try {

                if (androidUtil.isOnline()) {
                    imagem = imagemStorageRemoteService.uploadImage(array);
                    doador.setFoto(imagem);
                    doador = doadorRemoteService.updateDoador(doador);

                } else {
                }
            } catch (RestClientException e) {
                message = e.getMessage();
                e.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            }
            return imagem;
        }

        @Override
        protected void onPostExecute(Imagem imagem) {
            dismissProgressDialog();
            if (imagem != null) {
                Toast.makeText(getApplicationContext(), "Imagem atualizada com sucesso",Toast.LENGTH_LONG).show();
            }
        }
    }

}