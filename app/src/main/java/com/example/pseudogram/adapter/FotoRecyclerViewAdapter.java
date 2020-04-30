package com.example.pseudogram.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.pseudogram.R;
import com.example.pseudogram.model.Picture;

import java.util.List;
import java.util.function.Consumer;

public class FotoRecyclerViewAdapter extends RecyclerView.Adapter<FotoRecyclerViewAdapter.ViewHolder> {

    private final List<Picture> elementos;
    private final Consumer<Picture> onSelect;

    public FotoRecyclerViewAdapter(List<Picture> elementos, Consumer<Picture> onSelect) {
        this.elementos = elementos;
        this.onSelect = onSelect;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.fragment_list_foto, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int position) {
        viewHolder.setData(elementos.get(position));
    }

    @Override
    public int getItemCount() {
        return elementos.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder
            implements View.OnClickListener {

        private TextView tvTitulo;
        private TextView tvDescricao;
        private TextView tvPath; // TODO trocar por imagem

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
            tvTitulo = itemView.findViewById(R.id.txtTitulo);
            tvDescricao = itemView.findViewById(R.id.txtDescricao);
            tvPath = itemView.findViewById(R.id.txtPath);
        }

        private void setData(Picture picture) {
            tvTitulo.setText(picture.getTitle());
            tvDescricao.setText(picture.getDescription());
            tvPath.setText(picture.getPath());
        }
        public void onClick(View view) {
//            Toast.makeText(view.getContext(), "Você selecionou "
//                    + elementos.get(getLayoutPosition()).getNome(), Toast.LENGTH_LONG).
//                    show();
            onSelect.accept(elementos.get(getLayoutPosition()));
        }

    }

}
