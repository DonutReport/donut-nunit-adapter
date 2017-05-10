package com.magentys.donut.adapters;

import com.magentys.donut.gherkin.model.*;
import com.sun.org.apache.xerces.internal.dom.DeferredElementImpl;
import org.apache.commons.lang3.StringUtils;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.*;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class NUnitAdapter {

    private static final String PASSED = "passed";
    private static final String FAILED = "failed";
    public static final String UNIT_TEST_TYPE = "unit-test";
    public static final String UNIT_TEST_KEYWORD = "Unit Test";

    public List<Feature> transform(String absolutePath) throws Exception {
        return transform(extractDocument(absolutePath));
    }


    File readXml(String absolutePath) {
        return new File(absolutePath);
    }

    Document extractDocument(String absolutePath) throws ParserConfigurationException, IOException, SAXException {

        //Get the DOM Builder Factory
        DocumentBuilderFactory factory =
                DocumentBuilderFactory.newInstance();

        //Get the DOM Builder
        DocumentBuilder builder = factory.newDocumentBuilder();

        //Load and Parse the XML document
        //document contains the complete XML as a Tree.
        return builder.parse(readXml(absolutePath));
    }

    List<Feature> transform(Document document) throws Exception {
        List<Node> testFixtures = extractTestFixtures(document);

        List<Feature> features = new ArrayList<>();
        for (Node fixture : testFixtures) {
            features.add(makeFeature(fixture));
        }
        return features;
    }

    List<Node> extractTestFixtures(Document document) throws Exception {
        return getNodes(document, "//test-suite[@type=\"TestFixture\"]","test-suite");
    }

    List<Node> getNodes(Document document, String xpathString,String tagName) throws Exception {
        XPathFactory xPathfactory = XPathFactory.newInstance();
        XPath xpath = xPathfactory.newXPath();

        XPathExpression expr;
        expr = xpath.compile(xpathString);

        return getNodesByTagName((NodeList) expr.evaluate(document, XPathConstants.NODESET), tagName);
    }

    private Feature makeFeature(Node fixture) throws Exception {

        Feature feature = new Feature();
        feature.setName(StringUtils.defaultString(getProperty(fixture, "Feature", "Without feature")));
        feature.setDescription(StringUtils.defaultString(getProperty(fixture, "Description", "")));
        feature.setId(((DeferredElementImpl) fixture).getAttribute("id"));
        feature.setKeyword("Feature");
        feature.setLine((int) (Math.random() * 1000));
        feature.setTags(Collections.singletonList(new Tag("@complete", (int) (Math.random() * 1000))));
        feature.setUri("Test URI");
        feature.setElements(makeElements(getNodesByTagName(fixture.getChildNodes(), "test-case")));

        return feature;
    }

    List<com.magentys.donut.gherkin.model.Element> makeElements(List<Node> testCases) throws Exception {
        List<com.magentys.donut.gherkin.model.Element> elements = new ArrayList<>();

        for (Node testCase : testCases) {
            com.magentys.donut.gherkin.model.Element element = new com.magentys.donut.gherkin.model.Element();

            // Set the unit test description property as the test name for donut
            String testCaseName = getProperty(testCase, "Description", ((DeferredElementImpl) testCase).getAttribute("name"));
            element.setName(testCaseName);

            // Setting description as an empty string, as for NUnit unit tests, it's unlikely that people will need a description for unit tests. Name should be sufficient.
            // Also, it makes more sense to use the built-in <code>Description<code> attribute in absence of a built-in <code>Name<code> attribute.
            element.setDescription("");
            element.setLine((int) (Math.random() * 1000));
            element.setKeyword(UNIT_TEST_KEYWORD);

            DeferredElementImpl testCaseElem = (DeferredElementImpl) testCase;
            element.setId(testCaseElem.getAttribute("id"));
            element.setSteps(makeSteps(testCaseElem, testCaseName));
            element.setType(UNIT_TEST_TYPE);

            elements.add(element);
        }

        if (elements.isEmpty()) {
            throw new Exception("No test cases were found");
        }
        return elements;
    }

    private List<Step> makeSteps(DeferredElementImpl testCaseElem, String testCaseName) throws Exception {
        Step step = new Step();
        long duration = (long) (Double.valueOf(testCaseElem.getAttribute("duration")) * 1000000);
        String status = makeStatus(testCaseElem.getAttribute("result"));

        if (status.equals(PASSED)) {
            step.setResult(new Result(PASSED, duration, null));
        } else {
            step.setResult(new Result(FAILED, duration, makeErrorMessage(testCaseElem)));
        }

        step.setKeyword("");
        step.setLine((int) (Math.random() * 1000));
        step.setName(testCaseName);
        step.setMatch(new Match(testCaseElem.getAttribute("fullname")));

        return Collections.singletonList(step);
    }

    private String makeErrorMessage(DeferredElementImpl testCaseElem) {
        try {
            Node failureNode = getNodeByTagName(testCaseElem.getChildNodes(), "failure");
            Node messageNode = getNodeByTagName(failureNode.getChildNodes(), "message");
            Node stackTraceNode = getNodeByTagName(failureNode.getChildNodes(), "stack-trace");

            return "Error message: " + StringUtils.normalizeSpace(messageNode.getTextContent()) + " Stack trace: " + StringUtils.normalizeSpace(stackTraceNode.getTextContent());
        } catch (Exception e) {
            return null;
        }
    }

    private String makeStatus(String result) throws Exception {

        if (result.equalsIgnoreCase(FAILED))
            return FAILED;
        if (result.equalsIgnoreCase(PASSED))
            return PASSED;

        throw new Exception("Unknown result value of: " + result);
    }

    private String getProperty(Node node, String propertyName, String defaultValue) {
        try {
            NodeList properties = getNodeByTagName(node.getChildNodes(), "properties").getChildNodes();
            for (int i = 0; i < properties.getLength(); i++) {

                Node property = properties.item(i);
                if (property instanceof Element && ((Element) property).getAttribute("name").equals(propertyName)) {
                    return ((Element) property).getAttribute("value");
                }
            }
        } catch (Exception e) {
            // Ignore
        }
        return defaultValue;
    }

    private Node getNodeByTagName(NodeList nodeList, String tagName) throws Exception {
        return getNodesByTagName(nodeList, tagName).get(0);
    }

    List<Node> getNodesByTagName(NodeList nodeList, String tagName) throws Exception {
        List<Node> nodes = new ArrayList<>();

        if(nodeList.getLength() == 0){
            throw new Exception("There are no elements in the node with id: " + ((Element)nodeList).getAttribute("id") + " and name: "+ ((Element)nodeList).getAttribute("name"));
        }

        for (int i = 0; i < nodeList.getLength(); i++) {

            Node node = nodeList.item(i);
            if (node instanceof Element && ((Element) node).getTagName().equals(tagName)) {
                nodes.add(node);
            }
        }

        if (nodes.isEmpty()) {
            throw new Exception("Element with tag name: " + tagName + " not found.");
        }
        return nodes;
    }
}
