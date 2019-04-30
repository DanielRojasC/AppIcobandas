package com.icobandas.icobandasapp.Adapters;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.database.sqlite.SQLiteDatabase;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.icobandas.icobandasapp.Database.DbHelper;
import com.icobandas.icobandasapp.Entities.RegistrosRecientesEntities;
import com.icobandas.icobandasapp.FragmentRegistrosRecientes;
import com.icobandas.icobandasapp.MainActivity;
import com.icobandas.icobandasapp.R;

import java.nio.channels.AlreadyBoundException;
import java.util.ArrayList;

/**
 * Created by ADMIN on 25/10/2018.
 */

public class AdapterRegistrosRecientesOffline  extends RecyclerView.Adapter<AdapterRegistrosRecientesOffline.Holder> {

    ArrayList<RegistrosRecientesEntities> registrosRecienteEntities;
    AlertDialog.Builder alerta;


    private OnClickListener mlistener;
    public interface OnClickListener
    {
        void itemClick(int position, View itemView);
    }

    Context context;

    DbHelper dbHelper;

    public AdapterRegistrosRecientesOffline(ArrayList<RegistrosRecientesEntities> registrosRecienteEntities, Context context)
    {
        this.registrosRecienteEntities = registrosRecienteEntities;
        this.context=context;
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

        public Switch swtichRegistroActivo;
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
            swtichRegistroActivo=itemView.findViewById(R.id.switchActivarDesactivarRegistro);

        }

        public void fijarDatos(final RegistrosRecientesEntities registrosRecientesEntities)
        {
            dbHelper = new DbHelper(context,"prueba", null ,1);
            SQLiteDatabase db = dbHelper.getReadableDatabase();
            alerta= new AlertDialog.Builder(context);
            if(!registrosRecientesEntities.getRegistroActivado().equals("0"))
            {
                txtFecha.setText(registrosRecientesEntities.getFechaRegistro());
                txtParte.setText(registrosRecientesEntities.getNombreTransportador());
                txtPlanta.setText(registrosRecientesEntities.getNombrePlanta());
                txtCliente.setText(registrosRecientesEntities.getNombreCliente());
                if(registrosRecientesEntities.getRegistroActivado().equals("1"))
                {
                    swtichRegistroActivo.setChecked(true);
                }
            }

            swtichRegistroActivo.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(!swtichRegistroActivo.isChecked())
                    {
                        alerta.setTitle("ICOBANDAS S.A dice:");
                        alerta.setMessage("¿Desea desactivar este registro?");
                        alerta.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                                FragmentRegistrosRecientes.actualizarActivado(registrosRecientesEntities.getIdRegistro(),"0");
                            }
                        });
                        alerta.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                                swtichRegistroActivo.setChecked(true);
                            }
                        });
                        alerta.create();

                        alerta.show();

                    }
                    else
                    {
                        alerta.setTitle("ICOBANDAS S.A dice:");
                        alerta.setMessage("¿Desea reactivar este registro?");
                        alerta.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                                FragmentRegistrosRecientes.actualizarActivado(registrosRecientesEntities.getIdRegistro(),"1");
                            }
                        });
                        alerta.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                                swtichRegistroActivo.setChecked(false);
                            }
                        });
                        alerta.create();

                        alerta.show();

                    }

                }
            });


        }
    }
}