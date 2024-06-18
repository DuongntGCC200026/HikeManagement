package com.example.cw_hikermanagementapp;

public interface RecyclerViewInterface <T> {
    void onItemClick(int position);
    void add(T data);
    void update(T data);
    void seeAllInList(T data);
    void delete(T data);
}
