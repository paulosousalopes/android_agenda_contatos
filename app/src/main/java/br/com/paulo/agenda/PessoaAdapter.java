package br.com.paulo.agenda;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

/**
 * Created by catolica on 25/09/16.
 */
public class PessoaAdapter extends RecyclerView.Adapter<PessoaAdapter.PessoaViewHolder> {
    private static final String TAG = "recyclerview";
    private Context context;
    private LayoutInflater layoutInflater;
    private List<Pessoa> pessoas;
    private PessoaOnClickListener pessoaOnClickListener;

    public PessoaAdapter(Context context, List<Pessoa> pessoas, PessoaOnClickListener listener) {
        this.pessoas = pessoas;
        this.context = context;
        this.pessoaOnClickListener = listener;
        layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        Log.d(TAG, "PessoaAdapter");
    }

    @Override
    public PessoaViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = layoutInflater.inflate(R.layout.list_item_pessoa, viewGroup, false);
        PessoaViewHolder pessoaViewHolder = new PessoaViewHolder(view);
        Log.d(TAG, "onCreateViewHolder");
        return pessoaViewHolder;
    }


    @Override
    public void onBindViewHolder(final PessoaViewHolder holder, final int position) {
        holder.name.setText(pessoas.get(position).getName());
        holder.email.setText(pessoas.get(position).getEmail());
        holder.phone.setText(pessoas.get(position).getPhone());

        Log.d(TAG, "onCreateViewHolder");
        if (pessoaOnClickListener != null) {
            holder.itemView.setOnClickListener(new View.OnClickListener(){
               @Override
                public void onClick(View view) {
                   pessoaOnClickListener.onClickEvent( holder.itemView, position);
               }
            });
        }
    }

    public interface PessoaOnClickListener {
        public void onClickEvent(View view, int index);
    }

    @Override
    public int getItemCount() {
        Log.d(TAG, "getItemCount");
        return this.pessoas.size();
    }

    public static class PessoaViewHolder extends RecyclerView.ViewHolder {
        public TextView name, email, phone;

        public PessoaViewHolder(View itemView) {
            super(itemView);

            name = (TextView) itemView.findViewById(R.id.textViewNameRecycler);
            email = (TextView) itemView.findViewById(R.id.textViewEmailRecycler);
            phone = (TextView) itemView.findViewById(R.id.textViewPhoneRecycler);
        }
    }
}


