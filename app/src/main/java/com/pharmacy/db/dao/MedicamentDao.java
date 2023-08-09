package com.pharmacy.db.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import java.util.List;
import com.pharmacy.db.domain.Medicament;
@Dao
public interface MedicamentDao {
    @Query("SELECT * FROM Medicament")
    List<Medicament> getAll();

    @Insert
    void insertAll(List<Medicament> meds);

}
