package sv.edu.ues.occ.ingenieria.ppi115_2026.salud.galenoclient.resource;

import java.util.Date;
import java.util.Map;
import java.util.UUID;

public class ConsultaResourceTest extends ResourceTestBase {

    @Override
    protected String ruta() {
        return "consulta";
    }

    @Override
    protected String campoId() {
        return "idConsulta";
    }

    @Override
    protected Map<String, Object> cuerpoNuevo() {
        UUID personaRol = crearPersonaRol();
        return mapa("fechaInicio", new Date(), "observaciones", "creada por GalenoClient",
                "idPersonaRol", ref("idPersonaRol", personaRol));
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