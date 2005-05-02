 <!-- include: orderbythisfield.jsp -->
  
    <!-- the orderby wasn't defined yet -->
    <logic:notPresent name="orderBy">
       <!-- orderBy not present as bean --> 
        <logic:notPresent parameter="orderBy">
           <!-- not present as param, either.  no sort is specified: -->
           <bean:define id="orderBy" >notsorted</bean:define> 
        </logic:notPresent>

        <logic:present parameter="orderBy">
            <!-- order by is on parameter line -->
		    <bean:parameter id="orderBy" name="orderBy" />
        </logic:present>
    </logic:notPresent>
    
  

  <% // you must pass thisfield and fieldlabel as defined beans to this !! %>
          <logic:empty name="fieldlabel">
            <!-- if no label passed, use name -->
            <bean:define id="fieldlabel"><bean:write name="thisfield" /></bean:define>
          </logic:empty>
          <!-- default sort asc, this field -->
		<bean:define id="offer" >xorderby_<bean:write name="thisfield" /></bean:define>
		  <!-- default, not sorted by this col -->
		<bean:define id="showsort" value="" />
		<bean:define id="thclass" value="normal" />
		
		<bean:define id="dupl_orderby"><bean:write name="orderBy" /></bean:define>
		<% if ( dupl_orderby.equals("xorderby_" + thisfield) ) { %>
	        	  <!-- is sorted asc -->
		  <bean:define id="showsort" value="&uarr;" />
		  <bean:define id="offer" >xorderby_<bean:write name="thisfield" />_desc</bean:define>
		  <bean:define id="thclass" value="sorted" />
		<%  }  %>
		
		<% if ( dupl_orderby.equals("xorderby_" + thisfield + "_desc") ) { %>
			        	  <!-- is sorted desc -->
				  <bean:define id="showsort" value="&darr;" />
				  <bean:define id="thclass" value="sorteddesc" />
		<%  }  %>

        <logic:notPresent name="thatts">
          <!-- thatts not passed, assign to empty string -->
          <bean:define id="thatts" value="" />
        </logic:notPresent>
        <logic:empty name="th_or_td">
          <bean:define id="th_or_td" value="th" />
        </logic:empty>
        
        <<bean:write name='th_or_td'/> <bean:write name='thatts'/>  class="<bean:write name='thclass' />"><a title="Click to sort by this field" class="sortlink" href="<vegbank:changeParam paramName='orderBy' paramValue='<%= offer %>' absolute='true' />" ><bean:write name="fieldlabel" filter="false" /></a><span class="sortarrow"><bean:write name="showsort" filter="false"/></span>
                 </<bean:write name='th_or_td'/>>
		
        <!-- set to empty string all fields (so they don't get used again!) -->
        <bean:define value="" id="thatts" />
        <bean:define value="" id="thclass" />
        <bean:define value="" id="thisfield" />
        <bean:define value="" id="fieldlabel" />
        <bean:define value="" id="offer" />
        <bean:define value="" id="showsort" />
        <bean:define value="" id="th_or_td" />
        
        
        <!-- end of include -->	
