package com.pharmacy.common;

import android.util.JsonReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import com.pharmacy.db.domain.Category;

public class CategoriesStreamParser {
    private final InputStream inputStream;

    public CategoriesStreamParser(InputStream inputStream){
        this.inputStream = inputStream;
    }

    public List<Category> parse() throws IOException {
        JsonReader reader = new JsonReader(new InputStreamReader(this.inputStream, "UTF-8"));
        return parseArray(reader);
    }

    private List<Category> parseArray(JsonReader reader) throws IOException {
        List<Category> categories = new ArrayList<>();
        reader.beginArray();
        while (reader.hasNext()) {
            categories.add(parseItem(reader));
        }
        reader.endArray();
        return categories;
    }
    public Category parseItem(JsonReader reader) throws IOException {
        Category category = new Category();
        reader.beginObject();
        while (reader.hasNext()) {
            String key = reader.nextName();
            if (key.equals("id")) {
                category.id = reader.nextInt();
            } else if (key.equals("mark")) {
                category.mark = reader.nextString();
            } else if (key.equals("name")) {
                category.name = reader.nextString();
            } else if (key.equals("color")) {
                category.color = reader.nextString();
            } else {
                reader.skipValue();
            }
        }
        reader.endObject();
        return category;
    }
}
