package com.example.cw_hikermanagementapp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.view.menu.MenuBuilder;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.SearchView;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;


public class MainActivity extends AppCompatActivity implements RecyclerViewInterface<Hike>, ObservationDialog.AddObserDialogListener<Hike> {
    HikeAdapter myContactAdapter;
    List<Hike> arrHikes;
    RecyclerView recyclerView;
    final int REQUEST_CODE_ADD = 100;
    final int REQUEST_CODE_UPDATE = 200;
    Database database;
    SearchView edtSearch;
    ImageView reLoadHike;
    Spinner spinner;
    int getMaxID = 0;
    public static boolean isLoginIntentProcessed = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (!isLoginIntentProcessed) {
            Intent intent = new Intent(this, Login.class);
            startActivity(intent);
        }

        reLoadHike = (ImageView) findViewById(R.id.reLoadHike);
        reLoadHike.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, MainActivity.class);
                finish();
                startActivity(intent);
            }
        });

        Toolbar mytoolbar = findViewById(R.id.toolbar);
        setSupportActionBar(mytoolbar);

        database = new Database(this);

        arrHikes = new ArrayList<>();
        Cursor dataHike = database.GetData("Select * from Hike ORDER BY id DESC");
        while (dataHike.moveToNext()) {
            int id = dataHike.getInt(0);
            if (id > getMaxID) {
                getMaxID = id;
            }
            String name = dataHike.getString(1);
            String location = dataHike.getString(2);
            String dayOfHike = dataHike.getString(3);
            Double length = Double.valueOf(dataHike.getString(5));
            int isDeleted = dataHike.getInt(10);
            if (isDeleted == 0) {
                Hike hike = new Hike(id, name, location, dayOfHike, length, isDeleted);
                arrHikes.add(hike);
            }
        }

        searchQuick();
        loadDataOfHike(arrHikes);
    }

    private void searchQuick() {
        spinner = (Spinner) findViewById(R.id.spinnerSearch);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                String getItemSelected = spinner.getSelectedItem().toString();
                myContactAdapter.getItemSearch(getItemSelected);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        edtSearch = (SearchView) findViewById(R.id.edtSearch);
        edtSearch.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                myContactAdapter.getFilter().filter(newText);
                return false;
            }
        });
    }

    @SuppressLint("NotifyDataSetChanged")
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_ADD) {
            if (resultCode == RESULT_OK && data != null) {
                String name = data.getStringExtra("name");
                String location = data.getStringExtra("location");
                String date = data.getStringExtra("date");
                String parking = data.getStringExtra("parking");
                String length = data.getStringExtra("length");
                String level = data.getStringExtra("level");
                String des = data.getStringExtra("des");
                String timeS = data.getStringExtra("timeS");
                String timeE = data.getStringExtra("timeE");

                Hike hike = new Hike(getMaxID + 1, name, location, date, Integer.parseInt(parking), Double.parseDouble(length), Integer.parseInt(level), des, timeS, timeE, 0);

                database.insertHike(hike);
                arrHikes.add(hike);
                myContactAdapter.addItem(hike);
                loadDataOfHike(arrHikes);
                Toast.makeText(this, "Add new Hike Successfully!", Toast.LENGTH_SHORT).show();
            }
        } else if (requestCode == REQUEST_CODE_UPDATE) {
            if (resultCode == RESULT_OK && data != null) {
                String check = data.getStringExtra("type");
                Hike getHike = (Hike) data.getSerializableExtra("hikeObj");

                if (check == null) {
                    Hike hikeToUpdate = new Hike(getHike.getId(), getHike.getName(), getHike.getLocation(), getHike.getDateOfTheHike(), getHike.isParking(), getHike.getLength(), getHike.getLevel(), getHike.getDescription(), getHike.getTimeStart(), getHike.getTimeStop(), 0);
                    database.updateHike(hikeToUpdate);

                    for (Hike hike : arrHikes) {
                        if (hike.getId() == hikeToUpdate.getId()) {
                            hike.setName(hikeToUpdate.getName());
                            hike.setDateOfTheHike(hikeToUpdate.getDateOfTheHike());
                            hike.setLocation(hikeToUpdate.getLocation());
                            hike.setParking(hikeToUpdate.isParking());
                            hike.setLength(hikeToUpdate.getLength());
                            hike.setLevel(hikeToUpdate.getLevel());
                            hike.setDescription(hikeToUpdate.getDescription());
                            hike.setTimeStart(hikeToUpdate.getTimeStart());
                            hike.setTimeStop(hikeToUpdate.getTimeStop());
                        }
                    }
                    Toast.makeText(this, "Update Successfully!", Toast.LENGTH_SHORT).show();
                    loadDataOfHike(arrHikes);
                }
            }
        }
    }

    @Override
    public void onItemClick(int position) {
        Intent i = new Intent(MainActivity.this, UpdateHikeActivity.class);
        Hike hike = getHikeInDB(arrHikes.get(position));
        i.putExtra("hikeObj", hike);
        startActivityForResult(i, REQUEST_CODE_UPDATE);
    }

    @Override
    public void add(Hike hike) {
        ObservationDialog addObservationDialog = new ObservationDialog(hike, "add");
        addObservationDialog.show(getSupportFragmentManager(), "add Observation Dialog");
    }

    @Override
    public void update(Hike data) {

    }


    @Override
    public void seeAllInList(Hike hike) {
        HandleSeeAllObservation(hike);
    }

    private void HandleSeeAllObservation(Hike hike) {
        Intent i = new Intent(MainActivity.this, ObservationActivity.class);
        i.putExtra("id", hike.getId());
        startActivity(i);
    }

    @Override
    public void delete(Hike getHike) {
        DialogMessageBox confirmationDialog = new DialogMessageBox("Warning", "Do you want to delete this hike?");
        confirmationDialog.showConfirmationDialog(
                this,
                (dialog, which) -> HandleDelete(getHike)
        );
    }

    private void HandleDelete(Hike getHike) {
        Hike hikeToUpdate = new Hike(getHike.getId(), getHike.getName(), getHike.getLocation(), getHike.getDateOfTheHike(), getHike.isParking(), getHike.getLength(), getHike.getLevel(), getHike.getDescription(), getHike.getTimeStart(), getHike.getTimeStop(), 1);
        database.updateHike(hikeToUpdate);
        arrHikes.remove(getHike);
        loadDataOfHike(arrHikes);
        myContactAdapter.delete(getHike);
        Toast.makeText(this, "Delete Successfully!", Toast.LENGTH_SHORT).show();

    }

    @Override
    public void sendData(String name, String dateAndTime, String addition, Hike hike) {
        Observation observation = new Observation(getMaxIDofObser(), name, dateAndTime, addition, 0, hike.getId());
        database.insertObservation(observation);
        HandleSeeAllObservation(hike);
    }

    private int getMaxIDofObser() {
        Cursor getData = database.GetData("Select * from Observation");
        int max = 0;
        while (getData.moveToNext()) {
            int id = getData.getInt(0);
            if (id > max) {
                max = id;
            }
        }
        return max + 1;
    }

    private Hike getHikeInDB(Hike hike) {
        Cursor dataHike = database.GetData("Select * from Hike");
        while (dataHike.moveToNext()) {
            int id = dataHike.getInt(0);

            if (id == hike.getId()) {
                String name1 = dataHike.getString(1);
                String location1 = dataHike.getString(2);
                String dayOfHike1 = dataHike.getString(3);
                double park = dataHike.getDouble(4);
                double length = dataHike.getDouble(5);
                double level = dataHike.getDouble(6);
                String des = dataHike.getString(7);
                String timeS = dataHike.getString(8);
                String timeE = dataHike.getString(9);
                double isDeleted = dataHike.getDouble(10);

                return new Hike(id, name1, location1, dayOfHike1, (int) park, length, (int) level, des, timeS, timeE, (int) isDeleted);
            }
        }
        return null;
    }

    @SuppressLint("RestrictedApi")
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_toolbar, menu);

        if (menu instanceof MenuBuilder) {
            MenuBuilder m = (MenuBuilder) menu;
            m.setOptionalIconsVisible(true);
        }

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.add) {
            ButtonAddOnClickHolder();
        } else if (item.getItemId() == R.id.action_search) {
            handleSearch();
        } else if (item.getItemId() == R.id.logout) {
            Intent intent = new Intent(this, Login.class);
            startActivity(intent);
            isLoginIntentProcessed = false;
            Toast.makeText(this, "Logout successfully", Toast.LENGTH_SHORT).show();
        }
        return super.onOptionsItemSelected(item);
    }

    private void handleSearch() {
        EditText edtSearchName, editSearchTime, editSearchLocation, editSearchLength;

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Advance Search");
        builder.setIcon(R.drawable.baseline_search_24);

        View customLayout = getLayoutInflater().inflate(R.layout.layout_search_advanced, null);
        builder.setView(customLayout);

        edtSearchName = (EditText) customLayout.findViewById(R.id.searchNameDialog);
        editSearchTime = (EditText) customLayout.findViewById(R.id.searchTimeDialog);
        editSearchLocation = (EditText) customLayout.findViewById(R.id.searchLocationDialog);
        editSearchLength = (EditText) customLayout.findViewById(R.id.searchLengthDialog);

        editSearchTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DialogFragment newFragment = new DatePickerFragment(editSearchTime, "dd/MM/yyyy");
                newFragment.show(getSupportFragmentManager(), "datePicker");
            }
        });

        builder.setPositiveButton("Search", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                List<Hike> arrSearchHike = new ArrayList<>();

                String name = edtSearchName.getText().toString().toLowerCase().trim();
                String location = editSearchLocation.getText().toString().toLowerCase().trim();
                String time = editSearchTime.getText().toString();
                String length = editSearchLength.getText().toString();

                for (Hike h : arrHikes) {
                    if (h.getName().toLowerCase().trim().contains(name) &&
                            h.getLocation().toLowerCase().trim().contains(location) &&
                            h.getDateOfTheHike().contains(time) &&
                            String.valueOf(h.getLength()).contains(length)) {
                        arrSearchHike.add(h);
                    }
                }
                myContactAdapter = new HikeAdapter(MainActivity.this, arrSearchHike, MainActivity.this);
                recyclerView.setAdapter(myContactAdapter);
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

    private void ButtonAddOnClickHolder() {
        Intent i = new Intent(MainActivity.this, AddHikeActivity.class);
        startActivityForResult(i, REQUEST_CODE_ADD);
    }

    @SuppressLint("NotifyDataSetChanged")
    private void loadDataOfHike(List<Hike> arrHikes) {
        recyclerView = findViewById(R.id.recyclerview);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        if (myContactAdapter == null) {
            myContactAdapter = new HikeAdapter(this, arrHikes, this);
            recyclerView.setAdapter(myContactAdapter);
        } else {
            myContactAdapter.notifyDataSetChanged();
        }
    }
}