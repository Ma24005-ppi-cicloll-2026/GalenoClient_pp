package sv.edu.ues.occ.ingenieria.ppi115_2026.salud.galenoclient.resource;

import java.util.Map;
import java.util.UUID;

public class ProcedimientoPasoResourceTest extends ResourceTestBase {

    @Override
    protected String ruta() {
        return "procedimiento_paso";
    }

    @Override
    protected String campoId() {
        return "idProcedimientoPaso";
    }

    @Override
    protected Map<String, Object> cuerpoNuevo() {
        UUID procedimiento = crearProcedimiento();
        UUID rol = crearRol();
        return mapa("nombre", unico("TEST-paso"), "indicaFin", Boolean.FALSE,
                "idProcedimiento", ref("idProcedimiento", procedimiento),
                "idRol", ref("idRol", rol));
    }

    @Override
    protected String campoEditable() {
        return "nombre";
    }

    @Override
    protected Object valorEditado() {
        return "TEST-paso-modificado";
    }
}
