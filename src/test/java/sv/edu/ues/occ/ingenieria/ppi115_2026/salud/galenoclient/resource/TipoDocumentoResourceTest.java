package sv.edu.ues.occ.ingenieria.ppi115_2026.salud.galenoclient.resource;

import java.util.Map;

public class TipoDocumentoResourceTest extends ResourceTestBase {

    @Override
    protected String ruta() {
        return "tipo_documento";
    }

    @Override
    protected String campoId() {
        return "idTipoDocumento";
    }

    @Override
    protected Map<String, Object> cuerpoNuevo() {
        return mapa("nombre", unico("TEST-tipodoc"), "indicaciones", "creado por GalenoClient",
                "expresionRegular", "^\\d+$");
    }

    @Override
    protected String campoEditable() {
        return "indicaciones";
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
