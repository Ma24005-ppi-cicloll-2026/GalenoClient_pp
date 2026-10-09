package sv.edu.ues.occ.ingenieria.ppi115_2026.salud.galenoclient.resource;

import java.util.Date;
import java.util.Map;

public class PersonaResourceTest extends ResourceTestBase {

    @Override
    protected String ruta() {
        return "persona";
    }

    @Override
    protected String campoId() {
        return "idPersona";
    }

    @Override
    protected Map<String, Object> cuerpoNuevo() {
        return mapa("nombres", "Prueba", "apellidos", unico("Test"),
                "fechaNacimiento", new Date(), "fechaCreacion", new Date());
    }

    @Override
    protected String campoEditable() {
        return "nombres";
    }

    @Override
    protected Object valorEditado() {
        return "Modificado";
    }
}
