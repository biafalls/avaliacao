<%@ page language="java" contentType="text/html; charset=UTF-8"  pageEncoding="UTF8"%>
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

		<div class="container">
			<s:form action="/novoFuncionarios.action">

				<div class="card mt-5">
					<div class="card-header">
						<div class="row">
							<div class="col-sm-4">
								<s:url action="todosFuncionarios" var="todos"/>
								<a href="${todos}" class="btn btn-navigation"> <s:text name="label.funcionarios"/> </a>
							</div>
							
							<div class="col-sm-4 text-center">
								<h5 class="card-title">
									<s:if test="%{funcionarioVo.rowid != null}">
    									<s:text name="label.editar.funcionario"/>
									</s:if>

									<s:else>
    									<s:text name="label.novo.funcionario"/>
									</s:else>
								</h5>
							</div>
						</div>
					</div>
					
					<div class="card-body">
					
						<s:if test="hasActionErrors()">
        					<div class="alert alert-danger" role="alert">
           	 					<s:actionerror/>
       		 				</div>
    					</s:if>
    					
						<div class="row align-items-center">
							<label for="id" class="col-sm-2 col-form-label text-center">
								<s:text name="label.codigo"/>
							</label>	

							<div class="col-sm-3">
								<s:textfield cssClass="form-control" id="id" name="funcionarioVo.rowid" readonly="true"/>							
							</div>	
						</div>
						
						<div class="row align-items-center mt-3">
							<label for="nome" class="col-sm-2 col-form-label text-center">
								<s:text name="label.nome"/>
							</label>	

							<div class="col-sm-6">
								<s:textfield cssClass="form-control" id="nome" name="funcionarioVo.nome"/>							
								 <s:fielderror fieldName="funcionarioVo.nome"/>
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