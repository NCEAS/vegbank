@webpage_top_html@
  @stdvegbankget_jspdeclarations@
<jsp:useBean id="bean" class="org.vegbank.ui.struts.UserProfileForm"/>
  @webpage_head_html@
<!-- 
  * '$RCSfile: edit_permission.jsp,v $'
  * Purpose: 
  * Copyright: 2000 Regents of the University of California and the
  *               National Center for Ecological Analysis and Synthesis
  * Authors: @author@
  *
  * '$Author: mlee $'
  * '$Date: 2006-09-01 23:03:08 $'
  * '$Revision: 1.2 $'
  *
  *
  -->
  


<TITLE>Edit User Permissions</TITLE>

<script type="text/javascript">
function getHelpPageId() {
  return "update-user-permission";
}


function updatePermissionByChecks() {
  //custom js to edit permission by the checkboxes
  var intPerm = 0;
  var checkBoxes = document.getElementById('checkBoxes');
  //alert('got ' + checkBoxes.getElementsByTagName("input").length + ' boxes');
  for ( var i=0 ; i<checkBoxes.getElementsByTagName("input").length; i++) {
    //loop through boxes
    if (checkBoxes.getElementsByTagName("input")[i].checked) {
      intPerm = Number(intPerm) + Number(checkBoxes.getElementsByTagName("input")[i].value);
    }
  }
  document.getElementById("permissiontype" ).value = intPerm;
  
}

function updateChecksByPermission() {
  //custom js to edit permission of the checkboxes
  var intPerm = document.getElementById("permissiontype").value;
  if (isNaN(intPerm)) {
    //invalid!
    alert("please use only numeric permissions from 1-15");
    return;
  }
  //go through checks and check/uncheck them starting with the biggest one and update permission each time if checked.
  intPerm = checkIfGT(document.getElementById("check8"),intPerm);
  intPerm = checkIfGT(document.getElementById("check4"),intPerm);
  intPerm = checkIfGT(document.getElementById("check2"),intPerm);
  intPerm = checkIfGT(document.getElementById("check1"),intPerm);
   
}

function checkIfGT(inputToCheck,intValue) {
  // checks an input box if its value is at least as much and returns the difference
  if (Number(intValue) >= Number(inputToCheck.value)) {
    inputToCheck.checked = true;
    return (intValue - inputToCheck.value);
  } else {
    inputToCheck.checked = false; //uncheck
    return intValue;
  }
}

function checkPermissionValid() {
  //checks that the permission makes sense
  var oldPerm = Number(document.getElementById("initPerm").value);
  var newPerm = Number(document.getElementById("permissiontype").value);
  if ((isNaN(newPerm)) || (newPerm == null)) {
    alert("The new permission is invalid because it isn't a number: " + document.getElementById("permissiontype").value);
    return false;
  }
  // see if they are not different
  if ((oldPerm) == (newPerm)) {
    //didn't change, tell user
    alert("You didn't change the permission value, it was " + oldPerm + " before.");
    return false;
  } else { //value did change

    //check to see if "sane"
     if ((newPerm < 1) || (newPerm>15)) {
      //too high or too low
      alert("Your new permission value must be between 1 and 15");
      return false;
    } 
    
    if ( (newPerm==1) || (newPerm==3) || (newPerm==7) || (newPerm==15)) {
      //std value: OK
    } else { //not normal value:
      //between 1 and 15, but weird value
      if (confirm("You have picked a higher permission, but not a lower, and that's unusual (" + newPerm + ").  Continue?") == false) {
         return false; //cancelled
      } 
    } //normal or not
    
    // now check to see if downgrading
    if (oldPerm > newPerm) {
      //downgrading
      if (confirm("You are downgrading the user from " + oldPerm + ".  Continue?") == false) {
        return false; //cancelled
      } //confirm downgrade
    } //oldPerm more
    
  } //value diff
  //if still here, then it's ok
  //alert("OK!");
  return true;
}

</script>

@webpage_masthead_html@

<br />
<br />
<h2 align="center" class="vegbank">Edit User Permissions</h2>

<html:errors/>

<html:form method="get" action="/ChangePermission.do">
 	<bean:define id="beanUsrId"><bean:write name="webuser" property="userid"/></bean:define>
 	<input type="hidden" name="usrId" value="<bean:write name="beanUsrId"/>"/>
    <table bgcolor="#CCCCCC" cellpadding="1" cellspacing="1" border="0" >
    <tr bgcolor="#EBF3F8"> 
      <td align="center">Personal Information</td> 
      <td align="center">E-Mail</td>
      <td align="center">Permission</td>
      <td align="center">Permission (text) </td>
    </tr>
    <tr bgcolor="#FEFEFE">
      <td>
        <p>
          <bean:write name="webuser" property="givenname"/> 
          <bean:write name="webuser" property="surname"/>
        </p>
      </td>
      <td><bean:write name="webuser" property="email"/></td>
      <td>
        <!--<html:text name="upform" property="webuser.permissiontype" size="3" maxlength="3"/>-->
        <!-- remember init value -->
        <input id="initPerm" type="hidden" value='<bean:write name="webuser" property="permissiontype"/>' />
        <input id="permissiontype" name="permissiontype" type="text" size="3" maxlength="3" 
          value="<bean:write name="webuser" property="permissiontype"/>" onchange="updateChecksByPermission()"/>
        <input type="button" value="refresh-&gt;" onclick="updateChecksByPermission()" />  
      </td>
      <td id="checkBoxes">
        <input id="check1" value="1" type="checkbox" onchange="updatePermissionByChecks()"/>  Registered<br/>
        <input id="check2" value="2" type="checkbox" onchange="updatePermissionByChecks()"/>  Certified<br/>
        <input id="check4" value="4" type="checkbox" onchange="updatePermissionByChecks()"/>  Professional<br/>
        <input id="check8" value="8" type="checkbox" onchange="updatePermissionByChecks()"/>  Administrator
        <script type="text/javascript">
          // update checks:
          updateChecksByPermission();
        </script>
      </td>
    </tr>
    </table>
    <html:submit value="Update permission" onclick="return checkPermissionValid();"/>
	</html:form>

@webpage_footer_html@

