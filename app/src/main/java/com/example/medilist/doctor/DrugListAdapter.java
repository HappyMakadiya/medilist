package com.example.medilist.doctor;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.example.medilist.R;
import java.util.ArrayList;

public class DrugListAdapter extends ArrayAdapter<DrugList> {
    private Context mcontext;
    private int mresource;
    DrugListAdapter(@NonNull Context context, int resource, @NonNull ArrayList<DrugList> objects) {
        super(context, resource, objects);
        mcontext = context;
        mresource = resource;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        String drugname = getItem(position).getDrug_Name();
        String drugquant = getItem(position).getDrug_Quantity();
        //DrugList dl = new DrugList(drugname,drugquant);
        LayoutInflater inflater = LayoutInflater.from(mcontext);
        convertView = inflater.inflate(mresource,parent,false);
        TextView tvname = (TextView) convertView.findViewById(R.id.tvDrugName);
        TextView tvquant = (TextView) convertView.findViewById(R.id.tvDrugQuant);
        tvname.setText(drugname);
        tvquant.setText(drugquant);
        return convertView;
    }
}
