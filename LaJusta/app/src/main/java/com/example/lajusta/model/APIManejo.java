package com.example.lajusta.model;

import com.example.lajusta.Interface.APICall;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

import retrofit2.Retrofit;
import retrofit2.converter.jackson.JacksonConverterFactory;

public class APIManejo {

    public APICall crearService(){
        //Creacion del objeto mapper
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

        //instanciacion del retrofit con los parametros correspondientes
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://ec2-3-233-234-13.compute-1.amazonaws.com/swagger-ui/index.html/")
                .addConverterFactory(JacksonConverterFactory.create(mapper))
                .build();

        //inicia el servicio, ya se puede consumir
       return retrofit.create(APICall.class);
    }
}
