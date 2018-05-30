package com.example.roshani.sensorexample;

import android.content.Intent;
import android.graphics.Bitmap;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.ml.vision.FirebaseVision;
import com.google.firebase.ml.vision.common.FirebaseVisionImage;
import com.google.firebase.ml.vision.text.FirebaseVisionText;
import com.google.firebase.ml.vision.text.FirebaseVisionTextDetector;

import java.util.List;

public class MainActivity extends AppCompatActivity {
    ImageView imv;
    Button snap,detect;
    Bitmap bmp;
    private int CODE=15;
    TextView tv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        imv=findViewById(R.id.iv1);
        snap=findViewById(R.id.btn1);
        detect=findViewById(R.id.btn2);
        tv=findViewById(R.id.tv1);
        snap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(i,CODE);



            }
        });

    }
    public void ClickMe(View v){
        FirebaseVisionImage image=FirebaseVisionImage.fromBitmap(bmp);
        FirebaseVisionTextDetector detector= FirebaseVision.getInstance().getVisionTextDetector();
        detector.detectInImage(image).addOnSuccessListener(new OnSuccessListener<FirebaseVisionText>() {
            @Override
            public void onSuccess(FirebaseVisionText firebaseVisionText) {
                List<FirebaseVisionText.Block> mytexts=firebaseVisionText.getBlocks();
                if (mytexts.size()>0){
                    String displaydata="null";
                    for (FirebaseVisionText.Block myblock:firebaseVisionText.getBlocks()){
                        displaydata=displaydata+myblock.getText()+"\n";

                    }
                    tv.setText(displaydata);
                }

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(MainActivity.this,"no text image detected",Toast.LENGTH_SHORT).show();
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode==CODE&& resultCode==RESULT_OK){
            Bundle bundle=data.getExtras();
            bmp= (Bitmap) bundle.get("data");
            imv.setImageBitmap(bmp);

        }
    }
}
