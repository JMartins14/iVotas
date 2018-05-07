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
<script src="//ajax.googleapis.com/ajax/libs/jquery/1.9.1/jquery.min.js"></script>

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
	
	<h1>
		<span class="label label-default">Criar Lista Candidatos</span>
	</h1>

	<s:form action="createList">

		<div class="form-group col-lg-5">
			<strong><label for="nome">Nome da Lista</label></strong>
			<s:textfield size="60" name="name" cssClass="form-control input-sm" />
			<s:fielderror cssClass="alert alert-danger actionError"
				fieldName="name" />

		</div>
		<div class="form-group col-lg-2">
			<strong><label for="elec">Eleição</label></strong>
			<s:action name="getElect">
			</s:action>
			<s:select cssClass="form-control" name="election_id"
				list="#attr.elections"></s:select>
		</div>

		<div class="form-group col-lg-3">
			<strong><label for="type">Tipo de Lista:</label> </strong><br>
			<s:radio id="stud" name="category" list="#{'1' : ' Estudante'}"></s:radio>
			<br>
			<s:radio id="doc" name="category" list="#{'2' : ' Docente'}"></s:radio>
			<br>
			<s:radio id="func" name="category" list="#{'3' : ' Funcionário'}"></s:radio>
			<br>
			<s:fielderror cssClass="alert alert-danger actionError"
				fieldName="category" />

		</div>

		<div class="form-group col-lg-2">
			<strong><label for="num">Número de Candidatos:</label></strong>
			<s:textfield size="10" id="numcand" cssClass="form-control input-sm" />
<br>
			<div class="input_fields_wrap">
				<button class="add_field_button">Adicionar Candidatos</button>
							<br>
			<br>
			</div>
			<script>
						$(document).ready(function() {
			    var wrapper = $(".input_fields_wrap"); 
			    var add_button = $(".add_field_button");
			    var x = 0;
			    var xant = 0;
			    $(add_button).click(function(e){
				    x = eval($("#numcand").val()); 
			        e.preventDefault();
				    for(var i=0; i<x; i++){
			             $(wrapper).append('<div><strong><label for="num">Candidato</label></strong><s:textfield name="candidates" value="" cssClass="form-control input-sm" /><a href="#" class="remove_field">Remove</a><br></div>');
				    }
			    });
			    
			    $(wrapper).on("click",".remove_field", function(e){ 
			        e.preventDefault(); $(this).parent('div').remove();
			    })
			});
			</script>
			<s:fielderror cssClass="alert alert-danger actionError"
				fieldName="candidates" />


		</div>
		<s:submit cssClass="btn btn-primary btn-md" />

	</s:form>
	<br>
	<s:form action="gotoHomepage">
		<s:submit cssClass="btn btn-default btn-md" value="Voltar"></s:submit>
	</s:form>


</body>
</html>