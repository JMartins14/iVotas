<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<link rel="stylesheet"
	href="https://maxcdn.bootstrapcdn.com/bootstrap/4.0.0-beta.2/css/bootstrap.min.css"
	integrity="sha384-PsH8R72JQ3SOdhVi3uxftmaW6Vc51MKb0q5P2rRUpPvrszuE4W1povHYgTpBfshb"
	crossorigin="anonymous">

<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>iVotas - Voto</title>
</head>
<body>

	<script src="https://code.jquery.com/jquery-3.2.1.slim.min.js"
		integrity="sha384-KJ3o2DKtIkvYIK3UENzmM7KCkRr/rE9/Qpg6aAZGJwFDMVNA/GpGFF93hXpG5KkN"
		crossorigin="anonymous"></script>
	<script
		src="https://cdnjs.cloudflare.com/ajax/libs/popper.js/1.12.3/umd/popper.min.js"
		integrity="sha384-vFJXuSJphROIrBnz7yo7oB41mKfc8JzQZiCq4NCceLEaO4IHwicKwpJf9c9IpFgh"
		crossorigin="anonymous"></script>
	<script
		src="https://maxcdn.bootstrapcdn.com/bootstrap/4.0.0-beta.2/js/bootstrap.min.js"
		integrity="sha384-alpBpkh1PFOepccYVYDB4do5UnbKysX5WZXm3XxPqe5iKTfUKjNkCk9SaVuEZflJ"
		crossorigin="anonymous"></script>
<center>
	<h1>Voto na Eleição</h1>
	
	<s:form action="vote" namespace="/">
		<div class="form-group col-lg-2">
			<br>
			<h5>
				Titulo
				</h5>
				<s:label name="electionTitle"></s:label>
				<br> <br>

				<h5>Descrição</h5>
				<s:label name="description"></s:label>
				<br> <br>
				<s:hidden name="username" value="%{#attr.user.getName()}"></s:hidden>
				<s:hidden name="electionTitle" value="%{#attr.electionTitle}"></s:hidden>
				
				<h5>Categoria</h5>
				<s:if test="%{#attr.election.getCategory()=='nucleoestudantes'}">

					<s:label>Núcleo de Estudantes</s:label>
				</s:if>
				<s:else>
					<s:label>Conselho Geral</s:label>

				</s:else>
				<br> <br>
				<s:if test="%{#attr.election.getCategory()=='nucleoestudantes'}">
					<h5>Departamento</h5>
					<s:label name="department"></s:label>
					<br>
					<br>
				</s:if>
				<h5>Data de Início da Eleição</h5>
				<s:label name="begindate"></s:label>
				<br> <br>
				<h5>Data de Fim da Eleição</h5>
				<s:label name="enddate"></s:label>
				<br> <br>
				<h5>Listas</h5>
				
				<s:if test="%{#attr.user.getCategory()=='stud'}">
					<s:if test="%{#attr.studlistelection.size()>0}">
						  <s:radio label="lists" name="vote" list="#attr.studlistelection" />
						
						<br>
					</s:if>
				</s:if>
				<s:if test="%{#attr.user.getCategory()=='doc'}">
					<s:if test="%{#attr.doclistelection.size()>0}">
						  <s:radio label="lists" name="vote" list="#attr.doclistelection" />
						
						<br>
					</s:if>
				</s:if>
				<s:if test="%{#attr.user.getCategory()=='func'}">

					<s:if test="%{#attr.funclistelection.size()>0}">
						  <s:radio label="lists" name="vote" list="#attr.funclistelection"  />

					</s:if>
				</s:if>
				<s:radio id="white" name="vote" list="#{'branco' : ' Voto Branco'}"></s:radio><br>
				<s:radio id="null" name="vote" list="#{'nulo' : ' Voto Nulo'}"></s:radio>
				<br>
		</div>
		<s:submit cssClass="btn btn-primary btn-md" value="Votar">
		</s:submit>
		<br>
		<br>
	</s:form>

	<s:form action="getActiveElections">
					<s:hidden name="username" value="%{#attr.user.getName()}"></s:hidden>
				<s:hidden name="electionTitle" value="%{#attr.electionTitle}"></s:hidden>
		<s:submit cssClass="btn btn-default btn-md" value="Voltar"></s:submit>
	</s:form>
	<br>
</center>



</body>
</html>