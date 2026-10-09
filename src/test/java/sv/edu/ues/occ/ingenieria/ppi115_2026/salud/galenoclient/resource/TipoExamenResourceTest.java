package sv.edu.ues.occ.ingenieria.ppi115_2026.salud.galenoclient.resource;

import java.util.Map;

public class TipoExamenResourceTest extends ResourceTestBase {

    @Override
    protected String ruta() {
        return "tipo_examen";
    }

    @Override
    protected String campoId() {
        return "idTipoExamen";
    }

    @Override
    protected Map<String, Object> cuerpoNuevo() {
        return mapa("nombre", unico("TEST-tipoexamen"), "observaciones", "creado por GalenoClient");
    }

    @Override
    protected String campoEditable() {
        return "observaciones";
    }

    @Override
    protected Object valorEditado() {
        return "modificado por GalenoClient";
    }

    @Override
    protected boolean nacenActivos() {
        return true;
    }
}