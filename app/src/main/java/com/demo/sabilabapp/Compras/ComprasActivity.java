package com.demo.sabilabapp.Compras;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.demo.sabilabapp.Api.RetrofitClient;
import androidx.appcompat.app.AppCompatActivity;

import com.demo.sabilabapp.R;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ComprasActivity extends AppCompatActivity {
    //Button btnListar,btnListarPorFecha;
    //TextView txtDatos,txtDatos2;
    //EditText etxFecha;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_compras);
        //btnListar=findViewById(R.id.btnListar);
        //txtDatos=findViewById(R.id.txtDatos);
        //btnListarPorFecha=findViewById(R.id.btnListarPorFecha);
        //txtDatos2=findViewById(R.id.txtDatos2);
        //etxFecha=findViewById(R.id.etxFecha);
        /*btnListar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listar();
            }
        });
        btnListarPorFecha.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listarPorFecha(etxFecha.getText().toString());
            }
        });*/
    }

    /*private void listarPorFecha(String fecha) {
        Call<ComprasResponse> call=RetrofitClient.INSTANCE.getApiService().listarPorFecha(fecha);
        call.enqueue(new Callback<ComprasResponse>() {
            @Override
            public void onResponse(Call<ComprasResponse> call, Response<ComprasResponse> response) {
                try {
                    if(response.isSuccessful()){
                        ComprasResponse comprasResponse=response.body();
                        if(comprasResponse.getData().getResults().size()!=0){
                            List<Compras> compras=comprasResponse.getData().getResults();
                            txtDatos2.setText(compras.get(0).toString());
                        }
                    }

                }catch (Exception exception) {
                    Toast.makeText(ComprasActivity.this,exception.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ComprasResponse> call, Throwable t) {
                Toast.makeText(ComprasActivity.this, "Error de conexion", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void listar(){
        Call<ComprasResponse> call=RetrofitClient.INSTANCE.getApiService().listarCompras();
        call.enqueue(new Callback<ComprasResponse>() {
            @Override
            public void onResponse(Call<ComprasResponse> call, Response<ComprasResponse> response) {
                try {
                    if(response.isSuccessful()){
                       ComprasResponse comprasResponse=response.body();
                       if(comprasResponse.getData().getResults().size()!=0){
                           List<Compras> compras=comprasResponse.getData().getResults();
                           txtDatos.setText(compras.get(0).toString());
                       }
                    }

                }catch (Exception exception) {
                    Toast.makeText(ComprasActivity.this,exception.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ComprasResponse> call, Throwable t) {
                Toast.makeText(ComprasActivity.this, "Error de conexion", Toast.LENGTH_SHORT).show();
            }
        });
    }*/

}