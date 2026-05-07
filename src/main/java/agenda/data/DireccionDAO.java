package agenda.data;

import agenda.domain.Ciudad;
import agenda.domain.Direccion;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static agenda.connection.DBConnection.getDatabaseConnection;

public class DireccionDAO implements IDireccionDAO {

    @Override
    public List<Direccion> addressList() throws SQLException {
        List<Direccion> addresses = new ArrayList<>();
        Connection con = getDatabaseConnection();
        var sql = """
                SELECT d.id_direccion, d.calle, d.numero, d.piso, d.depto, d.cp,
                       c.id_ciudad, c.nombre, c.provincia, c.pais
                FROM direccion d
                INNER JOIN ciudad c ON d.id_ciudad = c.id_ciudad
                ORDER BY d.id_direccion
                """;
        try {
            PreparedStatement ps = con.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                addresses.add(buildAddress(rs));
            }
        } catch (Exception e) {
            System.out.println("Error al listar direcciones: " + e.getMessage());
        } finally {
            try {
                con.close();
            } catch (Exception e) {
                System.out.println("Error al cerrar conexion: " + e.getMessage());
            }
        }
        return addresses;
    }

    @Override
    public boolean getAddressById(Direccion direccion) throws SQLException {
        Connection con = getDatabaseConnection();
        var sql = """
                SELECT d.id_direccion, d.calle, d.numero, d.piso, d.depto, d.cp,
                       c.id_ciudad, c.nombre, c.provincia, c.pais
                FROM direccion d
                INNER JOIN ciudad c ON d.id_ciudad = c.id_ciudad
                WHERE d.id_direccion = ?
                """;
        try {
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setInt(1, direccion.getIdDireccion());
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                Direccion found = buildAddress(rs);
                direccion.setCalle(found.getCalle());
                direccion.setNumero(found.getNumero());
                direccion.setPiso(found.getPiso());
                direccion.setDepto(found.getDepto());
                direccion.setCp(found.getCp());
                direccion.setCiudad(found.getCiudad());
                return true;
            }
        } catch (Exception e) {
            System.out.println("Error al recuperar direccion por id: " + e.getMessage());
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
    public Direccion addAddress(Direccion direccion) throws SQLException {
        String sql = """
            INSERT INTO direccion(calle, numero, piso, depto, cp, id_ciudad) 
                VALUES(?, ?, ?, ?, ?, ?)
        """;

        try(
            Connection con = getDatabaseConnection();
            PreparedStatement ps = con.prepareStatement(
                sql,
                Statement.RETURN_GENERATED_KEYS
            )
        ) {
            ps.setString(1, direccion.getCalle());
            ps.setInt(2, direccion.getNumero());
            ps.setString(3, direccion.getPiso());
            ps.setString(4, direccion.getDepto());
            ps.setString(5, direccion.getCp());
            ps.setInt(6, direccion.getCiudad().getIdCiudad());
            ps.executeUpdate();
            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) {
                    int generatedId = rs.getInt(1);
                    direccion.setIdDireccion(generatedId);
                }
            }
            return direccion;
        } catch (SQLException e) {
            System.out.println("Error al agregar direccion: " + e.getMessage());
            throw e;
        }
    }

    @Override
    public boolean modifyAddress(Direccion direccion) throws SQLException {
        Connection con = getDatabaseConnection();
        var sql = """
                UPDATE direccion
                SET calle = ?, numero = ?, piso = ?, depto = ?, cp = ?, id_ciudad = ?
                WHERE id_direccion = ?
                """;
        try {
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setString(1, direccion.getCalle());
            ps.setInt(2, direccion.getNumero());
            ps.setString(3, direccion.getPiso());
            ps.setString(4, direccion.getDepto());
            ps.setString(5, direccion.getCp());
            ps.setInt(6, direccion.getCiudad().getIdCiudad());
            ps.setInt(7, direccion.getIdDireccion());
            ps.execute();
            return true;
        } catch (Exception e) {
            System.out.println("Error al modificar direccion: " + e.getMessage());
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
    public boolean deleteAddress(int idDireccion) throws SQLException {
        Connection con = getDatabaseConnection();
        var sql = "DELETE FROM direccion WHERE id_direccion = ?";
        try {
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setInt(1, idDireccion);
            ps.execute();
            return true;
        } catch (Exception e) {
            System.out.println("Error al eliminar direccion: " + e.getMessage());
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
    public Optional<Direccion> findAddress(Direccion direccion) throws SQLException {
        String sql = """ 
            SELECT 
                d.id_direccion, d.calle, d.numero, d.piso, d.depto, d.cp,
                c.id_ciudad, c.nombre, c.provincia, c.pais
            FROM direccion d
            INNER JOIN ciudad c
            ON d.id_ciudad = c.id_ciudad
            WHERE LOWER(d.calle) = LOWER(?)
                AND d.numero = ?
                AND LOWER(IFNULL(d.piso, '')) = LOWER(IFNULL(?, ''))
                AND LOWER(IFNULL(d.depto, '')) = LOWER(IFNULL(?, ''))
                AND LOWER(IFNULL(d.cp, '')) = LOWER(IFNULL(?, ''))
                AND d.id_ciudad = ?
            """;
        try (
            Connection con = getDatabaseConnection();
            PreparedStatement ps = con.prepareStatement(sql)
        ) {
            ps.setString(1, direccion.getCalle());
            ps.setInt(2, direccion.getNumero());
            ps.setString(3, direccion.getPiso());
            ps.setString(4, direccion.getDepto());
            ps.setString(5, direccion.getCp());
            ps.setInt(6, direccion.getCiudad().getIdCiudad());
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    Ciudad _ciudad = new Ciudad(
                        rs.getInt("id_ciudad"),
                        rs.getString("nombre"),
                        rs.getString("provincia"),
                        rs.getString("pais")
                    );
                    Direccion _direccion = new Direccion(
                        rs.getInt("id_direccion"),
                        rs.getString("calle"),
                        rs.getInt("numero"),
                        rs.getString("piso"),
                        rs.getString("depto"),
                        rs.getString("cp"),
                        _ciudad
                    );
                    return Optional.of(_direccion);
                }
            }
        } catch (SQLException e) {
            System.out.println("Error al buscar ciudad: " + e.getMessage());
            throw e;
        }
        return Optional.empty();
    }

    private Direccion buildAddress(ResultSet rs) throws SQLException {
        Ciudad ciudad = new Ciudad(
                rs.getInt("id_ciudad"),
                rs.getString("nombre"),
                rs.getString("provincia"),
                rs.getString("pais")
        );
        return new Direccion(
                rs.getInt("id_direccion"),
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
        var dao =  new DireccionDAO();
        dao.addressList().forEach(System.out::println);
    }
}
