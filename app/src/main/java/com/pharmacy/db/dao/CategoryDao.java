package com.pharmacy.db.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import java.util.List;
import com.pharmacy.db.domain.Category;

@Dao
public interface CategoryDao {

    @Query("SELECT * FROM Category")
    List<Category> getAll();

    @Insert
    void insertAll(List<Category> categories);
}
