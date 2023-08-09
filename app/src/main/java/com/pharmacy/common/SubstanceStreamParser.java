package com.pharmacy.common;

import android.util.JsonReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class SubstanceStreamParser {
    private final InputStream inputStream;

    public SubstanceStreamParser(InputStream inputStream){
        this.inputStream = inputStream;
    }

    public String parse() throws IOException {
        JsonReader reader = new JsonReader(new InputStreamReader(this.inputStream, "UTF-8"));
        return parseItem(reader);
    }

    public String parseItem(JsonReader reader) throws IOException {
        String activeSubstanceName = "";

        reader.beginArray();
        reader.beginObject();
        while (reader.hasNext()) {
            String key = reader.nextName();
            if (key.equals("id")) {
                reader.nextInt();
            } else if (key.equals("name")) {
                activeSubstanceName = reader.nextString();
            }
        }
        reader.endObject();
        reader.endArray();

        return activeSubstanceName;
    }
}
