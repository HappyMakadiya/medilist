package com.example.medilist.patient;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.medilist.R;
import com.example.medilist.ShowPdfViewerActivity;

import java.io.File;
import java.util.List;
import java.util.zip.Inflater;

public class ListrxAdapter extends RecyclerView.Adapter<ListrxAdapter.ListrxViewHolder> {
    Context mctx;
    List<ListDrRx> listmain;

    public ListrxAdapter(Context mctx, List<ListDrRx> listDrRxes) {
        this.mctx = mctx;
        this.listmain = listDrRxes;
    }

    @NonNull
    @Override
    public ListrxViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater minflater = LayoutInflater.from(mctx);
        View view = minflater.inflate(R.layout.card_listrx_patient,parent,false);
        ListrxViewHolder holder = new ListrxViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ListrxViewHolder holder, int position) {
        ListDrRx listDrRx = listmain.get(position);
        holder.tvdrname.setText(listDrRx.getDrName());
        holder.tvd.setText(listDrRx.getDay());
        holder.tvm.setText(listDrRx.getMonth());
        holder.tvy.setText(listDrRx.getYear());
        holder.btnshowpdf.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String uri = listmain.get(position).getRxURI();
                Intent intent = new Intent(mctx, ShowPdfViewerActivity.class);
                intent.putExtra("Rxuri",uri);
                mctx.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return listmain.size();
    }

    class ListrxViewHolder extends RecyclerView.ViewHolder{
        TextView tvdrname,tvm,tvd,tvy;
        Button btnshowpdf;
        public ListrxViewHolder(@NonNull View itemView) {
            super(itemView);
            tvdrname = itemView.findViewById(R.id.tvdrnamecv);
            tvm = itemView.findViewById(R.id.tvmonthcv);
            tvd = itemView.findViewById(R.id.tvdatecv);
            tvy = itemView.findViewById(R.id.tvyearcv);
            btnshowpdf = itemView.findViewById(R.id.btnshowpdfcv);
        }
    }
}
