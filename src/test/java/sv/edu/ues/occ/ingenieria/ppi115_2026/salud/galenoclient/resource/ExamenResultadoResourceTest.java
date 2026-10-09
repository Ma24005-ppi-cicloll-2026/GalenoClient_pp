package sv.edu.ues.occ.ingenieria.ppi115_2026.salud.galenoclient.resource;

import java.util.Date;
import java.util.Map;
import java.util.UUID;

public class ExamenResultadoResourceTest extends ResourceTestBase {

    @Override
    protected String ruta() {
        return "examen_resultado";
    }

    @Override
    protected String campoId() {
        return "idExamenResultado";
    }

    @Override
    protected Map<String, Object> cuerpoNuevo() {
        UUID orden = crearOrdenExamen();
        return mapa("fechaCreacion", new Date(), "resultado", "NORMAL", "interpretacion", "creado por GalenoClient",
                "idOrdenExamen", ref("idOrdenExamen", orden));
    }

    @Override
    protected String campoEditable() {
        return "interpretacion";
    }

    @Override
    protected Object valorEditado() {
        return "modificado por GalenoClient";
    }
}
