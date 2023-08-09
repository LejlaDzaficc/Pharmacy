package com.pharmacy.ui.activities;

import android.os.Bundle;
import android.view.MenuItem;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import com.pharmacy.R;
import com.pharmacy.ui.fragments.MedsRegisterFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

public class PharmacyActivity extends AppCompatActivity {
    private MenuItem activeMenuItem;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pharmacy);

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigationView);
        bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                if (item.getItemId() == R.id.medsRegisterItem && !item.equals(activeMenuItem)){
                    activeMenuItem = item;
                    getSupportFragmentManager()
                            .beginTransaction()
                            .replace(R.id.navigationViewContentFragment, new MedsRegisterFragment())
                            .commit();
                    return true;
                }
                return false;
            }
        });
        bottomNavigationView.setSelectedItemId(R.id.medsRegisterItem);
    }
}