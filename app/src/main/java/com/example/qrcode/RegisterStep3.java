package com.example.qrcode;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.example.qrcode.databinding.RegisterStep3Binding;
import com.example.qrcode.databinding.RegisterStep3Binding;
import com.example.qrcode.retrofit.APIServices;
import com.example.qrcode.retrofit.RetrofitInstance;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import retrofit2.Call;
import com.example.qrcode.model.Response;
import retrofit2.Callback;

public class RegisterStep3 extends AppCompatActivity {

    private RegisterStep3Binding binding;

    private final APIServices apiServices = RetrofitInstance.getInstance().create(APIServices.class);

    private EditText eTxtEmail;
    private EditText eTxtPhone;
    private Button btnFinalStep;

    private String qrCitizenData;

    private String qrUnitData;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = RegisterStep3Binding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        Intent i = getIntent();
        qrCitizenData = i.getStringExtra("qrCitizenData");
        qrUnitData = i.getStringExtra("qrUnitData");

        viewInit();
        viewEventInit();
    }

    private void viewEventInit() {
        btnFinalStep.setOnClickListener(view -> {
            String addData = eTxtPhone.getText().toString() + "|" + eTxtEmail.getText().toString();

            apiServices.createCitizen(qrCitizenData, qrUnitData, addData)
            .enqueue(new Callback<Response>() {
                @Override
                public void onResponse(Call<Response> call, retrofit2.Response<Response> response) {
//                    Log.e("TAG", "Response: " + response.body());
                    if(response.body().status == 200) {
                        Toast.makeText(RegisterStep3.this, response.body().toString() + ". Mật khẩu mặc định là 123456!", Toast.LENGTH_LONG).show();
                    } else if(response.body().status == 409) {
                        Toast.makeText(RegisterStep3.this, response.body().toString(), Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<Response> call, Throwable t) {
                    Log.e("TAG", "onFailure: " + t.toString() );
                    // Log error here since request failed
                }
            });

       });
    }

    private void viewInit() {
        eTxtEmail = findViewById(R.id.eTxtEmail);
        eTxtPhone = findViewById(R.id.eTxtPhone);
        btnFinalStep = findViewById(R.id.btnFinalStep);
    }

}
