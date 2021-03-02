package com.example.lajusta;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;
import com.example.lajusta.model.AvailableNode;
import com.example.lajusta.model.Image;

import java.util.ArrayList;

public class CustomAdapterListadoNodos extends BaseAdapter {
    private ArrayList<AvailableNode> nodos;
    private Context context;
    private int mResource;
    private int selectedIndex;
    private AvailableNode nodoSelec;
    private ActivitySeleccionarNodo activity;

    public CustomAdapterListadoNodos(ArrayList<AvailableNode> nodos,AvailableNode nodoSelec,Context context, int mResource, ActivitySeleccionarNodo activity){
        this.nodos=nodos;
        this.context=context;
        this.mResource=mResource;
        this.nodoSelec=nodoSelec;
        this.selectedIndex=-1;
        this.activity=activity;
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

        ImageView imagen = convertView.findViewById(R.id.imagenNodo);
        Image img =nodo.getNode().getImage();
        if(img!=null){
            String base64Str = img.getValue();
            if (base64Str != null) {
                String base64Image = base64Str.split(",")[1];
                byte[] decodedString = Base64.decode(base64Image, Base64.DEFAULT);
                Bitmap decoded = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
                imagen.setImageBitmap(decoded);
            }
        }

        //Guardar el nodo seleccionado
        if(selectedIndex == position){
            nodoSelec=nodo;
            activity.setNodoSeleccionado(nodo);
            radioButton.setChecked(true);
            activity.crearVisualizarNodo();

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
