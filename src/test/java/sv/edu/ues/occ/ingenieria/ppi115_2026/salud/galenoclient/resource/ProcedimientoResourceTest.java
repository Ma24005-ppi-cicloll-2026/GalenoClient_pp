package sv.edu.ues.occ.ingenieria.ppi115_2026.salud.galenoclient.resource;

import java.util.Map;

public class ProcedimientoResourceTest extends ResourceTestBase {

    @Override
    protected String ruta() {
        return "procedimiento";
    }

    @Override
    protected String campoId() {
        return "idProcedimiento";
    }

    @Override
    protected Map<String, Object> cuerpoNuevo() {
        return mapa("nombre", unico("TEST-proc"), "observaciones", "creado por GalenoClient");
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