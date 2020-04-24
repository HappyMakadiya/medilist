package com.example.medilist.doctor;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.medilist.R;
import java.util.List;

public class DrugListAdapter  extends RecyclerView.Adapter<DrugListAdapter.DrugListViewHolder> {

    Context mctx;
    List<DrugList> listmain;

    public DrugListAdapter(Context mctx, List<DrugList> listmain) {
        this.mctx = mctx;
        this.listmain = listmain;
    }
    @NonNull
    @Override
    public DrugListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater minflater = LayoutInflater.from(mctx);
        View view = minflater.inflate(R.layout.adapter_drug_layout,parent,false);
        DrugListAdapter.DrugListViewHolder holder = new DrugListAdapter.DrugListViewHolder(view);
        return holder;
    }
    @Override
    public void onBindViewHolder(@NonNull DrugListViewHolder holder, int position) {
        DrugList drugList = listmain.get(position);
        holder.tvDis.setText(drugList.getDiseaceName());
        holder.tvDrugName.setText(drugList.getDrugName());
        holder.tvDrugType.setText(drugList.getDrugType());
        holder.tvDrugQuant.setText(drugList.getDrugQuantity().concat("Qnt"));
        holder.tvDrugDirec.setText(drugList.getDrugDirec());
        holder.tvDrugFreq.setText(drugList.getDrugFreq());
        holder.btndelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listmain.remove(position);
                notifyItemRemoved(position);
                notifyItemRangeChanged(position,listmain.size());
            }
        });
    }
    @Override
    public int getItemCount() {
        return listmain.size();
    }
    public class DrugListViewHolder extends RecyclerView.ViewHolder {

        TextView tvDis,tvDrugName,tvDrugType,tvDrugQuant,tvDrugDirec,tvDrugFreq;
        Button btndelete;
        public DrugListViewHolder(@NonNull View itemView) {
            super(itemView);
            tvDis = itemView.findViewById(R.id.tvDis);
            tvDrugName = itemView.findViewById(R.id.tvDrugName);
            tvDrugType = itemView.findViewById(R.id.tvDrugType);
            tvDrugQuant = itemView.findViewById(R.id.tvDrugQuant);
            tvDrugDirec = itemView.findViewById(R.id.tvDrugDirec);
            tvDrugFreq = itemView.findViewById(R.id.tvDrugFreq);
            btndelete = itemView.findViewById(R.id.btndeletelist);
        }
    }
}