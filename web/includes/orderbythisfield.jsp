 <!-- include: -->
  <% // you must pass thisfield and fieldlabel as defined beans to this !! %>
          <logic:empty name="fieldlabel">
            <!-- if no label passed, use name -->
            <bean:define id="fieldlabel" name="thisfield" />
          </logic:empty>
          <!-- default sort asc, this field -->
		<bean:define id="offer" >orderby_<bean:write name="thisfield" /></bean:define>
		  <!-- default, not sorted by this col -->
		<bean:define id="showsort" value="" />
		<bean:define id="thclass" value="normal" />
		
		
		<% if ( dupl_orderby.equals("orderby_" + thisfield) ) { %>
	        	  <!-- is sorted asc -->
		  <bean:define id="showsort" value="&uarr;" />
		  <bean:define id="offer" >orderby_<bean:write name="thisfield" />_desc</bean:define>
		  <bean:define id="thclass" value="sorted" />
		<%  }  %>
		
		<% if ( dupl_orderby.equals("orderby_" + thisfield + "_desc") ) { %>
			        	  <!-- is sorted desc -->
				  <bean:define id="showsort" value="&darr;" />
				  <bean:define id="thclass" value="sorteddesc" />
		<%  }  %>


        <th class="<bean:write name='thclass' />"><a class="sortlink" href="/get/summary/project/?orderBy=<bean:write name='offer' />" ><bean:write name="fieldlabel" filter="false" /></a><span class="sortarrow"><bean:write name="showsort" filter="false"/></span>
				 </th>
		
        <!-- end of include -->	