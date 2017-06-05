package br.edu.ifpb.ajudemais.fragments;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;

import br.edu.ifpb.ajudemais.R;
import br.edu.ifpb.ajudemais.adapters.DisponibilidadeHorarioAdapter;
import br.edu.ifpb.ajudemais.asycnTasks.UpdateEstadoDonativoTask;
import br.edu.ifpb.ajudemais.domain.Donativo;
import br.edu.ifpb.ajudemais.domain.Endereco;
import br.edu.ifpb.ajudemais.domain.EstadoDoacao;
import br.edu.ifpb.ajudemais.enumarations.Estado;

/**
 * <p>
 * <b>{@link DonativoDetailFragment}</b>
 * </p>
 * <p>
 * Fragmento de detalhes para donativo.
 * <p>
 * <p>
 * </p>
 *
 * @author <a href="https://github.com/JoseRafael97">Rafael Feitosa</a>
 */

public class DonativoDetailFragment extends Fragment implements View.OnClickListener{

    private View view;
    private Donativo donativo;

    private TextView descricaoDonativo;
    private TextView nomeInstituicao;
    private TextView categoriaName;
    private TextView quantImages;
    private SeekBar seekBarImages;
    private TextView stateDoacao;
    private TextView descriptionStateDoacao;
    private EstadoDoacao estadoDoacao;
    private UpdateEstadoDonativoTask updateEstadoDonativoTask;

    private Button btnCancelDoacao;


    private RecyclerView recyclerView;
    private DisponibilidadeHorarioAdapter disponibilidadeHorarioAdapter;

    /**
     *
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_donativo_detail, container, false);

        Intent intentCampanha = getActivity().getIntent();
        donativo = (Donativo) intentCampanha.getSerializableExtra("Donativo");
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


        descricaoDonativo = (TextView) getView().findViewById(R.id.tv_description);
        nomeInstituicao = (TextView) getView().findViewById(R.id.tv_instituicao_name);
        categoriaName = (TextView) getView().findViewById(R.id.tv_categoria);
        descriptionStateDoacao = (TextView) getView().findViewById(R.id.tv_state);
        stateDoacao = (TextView) getView().findViewById(R.id.tv_donative_estado_lb);
        seekBarImages = (SeekBar) getView().findViewById(R.id.seekBar);
        quantImages = (TextView) getView().findViewById(R.id.tv_quant_images);
        btnCancelDoacao = (Button) getView().findViewById(R.id.btnCancelaDoacao);
        btnCancelDoacao.setOnClickListener(this);

        seekBarImages.setProgress(donativo.getFotosDonativo() != null ? donativo.getFotosDonativo().size() : 0);
        quantImages.setText(donativo.getFotosDonativo() != null ? donativo.getFotosDonativo().size() + "/3" : "0/3");

        descricaoDonativo.setText(donativo.getDescricao());
        nomeInstituicao.setText(getString(R.string.doado_to) + " " + donativo.getCategoria().getInstituicaoCaridade().getNome());
        categoriaName.setText("Categoria: " + donativo.getCategoria().getNome());

        recyclerView = (RecyclerView) view.findViewById(R.id.recycle_view_list);
        RecyclerView.LayoutManager layout = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(layout);

        disponibilidadeHorarioAdapter = new DisponibilidadeHorarioAdapter(donativo.getHorariosDisponiveis(), getContext());
        recyclerView.setAdapter(disponibilidadeHorarioAdapter);

        setAtrAddressIntoCard(donativo.getEndereco());
        validateAndSetStateDoacao();

    }

    /**
     * Valida o estado da doação e seta o estado na label.
     */
    private void validateAndSetStateDoacao(){
        for (EstadoDoacao estado : donativo.getEstadosDaDoacao()) {
            if (estado.getAtivo() != null && estado.getAtivo()) {
                if (!estado.getEstadoDoacao().name().equals(Estado.DISPONIBILIZADO)){
                    btnCancelDoacao.setVisibility(View.GONE);
                }
                if (estado.getEstadoDoacao().name().equals(Estado.CANCELADO)) {
                    stateDoacao.setBackground(getContext().getDrawable(R.drawable.screen_border_cancelado));
                }
                stateDoacao.setText(estado.getEstadoDoacao().name());
                this.estadoDoacao = estado;
            }
        }

    }


    /**
     * Seta endereço no cardview
     */
    private void setAtrAddressIntoCard(Endereco endereco) {
        CardView cardView = (CardView) getView().findViewById(R.id.componentAddress);
        ((TextView) cardView.findViewById(R.id.tv_logradouro_name)).setText(endereco.getLogradouro());
        ((TextView) cardView.findViewById(R.id.tv_bairro)).setText(endereco.getBairro());
        ((TextView) cardView.findViewById(R.id.tv_number)).setText(endereco.getNumero());
        ((TextView) cardView.findViewById(R.id.tv_cep_name)).setText(endereco.getCep());
        ((TextView) cardView.findViewById(R.id.tv_city)).setText(endereco.getLocalidade());
        ((TextView) cardView.findViewById(R.id.tv_uf_name)).setText(endereco.getUf());
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btnCancelaDoacao){
            for (EstadoDoacao estado : donativo.getEstadosDaDoacao()) {
                if (estado.getAtivo() != null && estado.getAtivo()) {
                    if (estado.getEstadoDoacao().name().equals(Estado.CANCELADO)){
                        btnCancelDoacao.setVisibility(View.GONE);
                    }
                    if (estado.getEstadoDoacao().name().equals(Estado.CANCELADO)) {
                        stateDoacao.setBackground(getContext().getDrawable(R.drawable.screen_border_cancelado));
                    }
                    stateDoacao.setText(estado.getEstadoDoacao().name());
                    this.estadoDoacao = estado;
                }
            }
        }
    }
}

