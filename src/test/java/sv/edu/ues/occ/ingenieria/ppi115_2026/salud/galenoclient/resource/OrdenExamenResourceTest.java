package sv.edu.ues.occ.ingenieria.ppi115_2026.salud.galenoclient.resource;

import java.util.Date;
import java.util.Map;
import java.util.UUID;

public class OrdenExamenResourceTest extends ResourceTestBase {

    @Override
    protected String ruta() {
        return "orden_examen";
    }

    @Override
    protected String campoId() {
        return "idOrdenExamen";
    }

    @Override
    protected Map<String, Object> cuerpoNuevo() {
        UUID paso = crearConsultaProcedimientoPaso();
        return mapa("fechaCreacion", new Date(), "indicaciones", "creada por GalenoClient",
                "idConsultaProcedimientoPaso", ref("idConsultaProcedimientoPaso", paso));
    }

    @Override
    protected String campoEditable() {
        return "indicaciones";
    }

    @Override
    protected Object valorEditado() {
        return "modificado por GalenoClient";
    }
}
