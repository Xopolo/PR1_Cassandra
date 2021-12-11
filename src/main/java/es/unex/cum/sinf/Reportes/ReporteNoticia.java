package es.unex.cum.sinf.Reportes;

import es.unex.cum.sinf.Noticia;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRField;

import java.util.LinkedList;
import java.util.List;

public class ReporteNoticia extends Reporte {

    private String titulo_reporte;
    private LinkedList<Noticia> noticias;
    private String desc_reporte;
    private Noticia next;

    public ReporteNoticia(List<Noticia> noticiaList, String titulo_reporte, String desc_reporte) {
        noticias = (LinkedList<Noticia>) noticiaList;

        this.titulo_reporte = titulo_reporte;
        this.desc_reporte = desc_reporte;
    }

    @Override
    public boolean next() throws JRException {
        if (noticias != null && noticias.size() >= 1) {
            next = noticias.poll();
            return true;
        }
        return false;
    }

    @Override
    public Object getFieldValue(JRField jrField) throws JRException {
        Object retorno = null;

        switch (jrField.getName()) {
            case "fecha":
                if (next.getFechaLocalDate() == null) {
                    retorno = next.getFecha().toString();
                } else {
                    retorno = next.getFechaLocalDate().toString();
                }
                break;
            case "tags":
                retorno = next.getEtiquetas().toString();
                break;
            case "descripcion":
                retorno = next.getDesc_autor();
                break;
            case "autor":
                retorno = next.getNick_autor();
                break;
            case "cuerpo":
                retorno = next.getCuerpo();
                break;
            case "titulo":
                retorno = next.getTitulo();
                break;
            case "Descripcion Report":
                retorno = desc_reporte;
                break;
            case "Titulo":
                retorno = titulo_reporte;
                break;
        }

        return retorno;
    }
}
