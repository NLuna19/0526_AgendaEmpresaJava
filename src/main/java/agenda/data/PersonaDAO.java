package agenda.data;

import agenda.domain.Ciudad;
import agenda.domain.Direccion;
import agenda.domain.Persona;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import static agenda.connection.DBConnection.getDatabaseConnection;

public class PersonaDAO implements IPersonaDAO {

    @Override
    public List<Persona> personList() throws SQLException {
        List<Persona> persons = new ArrayList<>();
        Connection con = getDatabaseConnection();
        var sql = """
                SELECT p.id_persona, p.nombre, p.apellido, p.telefono, p.email,
                       d.id_direccion, d.calle, d.numero, d.piso, d.depto, d.cp,
                       c.id_ciudad, c.nombre AS ciudad_nombre, c.provincia, c.pais
                FROM persona p
                LEFT JOIN direccion d ON p.id_direccion = d.id_direccion
                LEFT JOIN ciudad c ON d.id_ciudad = c.id_ciudad
                ORDER BY p.id_persona
                """;
        try {
            PreparedStatement ps = con.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                persons.add(buildPerson(rs));
            }
        } catch (Exception e) {
            System.out.println("Error al listar personas: " + e.getMessage());
        } finally {
            try {
                con.close();
            } catch (Exception e) {
                System.out.println("Error al cerrar conexion: " + e.getMessage());
            }
        }
        return persons;
    }

    @Override
    public boolean getPersonById(Persona persona) throws SQLException {
        Connection con = getDatabaseConnection();
        var sql = """
                SELECT p.id_persona, p.nombre, p.apellido, p.telefono, p.email,
                       d.id_direccion, d.calle, d.numero, d.piso, d.depto, d.cp,
                       c.id_ciudad, c.nombre AS ciudad_nombre, c.provincia, c.pais
                FROM persona p
                LEFT JOIN direccion d ON p.id_direccion = d.id_direccion
                LEFT JOIN ciudad c ON d.id_ciudad = c.id_ciudad
                WHERE p.id_persona = ?
                """;
        try {
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setInt(1, persona.getIdPersona());
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                Persona found = buildPerson(rs);
                persona.setNombre(found.getNombre());
                persona.setApellido(found.getApellido());
                persona.setTelefono(found.getTelefono());
                persona.setEmail(found.getEmail());
                persona.setDireccion(found.getDireccion());
                return true;
            }
        } catch (Exception e) {
            System.out.println("Error al recuperar persona por id: " + e.getMessage());
        } finally {
            try {
                con.close();
            } catch (Exception e) {
                System.out.println("Error al cerrar conexion: " + e.getMessage());
            }
        }
        return false;
    }

    @Override
    public boolean addPerson(Persona persona) throws SQLException {
        Connection con = getDatabaseConnection();
        var sql = "INSERT INTO persona(nombre, apellido, telefono, email, id_direccion) VALUES(?, ?, ?, ?, ?)";
        try {
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setString(1, persona.getNombre());
            ps.setString(2, persona.getApellido());
            ps.setString(3, persona.getTelefono());
            ps.setString(4, persona.getEmail());
            if (persona.getDireccion() != null) {
                ps.setInt(5, persona.getDireccion().getIdDireccion());
            } else {
                ps.setNull(5, java.sql.Types.INTEGER);
            }
            ps.execute();
            return true;
        } catch (Exception e) {
            System.out.println("Error al agregar persona: " + e.getMessage());
        } finally {
            try {
                con.close();
            } catch (Exception e) {
                System.out.println("Error al cerrar conexion: " + e.getMessage());
            }
        }
        return false;
    }

    @Override
    public boolean modifyPerson(Persona persona) throws SQLException {
        Connection con = getDatabaseConnection();
        var sql = """
                UPDATE persona
                SET nombre = ?, apellido = ?, telefono = ?, email = ?, id_direccion = ?
                WHERE id_persona = ?
                """;
        try {
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setString(1, persona.getNombre());
            ps.setString(2, persona.getApellido());
            ps.setString(3, persona.getTelefono());
            ps.setString(4, persona.getEmail());
            if (persona.getDireccion() != null) {
                ps.setInt(5, persona.getDireccion().getIdDireccion());
            } else {
                ps.setNull(5, java.sql.Types.INTEGER);
            }
            ps.setInt(6, persona.getIdPersona());
            ps.execute();
            return true;
        } catch (Exception e) {
            System.out.println("Error al modificar persona: " + e.getMessage());
        } finally {
            try {
                con.close();
            } catch (Exception e) {
                System.out.println("Error al cerrar conexion: " + e.getMessage());
            }
        }
        return false;
    }

    @Override
    public boolean deletePerson(int idPersona) throws SQLException {
        Connection con = getDatabaseConnection();
        var sql = "DELETE FROM persona WHERE id_persona = ?";
        try {
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setInt(1, idPersona);
            ps.execute();
            return true;
        } catch (Exception e) {
            System.out.println("Error al eliminar persona: " + e.getMessage());
        } finally {
            try {
                con.close();
            } catch (Exception e) {
                System.out.println("Error al cerrar conexion: " + e.getMessage());
            }
        }
        return false;
    }

    private Persona buildPerson(ResultSet rs) throws SQLException {
        Direccion direccion = buildAddressIfPresent(rs);
        Persona persona = new Persona(
                rs.getString("nombre"),
                rs.getString("apellido"),
                rs.getString("telefono"),
                rs.getString("email"),
                direccion
        );
        persona.setIdPersona(rs.getInt("id_persona"));
        return persona;
    }

    private Direccion buildAddressIfPresent(ResultSet rs) throws SQLException {
        int idDireccion = rs.getInt("id_direccion");
        if (rs.wasNull()) {
            return null;
        }
        Ciudad ciudad = new Ciudad(
                rs.getInt("id_ciudad"),
                rs.getString("ciudad_nombre"),
                rs.getString("provincia"),
                rs.getString("pais")
        );
        return new Direccion(
                idDireccion,
                rs.getString("calle"),
                rs.getInt("numero"),
                rs.getString("piso"),
                rs.getString("depto"),
                rs.getString("cp"),
                ciudad
        );
    }

    public static void main(String[] args) throws SQLException {
        System.out.println("TEST GET");
        var dao =  new PersonaDAO();
        dao.personList().forEach(System.out::println);
    }
}
