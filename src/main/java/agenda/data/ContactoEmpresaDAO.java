package agenda.data;

import agenda.domain.Ciudad;
import agenda.domain.ContactoEmpresa;
import agenda.domain.Direccion;
import agenda.domain.Empresa;
import agenda.domain.Persona;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import static agenda.connection.DBConnection.getDatabaseConnection;

public class ContactoEmpresaDAO implements IContactoEmpresaDAO {

    @Override
    public List<ContactoEmpresa> contactList() throws SQLException {
        List<ContactoEmpresa> contacts = new ArrayList<>();
        Connection con = getDatabaseConnection();
        var sql = """
                SELECT ce.cargo,
                       
                       e.id_empresa, e.razon_social, e.telefono AS empresa_telefono,
                       de.id_direccion AS empresa_direccion_id, de.calle AS empresa_calle,
                       de.numero AS empresa_numero, de.piso AS empresa_piso, de.depto AS empresa_depto,
                       de.cp AS empresa_cp, ce_ciudad.id_ciudad AS empresa_ciudad_id,
                       ce_ciudad.nombre AS empresa_ciudad_nombre, ce_ciudad.provincia AS empresa_provincia,
                       ce_ciudad.pais AS empresa_pais,
                       
                       p.id_persona, p.nombre, p.apellido, p.telefono AS persona_telefono, p.email,
                       dp.id_direccion AS persona_direccion_id, dp.calle AS persona_calle,
                       dp.numero AS persona_numero, dp.piso AS persona_piso, dp.depto AS persona_depto,
                       dp.cp AS persona_cp, cp_ciudad.id_ciudad AS persona_ciudad_id,
                       cp_ciudad.nombre AS persona_ciudad_nombre, cp_ciudad.provincia AS persona_provincia,
                       cp_ciudad.pais AS persona_pais
                FROM contacto_empresa ce
                INNER JOIN empresa e ON ce.id_empresa = e.id_empresa
                INNER JOIN persona p ON ce.id_persona = p.id_persona
                LEFT JOIN direccion de ON e.id_direccion = de.id_direccion
                LEFT JOIN ciudad ce_ciudad ON de.id_ciudad = ce_ciudad.id_ciudad
                LEFT JOIN direccion dp ON p.id_direccion = dp.id_direccion
                LEFT JOIN ciudad cp_ciudad ON dp.id_ciudad = cp_ciudad.id_ciudad
                ORDER BY e.id_empresa, p.id_persona
                """;
        try {
            PreparedStatement ps = con.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                contacts.add(buildContact(rs));
            }
        } catch (Exception e) {
            System.out.println("Error al listar contactos empresa: " + e.getMessage());
        } finally {
            try {
                con.close();
            } catch (Exception e) {
                System.out.println("Error al cerrar conexion: " + e.getMessage());
            }
        }
        return contacts;
    }

    @Override
    public boolean getContact(ContactoEmpresa contactoEmpresa) throws SQLException {
        Connection con = getDatabaseConnection();
        var sql = """
                SELECT ce.cargo,
                       e.id_empresa, e.razon_social, e.telefono AS empresa_telefono,
                       de.id_direccion AS empresa_direccion_id, de.calle AS empresa_calle,
                       de.numero AS empresa_numero, de.piso AS empresa_piso, de.depto AS empresa_depto,
                       de.cp AS empresa_cp, ce_ciudad.id_ciudad AS empresa_ciudad_id,
                       ce_ciudad.nombre AS empresa_ciudad_nombre, ce_ciudad.provincia AS empresa_provincia,
                       ce_ciudad.pais AS empresa_pais,
                       p.id_persona, p.nombre, p.apellido, p.telefono AS persona_telefono, p.email,
                       dp.id_direccion AS persona_direccion_id, dp.calle AS persona_calle,
                       dp.numero AS persona_numero, dp.piso AS persona_piso, dp.depto AS persona_depto,
                       dp.cp AS persona_cp, cp_ciudad.id_ciudad AS persona_ciudad_id,
                       cp_ciudad.nombre AS persona_ciudad_nombre, cp_ciudad.provincia AS persona_provincia,
                       cp_ciudad.pais AS persona_pais
                FROM contacto_empresa ce
                INNER JOIN empresa e ON ce.id_empresa = e.id_empresa
                INNER JOIN persona p ON ce.id_persona = p.id_persona
                LEFT JOIN direccion de ON e.id_direccion = de.id_direccion
                LEFT JOIN ciudad ce_ciudad ON de.id_ciudad = ce_ciudad.id_ciudad
                LEFT JOIN direccion dp ON p.id_direccion = dp.id_direccion
                LEFT JOIN ciudad cp_ciudad ON dp.id_ciudad = cp_ciudad.id_ciudad
                WHERE ce.id_empresa = ? AND ce.id_persona = ?
                """;
        try {
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setInt(1, contactoEmpresa.getEmpresa().getIdEmpresa());
            ps.setInt(2, contactoEmpresa.getPersona().getIdPersona());
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                ContactoEmpresa found = buildContact(rs);
                contactoEmpresa.setEmpresa(found.getEmpresa());
                contactoEmpresa.setPersona(found.getPersona());
                contactoEmpresa.setCargo(found.getCargo());
                return true;
            }
        } catch (Exception e) {
            System.out.println("Error al recuperar contacto empresa: " + e.getMessage());
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
    public boolean addContact(ContactoEmpresa contactoEmpresa) throws SQLException {
        Connection con = getDatabaseConnection();
        var sql = "INSERT INTO contacto_empresa(id_empresa, id_persona, cargo) VALUES(?, ?, ?)";
        try {
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setInt(1, contactoEmpresa.getEmpresa().getIdEmpresa());
            ps.setInt(2, contactoEmpresa.getPersona().getIdPersona());
            ps.setString(3, contactoEmpresa.getCargo());
            ps.execute();
            return true;
        } catch (Exception e) {
            System.out.println("Error al agregar contacto empresa: " + e.getMessage());
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
    public boolean modifyContact(ContactoEmpresa contactoEmpresa) throws SQLException {
        Connection con = getDatabaseConnection();
        var sql = """
                UPDATE contacto_empresa
                SET cargo = ?
                WHERE id_empresa = ? AND id_persona = ?
                """;
        try {
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setString(1, contactoEmpresa.getCargo());
            ps.setInt(2, contactoEmpresa.getEmpresa().getIdEmpresa());
            ps.setInt(3, contactoEmpresa.getPersona().getIdPersona());
            ps.execute();
            return true;
        } catch (Exception e) {
            System.out.println("Error al modificar contacto empresa: " + e.getMessage());
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
    public boolean deleteContact(int idEmpresa, int idPersona) throws SQLException {
        Connection con = getDatabaseConnection();
        var sql = "DELETE FROM contacto_empresa WHERE id_empresa = ? AND id_persona = ?";
        try {
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setInt(1, idEmpresa);
            ps.setInt(2, idPersona);
            ps.execute();
            return true;
        } catch (Exception e) {
            System.out.println("Error al eliminar contacto empresa: " + e.getMessage());
        } finally {
            try {
                con.close();
            } catch (Exception e) {
                System.out.println("Error al cerrar conexion: " + e.getMessage());
            }
        }
        return false;
    }

    private ContactoEmpresa buildContact(ResultSet rs) throws SQLException {
        Empresa empresa = new Empresa(
                rs.getInt("id_empresa"),
                rs.getString("razon_social"),
                rs.getString("empresa_telefono"),
                buildAddress(
                        rs,
                        "empresa_direccion_id",
                        "empresa_calle",
                        "empresa_numero",
                        "empresa_piso",
                        "empresa_depto",
                        "empresa_cp",
                        "empresa_ciudad_id",
                        "empresa_ciudad_nombre",
                        "empresa_provincia",
                        "empresa_pais"
                )
        );

        Persona persona = new Persona(
                rs.getString("nombre"),
                rs.getString("apellido"),
                rs.getString("persona_telefono"),
                rs.getString("email"),
                buildAddress(
                        rs,
                        "persona_direccion_id",
                        "persona_calle",
                        "persona_numero",
                        "persona_piso",
                        "persona_depto",
                        "persona_cp",
                        "persona_ciudad_id",
                        "persona_ciudad_nombre",
                        "persona_provincia",
                        "persona_pais"
                )
        );
        persona.setIdPersona(rs.getInt("id_persona"));

        return new ContactoEmpresa(empresa, persona, rs.getString("cargo"));
    }

    private Direccion buildAddress(
            ResultSet rs,
            String addressIdColumn,
            String streetColumn,
            String numberColumn,
            String floorColumn,
            String apartmentColumn,
            String zipCodeColumn,
            String cityIdColumn,
            String cityNameColumn,
            String provinceColumn,
            String countryColumn) throws SQLException {
        int idDireccion = rs.getInt(addressIdColumn);
        if (rs.wasNull()) {
            return null;
        }
        Ciudad ciudad = new Ciudad(
                rs.getInt(cityIdColumn),
                rs.getString(cityNameColumn),
                rs.getString(provinceColumn),
                rs.getString(countryColumn)
        );
        return new Direccion(
                idDireccion,
                rs.getString(streetColumn),
                rs.getInt(numberColumn),
                rs.getString(floorColumn),
                rs.getString(apartmentColumn),
                rs.getString(zipCodeColumn),
                ciudad
        );
    }

    public static void main(String[] args) throws SQLException {
        System.out.println("TEST GET");
        var dao =  new ContactoEmpresaDAO();
        dao.contactList().forEach(System.out::println);
    }
}
