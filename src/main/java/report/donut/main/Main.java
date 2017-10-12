package report.donut.main;

import org.apache.commons.lang3.StringUtils;
import org.kohsuke.args4j.CmdLineException;
import org.kohsuke.args4j.CmdLineParser;
import org.kohsuke.args4j.Option;
import report.donut.adapters.NUnitAdapter;
import report.donut.reporter.Reporter;

import java.io.File;
import java.io.IOException;

public class Main {
    @Option(name = "-p", aliases = "--nunit-result-xml-path", usage = "path of nunit result xml", required = true)
    private String resultXmlPath;
    @Option(name = "-o", aliases = "--outputdir", usage = "the directory for storing the json reports")
    private String outputDir;

    public static void main(
            String[] args) {

        new Main().doMain(args);
    }

    private void doMain(
            String[] args) {
        CmdLineParser parser = new CmdLineParser(this);
        try {
            parser.parseArgument(args);
            ensureResultXmlDirectory();

            generateDonutJsonFiles();

        }catch (CmdLineException c){
            System.err.println(c.getMessage());
            System.err.println("usage: Main [options ...]");
            parser.printUsage(System.err);
            System.exit(1);
        }catch (Exception e) {
            System.err.println(e.getMessage());
            System.exit(1);
        }
    }

    private void generateDonutJsonFiles() throws Exception {
        NUnitAdapter adapter = new NUnitAdapter();
        Reporter reporter = new Reporter();
        reporter.writeJsons(adapter.transform(resultXmlPath), outputDir);

        if (StringUtils.isBlank(outputDir)) {
            System.out.println("JSON reports saved at location: " + new File(".", Reporter.OUTPUT_FOLDER_NAME).getAbsolutePath());
        } else {
            System.out.println("JSON reports saved at location: " + outputDir);
        }
    }

    private void ensureResultXmlDirectory()
            throws IOException {
        File file = new File(resultXmlPath);
        if (!file.exists())
            throw new IOException("The test results xml file [" + file.getAbsolutePath() + "] does not exist.");

        if(!file.isFile())
            throw new IOException("The test results xml file [" + file.getAbsolutePath() + "] is not a file.");
    }
}