package br.com.soc.sistema.util;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

public class FormatadorDataHora {

    private static final DateTimeFormatter FORMATO_DATA =
            DateTimeFormatter.ofPattern("dd/MM/yyyy");

    private static final DateTimeFormatter FORMATO_HORA =
            DateTimeFormatter.ofPattern("HH:mm");

    private FormatadorDataHora() {
    }

    public static String formatarData(String data) {
        if (data == null || data.isEmpty())
            return "";

        LocalDate localDate = LocalDate.parse(data);

        return localDate.format(FORMATO_DATA);
    }

    public static String formatarHora(String hora) {
        if (hora == null || hora.isEmpty())
            return "";

        LocalTime localTime = LocalTime.parse(hora);

        return localTime.format(FORMATO_HORA);
    }
}