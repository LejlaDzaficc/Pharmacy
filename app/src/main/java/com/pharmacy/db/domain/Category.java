package com.pharmacy.db.domain;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class Category {

    @PrimaryKey
    public int id;
    @ColumnInfo(name = "mark")
    public String mark;

    @ColumnInfo(name = "name")
    public String name;

    @ColumnInfo(name = "color")
    public String color;
}
