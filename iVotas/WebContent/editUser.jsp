<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>iVotas</title>
<link rel="stylesheet"
	href="https://maxcdn.bootstrapcdn.com/bootstrap/4.0.0-beta.2/css/bootstrap.min.css"
	integrity="sha384-PsH8R72JQ3SOdhVi3uxftmaW6Vc51MKb0q5P2rRUpPvrszuE4W1povHYgTpBfshb"
	crossorigin="anonymous">
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
					class="dropdown-item" href="<s:url action="gotoSearchVote"/>">Local de Voto</a><a
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
<s:form action="editUser" namespace="/">
		<div class="form-group col-lg-2">
			<strong><label for="title">Nome</label></strong>
			<s:textfield name="name" cssClass="form-control input-sm" />
			<s:fielderror cssClass="alert alert-danger actionError"
				fieldName="nameError" />
		</div>
		
		<div class="form-group col-lg-2">
			<strong><label>Password</label></strong>
			<s:password name="password" cssClass="form-control input-sm" />
			<s:fielderror cssClass="alert alert-danger actionError"
				fieldName="passwordError" />
		</div>
		<div class="form-group col-lg-3">
			<strong><label for="type">Tipo de Utilizador:</label></strong> <br>
			<s:radio id="stud" name="category" list="#{'1' : ' Estudante'}"></s:radio>
			<br>
			<s:radio id="doc" name="category" list="#{'2' : ' Docente'}"></s:radio>
			<br>
			<s:radio id="func" name="category" list="#{'3' : ' Funcionário'}"></s:radio>
			<br>
			<s:fielderror cssClass="alert alert-danger actionError"
				fieldName="categoryError" />

		</div>
		
		<div class="form-group col-lg-2">
			<strong><label for="title">Morada</label></strong>
			<s:textfield name="adress" cssClass="form-control input-sm" />
			<s:fielderror cssClass="alert alert-danger actionError"
				fieldName="adressError" />
		</div>
		
		
		<div class="form-group col-lg-2">
		<strong><label for="title">Telemóvel</label></strong>
			<s:textfield name="telephone" cssClass="form-control input-sm" />
			<s:fielderror cssClass="alert alert-danger actionError"
				fieldName="telephoneError" />
		</div>
		
		
		<div class="form-group col-lg-2">
			<strong><label for="title">Cartão de Cidadão</label></strong>
			<s:textfield name="ccnumber" cssClass="form-control input-sm" />
			<s:fielderror cssClass="alert alert-danger actionError"
				fieldName="ccnumberError" />
		</div>
		
		
		<div class="form-group col-lg-2">
			<strong><label for="title">A expirar</label><strong>
			<s:textfield name="expiration" cssClass="form-control input-sm" />
			<s:fielderror cssClass="alert alert-danger actionError"
				fieldName="expirationError" />
		</div>
		
		
		<div class="form-group col-lg-1">
			<strong><label for="dep">Departamento</label></strong>
			<s:action name="getDepartments">
			</s:action>
			<s:select cssClass="form-control" name="department"
				list="#attr.departamentos"></s:select>
		</div>
			<s:submit cssClass="btn btn-primary btn-md" value="Editar"></s:submit>
	</s:form>
	<s:form action="gotoChooseUser">
		<s:submit cssClass="btn btn-default btn-md" value="Voltar"></s:submit>
	</s:form>
	
	</center>
</body>
</html>