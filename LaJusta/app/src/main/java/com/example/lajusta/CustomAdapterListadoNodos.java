package com.example.lajusta;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RadioButton;
import android.widget.TextView;

import com.example.lajusta.model.AvailableNode;

import java.util.ArrayList;

public class CustomAdapterListadoNodos extends BaseAdapter {
    private ArrayList<AvailableNode> nodos;
    private Context context;
    private int mResource;
    private int selectedIndex;
    private AvailableNode nodoSeleccionado;

    public CustomAdapterListadoNodos(ArrayList<AvailableNode> nodos,AvailableNode nodoSeleccionado,Context context, int mResource){
        this.nodos=nodos;
        this.context=context;
        this.mResource=mResource;
        this.nodoSeleccionado=nodoSeleccionado;
        this.selectedIndex=-1;
    }

    @Override
    public int getCount() {
        return nodos.size();
    }

    @Override
    public AvailableNode getItem(int position) {
        return nodos.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        AvailableNode nodo = getItem(position);

        LayoutInflater inflater = (LayoutInflater) this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        convertView = inflater.inflate(R.layout.nodos,null);

        TextView nombreNodo = convertView.findViewById(R.id.nombreNodo);
        nombreNodo.setText(nodo.getNode().getName());
        TextView descNodo = convertView.findViewById(R.id.descripcionNodo);
        descNodo.setText(nodo.getNode().getDescription());

        RadioButton radioButton = convertView.findViewById(R.id.radioSeleccionNodo);

        //Guardar el nodo seleccionado
        if(selectedIndex == position){
            nodoSeleccionado=nodo;
            radioButton.setChecked(true);
        }
        else{
            radioButton.setChecked(false);
        }

        return convertView;

    }

    public void setSelectedIndex(int index){
        selectedIndex = index;
    }
}