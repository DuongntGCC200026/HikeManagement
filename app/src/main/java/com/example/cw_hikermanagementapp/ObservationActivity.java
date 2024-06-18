package com.example.cw_hikermanagementapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.view.menu.MenuBuilder;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.SearchView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class ObservationActivity extends AppCompatActivity implements RecyclerViewInterface<Observation>, ObservationDialog.AddObserDialogListener<Observation> {
    ObservationAdapter myContactAdapter;
    List<Observation> arrObservation;
    RecyclerView recyclerView;
    Database database;
    public static int hikeID;
    int obID;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_observation);

        database = new Database(this);

        Intent i = getIntent();
        hikeID = i.getIntExtra("id", 0);
        obID = i.getIntExtra("ObId",0);

        Toolbar mytoolbar = findViewById(R.id.toolbarObservation);
        setSupportActionBar(mytoolbar);


        loadDataOfObservation();
    }


    @SuppressLint("NotifyDataSetChanged")
    private void loadDataOfObservation() {
        recyclerView = findViewById(R.id.recyclerviewOb);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        if (myContactAdapter == null) {
            arrObservation = new ArrayList<>();
            Cursor dataObser = database.GetData("Select * from Observation ORDER BY ob_id DESC");
            while (dataObser.moveToNext()) {
                int id = dataObser.getInt(0);
                String name = dataObser.getString(1);
                String date = dataObser.getString(2);
                String addition = dataObser.getString(3);
                int isDeleted = dataObser.getInt(4);
                int hikeId = dataObser.getInt(5);
                if (isDeleted == 0 && hikeId == hikeID) {
                    Observation observation = new Observation(id, name, date, addition, isDeleted, hikeId);
                    arrObservation.add(observation);
                }
            }
            myContactAdapter = new ObservationAdapter(this, arrObservation, this);
            recyclerView.setAdapter(myContactAdapter);
        } else {
            arrObservation = new ArrayList<>();
            Cursor dataObser = database.GetData("Select * from Observation ORDER BY ob_id DESC");
            while (dataObser.moveToNext()) {
                int id = dataObser.getInt(0);
                String name = dataObser.getString(1);
                String date = dataObser.getString(2);
                String addition = dataObser.getString(3);
                int isDeleted = dataObser.getInt(4);
                int hikeId = dataObser.getInt(5);
                if (isDeleted == 0 && hikeId == hikeID) {
                    Observation observation = new Observation(id, name, date, addition, isDeleted, hikeId);
                    arrObservation.add(observation);
                }
            }
            myContactAdapter.notifyDataSetChanged();
        }
    }

    @SuppressLint("RestrictedApi")
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_toolbar_observation, menu);

        MenuItem menuItem = menu.findItem(R.id.action_search);

        SearchView searchView = (SearchView) menuItem.getActionView();
        searchView.setQueryHint("observation name");
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                myContactAdapter.getFilter().filter(newText);
                return true;
            }
        });

        if (menu instanceof MenuBuilder) {
            MenuBuilder m = (MenuBuilder) menu;
            m.setOptionalIconsVisible(true);
        }

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.back) {
            Intent returnBtn = new Intent(ObservationActivity.this, MainActivity.class);
            startActivity(returnBtn);
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onItemClick(int position) {
        ObservationDialog observationDialog = new ObservationDialog(arrObservation.get(position), "update");
        observationDialog.show(getSupportFragmentManager(), "Update Observation Dialog");
    }

    @Override
    public void add(Observation data) {

    }

    @Override
    public void update(Observation data) {

    }

    @Override
    public void seeAllInList(Observation data) {
        Intent i = new Intent(ObservationActivity.this, ImageActivity.class);
        i.putExtra("id" , data.getId());
        startActivity(i);
    }

    @Override
    public void delete(Observation observation) {
        DialogMessageBox confirmationDialog = new DialogMessageBox("Warning", "Do you want to delete?");
        confirmationDialog.showConfirmationDialog(
                this,
                (dialog, which) -> HandleDelete(observation)
        );
    }

    private void HandleDelete(Observation observation) {
        Observation observationToUpdate = new Observation(observation.getId(), observation.getNameOfObservation(), observation.getDateAndTime(), observation.getAdditionalComments(), 1, observation.getHikeId());
        database.updateObservation(observationToUpdate);
        arrObservation.remove(observation);
        loadDataOfObservation();
        Toast.makeText(this, "Delete Successfully!", Toast.LENGTH_SHORT).show();

        Intent intent = new Intent(ObservationActivity.this, ObservationActivity.class);
        finish();
        startActivity(intent);
    }

    @Override
    public void sendData(String name, String dateAndTime, String addition, Observation observation) {
        Observation observationToUpdate = new Observation(observation.getId(), name, observation.getDateAndTime(), addition, observation.getIsDeleted(), observation.getHikeId());
        database.updateObservation(observationToUpdate);

        for (Observation o : arrObservation) {
            if (o.getId() == observationToUpdate.getId()) {
                o.setNameOfObservation(observationToUpdate.getNameOfObservation());
                o.setAdditionalComments(observationToUpdate.getAdditionalComments());
            }
        }
        Toast.makeText(this, "Update Successfully!", Toast.LENGTH_SHORT).show();
        loadDataOfObservation();
    }
}