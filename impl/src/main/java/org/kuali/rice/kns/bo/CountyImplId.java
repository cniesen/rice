/*
 * Copyright 2007 The Kuali Foundation
 *
 * Licensed under the Educational Community License, Version 1.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.opensource.org/licenses/ecl1.php
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.kuali.rice.kns.bo;

import javax.persistence.Column;
import javax.persistence.Id;

/**
 * PK for CountyImpl 
 * 
 * @author Kuali Rice Team (rice.collab@kuali.org)
 *
 */
public class CountyImplId extends CompositePrimaryKeyBase {
	@Id
	//@Column(name="POSTAL_CNTRY_CD")
    private String postalCountryCode;
	@Id
	//@Column(name="COUNTY_CD")
    private String countyCode;
	@Id
	//@Column(name="POSTAL_STATE_CD")
    private String stateCode;
	/**
	 * @return the postalCountryCode
	 */
	public String getPostalCountryCode() {
		return this.postalCountryCode;
	}
	/**
	 * @return the countyCode
	 */
	public String getCountyCode() {
		return this.countyCode;
	}
	/**
	 * @return the stateCode
	 */
	public String getStateCode() {
		return this.stateCode;
	}
}
