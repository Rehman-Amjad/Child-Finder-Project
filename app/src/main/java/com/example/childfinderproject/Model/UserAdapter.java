package com.example.childfinderproject.Model;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.childfinderproject.ParentShowInfoScreen;
import com.example.childfinderproject.R;
import com.google.firebase.database.DataSnapshot;

import java.io.ByteArrayOutputStream;
import java.util.List;
public class UserAdapter extends RecyclerView.Adapter<UserAdapter.MyViewHolder> {

    private Context context;
    private List<FinderUploadDataClass> mDatalist;

    public UserAdapter(Context context, List<FinderUploadDataClass> mDatalist) {
        this.context = context;
        this.mDatalist = mDatalist;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
    {
        View myview = LayoutInflater.from(context).inflate(R.layout.list_item,parent,false);

        return new MyViewHolder(myview);

    }



    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position)
    {


        FinderUploadDataClass dataClass = mDatalist.get(position);

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        byte[] imageBytes = baos.toByteArray();
        imageBytes = Base64.decode(dataClass.getFinderImg(), Base64.DEFAULT);
        Bitmap decodedImage = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.length);

        holder.img_parent_image.setImageBitmap(decodedImage);



        holder.btn_showInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                SharedPreferences preferences = context.getSharedPreferences("CUURENTDATA",Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = preferences.edit();
                editor.putString("FINDER_NAME",dataClass.getFinderName());
                editor.putString("FINDER_ADDRESS",dataClass.getFinderAddress());
                editor.putString("FINDER_MOBILE",dataClass.getFinderMobileNumber());
                editor.putString("BABY_NAME",dataClass.getBabyFullName());
                editor.putString("BABY_AGE",dataClass.getBabyAge());
                editor.putString("BABY_IMAGE",dataClass.getFinderImg());
                editor.putString("FINDER_ID",dataClass.getFinderID());
                editor.apply();

                Intent intent = new Intent(context, ParentShowInfoScreen.class);
                context.startActivity(intent);
            }
        });



    }

    @Override
    public int getItemCount() {
        return mDatalist.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{

       ImageView img_parent_image;
       Button btn_showInfo;
        View view;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            img_parent_image = itemView.findViewById(R.id.img_parent_image);
            btn_showInfo = itemView.findViewById(R.id.btn_showInfo);
            view=itemView;



        }
    }
}