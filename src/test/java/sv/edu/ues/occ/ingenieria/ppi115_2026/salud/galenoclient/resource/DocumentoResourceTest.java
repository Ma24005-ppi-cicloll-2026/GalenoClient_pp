package sv.edu.ues.occ.ingenieria.ppi115_2026.salud.galenoclient.resource;

import java.util.Map;
import java.util.UUID;

public class DocumentoResourceTest extends ResourceTestBase {

    @Override
    protected String ruta() {
        return "documento";
    }

    @Override
    protected String campoId() {
        return "idDocumento";
    }

    @Override
    protected Map<String, Object> cuerpoNuevo() {
        UUID persona = crearPersona();
        UUID tipoDocumento = crearTipoDocumento();
        return mapa("valor", unico("DOC"), "rutaFisica", "/tmp/creado-por-galenoclient",
                "idPersona", ref("idPersona", persona),
                "idTipoDocumento", ref("idTipoDocumento", tipoDocumento));
    }

    @Override
    protected String campoEditable() {
        return "rutaFisica";
    }

    @Override
    protected Object valorEditado() {
        return "/tmp/modificado-por-galenoclient";
    }
}