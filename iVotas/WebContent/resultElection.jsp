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
	<h1>Detalhes da Eleição</h1>
	<s:form action="gotoeditElection" namespace="/">
		<div class="form-group col-lg-2">
			<br>
			<h5>Titulo</h5>
				<s:label name="electionTitle"></s:label>
				<br> <br>
				<s:hidden name="id" value="%{#attr.election.getId()}"></s:hidden>

				<h5>Descrição</h5>
				<s:label name="description"></s:label>
				<br> <br>
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
				<s:if test="%{#attr.studlistelection.size()>0}">
					<h4>Listas de Estudantes</h4>
					<s:iterator value="#attr.studlistelection" var="arr1" status="itStatus">
							<h5><s:property value="arr1"/></h5>
							<s:set name="ite" value = "%{#itStatus.index}"></s:set> 
							&nbsp;&nbsp;&nbsp;&nbsp;<s:property value="studvotes[#ite]"/> Votos - <s:property value="percentagestud[#ite]"/> %
							
							
					</s:iterator>
					<br>
				</s:if>

				<s:if test="%{#attr.doclistelection.size()>0}">
					<h5>Listas de Docentes</h5>
					<s:iterator value="#attr.doclistelection" var="arr1" status="itStatus">
						<h5><s:property value="arr1"/></h5>
							<s:set name="ite" value = "%{#itStatus.index}"></s:set> 
							&nbsp;&nbsp;&nbsp;&nbsp;<s:property value="docvotes[#ite]"/> Votos - <s:property value="percentagedoc[#ite]"/> %
							
							
					</s:iterator>
					<br>
				</s:if>

				<s:if test="%{#attr.funclistelection.size()>0}">
					<h5>Listas de Funcionários</h5>
					<s:iterator value="#attr.funclistelection" var="arr1" status="itStatus">
						<h5><s:property value="arr1"/></h5>
							<s:set name="ite" value = "%{#itStatus.index}"></s:set> 
							&nbsp;&nbsp;&nbsp;&nbsp;<s:property value="funcvotes[#ite]"/> Votos - <s:property value="percentagefunc[#ite]"/> %
							
							
					</s:iterator>
				</s:if>
		</div> 
				<br> <br>
	</s:form>

	<s:form action="gotoChoosePastElection">
		<s:submit cssClass="btn btn-default btn-md" value="Voltar"></s:submit>
	</s:form>
	<br>
</center>



</body>
</html>