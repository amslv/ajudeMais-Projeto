package br.edu.ifpb.ajudemais.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.annotation.NonNull;
import android.util.Log;
import android.widget.EditText;

import com.redmadrobot.inputmask.MaskedTextChangedListener;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import br.edu.ifpb.ajudemais.activities.MainActivity;

/**
 * <p>
 * <b>{@link AndroidUtil}</b>
 * </p>
 * <p>
 * Classe com alguns métodos utilitários.
 * </p>
 * @author <a href="https://github.com/JoseRafael97">Rafael Feitosa</a>
 */


public class AndroidUtil {

    private Context context;

    public AndroidUtil(Context context){
        this.context = context;
    }


    /**
     * Verifica se o device possui alguma conexão com a internet.
     * @return
     */
    public boolean isOnline() {
        boolean connected = false;
        try {
            ConnectivityManager connectivityManager = (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
            connected = networkInfo != null && networkInfo.isAvailable() && networkInfo.isConnected();
            return connected;
        } catch (Exception e) {
            Log.e("AJUDEMAIS", e.getMessage());
        }
        return connected;
    }

    /**
     * Coloca no edit text uma máscara para celular.
     * @param editText
     */
    public void setMaskPhone(EditText editText){
        final MaskedTextChangedListener listener = new MaskedTextChangedListener(
                "([00]) [00000]-[0000]",
                true,
                editText,
                null,
                new MaskedTextChangedListener.ValueListener() {
                    @Override
                    public void onTextChanged(boolean maskFilled, @NonNull final String extractedValue) {
                        Log.d(MainActivity.class.getSimpleName(), extractedValue);
                        Log.d(MainActivity.class.getSimpleName(), String.valueOf(maskFilled));
                    }
                }
        );

        editText.addTextChangedListener(listener);
        editText.setOnFocusChangeListener(listener);
    }

    /**
     * Verifica que o campo contém um e-mail válido
     * @param email
     * @return
     */
    public boolean isEmailValid(String email)
    {
        String regExpn =
                "^(([\\w-]+\\.)+[\\w-]+|([a-zA-Z]{1}|[\\w-]{2,}))@"
                        +"((([0-1]?[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\.([0-1]?"
                        +"[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\."
                        +"([0-1]?[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\.([0-1]?"
                        +"[0-9]{1,2}|25[0-5]|2[0-4][0-9])){1}|"
                        +"([a-zA-Z]+[\\w-]+\\.)+[a-zA-Z]{2,4})$";

        Pattern pattern = Pattern.compile(regExpn, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(email);

        if(matcher.matches())
            return true;
        else
            return false;
    }


}
