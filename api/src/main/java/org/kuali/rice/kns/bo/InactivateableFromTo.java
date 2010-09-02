/*
 * Copyright 2007-2008 The Kuali Foundation
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
package org.kuali.rice.kns.bo;

import java.sql.Date;

/**
 * Business objects that have effective dating (from to dates) should implement this interface. This
 * translates the effective dates in terms of active/inactive status so the features built for
 * {@link Inactivateable} in the frameworks can be taken advantage of
 * 
 * @author Kuali Rice Team (rice.collab@kuali.org)
 */
public interface InactivateableFromTo extends Inactivateable {

	/**
	 * Sets the date for which record will be active
	 * 
	 * @param from
	 *            - Date value to set
	 */
	public void setActiveFromDate(Date from);
	
	/**
	 * Gets the date for which the record become active
	 * 
	 * @return Date
	 */
	public Date getActiveFromDate();

	/**
	 * Sets the date for which record will be active to
	 * 
	 * @param from
	 *            - Date value to set
	 */
	public void setActiveToDate(Date to);
	
	/**
	 * Gets the date for which the record become inactive
	 * 
	 * @return Date
	 */
	public Date getActiveToDate();

	/**
	 * Gets the date for which the record is being compared to in determining active/inactive
	 * 
	 * @return Date
	 */
	public Date getActiveAsOfDate();

	/**
	 * Sets the date for which the record should be compared to in determining active/inactive, if
	 * not set then the current date will be used
	 * 
	 * @param activeAsOfDate
	 *            - Date value to set
	 */
	public void setActiveAsOfDate(Date activeAsOfDate);

}
