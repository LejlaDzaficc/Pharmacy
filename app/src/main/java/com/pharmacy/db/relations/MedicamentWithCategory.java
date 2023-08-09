package com.pharmacy.db.relations;

import androidx.room.Embedded;
import androidx.room.Relation;

import com.pharmacy.db.domain.Category;
import com.pharmacy.db.domain.Medicament;

public class MedicamentWithCategory {
    @Embedded
    public Medicament med;

    @Relation(parentColumn = "categoryId", entityColumn = "id")
    public Category category;
}
