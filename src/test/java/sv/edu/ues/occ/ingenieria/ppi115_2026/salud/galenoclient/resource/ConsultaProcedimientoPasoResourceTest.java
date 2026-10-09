package sv.edu.ues.occ.ingenieria.ppi115_2026.salud.galenoclient.resource;

import java.util.Date;
import java.util.Map;
import java.util.UUID;

public class ConsultaProcedimientoPasoResourceTest extends ResourceTestBase {

    @Override
    protected String ruta() {
        return "consulta_procedimiento_paso";
    }

    @Override
    protected String campoId() {
        return "idConsultaProcedimientoPaso";
    }

    @Override
    protected Map<String, Object> cuerpoNuevo() {
        UUID consultaProcedimiento = crearConsultaProcedimiento();
        UUID personaRol = crearPersonaRol();
        return mapa("fechaInicio", new Date(), "estado", "EN_PROCESO",
                "idConsultaProcedimiento", ref("idConsultaProcedimiento", consultaProcedimiento),
                "idPersonaRol", ref("idPersonaRol", personaRol));
    }

    @Override
    protected String campoEditable() {
        return "estado";
    }

    @Override
    protected Object valorEditado() {
        return "FINALIZADO";
    }
}