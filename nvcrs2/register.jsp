<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<jsp:useBean id="beanManager" class="org.vegbank.sausage.web.BeanManager" scope="session" />
<jsp:useBean id="usrDetails" class="org.vegbank.sausage.util.UsrDetails" scope="request" />
<c:set var="serr" value="${beanManager.strErr}" />
<c:set var="registered" value="${beanManager.registered}" />
<center>
<h3><p>Register</h3>
<c:if test="${registered==0}" >
	<font color=#FF6600>${serr}</font>
</c:if>
<c:if test="${registered==1}" >
	<font color=#FF6600>Registered successfully!</font>
</c:if>
<c:if test="${registered==0}" >
	<c:url var="url" value="/registerAck" />
	<br><br><form action="${url}" method=post>
	<table border="0">
	   <tr>
		   <td>First name:</td>
		   <td><input type="text" size="20" value="${usrDetails.firstName}" name="firstName"></td>
	   </tr>
	   <tr>
		   <td>Last name:</td>
		   <td><input type="text" size="20" value="${usrDetails.lastName}" name="lastName"></td>
	   </tr>
	   <tr>
		   <td>Middle initial:</td>
		   <td><input type="text" size="2" value="${usrDetails.middleInitial}" name="middleInitial"></td>
	   </tr>
	   <tr>
		   <td>Street:</td>
		   <td><input type="text" size="50" value="${usrDetails.street}" name="street"></td>
	   </tr>
	   <tr>
		   <td>City:</td>
		   <td><input type="text" size="30" value="${usrDetails.city}" name="city"></td>
	   </tr>
	   <tr>
		   <td>State:</td>
		   <td><input type="text" size="30" value="${usrDetails.state}" name="state"></td>
	   </tr>
	   <tr>
		   <td>Zip:</td>
		   <td><input type="text" size="30" value="${usrDetails.zip}" name="zip"></td>
	   </tr>
	   <tr>
		   <td>Phone:</td>
		   <td><input type="text" size="20" value="${usrDetails.phone}" name="phone"></td>
	   </tr>
	   <tr>
		   <td>Email:</td>
		   <td><input type="text" size="50" value="${usrDetails.email}" name="email"></td>
	   </tr>
	   <tr>
		   <td>Login name:</td>
		   <td><input type="text" size="30" value="${usrDetails.loginName}" name="loginName"></td>
	   </tr>
	   <tr>
		   <td>Password:</td>
		   <td><input type="text" size="30" name="password"></td>
	   </tr>
	   <tr>
		   <td>Password again:</td>
		   <td><input type="text" size="30" name="passwordConfirm"></td>
	   </tr>
	   <tr>
		   <td></td>
		   <td align="right"><input type="submit" value="Submit"> </td>
	   </tr>
	   <tr>
		<td><br></td>
	   </tr>
	</table>
	</form>
</c:if>
</center>
