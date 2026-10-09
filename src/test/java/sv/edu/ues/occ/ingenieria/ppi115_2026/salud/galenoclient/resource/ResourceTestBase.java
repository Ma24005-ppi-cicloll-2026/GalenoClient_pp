package sv.edu.ues.occ.ingenieria.ppi115_2026.salud.galenoclient.resource;

import jakarta.ws.rs.client.Client;
import jakarta.ws.rs.client.ClientBuilder;
import jakarta.ws.rs.client.Entity;
import jakarta.ws.rs.client.WebTarget;
import jakarta.ws.rs.core.GenericType;
import jakarta.ws.rs.core.Response;
import java.util.ArrayDeque;
import java.util.Date;
import java.util.Deque;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Clase padre de todos los tests de la API (igual que DefaultResource en GalenoSV).
 *
 * Aqui estan las 3 pruebas que se repiten para CADA recurso:
 *   1. listar         -> GET  /recurso?max=1
 *   2. buscar inexistente -> GET /recurso/{id}  (404)
 *   3. ciclo completo -> POST, GET, PUT, DELETE, GET (201, 200, 200, 204, 404)
 *
 * Cada clase hija solo dice QUE recurso es y con QUE datos se prueba.
 * Requisito: Liberty y PostgreSQL encendidos con GalenoSV desplegado.
 */
public abstract class ResourceTestBase {

    protected static final String BASE = "http://localhost:9080/GalenoSV-1.0-SNAPSHOT/resources";

    protected static final GenericType<Map<String, Object>> MAPA = new GenericType<Map<String, Object>>() { };
    protected static final GenericType<List<Map<String, Object>>> LISTA = new GenericType<List<Map<String, Object>>>() { };

    protected static Client client;

    // Registros auxiliares creados por el test (ruta, id). Se borran al terminar, del ultimo al primero.
    private final Deque<String[]> dependencias = new ArrayDeque<>();

    // ---------------------------------------------------------------- lo que define cada hijo

    /** Parte final de la URL, igual al @Path del resource. Ej: "tipo_documento". */
    protected abstract String ruta();

    /** Nombre del campo id en el JSON. Ej: "idTipoDocumento". */
    protected abstract String campoId();

    /** JSON (como Map) del registro nuevo. Aqui se crean tambien sus dependencias (llaves foraneas). */
    protected abstract Map<String, Object> cuerpoNuevo();

    /** Campo de texto que se modifica en el PUT. */
    protected abstract String campoEditable();

    /** Nuevo valor para ese campo. */
    protected abstract Object valorEditado();

    /** true si el resource pone activo=true al crear (antesDeCrear). */
    protected boolean nacenActivos() {
        return false;
    }

    /** Aplica el cambio al cuerpo que se manda en el PUT. Los hijos pueden sobreescribirlo. */
    protected void aplicarCambio(Map<String, Object> cuerpo) {
        cuerpo.put(campoEditable(), valorEditado());
    }

    /** Comprueba, sobre lo que devuelve el GET, que el cambio quedo guardado. */
    protected void verificarCambio(Map<String, Object> guardado) {
        assertEquals(String.valueOf(valorEditado()), String.valueOf(guardado.get(campoEditable())),
                "El PUT no guardo el cambio en " + campoEditable());
    }

    // ---------------------------------------------------------------- ciclo de vida

    @BeforeAll
    static void abrirCliente() {
        client = ClientBuilder.newClient();
    }

    @AfterAll
    static void cerrarCliente() {
        client.close();
    }

    @AfterEach
    void borrarDependencias() {
        while (!dependencias.isEmpty()) {
            String[] d = dependencias.pop();
            client.target(BASE).path(d[0]).path(d[1]).request().delete().close();
        }
    }

    // ---------------------------------------------------------------- las pruebas

    @Test
    public void listar_devuelve200_y_respetaElMax() {
        Response r = recurso().queryParam("max", 1).request().get();
        assertEquals(200, r.getStatus());
        List<Map<String, Object>> lista = r.readEntity(LISTA);
        assertTrue(lista.size() <= 1, "max=1 no debe devolver mas de un registro");
    }

    @Test
    public void buscarInexistente_devuelve404() {
        Response r = recurso().path(UUID.randomUUID().toString()).request().get();
        assertEquals(404, r.getStatus());
        r.close();
    }

    @Test
    public void crear_buscar_modificar_eliminar() {
        UUID id = null;
        try {
            Map<String, Object> cuerpo = cuerpoNuevo();

            // POST -> 201 y devuelve el registro con el id que genero el servidor
            Response r = recurso().request().post(Entity.json(cuerpo));
            assertEquals(201, r.getStatus(), "POST debe devolver 201");
            Map<String, Object> creado = r.readEntity(MAPA);
            assertNotNull(creado.get(campoId()), "La respuesta debe traer " + campoId());
            id = UUID.fromString((String) creado.get(campoId()));
            if (nacenActivos()) {
                assertEquals(Boolean.TRUE, creado.get("activo"), "Debe nacer activo");
            }

            // GET por id -> 200
            Response rGet = recurso().path(id.toString()).request().get();
            assertEquals(200, rGet.getStatus());
            Map<String, Object> encontrado = rGet.readEntity(MAPA);
            assertEquals(id.toString(), encontrado.get(campoId()));

            // PUT -> 200 y el cambio queda guardado
            Map<String, Object> cambio = new LinkedHashMap<>(cuerpo);
            aplicarCambio(cambio);
            Response rPut = recurso().path(id.toString()).request().put(Entity.json(cambio));
            assertEquals(200, rPut.getStatus(), "PUT debe devolver 200");
            rPut.close();
            Response rGet2 = recurso().path(id.toString()).request().get();
            assertEquals(200, rGet2.getStatus());
            verificarCambio(rGet2.readEntity(MAPA));

            // DELETE -> 204 y despues el GET da 404
            Response rDel = recurso().path(id.toString()).request().delete();
            assertEquals(204, rDel.getStatus(), "DELETE debe devolver 204");
            rDel.close();
            Response rGet3 = recurso().path(id.toString()).request().get();
            assertEquals(404, rGet3.getStatus(), "Despues de borrar debe dar 404");
            rGet3.close();
            id = null;
        } finally {
            if (id != null) { // si algo fallo a medias, no dejar basura en la BD
                recurso().path(id.toString()).request().delete().close();
            }
        }
    }

    // ---------------------------------------------------------------- utilidades

    protected WebTarget recurso() {
        return client.target(BASE).path(ruta());
    }

    /** Arma un Map con pares clave, valor, clave, valor... */
    protected static Map<String, Object> mapa(Object... pares) {
        Map<String, Object> m = new LinkedHashMap<>();
        for (int i = 0; i < pares.length; i += 2) {
            m.put((String) pares[i], pares[i + 1]);
        }
        return m;
    }

    /** Referencia a otro registro: {"idPersona": "uuid"} (JSON-B lo convierte en la entidad con solo su id). */
    protected static Map<String, Object> ref(String campoId, UUID id) {
        return mapa(campoId, id.toString());
    }

    /** Texto unico para no chocar con datos que ya existan. */
    protected static String unico(String prefijo) {
        return prefijo + "-" + System.nanoTime();
    }

    /** Crea un registro auxiliar por la API, lo anota para borrarlo al final y devuelve su id. */
    protected UUID crearDependencia(String ruta, String campoId, Map<String, Object> cuerpo) {
        Response r = client.target(BASE).path(ruta).request().post(Entity.json(cuerpo));
        assertEquals(201, r.getStatus(), "No se pudo crear el registro auxiliar en /" + ruta);
        Map<String, Object> creado = r.readEntity(MAPA);
        UUID id = UUID.fromString((String) creado.get(campoId));
        dependencias.push(new String[]{ruta, id.toString()});
        return id;
    }

    // ---------------------------------------------------------------- registros auxiliares

    protected UUID crearRol() {
        return crearDependencia("rol", "idRol", mapa("nombre", unico("TEST-rol"), "observaciones", "auxiliar"));
    }

    protected UUID crearClinica() {
        return crearDependencia("clinica", "idClinica", mapa("nombre", unico("TEST-clinica"), "tipo", "CLINICA"));
    }

    protected UUID crearPersona() {
        return crearDependencia("persona", "idPersona", mapa("nombres", "Prueba", "apellidos", unico("Test"),
                "fechaNacimiento", new Date(), "fechaCreacion", new Date()));
    }

    protected UUID crearTipoDocumento() {
        return crearDependencia("tipo_documento", "idTipoDocumento", mapa("nombre", unico("TEST-tipodoc"),
                "indicaciones", "auxiliar", "expresionRegular", "^.+$"));
    }

    protected UUID crearTipoMedioContacto() {
        return crearDependencia("tipo_medio_contacto", "idTipoMedioContacto", mapa("nombre", unico("TEST-tipomedio"),
                "indicaciones", "auxiliar", "expresionRegular", "^.+$"));
    }

    protected UUID crearExamen() {
        return crearDependencia("examen", "idExamen", mapa("nombre", unico("TEST-examen"), "observaciones", "auxiliar"));
    }

    protected UUID crearTipoExamen() {
        return crearDependencia("tipo_examen", "idTipoExamen", mapa("nombre", unico("TEST-tipoexamen"), "observaciones", "auxiliar"));
    }

    protected UUID crearProcedimiento() {
        return crearDependencia("procedimiento", "idProcedimiento", mapa("nombre", unico("TEST-proc"), "observaciones", "auxiliar"));
    }

    protected UUID crearProcedimientoPaso() {
        UUID procedimiento = crearProcedimiento();
        UUID rol = crearRol();
        return crearDependencia("procedimiento_paso", "idProcedimientoPaso", mapa("nombre", unico("TEST-paso"),
                "indicaFin", Boolean.FALSE,
                "idProcedimiento", ref("idProcedimiento", procedimiento),
                "idRol", ref("idRol", rol)));
    }

    protected UUID crearPersonaRol() {
        UUID persona = crearPersona();
        UUID rol = crearRol();
        UUID clinica = crearClinica(); // PersonaRolDAO exige persona, rol y clinica activos
        return crearDependencia("persona_rol", "idPersonaRol", mapa("fechaCreacion", new Date(),
                "idPersona", ref("idPersona", persona),
                "idRol", ref("idRol", rol),
                "idClinica", ref("idClinica", clinica)));
    }

    protected UUID crearConsulta() {
        UUID personaRol = crearPersonaRol();
        return crearDependencia("consulta", "idConsulta", mapa("fechaInicio", new Date(), "observaciones", "auxiliar",
                "idPersonaRol", ref("idPersonaRol", personaRol)));
    }

    protected UUID crearConsultaProcedimiento() {
        UUID consulta = crearConsulta();
        UUID procedimiento = crearProcedimiento();
        return crearDependencia("consulta_procedimiento", "idConsultaProcedimiento", mapa(
                "idProcedimiento", procedimiento.toString(),
                "fechaInicio", new Date(), "observaciones", "auxiliar",
                "idConsulta", ref("idConsulta", consulta)));
    }

    protected UUID crearConsultaProcedimientoPaso() {
        UUID consultaProcedimiento = crearConsultaProcedimiento();
        UUID personaRol = crearPersonaRol();
        return crearDependencia("consulta_procedimiento_paso", "idConsultaProcedimientoPaso", mapa(
                "fechaInicio", new Date(), "estado", "EN_PROCESO",
                "idConsultaProcedimiento", ref("idConsultaProcedimiento", consultaProcedimiento),
                "idPersonaRol", ref("idPersonaRol", personaRol)));
    }

    protected UUID crearOrdenExamen() {
        UUID paso = crearConsultaProcedimientoPaso();
        return crearDependencia("orden_examen", "idOrdenExamen", mapa("fechaCreacion", new Date(), "indicaciones", "auxiliar",
                "idConsultaProcedimientoPaso", ref("idConsultaProcedimientoPaso", paso)));
    }
}