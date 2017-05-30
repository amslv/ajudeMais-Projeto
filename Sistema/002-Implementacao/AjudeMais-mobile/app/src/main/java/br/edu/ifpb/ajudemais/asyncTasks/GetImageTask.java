package br.edu.ifpb.ajudemais.asyncTasks;

import android.content.Context;
import android.os.AsyncTask;

import org.springframework.web.client.RestClientException;

import br.edu.ifpb.ajudemais.domain.Doador;
import br.edu.ifpb.ajudemais.remoteServices.ImagemStorageRemoteService;
import br.edu.ifpb.ajudemais.utils.ProgressDialog;

/**
 * <p>
 * <b>{@link GetImageTask}</b>
 * </p>
 * <p>
 * <p>
 * Asycn task para recuperar uma imagem.
 * </p>
 *
 * @author <a href="https://github.com/JoseRafael97">Rafael Feitosa</a>
 */
public class GetImageTask extends AsyncTask<Void, Void, byte[]> {

    private ProgressDialog progressDialog;
    private String imageName;
    private ImagemStorageRemoteService imagemStorageRemoteService;
    private String message;
    private Context context;
    public AsyncResponse<byte[]> delegate = null;


    public GetImageTask(Context context, String imageName){
        this.context = context;
        this.imageName = imageName;
        this.progressDialog = new ProgressDialog(context);
        this.imagemStorageRemoteService = new ImagemStorageRemoteService(context);
    }

    /**
     *
     */
    @Override
    protected void onPreExecute() {
        progressDialog.showProgressDialog();
        super.onPreExecute();
    }


    /**
     * Executa remote service que recupera um imagem.
     *
     * @param params
     * @return
     */
    @Override
    protected byte[] doInBackground(Void... params) {
        try {
            byte [] photo = imagemStorageRemoteService.getImage(imageName);

            return  photo;
        } catch (RestClientException e) {
            message = e.getMessage();
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     *
     * @param bytes
     */
    @Override
    protected void onPostExecute(byte[] bytes) {
        if (bytes != null){
            delegate.processFinish(bytes);
        }
        super.onPostExecute(bytes);
    }
}
