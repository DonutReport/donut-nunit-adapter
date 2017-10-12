package report.donut.reporter;

import org.apache.commons.io.FileUtils;
import org.junit.Before;
import org.junit.Test;
import report.donut.adapters.NUnitAdapter;
import report.donut.adapters.NUnitAdapterTest;
import report.donut.gherkin.model.Feature;

import java.io.File;
import java.util.List;

import static org.junit.Assert.assertTrue;

public class ReporterTest {

    private NUnitAdapter nUnitAdapter;
    private String absolutePath = FileUtils.toFile(NUnitAdapterTest.class.getResource("/nunit/sample-1/TestResult.xml")).getAbsolutePath();
    private Reporter reporter;

    @Before
    public void setUp() {
        nUnitAdapter = new NUnitAdapter();
        reporter = new Reporter();
    }

    // Behavior tests

    @Test
    public void shouldGenerateSameNumberOfJsonFilesAsNumberOfFeatures() throws Exception {
        List<Feature> features = nUnitAdapter.transform(absolutePath);
        File reportDir = FileUtils.toFile(NUnitAdapterTest.class.getResource("/nunit/"));
        reporter.writeJsons(features, reportDir.getAbsolutePath());

        assertTrue(features.size() == 1);
        assertTrue(FileUtils.listFiles(new File(reportDir, Reporter.OUTPUT_FOLDER_NAME), new String[]{"json"}, false).size() == 1);
    }

    @Test
    public void shouldGenerateJsonFilesInWorkingDirIfOutputDirIsBlank() throws Exception {
        List<Feature> features = nUnitAdapter.transform(absolutePath);
        File reportDir = FileUtils.toFile(NUnitAdapterTest.class.getResource("/nunit/"));
        reporter.writeJsons(features, reportDir.getAbsolutePath());

        assertTrue(features.size() == 1);
        assertTrue(FileUtils.listFiles(new File(reportDir, Reporter.OUTPUT_FOLDER_NAME), new String[]{"json"}, false).size() == 1);
    }

    @Test
    public void shouldGenerateJsonFilesInCurrentDirIfOutputDirIsBlank() throws Exception {
        List<Feature> features = nUnitAdapter.transform(absolutePath);
        reporter.writeJsons(features, "");

        assertTrue(features.size() == 1);
        assertTrue(FileUtils.listFiles(new File("./target", Reporter.OUTPUT_FOLDER_NAME), new String[]{"json"}, false).size() == 1);
    }
}
