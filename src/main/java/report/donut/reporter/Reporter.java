package report.donut.reporter;

import com.google.gson.Gson;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import report.donut.gherkin.model.Feature;

import java.io.File;
import java.io.IOException;
import java.util.List;

public class Reporter {
    public static final String OUTPUT_FOLDER_NAME = "nunit-reports";
    private final Gson gson = EntityGsonBuilder.createGson();

    public void writeJsons(List<Feature> features, String outputDir) {
        for (Feature feature : features) {

            String json = gson.toJson(new Feature[]{feature}, Feature[].class);
            String fileName = (features.indexOf(feature) + 1) + ".json";


            if (StringUtils.isBlank(outputDir)) {
                if (new File("./target").exists()) {
                    outputDir = "./target";
                } else {
                    outputDir = ".";
                }
            }
            File jsonFile = new File(outputDir, OUTPUT_FOLDER_NAME + File.separator + fileName);

            try {
                FileUtils.writeStringToFile(jsonFile, json, "UTF-8");
            } catch (IOException e) {
                System.out.println("Couldn't write to file" + jsonFile.getAbsolutePath());
            }
        }

    }
}
