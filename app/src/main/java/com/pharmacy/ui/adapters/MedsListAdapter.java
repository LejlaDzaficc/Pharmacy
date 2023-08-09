package com.pharmacy.ui.adapters;

import android.graphics.Color;
import android.widget.BaseAdapter;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.pharmacy.R;
import java.util.List;
import com.pharmacy.db.relations.MedicamentWithCategory;

public class MedsListAdapter extends BaseAdapter {
    private List<MedicamentWithCategory> medsWithCategories;
    private LayoutInflater inflater;

    public MedsListAdapter(Context context) {
        inflater = (LayoutInflater.from(context));
    }

    public void setData(List<MedicamentWithCategory> medsWithCategories) {
        this.medsWithCategories = medsWithCategories;
    }

    @Override
    public int getCount() {
        return medsWithCategories == null ? 0 : medsWithCategories.size();
    }

    @Override
    public Object getItem(int i) {
        return medsWithCategories.get(i);
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        view = inflater.inflate(R.layout.med_item, null);
        
        View medColor = view.findViewById(R.id.medsRegisterFragmentMedColor);
        TextView medName = view.findViewById(R.id.medsRegisterFragmentMedName);
        TextView medATC = view.findViewById(R.id.medsRegisterFragmentATCclassification);
        TextView medCategoryName = view.findViewById(R.id.medsRegisterFragmentMedCategory);
        
        medColor.setBackgroundColor(Color.parseColor(medsWithCategories.get(i).category.color));
        medName.setText(medsWithCategories.get(i).med.name);
        medATC.setText(medsWithCategories.get(i).med.atc);
        medCategoryName.setText(medsWithCategories.get(i).category.name);

        return view;
    }



}
