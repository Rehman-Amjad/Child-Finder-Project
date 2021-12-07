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
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.childfinderproject.ParentShowInfoScreen;
import com.example.childfinderproject.R;

import java.io.ByteArrayOutputStream;
import java.util.List;

public class FinderAdapter extends RecyclerView.Adapter<FinderAdapter.MyViewFinderHolder> {

    private Context context;
    private List<ParentUploadDataClass> mDatalist;

    public FinderAdapter(Context context, List<ParentUploadDataClass> mDatalist) {
        this.context = context;
        this.mDatalist = mDatalist;
    }

    @NonNull
    @Override
    public MyViewFinderHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
    {
        View myview = LayoutInflater.from(context).inflate(R.layout.list_item,parent,false);

        return new MyViewFinderHolder(myview);

    }



    @Override
    public void onBindViewHolder(@NonNull MyViewFinderHolder holder, int position)
    {


        ParentUploadDataClass dataClass = mDatalist.get(position);

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        byte[] imageBytes = baos.toByteArray();
        imageBytes = Base64.decode(dataClass.getParentImg(), Base64.DEFAULT);
        Bitmap decodedImage = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.length);

        holder.img_parent_image.setImageBitmap(decodedImage);




        holder.btn_showInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                SharedPreferences preferences = context.getSharedPreferences("CURRENT_DATA_PARENT",Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = preferences.edit();
                editor.putString("PARENT_NAME",dataClass.getParentName());
                editor.putString("PARENT_ADDRESS",dataClass.getParentAddress());
                editor.putString("PARENT_MOBILE",dataClass.getParentMobileNumber());
                editor.putString("BABY_NAME",dataClass.getBabyFullName());
                editor.putString("BABY_AGE",dataClass.getBabyAge());
                editor.putString("BABY_IMAGE",dataClass.getParentImg());
                editor.putString("PARENT_ID",dataClass.getParentId());
                editor.apply();

                Intent intent = new Intent(context, FinderShowInfoScreen.class);
                context.startActivity(intent);
            }
        });



    }

    @Override
    public int getItemCount() {
        return mDatalist.size();
    }

    public class MyViewFinderHolder extends RecyclerView.ViewHolder{

        ImageView img_parent_image;
        Button btn_showInfo;
        View view;
        public MyViewFinderHolder(@NonNull View itemView) {
            super(itemView);

            img_parent_image = itemView.findViewById(R.id.img_parent_image);
            btn_showInfo = itemView.findViewById(R.id.btn_showInfo);
            view=itemView;



        }
    }
}