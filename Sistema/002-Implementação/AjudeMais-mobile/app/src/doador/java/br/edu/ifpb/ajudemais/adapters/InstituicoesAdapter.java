package br.edu.ifpb.ajudemais.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import br.edu.ifpb.ajudemais.R;
import br.edu.ifpb.ajudemais.domain.InstituicaoCaridade;

/**
 * Created by Franck Aragão on 4/27/17.
 */
public class InstituicoesAdapter extends RecyclerView.Adapter<InstituicoesAdapter.ViewHolder>{

    private List<InstituicaoCaridade> instituicoes;
    private Context context;

    /**
     *
     * @param instituicoes
     * @param context
     */
    public InstituicoesAdapter(List<InstituicaoCaridade> instituicoes, Context context) {
        this.instituicoes = instituicoes;
        this.context = context;

    }

    /**
     *
     * @param parent
     * @param viewType
     * @return
     */
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_main_search_inst, parent, false);
        return new ViewHolder(view);
    }

    /**
     *
     * @param holder
     * @param position
     */
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.nomeInstituicao.setText(instituicoes.get(position).getNome());
        holder.descricaoInstituicao.setText(instituicoes.get(position).getDescricao());

    }

    /**
     *
     * @return
     */
    @Override
    public int getItemCount() {
        if(instituicoes == null){
            instituicoes = new ArrayList<>();
        }
        return instituicoes.size();    }

    /**
     *
     */
    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView nomeInstituicao;
        TextView descricaoInstituicao;

        public ViewHolder(View itemView) {
            super(itemView);
            nomeInstituicao = (TextView) itemView.findViewById(R.id.tv_instituicao_nome);
            descricaoInstituicao = (TextView) itemView.findViewById(R.id.tv_instituicao_description);
        }
    }
}
