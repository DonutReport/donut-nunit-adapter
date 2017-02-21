package com.magentys.donut.adapters;

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
    private String absolutePath = FileUtils.toFile(NUnitAdapterTest.class.getResource("/nunit/sample-1/TestResult.xml")).getAbsolutePath();

    @Before
    public void setUp() {
        nUnitAdapter = new NUnitAdapter();
    }

    @Test
    public void shouldBeAbleToReadTheXmlFileUsingAnAbsolutePath() throws IOException {
        File xmlFile = nUnitAdapter.readXml(absolutePath);

        assertTrue(xmlFile != null);
        assertTrue(xmlFile.getName().equals("TestResult.xml"));
    }

    @Test
    public void shouldBeAbleToTransformTheReadXmlFileToDocument() throws IOException, SAXException, ParserConfigurationException {
        Document document = nUnitAdapter.extractDocument(absolutePath);

        assertTrue(document != null);
        assertTrue(document.getDocumentElement().getTagName().equals("test-run"));
    }

    // Behavior
    @Test
    public void shouldBeAbleToTransformTheDocumentToFeaturesList() throws Exception {
        Document document = nUnitAdapter.extractDocument(absolutePath);
        List<Feature> features = nUnitAdapter.transform(document);

        assertFalse(features.isEmpty());
    }
}
