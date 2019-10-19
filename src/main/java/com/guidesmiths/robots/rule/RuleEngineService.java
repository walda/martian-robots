package com.guidesmiths.robots.rule;

import com.guidesmiths.robots.exception.InvalidRuleException;
import org.springframework.stereotype.Service;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service
public class RuleEngineService {

    private static final String RULES_FILE_PATH = "/rules/rules.xml";
    private static final String DIRECTION_QUERY_PATTERN = "/rules/rule[@instruction='%s']/directions/direction[@currentValue='%s']";
    private static final String MOVEMENT_QUERY_PATTERN = "/rules/rule[@instruction='%s']/movements/movement[@direction='%s']";

    public List<Action> calculateActions(char instruction, char direction) {
        var actionList = new ArrayList<Action>();
        try {
            var builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
            var xmlDocument = builder.parse(this.getClass().getResourceAsStream(RULES_FILE_PATH));

            var xPath = XPathFactory.newInstance().newXPath();

            actionList.addAll(loadDirectionActions(xmlDocument, xPath, instruction, direction));
            actionList.addAll(loadMovementActions(xmlDocument, xPath, instruction, direction));


        } catch (ParserConfigurationException | IOException | SAXException | XPathExpressionException e) {
            throw new InvalidRuleException(instruction, direction);
        }

        return Collections.unmodifiableList(actionList);
    }

    private static List<Action> loadDirectionActions(Document xmlDocument, XPath xPath, char instruction, char direction)
            throws XPathExpressionException {
        var expr = xPath.compile(String.format(DIRECTION_QUERY_PATTERN, instruction, direction));
        var nodes = (NodeList) expr.evaluate(xmlDocument, XPathConstants.NODESET);

        var actionList = new ArrayList<Action>();

        for(int i=0; i < nodes.getLength(); i++) {
            String value = nodes.item(i).getAttributes().getNamedItem("nextValue").getNodeValue();
            actionList.add(Action.fromString(value));
        }
        return actionList;
    }

    private static List<Action> loadMovementActions(Document xmlDocument, XPath xPath, char instruction, char direction)
            throws XPathExpressionException {
        var expr = xPath.compile(String.format(MOVEMENT_QUERY_PATTERN, instruction, direction));
        var nodes = (NodeList) expr.evaluate(xmlDocument, XPathConstants.NODESET);

        var actionList = new ArrayList<Action>();

        for(int i=0; i < nodes.getLength(); i++) {
            String value = nodes.item(i).getAttributes().getNamedItem("action").getNodeValue();
            actionList.add(Action.valueOf(value));
        }
        return actionList;
    }
}
