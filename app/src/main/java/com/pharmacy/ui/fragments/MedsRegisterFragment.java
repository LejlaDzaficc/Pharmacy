package com.pharmacy.ui.fragments;

import android.os.Bundle;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import android.os.Handler;
import android.os.Looper;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.Toast;
import com.pharmacy.R;
import com.pharmacy.ui.adapters.MedsListAdapter;
import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import com.pharmacy.common.CategoriesStreamParser;
import com.pharmacy.common.MedsStreamParser;

import com.pharmacy.db.PharmacyDatabase;
import com.pharmacy.db.domain.Category;
import com.pharmacy.db.domain.Medicament;
import com.pharmacy.db.relations.MedicamentWithCategory;

public class MedsRegisterFragment extends Fragment {
    private MedsListAdapter adapter;
    private List<MedicamentWithCategory> medsWithCategories = new ArrayList<>();
    private LinearLayout medsCategoryLayout;
    private SearchView searchView;
    public MedsRegisterFragment() {}

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        adapter = new MedsListAdapter(getContext());

        ExecutorService executorService = Executors.newSingleThreadExecutor();
        Handler handler = new Handler(Looper.getMainLooper());
        executorService.execute(() -> {
            PharmacyDatabase database = PharmacyDatabase.getDatabase(getContext());
            List<Medicament> meds = database.medicamentDao().getAll();
            List<Category> categories = database.categoryDao().getAll();
            try {
                if (meds.isEmpty()) {
                    meds = fetchMedicaments();
                    database.medicamentDao().insertAll(meds);
                }
                if (categories.isEmpty()) {
                    categories = fetchCategories();
                    database.categoryDao().insertAll(categories);
                }
            }
            catch (IOException e) {
                handler.post(() -> {
                    Toast.makeText(getContext(), R.string.meds_registar_fragment_connection_error, Toast.LENGTH_LONG).show();
                });
            }
            List<MedicamentWithCategory> joinedMedicamentsAndCategories = database.medicamentWithCategoryDao().getMedicamentsWithCategories();

            handler.post(() -> {
                medsWithCategories = joinedMedicamentsAndCategories;
                Collections.sort(medsWithCategories, (a, b) -> a.med.name.compareTo(b.med.name));
                updateMeds(medsWithCategories);
                initCategoryFilter();
            });
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View medsRegisterView = inflater.inflate(R.layout.fragment_meds_register, container, false);

        searchView = medsRegisterView.findViewById(R.id.medsRegisterFragmentSearch);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextChange(String s) {
                invalidateCategoryFilterButtons();
                updateMeds(filterMedsByName(s));
                return false;
            }
            @Override
            public boolean onQueryTextSubmit(String s) {
                return false;
            }
        });
        medsCategoryLayout = medsRegisterView.findViewById(R.id.medsRegisterFragmentMedsCategoryLayout);
        initCategoryFilter();

        ListView medsListView = medsRegisterView.findViewById(R.id.medsRegisterFragmentMedsList);
        medsListView.setAdapter(adapter);
        medsListView.setOnItemClickListener((adapterView, view, i, l) -> {
            MedicamentWithCategory selectedMedicamentWithCategory = (MedicamentWithCategory) adapter.getItem(i);
            getActivity().getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.navigationViewContentFragment, new MedsRegisterDetailsFragment(selectedMedicamentWithCategory))
                    .addToBackStack(null)
                    .commit();
        });
        return medsRegisterView;
    }

    @Override
    public void onResume() {
        super.onResume();
        invalidateSearchFilter();
        invalidateCategoryFilterButtons();
        updateMeds(medsWithCategories);
    }

    private void initCategoryFilter(){
        List<String> categoryMarks = getUniqueCategoryMarks();
        Collections.sort(categoryMarks);
        createCategoryFilterButtons(categoryMarks);
    }

    private List<String> getUniqueCategoryMarks(){
        List<String> categoryMarks = new ArrayList<>();
        for(int i = 0; i < medsWithCategories.size(); ++i){
            String categoryMark= medsWithCategories.get(i).category.mark;
            if(!categoryMarks.contains(categoryMark)){
                categoryMarks.add(categoryMark);
            }
        }
        return categoryMarks;
    }
    private void createCategoryFilterButtons(List<String> categoryMarks){
        List<Button> categoryButtons = new ArrayList<>();
        for(int i = 0; i < categoryMarks.size(); i++){
            Button categoryMarkButton = new Button(getContext());
            categoryMarkButton.setId(i);
            categoryMarkButton.setText(categoryMarks.get(i));
            categoryMarkButton.setTextColor(ContextCompat.getColor(getContext(), R.color.white));
            categoryMarkButton.setTextSize(15);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(75, 90);
            params.setMargins(12, 0, 12, 0);
            categoryMarkButton.setLayoutParams(params);
            categoryMarkButton.setIncludeFontPadding(false);
            categoryMarkButton.setPadding(0,0,0,0);
            categoryMarkButton.setGravity(Gravity.CENTER);
            categoryMarkButton.setBackgroundResource(R.drawable.meds_register_fragment_meds_category_button_selection);
            categoryButtons.add(categoryMarkButton);
            medsCategoryLayout.addView(categoryMarkButton);

            categoryMarkButton.setOnClickListener(view -> {
                invalidateSearchFilter();
                int clickedButtonId = view.getId();
                for(int i1 = 0; i1 < categoryButtons.size(); ++i1){
                    Button button = categoryButtons.get(i1);
                    if(i1 == clickedButtonId){
                        notifyCategoryFilterChanged(button);
                        button.setSelected(!button.isSelected());
                    } else {
                        button.setSelected(false);
                    }
                }
            });
        }
    }
    private void notifyCategoryFilterChanged(Button button){
        if(button.isSelected()){
            updateMeds(medsWithCategories);
        } else {
            updateMeds(filterMedsByCategory(button.getText().toString()));
        }
    }

    private List<MedicamentWithCategory> filterMedsByCategory(String categoryMark){
        List<MedicamentWithCategory> filteredMedicamentsByCategory = new ArrayList<>();
        for(int j = 0; j < medsWithCategories.size(); j++){
            if(medsWithCategories.get(j).category.mark.equals(categoryMark)){
                filteredMedicamentsByCategory.add(medsWithCategories.get(j));
            }
        }
        return filteredMedicamentsByCategory;
    }

    private List<MedicamentWithCategory> filterMedsByName(String medName){
        List<MedicamentWithCategory> filteredMedicamentsByName = new ArrayList<>();
        for (MedicamentWithCategory medicamentWithCategory: medsWithCategories) {
            if((medicamentWithCategory.med.name.toLowerCase()).contains(medName.toLowerCase())){
                filteredMedicamentsByName.add(medicamentWithCategory);
            }
        }
        return filteredMedicamentsByName;
    }

    private void updateMeds(List<MedicamentWithCategory> meds){
        adapter.setData(meds);
        adapter.notifyDataSetChanged();
    }

    private void invalidateSearchFilter(){
        searchView.setQuery("", false);
        searchView.clearFocus();
    }

    private void invalidateCategoryFilterButtons(){
        int numberOfCreatedButtons = medsCategoryLayout.getChildCount();
        for(int i = 0; i < numberOfCreatedButtons; ++i){
            medsCategoryLayout.getChildAt(i).setSelected(false);
        }
    }

    private List<Medicament> fetchMedicaments() throws IOException {
        List<Medicament> meds = new ArrayList<>();
        URL url = new URL("https://api.farmaceut.ba/test/medicaments");
        HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
        InputStream in = new BufferedInputStream(urlConnection.getInputStream());
        meds = readMedsStream(in);
        urlConnection.disconnect();
        return meds;
    }

    private List<Medicament> readMedsStream(InputStream inputStream) throws IOException {
        MedsStreamParser medsParser = new MedsStreamParser(inputStream);
        return medsParser.parse();
    }

    private List<Category> fetchCategories() throws IOException {
        List<Category> categories = new ArrayList<>();
        URL url = new URL("https://api.farmaceut.ba/test/categories");
        HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
        InputStream in = new BufferedInputStream(urlConnection.getInputStream());
        categories = readCategoriesStream(in);
        urlConnection.disconnect();
        return categories;
    }
    private List<Category> readCategoriesStream(InputStream inputStream) throws IOException {
        CategoriesStreamParser categoriesParser = new CategoriesStreamParser(inputStream);
        return categoriesParser.parse();
    }
}