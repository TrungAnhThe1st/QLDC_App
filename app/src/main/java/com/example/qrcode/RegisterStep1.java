package com.example.qrcode;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import com.budiyev.android.codescanner.AutoFocusMode;
import com.budiyev.android.codescanner.CodeScanner;
import com.budiyev.android.codescanner.DecodeCallback;
import com.budiyev.android.codescanner.ScanMode;
import androidx.appcompat.app.AppCompatActivity;

import android.view.Menu;
import android.view.MenuItem;
import com.example.qrcode.databinding.RegisterStep1Binding;
import com.example.qrcode.model.Response;
import com.example.qrcode.retrofit.APIServices;
import com.example.qrcode.retrofit.RetrofitInstance;
import com.google.zxing.Result;

import retrofit2.Call;
import retrofit2.Callback;

public class RegisterStep1 extends AppCompatActivity {
    private RegisterStep1Binding binding;

    private String qrCitizenData;

    private final int CAMERA_REQUEST_CODE = 101;

    private CodeScanner codeScanner;

    private final APIServices apiServices = RetrofitInstance.getInstance().create(APIServices.class);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = RegisterStep1Binding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        codeScanner = new CodeScanner(this, this.findViewById(R.id.scanner_view));

        viewEventInit();
        setupPermission();
        codeScanner();
    }

    private void viewEventInit() {
        findViewById(R.id.btnNxtStep).setOnClickListener(view -> {
//            if(qrCitizenData != null && !qrCitizenData.isEmpty()) {
//                Intent i = new Intent(RegisterStep1.this, RegisterStep2.class);
//                i.putExtra("qrCitizenData", qrCitizenData);
//                startActivity(i);
//            }
//            else {
//                Toast.makeText(RegisterStep1.this, "Không có dữ liệu QR, mời quét lại", Toast.LENGTH_SHORT).show();
//            }

            apiServices.scanQR(qrCitizenData)
                    .enqueue(new Callback<Response>() {
                        @Override
                        public void onResponse(Call<Response> call, retrofit2.Response<Response> response) {
                            Log.e("TAG", "Response: " + response.body());
                            Toast.makeText(RegisterStep1.this, response.body().toString(), Toast.LENGTH_LONG).show();

                        }

                        @Override
                        public void onFailure(Call<Response> call, Throwable t) {
                            Log.e("TAG", "onFailure: " + t.toString() );
                            // Log error here since request failed
                        }
                    });
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void codeScanner() {
        codeScanner.setCamera(CodeScanner.CAMERA_BACK);
        codeScanner.setFormats(CodeScanner.ALL_FORMATS);
        codeScanner.setAutoFocusMode(AutoFocusMode.SAFE);
        codeScanner.setScanMode(ScanMode.CONTINUOUS);
        codeScanner.setAutoFocusEnabled(true);
        codeScanner.setFlashEnabled(false);
        codeScanner.setDecodeCallback(new DecodeCallback() {
            @Override
            public void onDecoded(@NonNull Result result) {
                runOnUiThread(() -> {
                    qrCitizenData = result.getText();
                    ((TextView)findViewById(R.id.txtData)).setText("Thông tin của bạn: " + result.getText());
                });
            }
        });
        codeScanner.setErrorCallback(exception -> {
            Log.e("main", exception.getMessage());
        });

        this.findViewById(R.id.scanner_view).setOnClickListener(view -> {
            codeScanner.startPreview();
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        codeScanner.startPreview();
    }

    @Override
    protected void onPause() {
        codeScanner.releaseResources();
        super.onPause();
    }

    private void setupPermission(){
        int permission = ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA);

        if(permission != PackageManager.PERMISSION_GRANTED){
            makeRequest();
        }
    }

    private void makeRequest() {
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA}, CAMERA_REQUEST_CODE);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch(requestCode){
            case CAMERA_REQUEST_CODE:
                if(grantResults.length == 0 || grantResults[0] != PackageManager.PERMISSION_GRANTED){
                    Toast.makeText(this, "You need the camera permission to be able to use this app!", Toast.LENGTH_SHORT);
                }
                break;
        }
    }
}