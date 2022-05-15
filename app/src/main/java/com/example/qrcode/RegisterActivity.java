package com.example.qrcode;

import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import com.example.qrcode.databinding.RegisterFormBinding;
import com.example.qrcode.retrofit.APIServices;
import com.example.qrcode.retrofit.RetrofitInstance;
import com.google.gson.Gson;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RegisterActivity extends AppCompatActivity {

    private RegisterFormBinding binding;

    private final APIServices apiServices = RetrofitInstance.getInstance().create(APIServices.class);

    private EditText eTxtEmail;
    private Button btnFinalStep;

    private String qrData;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = RegisterFormBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        Intent i = getIntent();
        qrData = i.getStringExtra("qrCode");
        Toast.makeText(RegisterActivity.this, qrData, Toast.LENGTH_SHORT).show();

        viewInit();
        viewEventInit();
    }

    private void viewEventInit() {
        btnFinalStep.setOnClickListener(view -> {
            Call call = apiServices.createCitizen(qrData, eTxtEmail.getText().toString());

            call.enqueue(new Callback() {
                @Override
                public void onResponse(Call call, Response response) {
                    Log.e("TAG", "response 33: " + new Gson().toJson(response.body()) );
                }

                @Override
                public void onFailure(Call call, Throwable t) {
                    Log.e("TAG", "onFailure: " + t.toString() );
                    // Log error here since request failed
                }
            });
        });
    }

    private void viewInit() {
        eTxtEmail = findViewById(R.id.eTxtEmail);
        btnFinalStep = findViewById(R.id.btnFinalStep);
    }

}
