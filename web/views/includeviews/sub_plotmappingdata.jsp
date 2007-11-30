<!-- this file is included when we want to show information about plots in a google mapping window -->
<!-- onerowofobservation must be defined and have access to observation and plot fields -->
<a target="_blank" href="@get_link@comprehensive/observation/<bean:write name='onerowofobservation' property='observation_id'/>"><bean:write name="onerowofobservation" property="authorplotcode" /></a><!--<br/>-->
<!--Fuzzing (deg): <bean:write name="onerowofobservation" property="degrees_fuzzed" /><br/>
Accuracy (m): <bean:write name="onerowofobservation" property="locationaccuracy" />
<logic:empty name="onerowofobservation" property="locationaccuracy">unknown</logic:empty>-->