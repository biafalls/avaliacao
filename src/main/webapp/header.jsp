<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>

<%@ taglib prefix="s" uri="/struts-tags" %>

<header class="app-header">
    <div class="app-header__brand">
        <s:text name="label.aplicacao"/>
    </div>

    <nav class="app-nav">
        <s:a action="todosFuncionarios"><s:text name="menu.funcionarios" /></s:a>
        <s:a action="todasAgendas"><s:text name="menu.agendas" /></s:a>
        <s:a action="todosCompromissos"><s:text name="menu.compromissos" /></s:a>
        <s:a action="abrirRelatorio"><s:text name="menu.relatorios" /></s:a>
    </nav>
</header>