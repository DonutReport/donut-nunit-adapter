package report.donut.reporter;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

class EntityGsonBuilder {
    static Gson createGson() {
        return new GsonBuilder().setPrettyPrinting()
                .create();
    }
}
