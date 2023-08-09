package com.pharmacy.db.dao;

import androidx.room.Dao;
import androidx.room.Query;
import androidx.room.Transaction;
import java.util.List;
import com.pharmacy.db.relations.MedicamentWithCategory;
@Dao
public interface MedicamentWithCategoryDao {
    @Transaction
    @Query("SELECT * FROM Medicament")
    public List<MedicamentWithCategory> getMedicamentsWithCategories();
}
