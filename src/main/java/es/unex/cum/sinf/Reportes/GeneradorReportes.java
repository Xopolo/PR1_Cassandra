package es.unex.cum.sinf.Reportes;

import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;

import java.util.List;

public class GeneradorReportes {

    public void generarReporte(String filename, TipoReporte tipoReporte, List lista, String titulo_reporte, String desc_reporte) {
        Reporte reporte = null;
        String sourceFileName;
        switch (tipoReporte) {
            case USUARIO:
                sourceFileName = "Reportes/Templates/Usuarios.jasper";
                reporte = new ReporteUsuario(lista, titulo_reporte, desc_reporte);
                break;
            case NOTICIA:
                sourceFileName = "Reportes/Templates/Noticias.jasper";
                reporte = new ReporteNoticia(lista, titulo_reporte, desc_reporte);
                break;
            case COMENTARIO:
                sourceFileName = "Reportes/Templates/Comentario.jasper";
                reporte = new ReporteComentario(lista, titulo_reporte, desc_reporte);
                break;
            default:
                sourceFileName = null;
                break;
        }

        if(sourceFileName == null) return;

        String printFileName;
        try {

            printFileName = JasperFillManager.fillReportToFile(sourceFileName, null, reporte);
            if (printFileName != null) {
                JasperExportManager.exportReportToPdfFile(printFileName, String.format("Reportes/Cassandra/%s.pdf", filename));
                JasperExportManager.exportReportToHtmlFile(printFileName, String.format("Reportes/Cassandra/%s.html", filename));
            }
        } catch (JRException e) {
            e.printStackTrace();
        }
    }

}
