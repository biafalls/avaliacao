<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<!DOCTYPE html>
<html>
	<head>
		<meta charset="UTF8">
		<title><s:text name="label.titulo.pagina.cadastro"/></title>
		<link rel='stylesheet' href='webjars/bootstrap/5.1.3/css/bootstrap.min.css'>
		<link rel="stylesheet" href="css/style.css">
	</head>
	<body>
		<jsp:include page="/header.jsp"/>

		<div class="container">
			<s:form action="/salvarAgendas.action">

				<div class="card mt-5">
					<div class="card-header">

					    <div class="card-header-content">
					
					        <div>
					            <s:url action="todasAgendas" var="todas"/>
					
					            <a href="${todas}" class="btn btn-navigation">
					                <s:text name="label.voltar"/>
					            </a>
					        </div>
					
					        <h5 class="card-title">
					            <s:if test="%{funcionarioVo.rowid != null}">
					                <s:text name="label.editar.agenda"/>
					            </s:if>
					
					            <s:else>
					                <s:text name="label.nova"/>
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
    					
						<div class="row align-items-center">
							<label for="id" class="col-sm-3 col-form-label text-center">
								<s:text name="label.id.form"/>
							</label>	

							<div class="col-sm-3">
								<s:textfield cssClass="form-control" id="id" name="agendaVo.rowid" readonly="true"/>							
							</div>	
						</div>
						
						<div class="row align-items-center mt-3">
							<label for="nome" class="col-sm-3 col-form-label text-center">
								<s:text name="label.nome.form"/>
							</label>	

							<div class="col-sm-6">
								<s:textfield cssClass="form-control" id="nome" name="agendaVo.nome"/>							
								<s:fielderror fieldName="agendaVo.nome"/>
							</div>
						</div>

						<div class="row align-items-center mt-3">
							<label for="periodoDisponivel" class="col-sm-3 col-form-label text-center">
								<s:text name="label.periodo.disponivel.form"/>
							</label>	

							<div class="col-sm-4">
								<s:text name="label.selecione" var="labelSelecione"/>
								<s:select 
									cssClass="form-select" 
									id="periodoDisponivel"
									name="agendaVo.periodoDisponivel"
									list="#{'1':'Manhã', '2':'Tarde', '3':'Ambos'}"
									headerKey=""
									headerValue="%{#labelSelecione}"
								/>
								<s:fielderror fieldName="agendaVo.periodoDisponivel"/>
							</div>
						</div>
					</div>

					<div class="card-footer">
						<div class="form-row">
							<button class="btn btn-success col-sm-4 offset-sm-1"> <s:text name="label.salvar"/> </button>
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