package com.pharmacy.common;

import android.util.JsonReader;
import android.util.JsonToken;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import com.pharmacy.db.domain.Medicament;

public class MedsStreamParser {
    private final InputStream inputStream;
    
    public MedsStreamParser(InputStream inputStream){
        this.inputStream = inputStream;
    }

    public List<Medicament> parse() throws IOException {
        JsonReader reader = new JsonReader(new InputStreamReader(this.inputStream, "UTF-8"));
        return parseArray(reader);
    }

    private List<Medicament> parseArray(JsonReader reader) throws IOException {
        List<Medicament> meds = new ArrayList<>();
        reader.beginArray();
        while (reader.hasNext()) {
            meds.add(parseItem(reader));
        }
        reader.endArray();
        return meds;
    }
    public Medicament parseItem(JsonReader reader) throws IOException {
        Medicament medicament = new Medicament();
        reader.beginObject();
        while (reader.hasNext()) {
            String key = reader.nextName();
            if (key.equals("id")) {
                medicament.id = reader.nextInt();
            } else if (key.equals("name")) {
                medicament.name = reader.nextString();
            } else if (key.equals("atc")) {
                medicament.atc = reader.nextString();
            } else if (key.equals("shortDescription") && reader.peek() != JsonToken.NULL) {
                medicament.shortDescription = reader.nextString();
            } else if (key.equals("description") && reader.peek() != JsonToken.NULL) {
                medicament.description = reader.nextString();
            } else if (key.equals("categoryId")) {
                medicament.categoryId = reader.nextInt();
            } else if (key.equals("activeSubstanceValue")) {
                medicament.activeSubstanceValue = reader.nextInt();
            } else if (key.equals("activeSubstanceMeasurementUnit") && reader.peek() != JsonToken.NULL) {
                medicament.activeSubstanceMeasurementUnit = reader.nextString();
            } else if (key.equals("activeSubstanceSelectedQuantity")) {
                medicament.activeSubstanceSelectedQuantity = reader.nextInt();
            } else if (key.equals("activeSubstanceQuantityMeasurementUnit") && reader.peek() != JsonToken.NULL) {
                medicament.activeSubstanceQuantityMeasurementUnit = reader.nextString();
            } else if (key.equals("minimumDailyDose")) {
                medicament.minimumDailyDose = reader.nextInt();
            } else if (key.equals("maximumDailyDose")) {
                medicament.maximumDailyDose = reader.nextInt();
            } else if (key.equals("showOnCalculator")) {
                medicament.showOnCalculator = reader.nextBoolean();
            } else if (key.equals("forbiddenInPregnancy")) {
                medicament.forbiddenInPregnancy = reader.nextBoolean();
            } else {
                reader.skipValue();
            }
        }
        reader.endObject();
        return medicament;
    }
}
