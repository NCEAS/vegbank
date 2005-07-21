<!-- sub observation transl -->
<!-- translates observation ID into a string, writes the string and links to the comprehensive obs view -->
<vegbank:get id="obstransl" select="observation_transl" beanName="map" where="where_observation_pk"
  wparam="observation_pk" />
  <logic:notEmpty name="obstransl-BEAN">
    <a href="@get_link@comprehensive/observation/<bean:write name='observation_pk' />"><bean:write name="obstransl-BEAN" property="observation_id_transl" /></a>
  </logic:notEmpty>