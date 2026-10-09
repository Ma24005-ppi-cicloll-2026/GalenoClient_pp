package sv.edu.ues.occ.ingenieria.ppi115_2026.salud.galenoclient.resource;

import java.util.Date;
import java.util.Map;
import java.util.UUID;

public class ExamenTipoExamenResourceTest extends ResourceTestBase {

    @Override
    protected String ruta() {
        return "examen_tipo_examen";
    }

    @Override
    protected String campoId() {
        return "idExamenTipoExamen";
    }

    @Override
    protected Map<String, Object> cuerpoNuevo() {
        UUID examen = crearExamen();
        UUID tipoExamen = crearTipoExamen();
        return mapa("fechaCreacion", new Date(), "observaciones", "creado por GalenoClient",
                "idExamen", ref("idExamen", examen),
                "idTipoExamen", ref("idTipoExamen", tipoExamen));
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
