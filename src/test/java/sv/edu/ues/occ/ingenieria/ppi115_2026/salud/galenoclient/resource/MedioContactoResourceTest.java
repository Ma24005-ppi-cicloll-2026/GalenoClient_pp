package sv.edu.ues.occ.ingenieria.ppi115_2026.salud.galenoclient.resource;

import java.util.Date;
import java.util.Map;
import java.util.UUID;

public class MedioContactoResourceTest extends ResourceTestBase {

    @Override
    protected String ruta() {
        return "medio_contacto";
    }

    @Override
    protected String campoId() {
        return "idMedioContacto";
    }

    @Override
    protected Map<String, Object> cuerpoNuevo() {
        UUID persona = crearPersona();
        UUID tipoMedio = crearTipoMedioContacto();
        return mapa("valor", "creado@galenoclient.test", "fechaCreacion", new Date(),
                "idPersona", ref("idPersona", persona),
                "idTipoMedioContacto", ref("idTipoMedioContacto", tipoMedio));
    }

    @Override
    protected String campoEditable() {
        return "valor";
    }

    @Override
    protected Object valorEditado() {
        return "modificado@galenoclient.test";
    }
}
