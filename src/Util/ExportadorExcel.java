package util;

import com.google.common.base.Strings;
import estructuras.ArbolEstudiantes;
import estructuras.ArregloCursos;
import estructuras.ListaMatricula;
import modelo.Curso;
import modelo.Estudiante;
import modelo.Matricula;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public final class ExportadorExcel {

    private static final Logger LOGGER = LoggerFactory.getLogger(ExportadorExcel.class);

    private ExportadorExcel() {
    }

    public static void exportarCursos(ArregloCursos arreglo, File destino) throws IOException {
        try (Workbook wb = new HSSFWorkbook()) {
            Sheet sheet = wb.createSheet("Cursos");
            crearEncabezado(sheet, "Código", "Nombre", "Créditos", "Semestre");
            int fila = 1;
            for (Curso c : arreglo.obtenerCursos()) {
                if (c == null) continue;
                Row row = sheet.createRow(fila++);
                row.createCell(0).setCellValue(Strings.nullToEmpty(c.getCodigo()));
                row.createCell(1).setCellValue(Strings.nullToEmpty(c.getNombre()));
                row.createCell(2).setCellValue(c.getCreditos());
                row.createCell(3).setCellValue(c.getSemestre());
            }
            autoSize(sheet, 4);
            try (FileOutputStream out = new FileOutputStream(destino)) {
                wb.write(out);
            }
        }
        LOGGER.info("Cursos exportados a {}", destino);
    }

    public static void exportarEstudiantes(ArbolEstudiantes arbol, File destino) throws IOException {
        try (Workbook wb = new HSSFWorkbook()) {
            Sheet sheet = wb.createSheet("Estudiantes");
            crearEncabezado(sheet, "Carnet", "Nombre", "Carrera");
            int fila = 1;
            List<Estudiante> estudiantes = new ArrayList<>();
            arbol.inorden(estudiantes::add);
            for (Estudiante e : estudiantes) {
                Row row = sheet.createRow(fila++);
                row.createCell(0).setCellValue(Strings.nullToEmpty(e.getCarnet()));
                row.createCell(1).setCellValue(Strings.nullToEmpty(e.getNombre()));
                row.createCell(2).setCellValue(Strings.nullToEmpty(e.getCarrera()));
            }
            autoSize(sheet, 3);
            try (FileOutputStream out = new FileOutputStream(destino)) {
                wb.write(out);
            }
        }
        LOGGER.info("Estudiantes exportados a {}", destino);
    }

    public static void exportarMatriculas(ListaMatricula lista, File destino) throws IOException {
        try (Workbook wb = new HSSFWorkbook()) {
            Sheet sheet = wb.createSheet("Matriculas");
            crearEncabezado(sheet, "Carnet", "Estudiante", "Código Curso", "Curso");
            int fila = 1;
            List<Matricula> matriculas = new ArrayList<>();
            lista.recorrer(matriculas::add);
            for (Matricula m : matriculas) {
                Row row = sheet.createRow(fila++);
                row.createCell(0).setCellValue(Strings.nullToEmpty(m.getEstudiante().getCarnet()));
                row.createCell(1).setCellValue(Strings.nullToEmpty(m.getEstudiante().getNombre()));
                row.createCell(2).setCellValue(Strings.nullToEmpty(m.getCurso().getCodigo()));
                row.createCell(3).setCellValue(Strings.nullToEmpty(m.getCurso().getNombre()));
            }
            autoSize(sheet, 4);
            try (FileOutputStream out = new FileOutputStream(destino)) {
                wb.write(out);
            }
        }
        LOGGER.info("Matrículas exportadas a {}", destino);
    }

    private static void crearEncabezado(Sheet sheet, String... columnas) {
        Row header = sheet.createRow(0);
        CellStyle style = sheet.getWorkbook().createCellStyle();
        style.setFillForegroundColor(IndexedColors.ROYAL_BLUE.getIndex());
        style.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        Font font = sheet.getWorkbook().createFont();
        font.setColor(IndexedColors.WHITE.getIndex());
        font.setBold(true);
        style.setFont(font);
        for (int i = 0; i < columnas.length; i++) {
            Cell cell = header.createCell(i);
            cell.setCellValue(columnas[i]);
            cell.setCellStyle(style);
        }
    }

    private static void autoSize(Sheet sheet, int cols) {
        for (int i = 0; i < cols; i++) {
            sheet.autoSizeColumn(i);
        }
    }
}
