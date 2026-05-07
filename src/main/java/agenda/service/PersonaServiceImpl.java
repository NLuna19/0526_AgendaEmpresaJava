package agenda.service;

import agenda.data.ICiudadDAO;
import agenda.data.IDireccionDAO;
import agenda.data.IPersonaDAO;
import agenda.domain.Ciudad;
import agenda.domain.Direccion;
import agenda.domain.Persona;

import java.sql.SQLException;
import java.text.Normalizer;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.Optional;
import java.util.regex.Pattern;

public class PersonaServiceImpl implements PersonaService {
    private static final Pattern ACENTOS = Pattern.compile("\\p{M}");
    private static final Pattern ESPACIOS = Pattern.compile("\\s+");

    private final IPersonaDAO personaDAO;
    private final IDireccionDAO direccionDAO;
    private final ICiudadDAO ciudadDAO;

    public PersonaServiceImpl(
            IPersonaDAO personaDAO,
            IDireccionDAO direccionDAO,
            ICiudadDAO ciudadDAO
    ) {
        this.personaDAO = Objects.requireNonNull(personaDAO, "personaDAO es obligatorio");
        this.direccionDAO = Objects.requireNonNull(direccionDAO, "direccionDAO es obligatorio");
        this.ciudadDAO = Objects.requireNonNull(ciudadDAO, "ciudadDAO es obligatorio");
    }

    @Override
    public List<Persona> listar() throws SQLException {
        return personaDAO.personList();
    }

    @Override
    public Optional<Persona> buscarPorId(int idPersona) throws SQLException {
        if (idPersona <= 0) {
            throw new IllegalArgumentException("El id de persona debe ser mayor a cero.");
        }

        Persona persona = new Persona(idPersona);
        return personaDAO.getPersonById(persona)
                ? Optional.of(persona)
                : Optional.empty();
    }

    @Override
    public boolean crear(Persona persona) throws SQLException {
        validarPersona(persona);
        Direccion direccionPersistida = obtenerOPersistirDireccion(persona.getDireccion());
        persona.setDireccion(direccionPersistida);
        return personaDAO.addPerson(persona);
    }

    @Override
    public boolean actualizar(Persona persona) throws SQLException {
        validarPersona(persona);
        if (persona.getIdPersona() <= 0) {
            throw new IllegalArgumentException("El id de persona es obligatorio para actualizar.");
        }

        Direccion direccionPersistida = obtenerOPersistirDireccion(persona.getDireccion());
        persona.setDireccion(direccionPersistida);
        return personaDAO.modifyPerson(persona);
    }

    @Override
    public boolean eliminar(int idPersona) throws SQLException {
        if (idPersona <= 0) {
            throw new IllegalArgumentException("El id de persona debe ser mayor a cero.");
        }
        return personaDAO.deletePerson(idPersona);
    }

    @Override
    public List<Persona> buscarPorNombre(String nombre) throws SQLException {
        String nombreBuscado = normalizar(nombre);
        if (nombreBuscado.isEmpty()) {
            return List.of();
        }

        return personaDAO.personList().stream()
                .filter(persona -> normalizar(persona.getNombre()).contains(nombreBuscado)
                        || normalizar(persona.getApellido()).contains(nombreBuscado)
                        || normalizar(persona.getNombre() + " " + persona.getApellido()).contains(nombreBuscado))
                .toList();
    }

    @Override
    public List<Persona> buscarPorCiudad(String ciudad) throws SQLException {
        return buscarPorUbicacion(ciudad, UbicacionCampo.CIUDAD);
    }

    @Override
    public List<Persona> buscarPorProvincia(String provincia) throws SQLException {
        return buscarPorUbicacion(provincia, UbicacionCampo.PROVINCIA);
    }

    @Override
    public List<Persona> buscarPorPais(String pais) throws SQLException {
        return buscarPorUbicacion(pais, UbicacionCampo.PAIS);
    }

    @Override
    public List<Persona> buscarPorNombreYCiudades(String nombre, List<String> ciudades) throws SQLException {
        String nombreBuscado = normalizar(nombre);
        List<String> ciudadesBuscadas = Optional.ofNullable(ciudades)
                .orElse(Collections.emptyList())
                .stream()
                .map(this::normalizar)
                .filter(valor -> !valor.isEmpty())
                .toList();

        return personaDAO.personList().stream()
                .filter(persona -> nombreBuscado.isEmpty()
                        || normalizar(persona.getNombre()).contains(nombreBuscado)
                        || normalizar(persona.getApellido()).contains(nombreBuscado)
                        || normalizar(persona.getNombre() + " " + persona.getApellido()).contains(nombreBuscado))
                .filter(persona -> ciudadesBuscadas.isEmpty()
                        || ciudadesBuscadas.contains(normalizar(nombreCiudad(persona))))
                .toList();
    }

    private List<Persona> buscarPorUbicacion(String valor, UbicacionCampo campo) throws SQLException {
        String valorBuscado = normalizar(valor);
        if (valorBuscado.isEmpty()) {
            return List.of();
        }

        return personaDAO.personList().stream()
                .filter(persona -> normalizar(valorUbicacion(persona, campo)).contains(valorBuscado))
                .toList();
    }

    private Direccion obtenerOPersistirDireccion(Direccion direccion) throws SQLException {
        Ciudad ciudadPersistida = obtenerOPersistirCiudad(direccion.getCiudad());
        direccion.setCiudad(ciudadPersistida);
        Optional<Direccion> direccionExistente = direccionDAO.findAddress(direccion);
        if (direccionExistente.isPresent()) {
            return direccionExistente.get();
        }
        return direccionDAO.addAddress(direccion);
    }

    private Ciudad obtenerOPersistirCiudad(Ciudad ciudad) throws SQLException {
        validarCiudad(ciudad);
        Optional<Ciudad> ciudadExistente = ciudadDAO.findCity(ciudad);
        if (ciudadExistente.isPresent()) {
            return ciudadExistente.get();
        }
        return ciudadDAO.addCity(ciudad);
    }

    private void validarPersona(Persona persona) {
        if (persona == null) {
            throw new IllegalArgumentException("La persona es obligatoria.");
        }
        validarTexto(persona.getNombre(), "El nombre es obligatorio.");
        validarTexto(persona.getApellido(), "El apellido es obligatorio.");
        if (persona.getDireccion() == null) {
            throw new IllegalArgumentException("La direccion es obligatoria.");
        }
    }

    private void validarCiudad(Ciudad ciudad) {
        if (ciudad == null) {
            throw new IllegalArgumentException("La ciudad es obligatoria.");
        }
        validarTexto(ciudad.getNombre(), "El nombre de ciudad es obligatorio.");
    }

    private void validarTexto(String valor, String mensaje) {
        if (valor == null || valor.trim().isEmpty()) {
            throw new IllegalArgumentException(mensaje);
        }
    }

    private String nombreCiudad(Persona persona) {
        return valorUbicacion(persona, UbicacionCampo.CIUDAD);
    }

    private String valorUbicacion(Persona persona, UbicacionCampo campo) {
        if (persona.getDireccion() == null || persona.getDireccion().getCiudad() == null) {
            return "";
        }

        return switch (campo) {
            case CIUDAD -> persona.getDireccion().getCiudad().getNombre();
            case PROVINCIA -> persona.getDireccion().getCiudad().getProvincia();
            case PAIS -> persona.getDireccion().getCiudad().getPais();
        };
    }

    private String normalizar(String valor) {
        return Optional.ofNullable(valor)
                .map(String::trim)
                .map(texto -> ESPACIOS.matcher(texto).replaceAll(" "))
                .map(texto -> Normalizer.normalize(texto, Normalizer.Form.NFD))
                .map(texto -> ACENTOS.matcher(texto).replaceAll(""))
                .map(texto -> texto.toLowerCase(Locale.ROOT))
                .orElse("");
    }

    private enum UbicacionCampo {
        CIUDAD,
        PROVINCIA,
        PAIS
    }
}
