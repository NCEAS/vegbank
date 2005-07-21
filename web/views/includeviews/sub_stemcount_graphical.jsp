   <logic:greaterThan name="onerowofstemcount" property="stemdiameter" value="0">
                      <!-- define value for stem image: this is done because the scaling of round objects is a little funny.  these sizes are (somewhat) optimal -->
                      <bean:define id="stemPic" value="1" /> <!-- default for small ones -->
                      <logic:greaterThan name="onerowofstemcount" property="stemdiameter" value="18"><bean:define id="stemPic" value="3" /></logic:greaterThan>
                      <logic:greaterThan name="onerowofstemcount" property="stemdiameter" value="23"><bean:define id="stemPic" value="4" /></logic:greaterThan>
                      <logic:greaterThan name="onerowofstemcount" property="stemdiameter" value="24"><bean:define id="stemPic" value="2" /></logic:greaterThan>
                      <logic:greaterThan name="onerowofstemcount" property="stemdiameter" value="29"><bean:define id="stemPic" value="1" /></logic:greaterThan>
                      <logic:greaterThan name="onerowofstemcount" property="stemdiameter" value="32"><bean:define id="stemPic" value="4" /></logic:greaterThan>
                      <logic:greaterThan name="onerowofstemcount" property="stemdiameter" value="37"><bean:define id="stemPic" value="3" /></logic:greaterThan>
                      <logic:greaterThan name="onerowofstemcount" property="stemdiameter" value="50"><bean:define id="stemPic" value="4" /></logic:greaterThan>
                      <logic:greaterThan name="onerowofstemcount" property="stemdiameter" value="53"><bean:define id="stemPic" value="2" /></logic:greaterThan>
                      <logic:greaterThan name="onerowofstemcount" property="stemdiameter" value="56"><bean:define id="stemPic" value="5" /></logic:greaterThan>
                      <logic:greaterThan name="onerowofstemcount" property="stemdiameter" value="69"><bean:define id="stemPic" value="6" /></logic:greaterThan>
                      <logic:greaterThan name="onerowofstemcount" property="stemdiameter" value="77"><bean:define id="stemPic" value="3" /></logic:greaterThan>
                      <logic:greaterThan name="onerowofstemcount" property="stemdiameter" value="80"><bean:define id="stemPic" value="7" /></logic:greaterThan>
                      <logic:greaterThan name="onerowofstemcount" property="stemdiameter" value="94"><bean:define id="stemPic" value="8" /></logic:greaterThan>
                      <logic:greaterThan name="onerowofstemcount" property="stemdiameter" value="101"><bean:define id="stemPic" value="4" /></logic:greaterThan>
                      <logic:greaterThan name="onerowofstemcount" property="stemdiameter" value="104"><bean:define id="stemPic" value="8" /></logic:greaterThan>
                      <logic:greaterThan name="onerowofstemcount" property="stemdiameter" value="115"><bean:define id="stemPic" value="10" /></logic:greaterThan>
                      <logic:greaterThan name="onerowofstemcount" property="stemdiameter" value="125"><bean:define id="stemPic" value="5" /></logic:greaterThan>
                      <logic:greaterThan name="onerowofstemcount" property="stemdiameter" value="128"><bean:define id="stemPic" value="10" /></logic:greaterThan>
                      <img src="@images_link@stem_<bean:write name='stemPic' />sq.png" alt="stem" title="<bean:write name='onerowofstemcount' property='stemdiameter' /> cm" height="<bean:write name='onerowofstemcount' property='stemdiameter' />" width="<bean:write name='onerowofstemcount' property='stemdiameter' />" />
                      <bean:define id="themaxstems"><bean:write name="onerowofstemcount" property="stemcount" /></bean:define>
                      <bean:define id="tooManyStems" value="<!-- ok -->" />
                      <logic:greaterThan name="themaxstems" value="200">
                        <bean:define id="tooManyStems" value=" and more..." />
                        <bean:define id="themaxstems" value="200" />
                      </logic:greaterThan>
                      <logic:greaterThan name="onerowofstemcount" property="stemcount" value="1">
                        <!-- do again! -->
                        <!-- only iterates if more than one, even though this causes some duplication in this file.  This part most of the time omitted. -->
                        <!-- for (int i=2; i<= new Long(themaxstems).longValue(); i++) -->
                        <%
                         for (int i=2; i<= Long.parseLong(themaxstems); i++)
                         {
                        %>
                         <img src="@images_link@stem_<bean:write name='stemPic' />sq.png" alt="stem" title="<bean:write name='onerowofstemcount' property='stemdiameter' /> cm (#<%= i %>)" height="<bean:write name='onerowofstemcount' property='stemdiameter' />" width="<bean:write name='onerowofstemcount' property='stemdiameter' />" />
                        <%
                         }
                        %>
                        <bean:write name="tooManyStems" filter="false" />
                      </logic:greaterThan>
                    </logic:greaterThan>