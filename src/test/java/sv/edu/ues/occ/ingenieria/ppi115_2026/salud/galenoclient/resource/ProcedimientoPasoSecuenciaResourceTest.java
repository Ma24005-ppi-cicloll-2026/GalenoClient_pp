package sv.edu.ues.occ.ingenieria.ppi115_2026.salud.galenoclient.resource;

import java.util.Map;
import java.util.UUID;

public class ProcedimientoPasoSecuenciaResourceTest extends ResourceTestBase {

    @Override
    protected String ruta() {
        return "procedimiento_paso_secuencia";
    }

    @Override
    protected String campoId() {
        return "idProcedimientoPasoSecuencia";
    }

    @Override
    protected Map<String, Object> cuerpoNuevo() {
        UUID paso = crearProcedimientoPaso();
        UUID pasoReferencia = crearProcedimientoPaso();
        return mapa("tipoSecuencia", "SIGUIENTE",
                "idProcedimientoPasoReferencia", pasoReferencia.toString(),
                "idProcedimientoPaso", ref("idProcedimientoPaso", paso));
    }

    @Override
    protected String campoEditable() {
        return "tipoSecuencia";
    }

    @Override
    protected Object valorEditado() {
        return "ANTERIOR";
    }
}