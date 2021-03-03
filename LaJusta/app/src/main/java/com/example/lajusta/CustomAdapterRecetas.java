package com.example.lajusta;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.example.lajusta.model.Receta;
import java.util.ArrayList;

public class CustomAdapterRecetas extends ArrayAdapter<Receta> implements Filterable {

    private Context mContext;
    private int mResource;
    private ArrayList<Receta> recetas;
    private ArrayList<Receta> recetaFilter;

    public CustomAdapterRecetas(@NonNull Context context, int resource, @NonNull ArrayList<Receta> objects) {
        super(context, resource, objects);
        mContext = context;
        mResource = resource;
        recetas=objects;
        recetaFilter = objects;
    }

    @Override
    public int getCount() {
        return recetaFilter.size();
    }

    @Nullable
    @Override
    public Receta getItem(int position) {
        return recetaFilter.get(position);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        Receta receta = getItem(position);


        LayoutInflater inflater = LayoutInflater.from(mContext);
        convertView = inflater.inflate(mResource, parent, false);

        TextView nombreT = convertView.findViewById(R.id.nombreReceta);
        TextView tiempoT = convertView.findViewById(R.id.tiempoPreparacion);
        TextView preparacionT = convertView.findViewById(R.id.preparacionReceta);
        TextView ingredientesT = convertView.findViewById(R.id.ingredientes);
        ImageView imagen = convertView.findViewById(R.id.imagen);
        imagen.setImageResource(receta.getImagen());
        nombreT.setText(receta.getNombre());
        tiempoT.setText(receta.getTiempoPreparacion());
        preparacionT.setText(receta.getPreparacion());
        String listadoIngred="";
        for(String ingred: receta.getIngredientes()){
            listadoIngred+=ingred+",";
        }
        listadoIngred=listadoIngred.substring(0,listadoIngred.length()-1);
        ingredientesT.setText(listadoIngred);


        return convertView;

    }

    @Override
    public Filter getFilter() {
        Filter filter = new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                FilterResults filterResults = new FilterResults();
                if(constraint == null || constraint.length() == 0){
                    filterResults.count = recetas.size();
                    filterResults.values = recetas;
                }
                else {
                    String searchStr = constraint.toString().toLowerCase();
                    String[] parts = searchStr.split(",");
                    ArrayList<Receta> resultData = new ArrayList<>();
                    for (Receta r : recetas) {
                        ArrayList<String> ingredientes = r.getIngredientes();
                        int contador = 0;
                        for (String ingrediente : ingredientes) {
                            for (String s : parts) {
                                if (ingrediente.toLowerCase().contains(s)) {
                                    contador++;
                                }
                            }
                        }
                        if(contador==parts.length){
                            resultData.add(r);
                        }
                    }
                    filterResults.count= resultData.size();
                    filterResults.values = resultData;

                }

                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                if (results.count == 0) {
                    notifyDataSetInvalidated();
                } else {
                    notifyDataSetChanged();
                }
                recetaFilter= (ArrayList<Receta>) results.values;
            }
        };
        return filter;
    }
}
