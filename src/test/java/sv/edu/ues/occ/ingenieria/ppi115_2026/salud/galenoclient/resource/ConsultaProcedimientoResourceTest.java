package sv.edu.ues.occ.ingenieria.ppi115_2026.salud.galenoclient.resource;

import java.util.Date;
import java.util.Map;
import java.util.UUID;

public class ConsultaProcedimientoResourceTest extends ResourceTestBase {

    @Override
    protected String ruta() {
        return "consulta_procedimiento";
    }

    @Override
    protected String campoId() {
        return "idConsultaProcedimiento";
    }

    @Override
    protected Map<String, Object> cuerpoNuevo() {
        UUID consulta = crearConsulta();
        UUID procedimiento = crearProcedimiento();
        return mapa("idProcedimiento", procedimiento.toString(), "fechaInicio", new Date(),
                "observaciones", "creado por GalenoClient",
                "idConsulta", ref("idConsulta", consulta));
    }

    @Override
    protected String campoEditable() {
        return "observaciones";
    }

    @Override
    protected Object valorEditado() {
        return "modificado por GalenoClient";
    }
}
