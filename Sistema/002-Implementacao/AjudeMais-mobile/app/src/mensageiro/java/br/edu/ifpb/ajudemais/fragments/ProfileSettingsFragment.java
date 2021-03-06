package br.edu.ifpb.ajudemais.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import br.edu.ifpb.ajudemais.R;
import br.edu.ifpb.ajudemais.domain.Mensageiro;


public class ProfileSettingsFragment extends Fragment {

    private TextView tvNome;
    private TextView tvEmail;
    private TextView tvCpf;
    private TextView tvPhone;
    private View view;
    private Mensageiro mensageiro;

    public ProfileSettingsFragment() {
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);


    }

    /**
     * @param inflater
     * @param container
     * @param savedInstanceState
     * @return
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_profile_settings, container, false);
        setHasOptionsMenu(true);

        return view;
    }

    /**
     * @param view
     * @param savedInstanceState
     */
    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        tvNome = (TextView) getView().findViewById(R.id.tv_edit_nome);
        tvEmail = (TextView) getView().findViewById(R.id.tv_edit_email);
        tvPhone = (TextView) getView().findViewById(R.id.tv_edit_celular);
        tvCpf = (TextView) getView().findViewById(R.id.tv_edit_cpf);

        mensageiro = (Mensageiro) getArguments().getSerializable("Mensageiro");

        if (mensageiro != null) {
            tvPhone.setText(mensageiro.getTelefone());
            tvNome.setText(mensageiro.getNome());
            tvEmail.setText(mensageiro.getConta().getEmail());
            tvCpf.setText(mensageiro.getCpf());
        }



    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putSerializable("Mensageiro", mensageiro);

    }

    /**
     *
     */
    @Override
    public void onStart() {
        super.onStart();
    }

}
