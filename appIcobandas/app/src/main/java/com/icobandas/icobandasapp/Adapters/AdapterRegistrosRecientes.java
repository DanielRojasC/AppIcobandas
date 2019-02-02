package com.icobandas.icobandasapp.Adapters;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.icobandas.icobandasapp.Modelos.LoginJson;
import com.icobandas.icobandasapp.R;

import java.util.ArrayList;

/**
 * Created by ADMIN on 25/10/2018.
 */

public class AdapterRegistrosRecientes  extends RecyclerView.Adapter<AdapterRegistrosRecientes.Holder> {

    ArrayList<LoginJson> loginJson;

    private OnClickListener mlistener;
    public interface OnClickListener
    {
        void itemClick(int position, View itemView);
    }


    public AdapterRegistrosRecientes(ArrayList<LoginJson> loginJson)
    {
        this.loginJson = loginJson;
    }

    public void setMlistener(OnClickListener mlistener)
    {
        this.mlistener= mlistener;
    }


    @NonNull
    @Override
    public AdapterRegistrosRecientes.Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
    {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.itemlist_registros_recientes, parent, false);
        return new AdapterRegistrosRecientes.Holder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AdapterRegistrosRecientes.Holder holder, int position) {


        holder.fijarDatos(loginJson.get(position));

    }


    @Override
    public int getItemCount()
    {
        return loginJson.size();
    }



    public class Holder extends RecyclerView.ViewHolder
    {


        TextView txtFecha,txtParte,txtPlanta;
        public Holder(final View itemView)
        {
            super(itemView);

            itemView.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    if (mlistener!=null)
                    {
                        int position = getLayoutPosition();
                        if (position!=RecyclerView.NO_POSITION)
                        {
                            mlistener.itemClick(position,itemView);
                        }
                    }
                }
            });

            txtFecha  =itemView.findViewById(R.id.txtFecha);
            txtParte  = itemView.findViewById(R.id.txtParte);
            txtPlanta =itemView.findViewById(R.id.txtNombrePlanta);



        }

        public void fijarDatos(LoginJson loginJson)
        {

                txtFecha.setText(loginJson.getFechaRegistro());
                txtParte.setText(loginJson.getNombreTransportador());
                txtPlanta.setText(loginJson.getNameunido()+" - "+loginJson.getNameplanta());

        }
    }
}