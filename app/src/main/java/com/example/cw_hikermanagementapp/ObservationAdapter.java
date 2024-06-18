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

public class ObservationAdapter extends RecyclerView.Adapter<ObservationAdapter.ViewHolder> implements Filterable {
    private final RecyclerViewInterface recyclerViewInterface;
    private final List<Observation> observationArrayList;
    private final List<Observation> filteredList;
    Context context;

    public ObservationAdapter(Context context, List<Observation> observationArrayList, RecyclerViewInterface recyclerViewInterface) {
        this.context = context;
        this.observationArrayList = observationArrayList;
        this.recyclerViewInterface = recyclerViewInterface;
        this.filteredList = new ArrayList<>(observationArrayList);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_observation, parent, false);
        return new ViewHolder(v, recyclerViewInterface);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        if (filteredList.get(position).isDeleted == 0) {
            String add = filteredList.get(position).getAdditionalComments();
            if (add.length() > 15) {
                add = add.substring(0, 15) + "...";
            }
            holder.txtName.setText(filteredList.get(position).getNameOfObservation());
            holder.txtDateOfOb.setText(filteredList.get(position).getDateAndTime());
            holder.txtAddition.setText(add);
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
                List<Observation> filteredResults = new ArrayList<>();
                if (constraint == null || constraint.length() == 0) {
                    filteredResults.addAll(observationArrayList);
                } else {
                    String filterPattern = constraint.toString().toLowerCase().trim();
                    for (Observation item : observationArrayList) {
                        if (item.getNameOfObservation().toLowerCase().contains(filterPattern)) {
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
                filteredList.addAll((List<Observation>) results.values);
                notifyDataSetChanged();
            }
        };
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView txtName, txtDateOfOb, txtAddition;

        public ImageView imageView;
        public ViewHolder(@NonNull View itemView, RecyclerViewInterface recyclerViewInterface) {
            super(itemView);
            this.txtName = itemView.findViewById(R.id.txtNameOfOb);
            this.txtDateOfOb = itemView.findViewById(R.id.txtDateAndTimeOfOb);
            this.txtAddition = itemView.findViewById(R.id.txtAdditionOfOb);
            this.imageView = itemView.findViewById(R.id.imageView2);

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
                    showMenu(view, observationArrayList.get(pos));
                }
            });
        }
        private void showMenu(View view, Observation ob) {
            PopupMenu popupMenu = new PopupMenu(context, view);
            popupMenu.getMenuInflater().inflate(R.menu.menu_ob, popupMenu.getMenu());

            popupMenu.setOnMenuItemClickListener(item -> {
                int itemId = item.getItemId();
                if (itemId == R.id.seeAllImages) {
                    recyclerViewInterface.seeAllInList(ob);
                    return true;
                } else if (itemId == R.id.deleteOb) {
                    recyclerViewInterface.delete(ob);
                    return true;
                }
                return false;
            });
            popupMenu.show();
        }
    }
}
