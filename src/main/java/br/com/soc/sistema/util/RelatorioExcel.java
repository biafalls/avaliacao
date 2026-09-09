package br.com.soc.sistema.util;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;

import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import br.com.soc.sistema.exception.TechnicalException;
import br.com.soc.sistema.vo.CompromissoVo;

public class RelatorioExcel {
	
    private static final int COLUNA_ID_FUNCIONARIO = 0;
    private static final int COLUNA_NOME_FUNCIONARIO = 1;
    private static final int COLUNA_ID_AGENDA = 2;
    private static final int COLUNA_NOME_AGENDA = 3;
    private static final int COLUNA_DATA = 4;
    private static final int COLUNA_HORARIO = 5;

    private static final int TOTAL_COLUNAS = 6;
    
    public byte[] gerar(List<CompromissoVo> compromissos) {

        try (Workbook workbook = new XSSFWorkbook();
             ByteArrayOutputStream output = new ByteArrayOutputStream()) {

            Sheet sheet = workbook.createSheet("Relatório de Compromissos");

            criarCabecalho(sheet, workbook);
            preencherDados(sheet, workbook, compromissos);
            ajustarColunas(sheet);

            workbook.write(output);

            return output.toByteArray();

        } catch (IOException e) {
            throw new TechnicalException("Erro ao gerar relatório Excel.", e);
        }
    }

    private void criarCabecalho(Sheet sheet, Workbook workbook) {

        Row cabecalho = sheet.createRow(0);

        cabecalho.createCell(COLUNA_ID_FUNCIONARIO).setCellValue("Código Funcionário");
        cabecalho.createCell(COLUNA_NOME_FUNCIONARIO).setCellValue("Nome Funcionário");
        cabecalho.createCell(COLUNA_ID_AGENDA).setCellValue("Código Agenda");
        cabecalho.createCell(COLUNA_NOME_AGENDA).setCellValue("Nome Agenda");
        cabecalho.createCell(COLUNA_DATA).setCellValue("Data");
        cabecalho.createCell(COLUNA_HORARIO).setCellValue("Horário");
        
        CellStyle estiloCabecalho = criarEstiloCabecalho(workbook);
        
        for (int coluna = 0; coluna < TOTAL_COLUNAS; coluna++) {
            cabecalho.getCell(coluna).setCellStyle(estiloCabecalho);
        }
    }
    
    private CellStyle criarEstiloCabecalho(Workbook workbook) {
    	
    	CellStyle estilo = workbook.createCellStyle();

	    Font fonte = workbook.createFont();
	    fonte.setBold(true);
	    fonte.setColor(IndexedColors.WHITE.getIndex());

	    estilo.setFont(fonte);

	    estilo.setAlignment(HorizontalAlignment.CENTER);

	    estilo.setFillForegroundColor(IndexedColors.DARK_BLUE.getIndex());
	    estilo.setFillPattern(FillPatternType.SOLID_FOREGROUND);

	    return estilo;
    }

    private void preencherDados(Sheet sheet, Workbook workbook, List<CompromissoVo> compromissos) {
        CellStyle estiloCentralizado = workbook.createCellStyle();
        estiloCentralizado.setAlignment(HorizontalAlignment.CENTER);
        
    	int numeroLinha = 1;

        for (CompromissoVo compromisso : compromissos) {

            Row linha = sheet.createRow(numeroLinha++);

            linha.createCell(COLUNA_ID_FUNCIONARIO).setCellValue(compromisso.getFuncionario().getRowid());
            linha.createCell(COLUNA_NOME_FUNCIONARIO).setCellValue(compromisso.getFuncionario().getNome());
            linha.createCell(COLUNA_ID_AGENDA).setCellValue(compromisso.getAgenda().getRowid());
            linha.createCell(COLUNA_NOME_AGENDA).setCellValue(compromisso.getAgenda().getNome());
            linha.createCell(COLUNA_DATA).setCellValue(compromisso.getDataFormatada());
            linha.createCell(COLUNA_HORARIO).setCellValue(compromisso.getHorario());
       
            for (int coluna = 0; coluna < TOTAL_COLUNAS; coluna++) {
                linha.getCell(coluna).setCellStyle(estiloCentralizado);
            }
        }
    }

    private void ajustarColunas(Sheet sheet) {
        for (int coluna = 0; coluna < TOTAL_COLUNAS; coluna++) {
            sheet.autoSizeColumn(coluna);
        }
    }
}
