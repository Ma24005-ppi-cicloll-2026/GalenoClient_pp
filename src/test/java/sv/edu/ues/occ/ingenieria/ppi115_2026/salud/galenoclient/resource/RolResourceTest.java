package sv.edu.ues.occ.ingenieria.ppi115_2026.salud.galenoclient.resource;

import jakarta.ws.rs.core.Response;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class RolResourceTest extends ResourceTestBase {

    @Override
    protected String ruta() {
        return "rol";
    }

    @Override
    protected String campoId() {
        return "idRol";
    }

    @Override
    protected Map<String, Object> cuerpoNuevo() {
        return mapa("nombre", unico("TEST-rol"), "observaciones", "creado por GalenoClient");
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

    // GET .../rol/activos -> 200 y todos los que devuelve estan activos
    @Test
    public void activos_devuelve200_y_soloActivos() {
        Response r = recurso().path("activos").request().get();
        assertEquals(200, r.getStatus());
        List<Map<String, Object>> lista = r.readEntity(LISTA);
        assertTrue(lista.stream().allMatch(x -> Boolean.TRUE.equals(x.get("activo"))),
                "/activos solo debe devolver registros activos");
    }
}
