package com.example.cw_hikermanagementapp;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;
public class ImgAdapter extends RecyclerView.Adapter<ImgAdapter.ViewHolder> {
    List<Img> arrImages;
    Context context;
    RecyclerViewInterface recyclerViewInterface;

    public ImgAdapter(Context context, List<Img> arr, RecyclerViewInterface recyclerViewInterface) {
        this.arrImages = arr;
        this.context = context;
        this.recyclerViewInterface = recyclerViewInterface;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_picture, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        if (arrImages.get(position).getIsDeleted() == 0 && arrImages.get(position).getId() >= 4) {
            holder.txtTitle.setText(arrImages.get(position).getTitle());
            holder.txtTime.setText(arrImages.get(position).getDate());
            byte[] img = arrImages.get(position).getImage();
            Bitmap bitmap = BitmapFactory.decodeByteArray(img, 0, img.length);
            holder.viewImg.setImageBitmap(bitmap);
        } else {
            holder.txtTitle.setText(arrImages.get(position).getTitle());
            holder.txtTime.setText(arrImages.get(position).getDate());
            holder.viewImg.setImageResource(R.drawable.img);
        }
    }

    @Override
    public int getItemCount() {
        return arrImages.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView txtTitle, txtTime;
        public ImageView viewImg, seeIcon;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            this.txtTitle = itemView.findViewById(R.id.nameOfImage);
            this.txtTime = itemView.findViewById(R.id.timeP);
            this.viewImg = itemView.findViewById(R.id.ImgObser);
            this.seeIcon = itemView.findViewById(R.id.seeIcon);

            seeIcon.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int pos = getAdapterPosition();
                    showMenu(view, arrImages.get(pos));
                }
            });
        }
        private void showMenu(View view, Img image) {
            PopupMenu popupMenu = new PopupMenu(context, view);
            popupMenu.getMenuInflater().inflate(R.menu.menu_img, popupMenu.getMenu());

            popupMenu.setOnMenuItemClickListener(item -> {
                int itemId = item.getItemId();
                if (itemId == R.id.updateImg) {
                    recyclerViewInterface.update(image);
                    return true;
                } else if (itemId == R.id.deleteImg) {
                    recyclerViewInterface.delete(image);
                    return true;
                }
                return false;
            });

            popupMenu.show();
        }
    }
    @SuppressLint("NotifyDataSetChanged")
    public void addItem(Img newItem) {
        arrImages.add(0, newItem);
        notifyDataSetChanged();
    }
}