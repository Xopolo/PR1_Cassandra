package es.unex.cum.sinf.Reportes;

import es.unex.cum.sinf.Comentario;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRField;

import java.util.LinkedList;
import java.util.List;

public class ReporteComentario extends Reporte {

    private String titulo_reporte;
    private LinkedList<Comentario> comentarios;
    private String desc_reporte;
    private Comentario next;

    public ReporteComentario(List<Comentario> comentarioList, String titulo_reporte, String desc_reporte) {
        this.comentarios = (LinkedList<Comentario>) comentarioList;

        this.titulo_reporte = titulo_reporte;
        this.desc_reporte = desc_reporte;
    }

    @Override
    public boolean next() throws JRException {
        if (comentarios != null && comentarios.size() >= 1) {
            next = comentarios.poll();
            return true;
        }
        return false;
    }

    @Override
    public Object getFieldValue(JRField jrField) throws JRException {
        Object retorno = null;

        switch (jrField.getName()) {
            case "autor_comentario":
                retorno = next.getUsuario();
                break;
            case "fecha":
                retorno = next.getFecha().toString();
                break;
            case "titulo_noticia":
                retorno = next.getTitulo_noticia();
                break;
            case "autor_noticia":
                if (next.getAutor_noticia() != null) {
                    retorno = next.getAutor_noticia();
                }
                break;
            case "cuerpo":
                retorno = next.getCuerpo();
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
