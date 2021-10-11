package com.example.notepadap;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.text.SimpleDateFormat;
import java.util.Locale;
import java.util.jar.Pack200;

public class MainActivity extends AppCompatActivity {

    EditText mInputEt;
    Button mSaveBtn;
    String mText;
    private  static  final  int WRITE_EXTERNAL_STORAGE_CODE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Tao View
        mInputEt = findViewById(R.id.inputEt);
        mSaveBtn = findViewById(R.id.saveBtn);

        // Xu li Button
        mSaveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                mText = mInputEt.getText().toString().trim();

                if(mText.isEmpty()){
                    Toast.makeText(MainActivity.this, "Vui long Nhap...", Toast.LENGTH_SHORT).show();
                }
                else{
                    if(Build.VERSION.SDK_INT >=Build.VERSION_CODES.M){
                        if(checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                                    == PackageManager.PERMISSION_DENIED){
                            String[] permissions = {Manifest.permission.WRITE_EXTERNAL_STORAGE};
                            requestPermissions(permissions,WRITE_EXTERNAL_STORAGE_CODE);
                        }
                        else{
                            saveToTxtFile(mText);
                        }
                    }
                    else{
                        saveToTxtFile(mText);
                    }

                }
            }
        });
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case WRITE_EXTERNAL_STORAGE_CODE: {
                if (grantResults.length > 0 && grantResults[0]
                        == PackageManager.PERMISSION_GRANTED) {
                    saveToTxtFile(mText);
                } else {
                    Toast.makeText(this, "Storage permission is required to store data", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

    private void saveToTxtFile(String mText){
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss",
                Locale.getDefault()).format(System.currentTimeMillis());
        try {
            File path = Environment.getExternalStorageDirectory();
            File dir = new File(path + "/My Files/");
            dir.mkdir();
            String fileName = "MyFile_" + timeStamp + ".txt";

            File file = new File(dir,fileName);

            FileWriter fw = new FileWriter(file.getAbsoluteFile());
            BufferedWriter bw = new BufferedWriter(fw);
            bw.write(mText);
            bw.close();

            Toast.makeText(this, fileName+"is saved to\n" +dir, Toast.LENGTH_SHORT).show();
        }
        catch (Exception e){
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }
}