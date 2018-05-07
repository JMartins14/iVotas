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
<title>iVotas</title>
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
		<div class="btn-group d-flex" role="group">
			<div class="btn-group  w-100" role="group">
			<button id="btnGroupDrop4" type="button"
				class="btn btn-primary dropdown-toggle w-100" data-toggle="dropdown"
				aria-haspopup="true" aria-expanded="false">Gerir
				Eleitores</button>
			<div class="dropdown-menu btn-block text-center " aria-labelledby="btnGroupDrop2">
				<a class="dropdown-item" href="<s:url action="gotoRegister"/>">Registar</a> <a
					class="dropdown-item" href="<s:url action="gotoEditUser"/>">Editar</a> <a
					class="dropdown-item" href="<s:url action="gotoSearchVote"/>">Local de Voto<a
					class="dropdown-item" href="<s:url action="gotoOnline"/>">Online</a>
			</div>
		</div>
		
		<div class="btn-group  w-100" role="group">
			<button id="btnGroupDrop2" type="button"
				class="btn btn-primary dropdown-toggle w-100" data-toggle="dropdown"
				aria-haspopup="true" aria-expanded="false">Gerir
				Departamentos</button>
			<div class="dropdown-menu btn-block text-center" aria-labelledby="btnGroupDrop2">
				<a class="dropdown-item" href="<s:url action="gotoAddDepartment"/>">Adicionar</a> <a
					class="dropdown-item" href="<s:url action="gotoDelDepartment"/>">Remover</a> <a
					class="dropdown-item" href="<s:url action="gotoEditDepartment"/>">Alterar</a>
			</div>
		</div>

		<div class="btn-group w-100" role="group">
			<button id="btnGroupDrop3" type="button"
				class="btn btn-primary dropdown-toggle  w-100"
				data-toggle="dropdown" aria-haspopup="true" aria-expanded="false">
				Gerir Mesas de Voto</button>
			<div class="dropdown-menu btn-block text-center" aria-labelledby="btnGroupDrop3">
				<a class="dropdown-item" href="<s:url action="gotoAddTable"/>">Adicionar</a> <a
					class="dropdown-item" href="<s:url action="gotoDelTable"/>">Remover</a>
			</div>
		</div>
		

		<div class="btn-group  w-100" role="group">
			<button id="btnGroupDrop1" type="button"
				class="btn btn-primary dropdown-toggle  w-100"
				data-toggle="dropdown" aria-haspopup="true" aria-expanded="false">
				Gerir Eleições</button>
			<div class="dropdown-menu  btn-block text-center" aria-labelledby="btnGroupDrop1">
				<a class="dropdown-item" href="<s:url action="gotoCreateElection"/>">Criar</a> <a
					class="dropdown-item" href="<s:url action="gotoChooseElection"/>">Consultar</a> <a
					class="dropdown-item" href="<s:url action="gotoCreateList"/>">Criar Lista de
					Candidatos</a> <a class="dropdown-item"
					href="<s:url action="gotoChoosePastElection"/>">Consultar Resultados</a>

			</div>
		</div>

	</div>
	
	
	<s:actionmessage cssClass="alert alert-success" />
	<s:actionerror cssClass="alert alert-danger" />
	
	
	<br>	
	<br>
	<center>
	<s:form action="register" namespace="/">
		<h1>Registo de um Eleitor</h1>
		<br>
		<div class="form-group col-lg-2">
			<strong><label for="user">Nickname</label></strong>
			<s:textfield name="nickname" cssClass="form-control input-sm" />
			<s:fielderror cssClass="alert alert-danger actionError"
				fieldName="username" />

		</div>
		<div class="form-group col-lg-2">
			<strong><label for="pwd">Password</label></strong>
			<s:password name="password" cssClass="form-control input-sm"></s:password>
			<s:fielderror cssClass="alert alert-danger actionError"
				fieldName="password" />
		</div>
		<div class="form-group col-lg-3">
			<strong><label for="type">Tipo de Utilizador</label></strong> <br>
			<s:radio id="stud" name="category" list="#{'1' : ' Estudante'}"></s:radio>
			<br>
			<s:radio id="doc" name="category" list="#{'2' : ' Docente'}"></s:radio>
			<br>
			<s:radio id="func" name="category" list="#{'3' : ' Funcionário'}"></s:radio>
			<br>
			<s:fielderror cssClass="alert alert-danger actionError"
				fieldName="category" />

		</div>
		<div class="form-group col-lg-5">
			<strong><label for="user">Morada</label></strong>
			<s:textfield size="60" name="morada" cssClass="form-control input-sm" />
			<s:fielderror cssClass="alert alert-danger actionError"
				fieldName="address" />

		</div>
		<div class="form-group col-lg-1">
			<strong><label for="dep">Departamento</label></strong>	
			<s:action name="getDepartments">
			</s:action>
			<s:select cssClass="form-control" name="departamento"
				list="#attr.categories"></s:select>
		</div>

		<div class="form-group col-lg-2">
			<strong><label for="phone">Contacto Telefónico</label></strong>
			<s:textfield size="60" name="contacto" cssClass="form-control input-sm" />
			<s:fielderror cssClass="alert alert-danger actionError"
				fieldName="phone" />

		</div>
		<div class="form-group col-lg-2">
			<strong><label for="ccnumber">Numero de CC</label></strong>
			<s:textfield size="60" name="ccnumber" cssClass="form-control input-sm" />
			<s:fielderror cssClass="alert alert-danger actionError"
				fieldName="ccnumber" />

		</div>
		<div class="form-group col-lg-2">
			<strong><label for="ccnumberdate">Data de Validade</label></strong>
			<s:textfield size="60" name="ccdate" cssClass="form-control input-sm" />
			<s:fielderror cssClass="alert alert-danger actionError"
				fieldName="date" />

		</div>
		<s:submit cssClass="btn btn-primary btn-md" value="Registar"></s:submit>
	</s:form>

	<br>
	<s:form action="gotoHomepage">
		<s:submit cssClass="btn btn-default btn-md" value="Voltar"></s:submit>
	</s:form>
</center>
</body>
</html>