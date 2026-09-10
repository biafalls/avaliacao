<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<!DOCTYPE html>
<html>
	<head>
		<meta charset="UTF8">
		<title>
			<s:if test="%{compromissoVo.rowid != null}">
		        <s:text name="label.titulo.pagina.atualizacao"/>
		    </s:if>
		    <s:else>
		        <s:text name="label.titulo.pagina.cadastro"/>
		    </s:else>
    	</title>
		<link rel="stylesheet" href="webjars/bootstrap/5.1.3/css/bootstrap.min.css">
		<link rel="stylesheet" href="css/style.css">
	</head>
	<body>
		<jsp:include page="/header.jsp"/>

		<div class="container">
			<s:form action="/salvarCompromissos.action">

				<div class="card mt-5">
					<div class="card-header">
						<div class="card-header-content">
							<div>
								<s:url action="todosCompromissos" var="todos"/>
								<a href="${todos}" class="btn btn-navigation">
									<s:text name="label.voltar"/>
								</a>
							</div>

							<h5 class="card-title">
								<s:if test="%{compromissoVo.rowid != null}">
									<s:text name="label.editar.compromisso"/>
								</s:if>

								<s:else>
									<s:text name="label.novo"/>
								</s:else>
							</h5>
							<div></div>
						</div>
					</div>


					<div class="card-body">
						<s:if test="hasActionErrors()">
							<div class="alert alert-danger" role="alert">
								<s:actionerror/>
							</div>
						</s:if>
						
						<s:if test="%{compromissoVo.rowid != null}">
							<div class="row align-items-center">
								<label for="id" class="col-sm-2 col-form-label text-center">
									<s:text name="label.id.form"/>
								</label>
	
								<div class="col-sm-3">
									<s:textfield cssClass="form-control" id="id"
										name="compromissoVo.rowid" readonly="true"/>
								</div>
							</div>
						</s:if>

						<div class="row mt-3">
							<label for="idFuncionario" class="col-sm-2 col-form-label text-center">
								<s:text name="label.funcionario.form"/>
							</label>

							<div class="col-sm-6">
								<s:text name="label.selecione" var="labelSelecione"/>
								<s:select
									cssClass="form-select"
									id="idFuncionario"
									name="compromissoVo.funcionario.rowid"
									list="funcionarios"
									listKey="rowid"
									listValue="nome"
									headerKey=""
									headerValue="%{#labelSelecione}"
								/>
								<s:fielderror fieldName="compromissoVo.funcionario.rowid"/>
							</div>
						</div>


						<div class="row mt-3">
							<label for="idAgenda" class="col-sm-2 col-form-label text-center">
								<s:text name="label.agenda.form"/>
							</label>

							<div class="col-sm-6">
								<s:text name="label.selecione" var="labelSelecione"/>
								<s:select
									cssClass="form-select"
									id="idAgenda"
									name="compromissoVo.agenda.rowid"
									list="agendas"
									listKey="rowid"
									listValue="nome"
									headerKey=""
									headerValue="%{#labelSelecione}"
								/>
								<s:fielderror fieldName="compromissoVo.agenda.rowid"/>
							</div>
						</div>

						<div class="row mt-3">
							<label for="data" class="col-sm-2 col-form-label text-center">
								<s:text name="label.data.form"/>
							</label>

							<div class="col-sm-4">
								<s:textfield cssClass="form-control" id="data"
									name="compromissoVo.data" type="date"/>
								<s:fielderror fieldName="compromissoVo.data"/>
							</div>
						</div>

						<div class="row mt-3">
							<label for="horario" class="col-sm-2 col-form-label text-center">
								<s:text name="label.horario.form"/>
							</label>
							
							<div class="col-sm-3">
								<s:textfield cssClass="form-control" id="horario"
									name="compromissoVo.horario" type="time"/>
								<s:fielderror fieldName="compromissoVo.horario"/>
							</div>
						</div>
					</div>

					<div class="card-footer">
						<div class="form-row">
							<button class="btn btn-success col-sm-4 offset-sm-1">
								<s:text name="label.salvar"/>
							</button>

							<button type="reset" class="btn btn-secondary col-sm-4 offset-sm-2">
								<s:text name="label.limpar.formulario"/>
							</button>
						</div>
					</div>
				</div>
			</s:form>
		</div>

		<script src="webjars/bootstrap/5.1.3/js/bootstrap.bundle.min.js"></script>
	</body>
</html>