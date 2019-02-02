package com.icobandas.icobandasapp;


import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.icobandas.icobandasapp.Modelos.InfoCliente;

import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 */
public class AgregarCliente extends Fragment {

    EditText txtNit;
    EditText txtNombreCliente;
    EditText txtNombrePlanta;
    Spinner spinnerCiudades;
    EditText txtDireccion;
    Button btnGuardarCliente;
    String ciudad;



    ArrayList<String> listaCiudades= new ArrayList<>();

    View view;



    public AgregarCliente() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        view= inflater.inflate(R.layout.fragment_agregar_cliente, container, false);
        inicializar();

        listas();

        spinnerCiudades.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                ciudad= listaCiudades.get(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        btnGuardarCliente.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                InfoCliente infoCliente= new InfoCliente();

                String nombrePlanta=txtNombrePlanta.getText().toString();

                FragmentSeleccionarTransportador.listaClientes.add(infoCliente.getNameunido() + " - "+nombrePlanta);

                txtNombrePlanta.setText("");
                txtDireccion.setText("");
                txtNit.setText("");
                txtNombreCliente.setText("");
                Toast.makeText(getContext(), "Cliente Agregado Correctamente", Toast.LENGTH_SHORT).show();
            }
        });


        return view;
    }

    private void inicializar() {
        spinnerCiudades=view.findViewById(R.id.spinnerCiudades);
        txtDireccion=view.findViewById(R.id.txtDireccion);
        txtNit=view.findViewById(R.id.txtNit);
        txtNombreCliente=view.findViewById(R.id.txtNombreCliente);
        txtNombrePlanta=view.findViewById(R.id.txtNombrePlanta);
        btnGuardarCliente=view.findViewById(R.id.btnGuardarCliente);

    }
    public void listas()
    {

        listaCiudades.add("Bogotá");
        listaCiudades.add("Cali");
        listaCiudades.add("Medellín");
        listaCiudades.add("Popayán");
        listaCiudades.add("Bucaramanga");
        listaCiudades.add("Villavicencio");
        listaCiudades.add("Barranquilla");
        listaCiudades.add("Tunja");
        listaCiudades.add("Pasto");

        ArrayAdapter<String> adapterCiudades =new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_dropdown_item, listaCiudades);
        spinnerCiudades.setAdapter(adapterCiudades);


    }

}
