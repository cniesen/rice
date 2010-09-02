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
package edu.sampleu.travel.bo;

import java.util.LinkedHashMap;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import org.kuali.rice.kns.bo.InactivatableFromToImpl;
import org.kuali.rice.kns.util.KualiPercent;

@Entity
@Table(name = "TRV_ACCT_USE_RT")
public class TravelAccountUseRate extends InactivatableFromToImpl {

	@Id
	@Column(name = "id")
	private String id;

	@Column(name = "acct_num")
	private String number;

	@Column(name = "rate")
	private KualiPercent rate;

	public TravelAccountUseRate() {
		super();
	}

	public String getId() {
		return this.id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getNumber() {
		return this.number;
	}

	public void setNumber(String number) {
		this.number = number;
	}

	public KualiPercent getRate() {
		return this.rate;
	}

	public void setRate(KualiPercent rate) {
		this.rate = rate;
	}

	@Override
	protected LinkedHashMap toStringMapper() {
        LinkedHashMap propMap = new LinkedHashMap();
        propMap.put("id", getId());
        return propMap;
	}

}
