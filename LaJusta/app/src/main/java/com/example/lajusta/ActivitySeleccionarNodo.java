package com.example.lajusta;

import android.content.Context;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.lajusta.Interface.APICall;
import com.example.lajusta.model.APIManejo;
import com.example.lajusta.model.Nodo;

import org.osmdroid.api.IMapController;
import org.osmdroid.config.Configuration;
import org.osmdroid.tileprovider.tilesource.TileSourceFactory;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.ItemizedIconOverlay;
import org.osmdroid.views.overlay.ItemizedOverlayWithFocus;
import org.osmdroid.views.overlay.OverlayItem;
import org.osmdroid.views.overlay.mylocation.GpsMyLocationProvider;
import org.osmdroid.views.overlay.mylocation.MyLocationNewOverlay;

import java.util.ArrayList;
import java.util.ListIterator;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ActivitySeleccionarNodo extends AppCompatActivity {
    MapView map = null;
    private ArrayList<Nodo> Nodos;
    private String nodoSeleccionado;

    /* Fede, aca lo que se haría es obtener el nombre del titulo del nodo seleccionado y ponerlo en el string nodoSeleccionado,
    eso deberia de ya filtrar los resultados y visualizar el que se busca. Cualquier cosa, va a imprimir por debug algo seguro
    Esta todo bastante comentado, de que hace cada cosa.
       Dentro de la vista de los nodos, le puse un radiobutton, la idea seria que al hacer click, se muestre eso visualmente
       como seleccionado y se cambie dinamicamente a la ubicacion nueva del mapa. No se si lo hará al momento de cambiar el
       contenido de nodoSeleccionado */

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Context ctx = getApplicationContext();
        Configuration.getInstance().load(ctx, PreferenceManager.getDefaultSharedPreferences(ctx));
        setContentView(R.layout.activity_seleccionar_nodo);
        map = (MapView) findViewById(R.id.mapaSeleccionarNodos);
        map.setTileSource(TileSourceFactory.MAPNIK);

        IMapController mapController = map.getController();
        mapController.setZoom(16.0); //Realiza un zoom

        Toast.makeText(this.getApplicationContext(),"Espere mientras se obtienen los Nodos",Toast.LENGTH_SHORT).show();

        ActivitySeleccionarNodo estoMismo = this;

        APIManejo apiManejo = new APIManejo();
        APICall service = apiManejo.crearService();

        //Hace la consulta HTTP de forma asincronica, una vez que esté la respuesta, se ejecuta
        // el onResponse()
        service.getNodes().enqueue(new Callback<ArrayList<Nodo>>() {
            @Override
            public void onResponse(Call<ArrayList<Nodo>> call, Response<ArrayList<Nodo>> response) {
                //Crea la vista del listado de nodos
                Nodos = response.body();

                ArrayList<OverlayItem> nodosEntrega = new ArrayList<OverlayItem>(); //Array de overlays de los nodos de entrega

                for(int i = 0; i < Nodos.size(); i++) { //Recorre el response de nodos creando los GeoPoints y los añade a nodosEntrega con su nombre
                    GeoPoint geop = new GeoPoint(Nodos.get(i).getAddress().getLatitude(), Nodos.get(i).getAddress().getLongitude());
                    nodosEntrega.add(new OverlayItem(Nodos.get(i).getName(), Nodos.get(i).getName(), geop));
                }

                if (nodoSeleccionado == null) {
                    if(nodosEntrega.get(0).getPoint() != null) {
                        mapController.setCenter(nodosEntrega.get(0).getPoint()); //Si no hay un nodo seleccionado para centrar, se centra el primero que haya
                    } else {
                        System.out.println("No se obtuvo ningun nodo de entrega"); //Se imprime un mensaje de error. Indica un error interno, el usuario no deberia de llegar a esta instancia
                    }

                } else {
                    ListIterator it = nodosEntrega.listIterator();
                    GeoPoint nodoEncontrado = null;
                    while(nodoEncontrado == null && it.hasNext()) { //Recorre la lista usando un iterador mientras haya un elemento proximo en el iterador y el nodo buscado no haya sido encontrado aun
                        OverlayItem actual = (OverlayItem) it.next();
                        if(actual.getTitle().compareTo(nodoSeleccionado) == 0) { //Si coincida el titulo con el string "nodoSeleccionado", es el que estamos buscando
                            nodoEncontrado = (GeoPoint) actual.getPoint(); //Se asigna a nodoEncontrado
                        }
                    }
                    if (nodoEncontrado != null) {
                        mapController.setCenter(nodoEncontrado); //Setea el nodo encontrado anteriormente como el centro del mapa a mostrar
                    } else {
                        System.out.println("No se encontró el nodo solicitado por parametro"); //Se imprime un mensaje de error. Indica un error interno, el usuario no debería de llegar a esta instancia
                    }
                }

                final MyLocationNewOverlay myLocationOverlay = new MyLocationNewOverlay(new GpsMyLocationProvider(getApplicationContext()), map);
                map.getOverlays().add(myLocationOverlay);
                myLocationOverlay.enableMyLocation(); //Las 3 lineas estas deberian de mostrar la ubicacion actual

                ItemizedIconOverlay.OnItemGestureListener<OverlayItem> tap = new ItemizedIconOverlay.OnItemGestureListener<OverlayItem>() {
                    @Override
                    public boolean onItemSingleTapUp(int index, OverlayItem item) {
                        return false;
                    }

                    @Override
                    public boolean onItemLongPress(int index, OverlayItem item) {
                        return false;
                    }
                };
                ItemizedOverlayWithFocus<OverlayItem> capa = new ItemizedOverlayWithFocus<OverlayItem>(estoMismo, nodosEntrega, tap); //Crea una capa con los nodos sobre el mapa
                capa.setFocusItemsOnTap(true); //setea el valor para que se pueda hacer focus al tocarlos
                map.getOverlays().add(capa); //agrega la capa al mapa
            }

            @Override
            public void onFailure(Call<ArrayList<Nodo>> call, Throwable t) {
                System.out.println("No se obtuvieron los nodos");
            }
        });

    }

    public void onResume() {
        super.onResume();
        map.onResume();
    }

    public void onPause() {
        super.onPause();
        map.onPause();
    }
}