/*
 *	'$RCSfile: AddJournalForm.java,v $'
 *	Authors: @author@
 *	Release: @release@
 *
 *	'$Author: farrell $'
 *	'$Date: 2003-07-21 17:52:13 $'
 *	'$Revision: 1.2 $'
 * 
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
 */
 
package org.vegbank.ui.struts;

import org.apache.struts.validator.ValidatorForm;
import org.vegbank.common.model.Referencejournal;

/**
 * @author farrell
 */

public class AddJournalForm extends ValidatorForm
{
	private Referencejournal referenceJournal= new Referencejournal();

	/**
	 * @return
	 */
	public Referencejournal getReferenceJournal()
	{
		return referenceJournal;
	}

	/**
	 * @param journal
	 */
	public void setReferenceJournal(Referencejournal journal)
	{
		referenceJournal = journal;
	}

}
