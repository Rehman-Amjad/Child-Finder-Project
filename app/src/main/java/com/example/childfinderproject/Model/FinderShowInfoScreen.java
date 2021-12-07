package com.example.childfinderproject.Model;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.AssetFileDescriptor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Rect;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.childfinderproject.FinderSearchDataScreen;
import com.example.childfinderproject.ParentSearchScreen;
import com.example.childfinderproject.ParentShowInfoScreen;
import com.example.childfinderproject.R;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.mlkit.vision.common.InputImage;
import com.google.mlkit.vision.face.Face;
import com.google.mlkit.vision.face.FaceDetection;
import com.google.mlkit.vision.face.FaceDetector;

import org.tensorflow.lite.DataType;
import org.tensorflow.lite.Interpreter;
import org.tensorflow.lite.support.common.TensorOperator;
import org.tensorflow.lite.support.common.ops.NormalizeOp;
import org.tensorflow.lite.support.image.ImageProcessor;
import org.tensorflow.lite.support.image.TensorImage;
import org.tensorflow.lite.support.image.ops.ResizeOp;
import org.tensorflow.lite.support.image.ops.ResizeWithCropOrPadOp;

import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.util.List;

public class FinderShowInfoScreen extends AppCompatActivity {

    ImageView img_back_signUp,img_current,imgGallery;
    TextView tv_finderName,tv_FinderAddress,tv_FinderPhone,tv_babyName,tv_babyAge;
    Button btn_parent_face_match,btn_Cam,btn_parent_set_loc,btn_verify;

    CardView card;



    protected Interpreter tflite;
    private  int imageSizeX;
    private  int imageSizeY;

    private static final float IMAGE_MEAN = 0.0f;
    private static final float IMAGE_STD = 1.0f;

    public Bitmap oribitmap,testbitmap;
    public static Bitmap cropped;
    Uri imageuri;

    float[][] ori_embedding = new float[1][128];
    float[][] test_embedding = new float[1][128];

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_finder_show_info_screen);


        //image Passing Ref
        img_back_signUp=findViewById(R.id.img_back_signUp);
        img_current=findViewById(R.id.img_current);
        imgGallery=findViewById(R.id.imgGallery);

        //TextView Passing Ref
        tv_finderName=findViewById(R.id.tv_finderName);
        tv_FinderAddress=findViewById(R.id.tv_FinderAddress);
        tv_FinderPhone=findViewById(R.id.tv_FinderPhone);
        tv_babyName=findViewById(R.id.tv_babyName);
        tv_babyAge=findViewById(R.id.tv_babyAge);

        //Button Passing Ref
        btn_parent_face_match=findViewById(R.id.btn_parent_face_match);
        btn_parent_set_loc=findViewById(R.id.btn_parent_set_loc);

        btn_verify=findViewById(R.id.btn_verify);
        btn_Cam=findViewById(R.id.btn_Cam);

        card=findViewById(R.id.card);



        img_back_signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent backIntent = new Intent(FinderShowInfoScreen.this, FinderSearchDataScreen.class);
                startActivity(backIntent);
                finish();
            }
        });

        imgGallery.setVisibility(View.INVISIBLE);
        btn_verify.setVisibility(View.INVISIBLE);
        card.setVisibility(View.INVISIBLE);
        btn_Cam.setVisibility(View.INVISIBLE);

        SharedPreferences preferences = getSharedPreferences("CURRENT_DATA_PARENT", Context.MODE_PRIVATE);

        String parentName = preferences.getString("PARENT_NAME","");
        String parentAddress = preferences.getString("PARENT_ADDRESS","");
        String parentPhoneNumber = preferences.getString("PARENT_MOBILE","");
        String babyName = preferences.getString("BABY_NAME","");
        String babyAge = preferences.getString("BABY_AGE","");
        String babyImage = preferences.getString("BABY_IMAGE","");




        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        byte[] imageBytes = baos.toByteArray();
        imageBytes = Base64.decode(babyImage, Base64.DEFAULT);
        oribitmap = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.length);
        img_current.setImageBitmap(oribitmap);


        tv_finderName.setText("Parent Name: " + parentName);
        tv_FinderAddress.setText("Parent Address: " + parentAddress);
        tv_FinderPhone.setText("Parent Phone Number: " + parentPhoneNumber);
        tv_babyName.setText("Baby Name: " + babyName);
        tv_babyAge.setText("Baby Age: " + babyAge);

        if (ContextCompat.checkSelfPermission(FinderShowInfoScreen.this, Manifest.permission.CAMERA)!= PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(FinderShowInfoScreen.this, new String[]{
                    Manifest.permission.CAMERA

            }, 100);
        }
        try{
            tflite=new Interpreter(loadmodelfile(FinderShowInfoScreen.this));
        }catch (Exception e) {
            e.printStackTrace();
        }

        btn_parent_face_match.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                imgGallery.setVisibility(View.VISIBLE);
                btn_Cam.setVisibility(View.VISIBLE);
                btn_parent_set_loc.setVisibility(View.INVISIBLE);
            }
        });



        btn_Cam.setOnClickListener(new View.OnClickListener() {
            @SuppressWarnings("deprecation")
            @Override
            public void onClick(View v) {

                btn_verify.setVisibility(View.VISIBLE);
                btn_parent_face_match.setVisibility(View.INVISIBLE);

                Intent intent= new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(intent,100);
            }
        });

        retriveImae();

        btn_verify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {



                double distance=calculate_distance(ori_embedding,test_embedding);

                if(distance<6.0)
                {

                    btn_parent_set_loc.setVisibility(View.VISIBLE);
                    card.setVisibility(View.VISIBLE);

                    AlertDialog.Builder builder=new AlertDialog.Builder(FinderShowInfoScreen.this);

                    builder.setTitle("Face Match Successfully");
                    builder.setMessage("Get Location and Take your Lost baby");
                    builder.setCancelable(false);
                    builder.setNegativeButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                        }
                    });
                    AlertDialog myalter=builder.create();
                    myalter.show();

                }
                else
                {
                    AlertDialog.Builder builder=new AlertDialog.Builder(FinderShowInfoScreen.this);

                    builder.setTitle("Face Not Match");
                    builder.setMessage("This is not your Baby Find other");
                    builder.setCancelable(false);
                    builder.setNegativeButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                        }
                    });
                    AlertDialog myalter=builder.create();
                    myalter.show();
                }
            }
        });


        btn_parent_set_loc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                Intent nextIntent = new Intent(FinderShowInfoScreen.this,FinderSetMapScreen.class);
                startActivity(nextIntent);
                finish();


            }
        });

    }

    private void retriveImae()
    {
        SharedPreferences preferences = getSharedPreferences("CURRENT_DATA_PARENT", Context.MODE_PRIVATE);
        String babyImage = preferences.getString("BABY_IMAGE","");

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        byte[] imageBytes = baos.toByteArray();
        imageBytes = Base64.decode(babyImage, Base64.DEFAULT);
        oribitmap = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.length);
        img_current.setImageBitmap(oribitmap);

        face_detector(oribitmap,"original");
    }



    private double calculate_distance(float[][] ori_embedding, float[][] test_embedding) {
        double sum =0.0;
        for(int i=0;i<128;i++){
            sum=sum+Math.pow((ori_embedding[0][i]-test_embedding[0][i]),2.0);
        }
        return Math.sqrt(sum);
    }
    private MappedByteBuffer loadmodelfile(Activity activity) throws IOException {
        AssetFileDescriptor fileDescriptor=activity.getAssets().openFd("Qfacenet.tflite");
        FileInputStream inputStream=new FileInputStream(fileDescriptor.getFileDescriptor());
        FileChannel fileChannel=inputStream.getChannel();
        long startoffset = fileDescriptor.getStartOffset();
        long declaredLength=fileDescriptor.getDeclaredLength();
        return fileChannel.map(FileChannel.MapMode.READ_ONLY,startoffset,declaredLength);
    }

    private TensorImage loadImage(final Bitmap bitmap, TensorImage inputImageBuffer ) {
        // Loads bitmap into a TensorImage.
        inputImageBuffer.load(bitmap);

        // Creates processor for the TensorImage.
        int cropSize = Math.min(bitmap.getWidth(), bitmap.getHeight());
        // TODO(b/143564309): Fuse ops inside ImageProcessor.
        ImageProcessor imageProcessor =
                new ImageProcessor.Builder()
                        .add(new ResizeWithCropOrPadOp(cropSize, cropSize))
                        .add(new ResizeOp(imageSizeX, imageSizeY, ResizeOp.ResizeMethod.NEAREST_NEIGHBOR))
                        .add(getPreprocessNormalizeOp())
                        .build();
        return imageProcessor.process(inputImageBuffer);
    }
    private TensorOperator getPreprocessNormalizeOp() {
        return new NormalizeOp(IMAGE_MEAN, IMAGE_STD);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==100 && resultCode==RESULT_OK && data!=null) {
            imageuri = data.getData();

            testbitmap = (Bitmap) data.getExtras().get("data");
            imgGallery.setImageBitmap(testbitmap);
            face_detector(testbitmap,"test");
        }
    }
    public void face_detector(final Bitmap bitmap, final String imagetype){

        final InputImage image = InputImage.fromBitmap(bitmap,0);
        FaceDetector detector = FaceDetection.getClient();
        detector.process(image)
                .addOnSuccessListener(
                        new OnSuccessListener<List<Face>>() {
                            @Override
                            public void onSuccess(List<Face> faces) {
                                // Task completed successfully
                                for (Face face : faces) {
                                    Rect bounds = face.getBoundingBox();
                                    cropped = Bitmap.createBitmap(bitmap, bounds.left, bounds.top,
                                            bounds.width(), bounds.height());
                                    get_embaddings(cropped,imagetype);
                                }
                            }

                        })
                .addOnFailureListener(
                        new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                // Task failed with an exception
                                Toast.makeText(getApplicationContext(),e.getMessage(),Toast.LENGTH_LONG).show();
                            }
                        });
    }
    public void get_embaddings(Bitmap bitmap,String imagetype){

        TensorImage inputImageBuffer;
        float[][] embedding = new float[1][128];

        int imageTensorIndex = 0;
        int[] imageShape = tflite.getInputTensor(imageTensorIndex).shape(); // {1, height, width, 3}
        imageSizeY = imageShape[1];
        imageSizeX = imageShape[2];
        DataType imageDataType = tflite.getInputTensor(imageTensorIndex).dataType();

        inputImageBuffer = new TensorImage(imageDataType);

        inputImageBuffer = loadImage(bitmap,inputImageBuffer);

        tflite.run(inputImageBuffer.getBuffer(),embedding);

        if(imagetype.equals("original"))
            ori_embedding=embedding;
        else if (imagetype.equals("test"))
            test_embedding=embedding;
    }
}