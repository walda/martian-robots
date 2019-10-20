package com.guidesmiths.robots.config;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathFactory;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

@RunWith(PowerMockRunner.class)
@PrepareForTest({ DocumentBuilderFactory.class, XPathFactory.class })
public class MartianRobotsConfigTest {

    @Test
    public void shouldReturnDocumentBuilder() throws ParserConfigurationException {

        var documentBuilderFactory = mock(DocumentBuilderFactory.class);
        var documentBuilder = mock(DocumentBuilder.class);

        when(documentBuilderFactory.newDocumentBuilder()).thenReturn(documentBuilder);

        PowerMockito.mockStatic(DocumentBuilderFactory.class);
        PowerMockito.when(DocumentBuilderFactory.newInstance()).thenReturn(documentBuilderFactory);

        assertThat(new MartianRobotsConfig().documentBuilder())
                .isNotNull()
                .isInstanceOf(DocumentBuilder.class)
                .isEqualTo(documentBuilder);

        PowerMockito.verifyStatic(DocumentBuilderFactory.class);
        DocumentBuilderFactory.newInstance();

        verify(documentBuilderFactory).newDocumentBuilder();

        verifyNoMoreInteractions(documentBuilderFactory);
        PowerMockito.verifyNoMoreInteractions(DocumentBuilderFactory.class);
    }

    @Test
    public void shouldReturnXPath() {

        var xPath = mock(XPath.class);
        var xPathFactory = mock(XPathFactory.class);

        when(xPathFactory.newXPath()).thenReturn(xPath);

        PowerMockito.mockStatic(XPathFactory.class);
        PowerMockito.when(XPathFactory.newInstance()).thenReturn(xPathFactory);

        assertThat(new MartianRobotsConfig().xPath())
                .isNotNull()
                .isInstanceOf(XPath.class)
                .isEqualTo(xPath);

        verify(xPathFactory).newXPath();
        PowerMockito.verifyStatic(XPathFactory.class);
        XPathFactory.newInstance();

        verifyNoMoreInteractions(xPath, xPathFactory);
        PowerMockito.verifyNoMoreInteractions(XPathFactory.class);
    }
}
