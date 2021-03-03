package com.example.lajusta;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.example.lajusta.Interface.APICall;
import com.example.lajusta.model.APIManejo;
import com.example.lajusta.model.AvailableNode;
import com.example.lajusta.model.Cart;
import com.example.lajusta.model.CartProduct;
import com.example.lajusta.model.General;
import com.example.lajusta.model.Nodo;
import com.example.lajusta.model.Token;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
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
import java.lang.reflect.Type;
import java.util.ArrayList;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ActivitySeleccionarNodo extends AppCompatActivity {
    MapView map = null;
    Button botonSeleccionarNodo = null;
    private ArrayList<Nodo> Nodos;
    private AvailableNode nodoSeleccionado;
    private ActivitySeleccionarNodo activity = this;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Context ctx = getApplicationContext();
        Configuration.getInstance().load(ctx, PreferenceManager.getDefaultSharedPreferences(ctx));
        setContentView(R.layout.activity_seleccionar_nodo);
        map = (MapView) findViewById(R.id.mapaSeleccionarNodos);
        map.setVisibility(View.INVISIBLE);
        map.setTileSource(TileSourceFactory.MAPNIK);
        ListView listado = findViewById(R.id.listadoNodo);
        botonSeleccionarNodo = findViewById(R.id.botonConfirmarNodo);
        ImageButton ib = findViewById(R.id.botonAtrasNodo);
        ib.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        SharedPreferences sharedPreferences = getSharedPreferences("shared preferences", MODE_PRIVATE);
        Gson gson = new Gson();
        String json = sharedPreferences.getString("general",null);
        Type type = new TypeToken<General>() {}.getType();
        General general = gson.fromJson(json,type);
        //Aca se arma el carrito para guardarlo en la base de datos
        json = sharedPreferences.getString("carrito",null);
        Type typeProdsCarrito = new TypeToken<Cart>() {}.getType();
        Cart cart = gson.fromJson(json,typeProdsCarrito);
        json = sharedPreferences.getString("usuarioToken","");
        Token usuarioLogin= gson.fromJson(json,Token.class);

        //Pasa los nodos disponibles de Array a ArrayList para poder manejar mejor el Adapter
        ArrayList<AvailableNode> nodos = new ArrayList<>();
        for(int i=0;i<general.getActiveNodes().length;i++){
            nodos.add(general.getActiveNodes()[i]);
        }
        CustomAdapterListadoNodos adapter = new CustomAdapterListadoNodos(nodos,nodoSeleccionado,this,R.layout.nodos, activity);
        listado.setAdapter(adapter);

        botonSeleccionarNodo.setOnClickListener(v -> {
            boolean SignedIn = sharedPreferences.getBoolean("SignedIn", false);
            VerificarToken verificarToken = new VerificarToken();
            if(SignedIn) {
                if(verificarToken.verificarToken(sharedPreferences)) {
                    //Va al login informando que fallo el token
                    Intent intent = new Intent(ActivitySeleccionarNodo.this, ActivityLogin.class);
                    startActivity(intent);
                }
            }
            if(nodoSeleccionado == null) {
                Toast.makeText(ActivitySeleccionarNodo.this,"Debe seleccionar un nodo de la lista",Toast.LENGTH_SHORT).show();
            } else {
                ib.setVisibility(View.GONE);
                ProgressBar pb = findViewById(R.id.progressBar);
                botonSeleccionarNodo.setVisibility(View.GONE);
                pb.setVisibility(View.VISIBLE);
                cart.setNodeDate(nodoSeleccionado);
                APIManejo apiManejo = new APIManejo();
                APICall service = apiManejo.crearService();
                service.saveCart(cart, "Bearer " + usuarioLogin.getValue()).enqueue(new Callback<Cart>() {
                    @Override
                    public void onResponse(Call<Cart> call, Response<Cart> response) {
                        if(response.code()==200){
                            String json = gson.toJson(cart);
                            SharedPreferences.Editor editor = sharedPreferences.edit();
                            editor.putString("carrito",json);
                            ArrayList<CartProduct> reiniciarProductos = new ArrayList<>();
                            json = gson.toJson(reiniciarProductos);
                            editor.putString("compras",json);
                            editor.commit();
                            Intent i = new Intent(ActivitySeleccionarNodo.this, ActivityTicket.class);
                            startActivity(i);
                        }
                        else{
                            if(response.code()==409){
                                Intent i = new Intent(ActivitySeleccionarNodo.this,ActivityFaltaStock.class);
                                i.putExtra("errorBody",response.errorBody().toString());
                                startActivity(i);
                            }
                            else{
                                Toast.makeText(ActivitySeleccionarNodo.this,"No se pudo realizar su compra",Toast.LENGTH_LONG).show();
                                Intent i = new Intent(ActivitySeleccionarNodo.this, ActivityMain.class);
                                startActivity(i);
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<Cart> call, Throwable t) {
                        Toast.makeText(ActivitySeleccionarNodo.this,"Error en el servidor",Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });

        //Esto es para que se pueda seleccionar solo 1, sino se podian apretar mas de 1
        listado.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                adapter.setSelectedIndex(position);
                adapter.notifyDataSetChanged();
                if(map.getVisibility() == View.INVISIBLE) {
                    map.setVisibility(View.VISIBLE);
                }
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

    public void crearVisualizarNodo() {
        IMapController mapController = map.getController();
        mapController.setZoom(16.0); //Realiza un zoom

        ActivitySeleccionarNodo estoMismo = this;
        GeoPoint geoPoint = new GeoPoint(nodoSeleccionado.getNode().getAddress().getLatitude(), nodoSeleccionado.getNode().getAddress().getLongitude());
        mapController.setCenter(geoPoint);
        ArrayList<OverlayItem> nodosVisualizados = new ArrayList<OverlayItem>(); //Array de overlays de los nodos de entrega
        nodosVisualizados.add(new OverlayItem(nodoSeleccionado.getNode().getName(), nodoSeleccionado.getNode().getName(), geoPoint));
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
        ItemizedOverlayWithFocus<OverlayItem> capa = new ItemizedOverlayWithFocus<OverlayItem>(estoMismo, nodosVisualizados, tap); //Crea una capa con los nodos sobre el mapa
        capa.setFocusItemsOnTap(true); //setea el valor para que se pueda hacer focus al tocarlos
        map.getOverlays().add(capa); //agrega la capa al mapa

    }
    public void setNodoSeleccionado(AvailableNode nodoSeleccionado) {
        this.nodoSeleccionado=nodoSeleccionado;
    }
}