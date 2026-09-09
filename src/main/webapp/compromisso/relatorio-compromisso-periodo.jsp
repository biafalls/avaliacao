<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<!DOCTYPE html>
<html>
	<head>
		<meta charset="UTF-8">
		<title><s:text name="label.titulo.pagina.relatorio"/></title>
		<link rel="stylesheet" href="webjars/bootstrap/5.1.3/css/bootstrap.min.css">
		<link rel="stylesheet" href="css/style.css">
	</head>
	<body>
	
		<jsp:include page="/header.jsp" />

		<div class="container">

			<div class="page-header">
				<h1><s:text name="label.titulo.pagina.relatorio"/></h1>
			</div>

			<s:if test="hasActionErrors()">
				<div class="alert alert-danger py-2 px-3 mb-3" role="alert">
					<s:actionerror/>
				</div>
			</s:if>

			<div class="row mt-3 mb-4">

				<div class="col-sm p-0">
					<s:form action="gerarRelatorio" method="post">
						<div class="row align-items-end">
							<div class="col-md-4">
								<label class="form-label"><s:text name="label.data.inicial.form"/></label>
								<s:textfield name="filtro.dataInicial" type="date" cssClass="form-control"/>
							</div>

							<div class="col-md-4">
								<label class="form-label"><s:text name="label.data.final.form"/></label>
								<s:textfield name="filtro.dataFinal" type="date" cssClass="form-control"/>
							</div>

							<div class="col-md-4">
								<button class="btn btn-primary w-100" type="submit">
									<s:text name="label.gerar.relatorio"/>
								</button>
							</div>
						</div>
					</s:form>
				</div>
			</div>

			<s:if test="compromissos != null && !compromissos.isEmpty()">
				<div class="row mb-2">
					<div class="col-sm p-0 d-flex justify-content-end">
						<s:form action="exportarRelatorio" method="post">
							<s:hidden name="filtro.dataInicial"/>
							<s:hidden name="filtro.dataFinal"/>
							<button class="btn btn-success" type="submit">
								<s:text name="label.exportar.excel"/>
							</button>
						</s:form>
					</div>
				</div>

				<div class="row">
					<div class="col-sm p-0">
						<table class="table table-striped table-hover align-middle">
							<thead>
								<tr>
									<th><s:text name="label.codigo.funcionario"/></th>
									<th><s:text name="label.funcionario"/></th>
									<th><s:text name="label.codigo.agenda"/></th>
									<th><s:text name="label.agenda"/></th>
									<th><s:text name="label.data"/></th>
									<th><s:text name="label.horario"/></th>
								</tr>
							</thead>

							<tbody>
								<s:iterator value="compromissos">
									<tr>
										<td><s:property value="funcionario.rowid"/></td>
										<td><s:property value="funcionario.nome"/></td>
										<td><s:property value="agenda.rowid"/></td>
										<td><s:property value="agenda.nome"/></td>
										<td><s:property value="dataFormatada"/></td>
										<td><s:property value="horario"/></td>
									</tr>
								</s:iterator>
							</tbody>
						</table>
					</div>
				</div>
			</s:if>
		</div>

		<script src="webjars/bootstrap/5.1.3/js/bootstrap.bundle.min.js"></script>

	</body>
</html>