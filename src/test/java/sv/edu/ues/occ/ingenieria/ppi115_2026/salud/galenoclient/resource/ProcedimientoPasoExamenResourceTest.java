package sv.edu.ues.occ.ingenieria.ppi115_2026.salud.galenoclient.resource;

import java.util.Date;
import java.util.Map;
import java.util.UUID;

public class ProcedimientoPasoExamenResourceTest extends ResourceTestBase {

    @Override
    protected String ruta() {
        return "procedimiento_paso_examen";
    }

    @Override
    protected String campoId() {
        return "idProcedimientoPasoExamen";
    }

    @Override
    protected Map<String, Object> cuerpoNuevo() {
        UUID examen = crearExamen();
        UUID paso = crearProcedimientoPaso();
        return mapa("fechaCreacion", new Date(), "observaciones", "creado por GalenoClient",
                "idExamen", ref("idExamen", examen),
                "idProcedimientoPaso", ref("idProcedimientoPaso", paso));
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
