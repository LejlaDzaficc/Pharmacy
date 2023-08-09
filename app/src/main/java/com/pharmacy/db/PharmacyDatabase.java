package com.pharmacy.db;

import android.content.Context;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import com.pharmacy.db.dao.CategoryDao;
import com.pharmacy.db.dao.MedicamentWithCategoryDao;
import com.pharmacy.db.dao.MedicamentDao;
import com.pharmacy.db.domain.Category;
import com.pharmacy.db.domain.Medicament;

@Database(entities = {Medicament.class, Category.class}, version = 1, exportSchema = false)
public abstract class PharmacyDatabase extends RoomDatabase {
    public abstract MedicamentDao medicamentDao();
    public abstract CategoryDao categoryDao();
    public abstract MedicamentWithCategoryDao medicamentWithCategoryDao();
    private static PharmacyDatabase INSTANCE;

    public static PharmacyDatabase getDatabase(Context context){
        if(INSTANCE == null) {
            INSTANCE = Room.databaseBuilder(context, PharmacyDatabase.class, "pharmacy_database").build();
        }
        return INSTANCE;
    }
}
