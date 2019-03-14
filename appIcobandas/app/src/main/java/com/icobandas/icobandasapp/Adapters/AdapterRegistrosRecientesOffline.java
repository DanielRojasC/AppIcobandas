package com.icobandas.icobandasapp.Adapters;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.icobandas.icobandasapp.Entities.RegistrosRecientesEntities;
import com.icobandas.icobandasapp.R;

import java.util.ArrayList;

/**
 * Created by ADMIN on 25/10/2018.
 */

public class AdapterRegistrosRecientesOffline  extends RecyclerView.Adapter<AdapterRegistrosRecientesOffline.Holder> {

    ArrayList<RegistrosRecientesEntities> registrosRecienteEntities;

    private OnClickListener mlistener;
    public interface OnClickListener
    {
        void itemClick(int position, View itemView);
    }


    public AdapterRegistrosRecientesOffline(ArrayList<RegistrosRecientesEntities> registrosRecienteEntities)
    {
        this.registrosRecienteEntities = registrosRecienteEntities;
    }

    public void setMlistener(OnClickListener mlistener)
    {
        this.mlistener= mlistener;
    }


    @NonNull
    @Override
    public AdapterRegistrosRecientesOffline.Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
    {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.itemlist_registros_recientes, parent, false);
        return new AdapterRegistrosRecientesOffline.Holder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AdapterRegistrosRecientesOffline.Holder holder, int position) {


        holder.fijarDatos(registrosRecienteEntities.get(position));

    }


    @Override
    public int getItemCount()
    {
        return registrosRecienteEntities.size();
    }



    public class Holder extends RecyclerView.ViewHolder
    {


        TextView txtFecha,txtParte,txtPlanta, txtCliente;
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
            txtCliente=itemView.findViewById(R.id.txtNombreCliente);



        }

        public void fijarDatos(RegistrosRecientesEntities registrosRecientesEntities)
        {

            txtFecha.setText(registrosRecientesEntities.getFechaRegistro());
            txtParte.setText(registrosRecientesEntities.getNombreTransportador());
            txtPlanta.setText(registrosRecientesEntities.getNombrePlanta());
            txtCliente.setText(registrosRecientesEntities.getNombreCliente());

        }
    }
}