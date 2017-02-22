package com.magentys.donut.adapters;

import com.magentys.donut.gherkin.model.Element;
import com.magentys.donut.gherkin.model.Feature;
import org.apache.commons.io.FileUtils;
import org.junit.Before;
import org.junit.Test;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.IOException;
import java.util.List;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class NUnitAdapterTest {

    private NUnitAdapter nUnitAdapter;
    private String absolutePath1 = FileUtils.toFile(NUnitAdapterTest.class.getResource("/nunit/sample-1/TestResult.xml")).getAbsolutePath();
    private String absolutePath2 = FileUtils.toFile(NUnitAdapterTest.class.getResource("/nunit/sample-2/TestResult.xml")).getAbsolutePath();


    @Before
    public void setUp() {
        nUnitAdapter = new NUnitAdapter();
    }

    @Test
    public void shouldBeAbleToReadTheXmlFileUsingAnAbsolutePath() throws IOException {
        File xmlFile = nUnitAdapter.readXml(absolutePath1);

        assertTrue(xmlFile != null);
        assertTrue(xmlFile.getName().equals("TestResult.xml"));
    }

    @Test
    public void shouldBeAbleToTransformTheReadXmlFileToDocument() throws IOException, SAXException, ParserConfigurationException {
        Document document = nUnitAdapter.extractDocument(absolutePath1);

        assertTrue(document != null);
        assertTrue(document.getDocumentElement().getTagName().equals("test-run"));
    }

    // Behavior
    @Test
    public void shouldBeAbleToTransformTheDocumentToFeaturesList() throws Exception {
        Document document = nUnitAdapter.extractDocument(absolutePath1);
        List<Feature> features = nUnitAdapter.transform(document);

        assertFalse(features.isEmpty());
    }

    @Test
    public void shouldBeAbleToTransformResultXmlWithFailure() throws Exception {
        Document document = nUnitAdapter.extractDocument(absolutePath2);
        List<Feature> features = nUnitAdapter.transform(document);
        List<Element> scenarios = features.get(0).getElements();
        Element scenario = scenarios.get(0);

        assertTrue(features.size() == 1);
        assertTrue(scenarios.size() == 2);
        assertTrue(scenario.getName().equals("Comparison"));
        assertTrue(scenario.getSteps().get(0).getResult().getErrorMessage().contains("Error message:"));

    }
}
