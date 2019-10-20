package com.guidesmiths.robots.rule;

import com.guidesmiths.robots.exception.InvalidRuleException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathExpressionException;

import java.io.IOException;
import java.io.InputStream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
public class RuleEngineServiceTest {

    @Mock
    private DocumentBuilder documentBuilder;
    @Mock
    private Document document;
    @Mock
    private XPath xPath;
    @Mock
    private NodeList nodeList;
    @Mock
    private XPathExpression xPathExpression;
    @Mock
    private Node node;
    @Mock
    private NamedNodeMap namedNodeMap;
    @InjectMocks
    private RuleEngineService ruleEngineService;

    @Test
    public void shouldReturnActions() throws IOException, SAXException, XPathExpressionException {

        when(documentBuilder.parse(any(InputStream.class))).thenReturn(document);
        when(xPath.compile("/rules/rule[@instruction='F']/directions/direction[@currentValue='E']"))
                .thenReturn(xPathExpression);
        when(xPath.compile("/rules/rule[@instruction='F']/movements/movement[@direction='E']"))
                .thenReturn(xPathExpression);
        when(xPathExpression.evaluate(document, XPathConstants.NODESET))
                .thenReturn(nodeList);
        when(nodeList.getLength()).thenReturn(1);
        when(nodeList.item(0)).thenReturn(node);
        when(node.getAttributes()).thenReturn(namedNodeMap);
        when(namedNodeMap.getNamedItem("nextValue")).thenReturn(node);
        when(namedNodeMap.getNamedItem("action")).thenReturn(node);
        when(node.getNodeValue()).thenReturn("W", "INCREASE_X");


        assertThat(ruleEngineService.calculateActions('F', 'E'))
        .isNotNull()
        .containsExactly(Action.DIRECTION_W, Action.INCREASE_X);

        verify(documentBuilder).parse(any(InputStream.class));
        verify(xPath).compile("/rules/rule[@instruction='F']/directions/direction[@currentValue='E']");
        verify(xPath).compile("/rules/rule[@instruction='F']/movements/movement[@direction='E']");
        verify(xPathExpression, times(2)).evaluate(document, XPathConstants.NODESET);
        verify(nodeList, times(4)).getLength();
        verify(nodeList, times(2)).item(0);
        verify(node, times(2)).getAttributes();
        verify(namedNodeMap).getNamedItem("nextValue");
        verify(namedNodeMap).getNamedItem("action");
        verify(node, times(2)).getNodeValue();
    }

    @Test
    public void shouldReturnActionsOnlyForDirection() throws IOException, SAXException, XPathExpressionException {
        when(documentBuilder.parse(any(InputStream.class))).thenReturn(document);
        when(xPath.compile("/rules/rule[@instruction='F']/directions/direction[@currentValue='E']"))
                .thenReturn(xPathExpression);
        when(xPath.compile("/rules/rule[@instruction='F']/movements/movement[@direction='E']"))
                .thenReturn(xPathExpression);
        when(xPathExpression.evaluate(document, XPathConstants.NODESET))
                .thenReturn(nodeList);
        when(nodeList.getLength()).thenReturn(1, 0);
        when(nodeList.item(0)).thenReturn(node);
        when(node.getAttributes()).thenReturn(namedNodeMap);
        when(namedNodeMap.getNamedItem("nextValue")).thenReturn(node);
        when(namedNodeMap.getNamedItem("action")).thenReturn(node);
        when(node.getNodeValue()).thenReturn("W");


        assertThat(ruleEngineService.calculateActions('F', 'E'))
                .isNotNull()
                .containsExactly(Action.DIRECTION_W);

        verify(documentBuilder).parse(any(InputStream.class));
        verify(xPath).compile("/rules/rule[@instruction='F']/directions/direction[@currentValue='E']");
        verify(xPath).compile("/rules/rule[@instruction='F']/movements/movement[@direction='E']");
        verify(xPathExpression, times(2)).evaluate(document, XPathConstants.NODESET);
        verify(nodeList, times(3)).getLength();
        verify(nodeList).item(0);
        verify(node).getAttributes();
        verify(namedNodeMap).getNamedItem("nextValue");
        verify(node).getNodeValue();
    }

    @Test
    public void shouldReturnActionsOnlyForMovements() throws IOException, SAXException, XPathExpressionException {

        when(documentBuilder.parse(any(InputStream.class))).thenReturn(document);
        when(xPath.compile("/rules/rule[@instruction='F']/directions/direction[@currentValue='E']"))
                .thenReturn(xPathExpression);
        when(xPath.compile("/rules/rule[@instruction='F']/movements/movement[@direction='E']"))
                .thenReturn(xPathExpression);
        when(xPathExpression.evaluate(document, XPathConstants.NODESET))
                .thenReturn(nodeList);
        when(nodeList.getLength()).thenReturn(0,1);
        when(nodeList.item(0)).thenReturn(node);
        when(node.getAttributes()).thenReturn(namedNodeMap);
        when(namedNodeMap.getNamedItem("nextValue")).thenReturn(node);
        when(namedNodeMap.getNamedItem("action")).thenReturn(node);
        when(node.getNodeValue()).thenReturn("INCREASE_X");


        assertThat(ruleEngineService.calculateActions('F', 'E'))
                .isNotNull()
                .containsExactly(Action.INCREASE_X);

        verify(documentBuilder).parse(any(InputStream.class));
        verify(xPath).compile("/rules/rule[@instruction='F']/directions/direction[@currentValue='E']");
        verify(xPath).compile("/rules/rule[@instruction='F']/movements/movement[@direction='E']");
        verify(xPathExpression, times(2)).evaluate(document, XPathConstants.NODESET);
        verify(nodeList, times(3)).getLength();
        verify(nodeList).item(0);
        verify(node).getAttributes();
        verify(namedNodeMap).getNamedItem("action");
        verify(node).getNodeValue();
    }

    @Test
    public void shouldRaiseInvalidRuleExceptionWhenIOException() throws IOException, SAXException {
        when(documentBuilder.parse(any(InputStream.class))).thenThrow(new IOException());

        Throwable t = catchThrowable(() -> ruleEngineService.calculateActions('F', 'N'));

        assertThat(t)
                .isNotNull()
                .isInstanceOf(InvalidRuleException.class)
                .hasMessage("No rule found for instruction F and direction N");

        verify(documentBuilder).parse(any(InputStream.class));
    }

    @Test
    public void shouldRaiseInvalidRuleExceptionWhenSAXException() throws IOException, SAXException {
        when(documentBuilder.parse(any(InputStream.class))).thenThrow(new SAXException());

        Throwable t = catchThrowable(() -> ruleEngineService.calculateActions('F', 'N'));

        assertThat(t)
                .isNotNull()
                .isInstanceOf(InvalidRuleException.class)
                .hasMessage("No rule found for instruction F and direction N");

        verify(documentBuilder).parse(any(InputStream.class));
    }

    @Test
    public void shouldRaiseInvalidRuleExceptionWhenXPathExpressionException() throws IOException, SAXException, XPathExpressionException {

        when(documentBuilder.parse(any(InputStream.class))).thenReturn(document);
        when(xPath.compile(anyString())).thenThrow(new XPathExpressionException(""));

        Throwable t = catchThrowable(() -> ruleEngineService.calculateActions('F', 'N'));

        assertThat(t)
                .isNotNull()
                .isInstanceOf(InvalidRuleException.class)
                .hasMessage("No rule found for instruction F and direction N");

        verify(documentBuilder).parse(any(InputStream.class));
        verify(xPath).compile("/rules/rule[@instruction='F']/directions/direction[@currentValue='N']");
    }

    @AfterEach
    public void tearDown() {
        verifyNoMoreInteractions(documentBuilder, xPath, xPathExpression, nodeList, node, namedNodeMap);
    }

}
