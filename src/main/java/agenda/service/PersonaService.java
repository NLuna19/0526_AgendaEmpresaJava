package agenda.service;

import agenda.domain.Persona;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public interface PersonaService {
    List<Persona> listar() throws SQLException;

    Optional<Persona> buscarPorId(int idPersona) throws SQLException;

    boolean crear(Persona persona) throws SQLException;

    boolean actualizar(Persona persona) throws SQLException;

    boolean eliminar(int idPersona) throws SQLException;

    List<Persona> buscarPorNombre(String nombre) throws SQLException;

    List<Persona> buscarPorCiudad(String ciudad) throws SQLException;

    List<Persona> buscarPorProvincia(String provincia) throws SQLException;

    List<Persona> buscarPorPais(String pais) throws SQLException;

    List<Persona> buscarPorNombreYCiudades(String nombre, List<String> ciudades) throws SQLException;
}
