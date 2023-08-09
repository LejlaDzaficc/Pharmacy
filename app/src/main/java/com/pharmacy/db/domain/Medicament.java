package com.pharmacy.db.domain;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class Medicament {

    @PrimaryKey
    public int id;

    @ColumnInfo(name = "name")
    public String name;
    @ColumnInfo(name = "atc")
    public String atc;
    @ColumnInfo(name = "shortDescription")
    public String shortDescription;
    @ColumnInfo(name = "description")
    public String description;
    @ColumnInfo(name = "categoryId")
    public int categoryId;
    @ColumnInfo(name = "activeSubstanceValue")
    public int activeSubstanceValue;
    @ColumnInfo(name = "activeSubstanceMeasurementUnit")
    public String activeSubstanceMeasurementUnit;
    @ColumnInfo(name = "activeSubstanceSelectedQuantity")
    public int activeSubstanceSelectedQuantity;
    @ColumnInfo(name = "activeSubstanceQuantityMeasurementUnit")
    public String activeSubstanceQuantityMeasurementUnit;
    @ColumnInfo(name = "minimumDailyDose")
    public int minimumDailyDose;
    @ColumnInfo(name = "maximumDailyDose")
    public int maximumDailyDose;
    @ColumnInfo(name = "showOnCalculator")
    public boolean showOnCalculator;
    @ColumnInfo(name = "forbiddenInPregnancy")
    public boolean forbiddenInPregnancy;

}
