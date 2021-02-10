package com.example.lajusta;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.preference.PreferenceManager;

import org.osmdroid.api.IMapController;
import org.osmdroid.config.Configuration;
import org.osmdroid.tileprovider.tilesource.TileSourceFactory;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.ItemizedIconOverlay;
import org.osmdroid.views.overlay.ItemizedOverlayWithFocus;
import org.osmdroid.views.overlay.Overlay;
import org.osmdroid.views.overlay.OverlayItem;
import org.osmdroid.views.overlay.mylocation.GpsMyLocationProvider;
import org.osmdroid.views.overlay.mylocation.MyLocationNewOverlay;

import java.util.ArrayList;
import java.util.ListIterator;

public class ActivityMapaNodos extends Activity {
    MapView map = null;
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Context ctx = getApplicationContext();
        Configuration.getInstance().load(ctx, PreferenceManager.getDefaultSharedPreferences(ctx));
        setContentView(R.layout.mapa_osmdroid);
        map = (MapView) findViewById(R.id.mapaNodos);
        map.setTileSource(TileSourceFactory.MAPNIK);

        IMapController mapController = map.getController();
        mapController.setZoom(16.0); //Realiza un zoom

        //GeoPoint con los nodos cargados
        GeoPoint nodoLaHormiguera = new GeoPoint(-34.9228948,-57.9422703);
        GeoPoint nodoCNCT = new GeoPoint(-35.0890685,-57.9246753); //Las coordenadas estan mal. No tengo idea donde queda el nodo
        GeoPoint nodoADULP = new GeoPoint(-34.9094548,-57.9631481);
        GeoPoint nodoATULP = new GeoPoint(-34.913084,-57.9669367);
        GeoPoint nodoFacultadAgronomia = new GeoPoint(-34.910088,-57.938226);
        GeoPoint nodoUBEvita = new GeoPoint(-34.8920261,-57.9887553);
        GeoPoint nodoCasaBelen = new GeoPoint(-35.0067757,-57.95016); //Las coordenadas estan mal. No tengo idea donde queda el nodo
        GeoPoint nodoClubGorina = new GeoPoint(-34.9088062,-58.0564834); // idem nodo anterior
        GeoPoint nodoCDVillaElisa = new GeoPoint(-34.8521699,-58.0875426);

        ArrayList<OverlayItem> nodosEntrega = new ArrayList<OverlayItem>(); //Array de overlays de los nodos de entrega
        nodosEntrega.add(new OverlayItem("Nodo La Hormiguera", "Nodo La Hormiguera", nodoLaHormiguera));
        nodosEntrega.add(new OverlayItem("Nodo CNCT", "Nodo CNCT", nodoCNCT));
        nodosEntrega.add(new OverlayItem("Nodo ADULP", "Nodo ADULP", nodoADULP));
        nodosEntrega.add(new OverlayItem("Nodo ATULP", "Nodo ATULP", nodoATULP));
        nodosEntrega.add(new OverlayItem("Nodo Facultad de Agronomia UNLP", "Nodo Facultad de Agronomia UNLP", nodoFacultadAgronomia));
        nodosEntrega.add(new OverlayItem("Nodo Unidad Basica 'Compañera Evita'", "Nodo Unidad Basica 'Compañera Evita'", nodoUBEvita));
        nodosEntrega.add(new OverlayItem("Nodo Arturo Seguí: casa de Belén", "Nodo Arturo Seguí: casa de Belén", nodoCasaBelen));
        nodosEntrega.add(new OverlayItem("Nodo Club de Gorina", "Nodo Club de Gorina", nodoClubGorina));
        nodosEntrega.add(new OverlayItem("Nodo Club Deportivo Villa Elisa", "Nodo Club Deportivo Villa Elisa", nodoCDVillaElisa));

        String nodoSeleccionado = getIntent().getStringExtra("EXTRA_NOMBRE_NODO");
        if (nodoSeleccionado == null) {
            mapController.setCenter(nodoLaHormiguera); //Setea el nodo La Hormiguera como el default a mostrar
        } else { //Busca en la lista de OverlayItems por uno que contenga el titulo igual al string "nodoSeleccionado"
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
                System.out.println("No se encontró el nodo solicitado"); //Se imprime un mensaje de error. Indica un error interno, el usuario no debería de llegar aqui
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
        ItemizedOverlayWithFocus<OverlayItem> capa = new ItemizedOverlayWithFocus<OverlayItem>(this, nodosEntrega, tap); //Crea una capa con los nodos sobre el mapa
        capa.setFocusItemsOnTap(true); //setea el valor para que se pueda hacer focus al tocarlos
        map.getOverlays().add(capa); //agrega la capa al mapa

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