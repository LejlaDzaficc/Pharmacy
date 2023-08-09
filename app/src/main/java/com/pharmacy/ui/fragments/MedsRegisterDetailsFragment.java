package com.pharmacy.ui.fragments;

import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.pharmacy.R;
import com.pharmacy.common.SubstanceStreamParser;
import com.squareup.picasso.Picasso;
import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import com.pharmacy.db.relations.MedicamentWithCategory;

public class MedsRegisterDetailsFragment extends Fragment {
    private final MedicamentWithCategory selectedMedicamentWithCategory;
    private ImageView medImage;
    private TextView medActiveSubstanceName;

    public MedsRegisterDetailsFragment(MedicamentWithCategory selectedMedicamentWithCategory) {
        this.selectedMedicamentWithCategory =  selectedMedicamentWithCategory;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ExecutorService executorService = Executors.newSingleThreadExecutor();
        Handler handler = new Handler(Looper.getMainLooper());
        executorService.execute(() -> {
            String activeSubstanceName = "";
            try {
                URL url = new URL("https://api.farmaceut.ba/test/substances?drugid=" + selectedMedicamentWithCategory.med.id);
                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                InputStream in = new BufferedInputStream(urlConnection.getInputStream());
                activeSubstanceName = readSubstanceStream(in);
                urlConnection.disconnect();
            } catch (IOException e) {
                handler.post(() -> {
                    Toast.makeText(getContext(), R.string.meds_registar_fragment_connection_error, Toast.LENGTH_LONG).show();
                });
            }
            String activeSubstanceLabel = activeSubstanceName.equals("") ?
                    getString(R.string.meds_registar_details_fragment_active_substance_name) : activeSubstanceName;

            handler.post(() -> {
                medActiveSubstanceName.setText(activeSubstanceLabel);
                medActiveSubstanceName.setBackgroundResource(R.drawable.meds_register_details_fragment_supstances_background);
            });
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_meds_register_details, container, false);

        medImage = view.findViewById(R.id.medsRegisterDetailsFragmentMedImage);
        Picasso.get()
                .load("https://cdn-icons-png.flaticon.com/512/883/883356.png")
                .resize(160, 180)
                .error(R.drawable.ic_default_image)
                .placeholder(R.drawable.progress_animation)
                .into(medImage);

        TextView medName = view.findViewById(R.id.medsRegisterDetailsFragmentMedName);
        TextView medATC = view.findViewById(R.id.medsRegisterDetailsFragmentMedATC);
        TextView medCategory = view.findViewById(R.id.medsRegisterDetailsFragmentMedCategory);
        TextView medShortDescr = view.findViewById(R.id.medsRegisterDetailsFragmentMedShortDescr);
        TextView medActiveSubstanceValue = view.findViewById(R.id.medsRegisterDetailsFragmentMedActiveSubstanceValue);
        TextView medActiveSubstanceSelectedQuantityValue = view.findViewById(R.id.medsRegisterDetailsFragmentMedActiveSubstanceSelectedQuantityValue);
        TextView medRecommendedDailyDoseValue = view.findViewById(R.id.medsRegisterDetailsFragmentMedRecommendedDailyDoseValue);
        medActiveSubstanceName = view.findViewById(R.id.medsRegisterDetailsFragmentMedActiveSubstanceName);
        medActiveSubstanceName.setBackgroundResource(R.drawable.progress_animation);
        TextView medDescr = view.findViewById(R.id.medsRegisterDetailsFragmentMedDescr);

        medName.setText(selectedMedicamentWithCategory.med.name);
        medATC.setText(selectedMedicamentWithCategory.med.atc);
        medCategory.setText(selectedMedicamentWithCategory.category.name);

        if(selectedMedicamentWithCategory.med.shortDescription != null){
            medShortDescr.setVisibility(View.VISIBLE);
            medShortDescr.setText(selectedMedicamentWithCategory.med.shortDescription);
        }

        if (selectedMedicamentWithCategory.med.activeSubstanceValue == 0 || selectedMedicamentWithCategory.med.activeSubstanceMeasurementUnit == null) {
            medActiveSubstanceValue.setText("/");
        } else {
            String activeSubstanceValue = selectedMedicamentWithCategory.med.activeSubstanceValue + " " + selectedMedicamentWithCategory.med.activeSubstanceMeasurementUnit;
            medActiveSubstanceValue.setText(activeSubstanceValue);
        }

        if (selectedMedicamentWithCategory.med.activeSubstanceSelectedQuantity == 0 || selectedMedicamentWithCategory.med.activeSubstanceQuantityMeasurementUnit == null) {
            medActiveSubstanceSelectedQuantityValue.setText("/");
        } else {
            String activeSubstanceValue = selectedMedicamentWithCategory.med.activeSubstanceSelectedQuantity + " " + selectedMedicamentWithCategory.med.activeSubstanceQuantityMeasurementUnit;
            medActiveSubstanceSelectedQuantityValue.setText(activeSubstanceValue);
        }

        if (selectedMedicamentWithCategory.med.minimumDailyDose == 0 && selectedMedicamentWithCategory.med.maximumDailyDose == 0) {
            medRecommendedDailyDoseValue.setText("/");
        } else {
            String activeSubstanceValue = selectedMedicamentWithCategory.med.minimumDailyDose + " - " + selectedMedicamentWithCategory.med.maximumDailyDose;
            medRecommendedDailyDoseValue.setText(activeSubstanceValue);
        }

        if(selectedMedicamentWithCategory.med.description != null){
            view.findViewById(R.id.divider2).setVisibility(View.VISIBLE);
            medDescr.setText(selectedMedicamentWithCategory.med.description);
        }
        return view;
    }

    private String readSubstanceStream(InputStream inputStream) throws IOException {
        SubstanceStreamParser substanceParser = new SubstanceStreamParser(inputStream);
        return substanceParser.parse();
    }
}