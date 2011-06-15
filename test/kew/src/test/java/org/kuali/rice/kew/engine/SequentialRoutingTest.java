/*
 * Copyright 2005-2007 The Kuali Foundation
 * 
 * 
 * Licensed under the Educational Community License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 * http://www.opensource.org/licenses/ecl2.php
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.kuali.rice.kew.engine;


import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.List;

import org.junit.Test;
import org.kuali.rice.kew.api.action.ActionRequestStatus;
import org.kuali.rice.kew.dto.ActionRequestDTO;
import org.kuali.rice.kew.engine.node.RouteNodeInstance;
import org.kuali.rice.kew.routeheader.DocumentRouteHeaderValue;
import org.kuali.rice.kew.service.KEWServiceLocator;
import org.kuali.rice.kew.service.WorkflowDocument;
import org.kuali.rice.kew.test.KEWTestCase;
import org.kuali.rice.kew.util.KEWConstants;

public class SequentialRoutingTest extends KEWTestCase {
    
    
    private static final String DOCUMENT_TYPE_NAME = "SeqDocType";
	private static final String ADHOC_NODE = "AdHoc";
	private static final String WORKFLOW_DOCUMENT_NODE = "WorkflowDocument";
    private static final String ACKNOWLEDGE_1_NODE = "Acknowledge1";
    private static final String ACKNOWLEDGE_2_NODE = "Acknowledge2";
	    
    protected void loadTestData() throws Exception {
        loadXmlFile("EngineConfig.xml");
    }
        
    @Test public void testSequentialRoute() throws Exception {
    	WorkflowDocument document = WorkflowDocument.createDocument(getPrincipalIdForName("ewestfal"), DOCUMENT_TYPE_NAME);
    	document.saveRoutingData();
    	assertNotNull(document.getDocumentId());
    	assertTrue("Document should be initiatied", document.stateIsInitiated());
    	assertEquals("Invalid route level.", new Integer(0), document.getRouteHeader().getDocRouteLevel());
    	String[] nodeNames = document.getNodeNames();
    	assertEquals("Wrong number of node names.", 1, nodeNames.length);
    	assertEquals("Wrong node name.", ADHOC_NODE, nodeNames[0]);
    	document.routeDocument("Routing sequentially.");
        
        // should have generated a request to "bmcgough"
    	document = WorkflowDocument.loadDocument(getPrincipalIdForName("bmcgough"), document.getDocumentId());
        assertTrue("Document should be enroute", document.stateIsEnroute());
    	assertEquals("Invalid route level.", new Integer(1), document.getRouteHeader().getDocRouteLevel());
    	nodeNames = document.getNodeNames();
    	assertEquals("Wrong number of node names.", 1, nodeNames.length);
    	assertEquals("Wrong node name.", WORKFLOW_DOCUMENT_NODE, nodeNames[0]);
        ActionRequestDTO[] requests = document.getActionRequests();
        assertEquals(1, requests.length);
        ActionRequestDTO request = requests[0];
        assertEquals(getPrincipalIdForName("bmcgough"), request.getPrincipalId());
        assertEquals(KEWConstants.ACTION_REQUEST_APPROVE_REQ, request.getActionRequested());
        assertEquals(new Integer(1), request.getRouteLevel());
        assertTrue(document.isApprovalRequested());
        document.approve("Test approve by bmcgough");
        
        document = WorkflowDocument.loadDocument(getPrincipalIdForName("temay"), document.getDocumentId());
        assertTrue("Document should be processed.", document.stateIsProcessed());
        requests = document.getActionRequests();
        assertEquals(3, requests.length);
        boolean toTemay = false;
        boolean toJhopf = false;
        for (int i = 0; i < requests.length; i++) {
            ActionRequestDTO requestVO = requests[i];
            if (requestVO.getPrincipalId().equals(getPrincipalIdForName("temay"))) {
                toTemay = true;
                assertEquals(KEWConstants.ACTION_REQUEST_ACKNOWLEDGE_REQ, requestVO.getActionRequested());
                assertEquals(new Integer(2), requestVO.getRouteLevel());
                assertEquals(ActionRequestStatus.ACTIVATED.getCode(), requestVO.getStatus());
            } else if (requestVO.getPrincipalId().equals(getPrincipalIdForName("jhopf"))) {
                toJhopf = true;
                assertEquals(KEWConstants.ACTION_REQUEST_ACKNOWLEDGE_REQ, requestVO.getActionRequested());
                assertEquals(new Integer(3), requestVO.getRouteLevel());
                assertEquals(ActionRequestStatus.ACTIVATED.getCode(), requestVO.getStatus());
            }
        }
        assertTrue("Should be an acknowledge to temay", toTemay);
        assertTrue("Should be an acknowledge to jhopf", toJhopf);
//        assertEquals(ACKNOWLEDGE_2_NODE, document.getRouteHeader().getNodeNames()[0]);
        // have temay take her acknowledge
        document.acknowledge("Temay taking acknowledge");
        
        document = WorkflowDocument.loadDocument(getPrincipalIdForName("jhopf"), document.getDocumentId());
        assertTrue("Document should be processed.", document.stateIsProcessed());
        requests = document.getActionRequests();
        toTemay = false;
        toJhopf = false;
        for (int i = 0; i < requests.length; i++) {
            ActionRequestDTO requestVO = requests[i];
            if (requestVO.getPrincipalId().equals(getPrincipalIdForName("temay"))) {
                toTemay = true;
                assertEquals(KEWConstants.ACTION_REQUEST_ACKNOWLEDGE_REQ, requestVO.getActionRequested());
                assertEquals(ActionRequestStatus.DONE.getCode(), requestVO.getStatus());
            } else if (requestVO.getPrincipalId().equals(getPrincipalIdForName("jhopf"))) {
                toJhopf = true;
                assertEquals(KEWConstants.ACTION_REQUEST_ACKNOWLEDGE_REQ, requestVO.getActionRequested());
                assertEquals(new Integer(3), requestVO.getRouteLevel());
                assertEquals(ActionRequestStatus.ACTIVATED.getCode(), requestVO.getStatus());
            }
        }
        assertTrue("Should be a DONE acknowledge to temay", toTemay);
        assertTrue("Should be an acknowledge to jhopf", toJhopf);
        // have jhopf take his acknowledge, this should cause the document to go final
        document.acknowledge("Jhopf taking acknowledge");
        
    	// TODO when we are able to, we should also verify the RouteNodeInstances are correct
        document = WorkflowDocument.loadDocument(getPrincipalIdForName("ewestfal"), document.getDocumentId());
    	assertTrue("Document should be final.", document.stateIsFinal());
        
        verifyRoutingPath(document.getDocumentId());
    }        

    private void verifyRoutingPath(String documentId) {
        DocumentRouteHeaderValue document = KEWServiceLocator.getRouteHeaderService().getRouteHeader(documentId);
        List initial = document.getInitialRouteNodeInstances();
        assertEquals(1, initial.size());
        RouteNodeInstance adhoc = (RouteNodeInstance)initial.get(0);
        assertEquals(ADHOC_NODE, adhoc.getRouteNode().getRouteNodeName());
        assertEquals(0, adhoc.getPreviousNodeInstances().size());
        
        List next = adhoc.getNextNodeInstances();
        assertEquals(1, next.size());
        RouteNodeInstance wd = (RouteNodeInstance)next.get(0);
        assertEquals(WORKFLOW_DOCUMENT_NODE, wd.getRouteNode().getRouteNodeName());
        assertEquals(1, wd.getPreviousNodeInstances().size());
        
        next = wd.getNextNodeInstances();
        assertEquals(1, next.size());
        RouteNodeInstance ack1 = (RouteNodeInstance)next.get(0);
        assertEquals(ACKNOWLEDGE_1_NODE, ack1.getRouteNode().getRouteNodeName());
        assertEquals(1, ack1.getPreviousNodeInstances().size());
        
        next = ack1.getNextNodeInstances();
        assertEquals(1, next.size());
        RouteNodeInstance ack2 = (RouteNodeInstance)next.get(0);
        assertEquals(ACKNOWLEDGE_2_NODE, ack2.getRouteNode().getRouteNodeName());
        assertEquals(1, ack2.getPreviousNodeInstances().size());
        
        next = ack2.getNextNodeInstances();
        assertEquals(0, next.size());
    }

}
