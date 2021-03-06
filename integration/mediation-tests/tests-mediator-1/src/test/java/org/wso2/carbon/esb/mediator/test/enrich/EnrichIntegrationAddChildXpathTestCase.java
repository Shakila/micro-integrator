/*
 *Copyright (c) 2005-2010, WSO2 Inc. (http://www.wso2.org) All Rights Reserved.
 *
 *WSO2 Inc. licenses this file to you under the Apache License,
 *Version 2.0 (the "License"); you may not use this file except
 *in compliance with the License.
 *You may obtain a copy of the License at
 *
 *http://www.apache.org/licenses/LICENSE-2.0
 *
 *Unless required by applicable law or agreed to in writing,
 *software distributed under the License is distributed on an
 *"AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 *KIND, either express or implied.  See the License for the
 *specific language governing permissions and limitations
 *under the License.
 */
package org.wso2.carbon.esb.mediator.test.enrich;

import org.apache.axiom.om.OMAbstractFactory;
import org.apache.axiom.om.OMElement;
import org.apache.axiom.om.OMFactory;
import org.apache.axiom.om.OMNamespace;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import org.wso2.esb.integration.common.utils.ESBIntegrationTest;

import javax.xml.namespace.QName;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotNull;

public class EnrichIntegrationAddChildXpathTestCase extends ESBIntegrationTest {

    @BeforeClass(alwaysRun = true)
    public void setEnvironment() throws Exception {
        super.init();
        verifyProxyServiceExistence("enrichAddChildByXPathTestProxy");
    }

    @Test(groups = "wso2.esb", description = "Add custom content as a child to the part of message"
            + " specified by xpath expression ")
    public void testEnrichMediator() throws Exception {
        OMElement response;

        response = axis2Client
                .sendSimpleStockQuoteRequest(getProxyServiceURLHttp("enrichAddChildByXPathTestProxy"), null,
                        createQuoteRequestBody());
        assertNotNull(response, "Response message null");
        OMElement returnTag = response.getFirstElement();
        String symbolResponse = returnTag.getFirstChildWithName(new QName("http://services.samples/xsd", "symbol"))
                .getText();

        assertEquals(symbolResponse, "Test", "child symbol not added.");

    }

    @AfterClass(alwaysRun = true)
    public void close() throws Exception {
        super.cleanup();
    }

    //method to create a request message without the 'symbol' tag.
    private OMElement createQuoteRequestBody() {
        OMFactory fac = OMAbstractFactory.getOMFactory();
        OMNamespace omNs = fac.createOMNamespace("http://services.samples", "ns");
        OMElement method = fac.createOMElement("getQuote", omNs);
        OMElement value1 = fac.createOMElement("request", omNs);
        method.addChild(value1);
        return method;
    }

}
