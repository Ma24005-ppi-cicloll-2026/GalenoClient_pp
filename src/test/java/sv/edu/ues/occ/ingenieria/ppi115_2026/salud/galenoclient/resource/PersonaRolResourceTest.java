package sv.edu.ues.occ.ingenieria.ppi115_2026.salud.galenoclient.resource;

import java.util.Date;
import java.util.Map;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class PersonaRolResourceTest extends ResourceTestBase {

    @Override
    protected String ruta() {
        return "persona_rol";
    }

    @Override
    protected String campoId() {
        return "idPersonaRol";
    }

    @Override
    protected Map<String, Object> cuerpoNuevo() {
        UUID persona = crearPersona();
        UUID rol = crearRol();
        UUID clinica = crearClinica(); // PersonaRolDAO exige persona, rol y clinica activos
        return mapa("fechaCreacion", new Date(),
                "idPersona", ref("idPersona", persona),
                "idRol", ref("idRol", rol),
                "idClinica", ref("idClinica", clinica));
    }

    // PersonaRol no tiene campos de texto: para probar el PUT se le cambia la clinica por otra.
    private UUID clinicaNueva;

    @Override
    protected String campoEditable() {
        return "idClinica";
    }

    @Override
    protected Object valorEditado() {
        return clinicaNueva;
    }

    @Override
    protected void aplicarCambio(Map<String, Object> cuerpo) {
        clinicaNueva = crearClinica();
        cuerpo.put("idClinica", ref("idClinica", clinicaNueva));
    }

    @Override
    @SuppressWarnings("unchecked")
    protected void verificarCambio(Map<String, Object> guardado) {
        Map<String, Object> clinica = (Map<String, Object>) guardado.get("idClinica");
        assertNotNull(clinica, "El PUT no guardo la clinica");
        assertEquals(clinicaNueva.toString(), clinica.get("idClinica"));
    }
}