package es.unex.cum.sinf.Reportes;

import es.unex.cum.sinf.Usuario;
import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRField;

import java.util.LinkedList;
import java.util.List;

public class ReporteUsuario extends Reporte implements JRDataSource {

    private String titulo_reporte;
    private LinkedList<Usuario> usuarios;
    private String desc_reporte;
    private Usuario next;

    public ReporteUsuario(List<Usuario> usuarioList, String titulo_reporte, String desc_reporte) {
        this.usuarios = (LinkedList<Usuario>) usuarioList;

        this.titulo_reporte = titulo_reporte;
        this.desc_reporte = desc_reporte;
    }

    @Override
    public boolean next() throws JRException {
        if (usuarios != null && usuarios.size() >= 1) {
            next = usuarios.poll();
            return true;
        }
        return false;
    }

    @Override
    public Object getFieldValue(JRField jrField) throws JRException {
        Object retorno = null;

        switch (jrField.getName()) {
            case "nombre_usuario":
                retorno = next.getNombre_usuario();
                break;
            case "cuenta_twitter":
                retorno = next.getCuenta_twitter();
                break;
            case "descripcion":
                retorno = next.getDescripcion();
                break;
            case "nombre":
                retorno = next.getNombre();
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
