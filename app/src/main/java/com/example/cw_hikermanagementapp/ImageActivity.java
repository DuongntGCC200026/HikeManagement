package com.example.cw_hikermanagementapp;

import static android.provider.MediaStore.ACTION_IMAGE_CAPTURE;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.view.menu.MenuBuilder;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ImageActivity extends AppCompatActivity implements RecyclerViewInterface<Img>{
    ImgAdapter imgAdapter;
    public Bitmap bitmap;
    List<Img> arrImages;
    RecyclerView recyclerView;
    Database database;
    int obID;
    int getMaxID = 0;
    public ImageView imageView;
    private EditText edtDatePicture, edtTitle;


    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image);

        database = new Database(this);

        Toolbar mytoolbar = findViewById(R.id.toolbarImg);
        setSupportActionBar(mytoolbar);

        Intent i = getIntent();
        obID = i.getIntExtra("id", 0);


        loadDataOfImage();
    }


    @SuppressLint("NotifyDataSetChanged")
    private void loadDataOfImage() {
        recyclerView = findViewById(R.id.recyclerviewImg);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 2));
        if (imgAdapter == null) {
            arrImages = new ArrayList<>();
            Cursor getData = database.GetData("Select * from Image ORDER BY id_img DESC");
            while (getData.moveToNext()) {
                int id = getData.getInt(0);
                if (id > getMaxID) {
                    getMaxID = id;
                }
                int ob_id = getData.getInt(1);
                String title = getData.getString(2);
                String date = getData.getString(3);
                int delete = getData.getInt(4);
                byte[] img = getData.getBlob(5);
                if (delete == 0 && ob_id == obID) {
                    Img i = new Img(id, ob_id, title, date, delete, img);
                    arrImages.add(i);
                }
            }
            imgAdapter = new ImgAdapter(this,arrImages, this);

            recyclerView.setAdapter(imgAdapter);
        } else {
            arrImages = new ArrayList<>();
            Cursor getData = database.GetData("Select * from Image ORDER BY id_img DESC");
            while (getData.moveToNext()) {
                int id = getData.getInt(0);
                if (id > getMaxID) {
                    getMaxID = id;
                }
                int ob_id = getData.getInt(1);
                String title = getData.getString(2);
                String date = getData.getString(3);
                int delete = getData.getInt(4);
                byte[] img = getData.getBlob(5);
                if (delete == 0 && ob_id == obID) {
                    Img i = new Img(id, ob_id, title, date, delete, img);
                    arrImages.add(i);
                }
            }
            imgAdapter.notifyDataSetChanged();
        }
    }

    @SuppressLint("RestrictedApi")
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_toolbar_img, menu);
        if (menu instanceof MenuBuilder) {
            MenuBuilder m = (MenuBuilder) menu;
            m.setOptionalIconsVisible(true);
        }
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.imgBack) {
            int hikeID = ObservationActivity.hikeID;
            Intent returnBtn = new Intent(ImageActivity.this, ObservationActivity.class);
            returnBtn.putExtra("ObId",obID);
            returnBtn.putExtra("id",hikeID);
            startActivity(returnBtn);
        } else if (item.getItemId() == R.id.addImg) {
            Intent intent = new Intent(ACTION_IMAGE_CAPTURE);
            if (ActivityCompat.checkSelfPermission(ImageActivity.this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(ImageActivity.this, new String[]{Manifest.permission.CAMERA}, 1);
            }
            startActivityForResult(intent,99);
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 99 && resultCode == RESULT_OK) {
            bitmap = (Bitmap) data.getExtras().get("data");
            addImgDialog();
        }
    }

    private void addImgDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Do you want to save this picture?");
        builder.setMessage("");

        View customLayout = getLayoutInflater().inflate(R.layout.layout_add_new_img, null);
        builder.setView(customLayout);

        imageView = customLayout.findViewById(R.id.imageViewDisplay);
        edtDatePicture = (EditText) customLayout.findViewById(R.id.imgDateAndTime);
        edtTitle = (EditText) customLayout.findViewById(R.id.txtTitle);

        imageView.setImageBitmap(bitmap);

        String pattern = "yyyy-MM-dd hh:mm:ss";
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
        String date = simpleDateFormat.format(new Date());
        edtDatePicture.setText(date);
        edtDatePicture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DialogFragment newFragment = new DatePickerFragment(edtDatePicture, "dd/MM/yyyy");
                newFragment.show(getSupportFragmentManager(), "datePicker");
            }
        });

        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String title = edtTitle.getText().toString();
                String time = edtDatePicture.getText().toString();
                byte [] img = bitmapToByteArray(bitmap);
                database.insertImage(new Img(getMaxID + 1,obID,title,time,0,img));
                imgAdapter.addItem(new Img(getMaxID,obID,title,time,0,img));
                loadDataOfImage();
                Toast.makeText(ImageActivity.this,"Add new Image Successfully!", Toast.LENGTH_SHORT).show();

                Intent intent = new Intent(ImageActivity.this, ImageActivity.class);
                finish();
                intent.putExtra("id",obID);
                startActivity(intent);
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });

        AlertDialog dialog = builder.create();
        dialog.show();
    }

    public byte[] bitmapToByteArray(Bitmap bitmap) {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
        return stream.toByteArray();
    }

    @Override
    public void onItemClick(int position) {

    }

    @Override
    public void add(Img data) {

    }

    @Override
    public void update(Img data) {
        updateImgDialog(data);
    }

    private void updateImgDialog(Img img) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Update Image");
        builder.setMessage("");

        View customLayout = getLayoutInflater().inflate(R.layout.layout_add_new_img, null);
        builder.setView(customLayout);

        imageView = customLayout.findViewById(R.id.imageViewDisplay);
        edtDatePicture = (EditText) customLayout.findViewById(R.id.imgDateAndTime);
        edtTitle = (EditText) customLayout.findViewById(R.id.txtTitle);

        byte[] img1 = img.getImage();
        Bitmap b = BitmapFactory.decodeByteArray(img1, 0, img1.length);
        imageView.setImageBitmap(b);

        edtTitle.setText(img.getTitle());

        String date = img.getDate();
        edtDatePicture.setText(date);
        edtDatePicture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DialogFragment newFragment = new DatePickerFragment(edtDatePicture, "dd/MM/yyyy");
                newFragment.show(getSupportFragmentManager(), "datePicker");
            }
        });

        builder.setPositiveButton("Update", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String title = edtTitle.getText().toString();
                Img imgToUpdate = new Img(img.getId(),img.getOb_id(),title,img.getDate(),0,img.getImage());
                database.updateImage(imgToUpdate);

                for (Img i : arrImages) {
                    if (i.getId() == imgToUpdate.getId()) {
                        i.setTitle(imgToUpdate.getTitle());
                    }
                }
                Toast.makeText(ImageActivity.this,"Update Image Successfully!", Toast.LENGTH_SHORT).show();

                Intent intent = new Intent(ImageActivity.this, ImageActivity.class);
                finish();
                intent.putExtra("id",obID);
                startActivity(intent);
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });

        AlertDialog dialog = builder.create();
        dialog.show();
    }

    @Override
    public void seeAllInList(Img data) {

    }

    @Override
    public void delete(Img img) {
        DialogMessageBox confirmationDialog = new DialogMessageBox("Warning", "Do you want to delete?");
        confirmationDialog.showConfirmationDialog(
                this,
                (dialog, which) -> HandleDelete(img)
        );
    }

    private void HandleDelete(Img img) {
        Img imgToUpdate = new Img(img.getId(),img.getOb_id(),img.getTitle(),img.getDate(),1,img.getImage());
        database.updateImage(imgToUpdate);
        arrImages.remove(img);
        Toast.makeText(this, "Delete Successfully!", Toast.LENGTH_SHORT).show();

        Intent intent = new Intent(ImageActivity.this, ImageActivity.class);
        finish();
        intent.putExtra("id",obID);
        startActivity(intent);
    }
}