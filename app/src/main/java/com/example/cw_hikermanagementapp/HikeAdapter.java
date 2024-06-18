package com.example.cw_hikermanagementapp;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class HikeAdapter extends RecyclerView.Adapter<HikeAdapter.ViewHolder> implements Filterable {
    private final RecyclerViewInterface recyclerViewInterface;
    private final List<Hike> hikeArrayList;
    private final List<Hike> filteredList;
    Context context;
    String ObjSearch;

    public HikeAdapter(Context context, List<Hike> hikeArrayList, RecyclerViewInterface recyclerViewInterface) {
        this.context = context;
        this.hikeArrayList = hikeArrayList;
        this.recyclerViewInterface = recyclerViewInterface;
        this.filteredList = new ArrayList<>(hikeArrayList);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item, parent, false);
        return new ViewHolder(v, recyclerViewInterface);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        if (filteredList.get(position).isDeleted() == 0) {
            String add = filteredList.get(position).getName();
            if (add.length() > 20) {
                add = add.substring(0, 20) + "...";
            }
            holder.txtName.setText(add);
            holder.txtDateOfHike.setText(filteredList.get(position).getDateOfTheHike());
            holder.txtLocation.setText(filteredList.get(position).getLocation());
            Double length = (Double) filteredList.get(position).getLength();
            holder.txtLength.setText(String.valueOf(length));
        }
    }

    @Override
    public int getItemCount() {
        return filteredList.size();
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                List<Hike> filteredResults = new ArrayList<>();
                if (constraint == null || constraint.length() == 0) {
                    filteredResults.addAll(hikeArrayList);
                } else {
                    String filterPattern = constraint.toString().toLowerCase().trim();
                    for (Hike item : hikeArrayList) {
                        if (ObjSearch.equals("Name") && item.getName().toLowerCase().contains(filterPattern)) {
                            filteredResults.add(item);
                        } else if (ObjSearch.equals("Location") && item.getLocation().toLowerCase().contains(filterPattern)) {
                            filteredResults.add(item);
                        } else if (ObjSearch.equals("Length") && String.valueOf(item.getLength()).contains(filterPattern)) {
                            filteredResults.add(item);
                        } else if (ObjSearch.equals("Date") && item.getDateOfTheHike().toLowerCase().contains(filterPattern)) {
                            filteredResults.add(item);
                        }
                    }
                }
                FilterResults results = new FilterResults();
                results.values = filteredResults;
                return results;
            }

            @SuppressLint("NotifyDataSetChanged")
            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                filteredList.clear();
                filteredList.addAll((List<Hike>) results.values);
                notifyDataSetChanged();
            }
        };
    }

    public void getItemSearch(String getItemSelected) {
        this.ObjSearch = getItemSelected;
        getFilter().filter("");
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView txtName, txtDateOfHike, txtLocation, txtLength;
        public ImageView imageView;

        public ViewHolder(@NonNull View itemView, RecyclerViewInterface recyclerViewInterface) {
            super(itemView);
            this.txtName = itemView.findViewById(R.id.txtName);
            this.txtDateOfHike = itemView.findViewById(R.id.txtDayOfHike);
            this.txtLocation = itemView.findViewById(R.id.txtLocation);
            this.txtLength = itemView.findViewById(R.id.txtLengthAdap);

            this.imageView = itemView.findViewById(R.id.imageView);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (recyclerViewInterface != null) {
                        int pos = getAdapterPosition();

                        if (pos != RecyclerView.NO_POSITION) {
                            recyclerViewInterface.onItemClick(pos);
                        }
                    }
                }
            });

            imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int pos = getAdapterPosition();
                    showMenu(view, hikeArrayList.get(pos));
                }
            });
        }

        private void showMenu(View view, Hike hike) {
            PopupMenu popupMenu = new PopupMenu(context, view);
            popupMenu.getMenuInflater().inflate(R.menu.menu_hike, popupMenu.getMenu());

            popupMenu.setOnMenuItemClickListener(item -> {
                int itemId = item.getItemId();
                if (itemId == R.id.addObservationHike) {
                    recyclerViewInterface.add(hike);
                    return true;
                } else if (itemId == R.id.seeObservationHike) {
                    recyclerViewInterface.seeAllInList(hike);
                    return true;
                } else if (itemId == R.id.deleteHike) {
                    recyclerViewInterface.delete(hike);
                    return true;
                }
                return false;
            });

            popupMenu.show();
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    public void addItem(Hike newItem) {
        filteredList.add(0, newItem);
        notifyDataSetChanged();
    }

    @SuppressLint("NotifyDataSetChanged")
    public void delete(Hike newItem) {
        for (Hike hike : filteredList) {
            if (hike.getId() == newItem.getId()) {
                filteredList.remove(hike);
                break;
            }
        }
        notifyDataSetChanged();
    }
}
