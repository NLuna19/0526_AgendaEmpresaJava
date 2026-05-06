package agenda.data;

import agenda.domain.Ciudad;
import agenda.domain.Direccion;
import agenda.domain.Empresa;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import static agenda.connection.DBConnection.getDatabaseConnection;

public class EmpresaDAO implements IEmpresaDAO {

    @Override
    public List<Empresa> companyList() throws SQLException {
        List<Empresa> companies = new ArrayList<>();
        Connection con = getDatabaseConnection();
        var sql = """
                SELECT e.id_empresa, e.razon_social, e.telefono,
                       d.id_direccion, d.calle, d.numero, d.piso, d.depto, d.cp,
                       c.id_ciudad, c.nombre AS ciudad_nombre, c.provincia, c.pais
                FROM empresa e
                LEFT JOIN direccion d ON e.id_direccion = d.id_direccion
                LEFT JOIN ciudad c ON d.id_ciudad = c.id_ciudad
                ORDER BY e.id_empresa
                """;
        try {
            PreparedStatement ps = con.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                companies.add(buildCompany(rs));
            }
        } catch (Exception e) {
            System.out.println("Error al listar empresas: " + e.getMessage());
        } finally {
            try {
                con.close();
            } catch (Exception e) {
                System.out.println("Error al cerrar conexion: " + e.getMessage());
            }
        }
        return companies;
    }

    @Override
    public boolean getCompanyById(Empresa empresa) throws SQLException {
        Connection con = getDatabaseConnection();
        var sql = """
                SELECT e.id_empresa, e.razon_social, e.telefono,
                       d.id_direccion, d.calle, d.numero, d.piso, d.depto, d.cp,
                       c.id_ciudad, c.nombre AS ciudad_nombre, c.provincia, c.pais
                FROM empresa e
                LEFT JOIN direccion d ON e.id_direccion = d.id_direccion
                LEFT JOIN ciudad c ON d.id_ciudad = c.id_ciudad
                WHERE e.id_empresa = ?
                """;
        try {
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setInt(1, empresa.getIdEmpresa());
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                Empresa found = buildCompany(rs);
                empresa.setRazonSocial(found.getRazonSocial());
                empresa.setTelefono(found.getTelefono());
                empresa.setDireccion(found.getDireccion());
                return true;
            }
        } catch (Exception e) {
            System.out.println("Error al recuperar empresa por id: " + e.getMessage());
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
    public boolean addCompany(Empresa empresa) throws SQLException {
        Connection con = getDatabaseConnection();
        var sql = "INSERT INTO empresa(razon_social, telefono, id_direccion) VALUES(?, ?, ?)";
        try {
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setString(1, empresa.getRazonSocial());
            ps.setString(2, empresa.getTelefono());
            if (empresa.getDireccion() != null) {
                ps.setInt(3, empresa.getDireccion().getIdDireccion());
            } else {
                ps.setNull(3, java.sql.Types.INTEGER);
            }
            ps.execute();
            return true;
        } catch (Exception e) {
            System.out.println("Error al agregar empresa: " + e.getMessage());
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
    public boolean modifyCompany(Empresa empresa) throws SQLException {
        Connection con = getDatabaseConnection();
        var sql = """
                UPDATE empresa
                SET razon_social = ?, telefono = ?, id_direccion = ?
                WHERE id_empresa = ?
                """;
        try {
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setString(1, empresa.getRazonSocial());
            ps.setString(2, empresa.getTelefono());
            if (empresa.getDireccion() != null) {
                ps.setInt(3, empresa.getDireccion().getIdDireccion());
            } else {
                ps.setNull(3, java.sql.Types.INTEGER);
            }
            ps.setInt(4, empresa.getIdEmpresa());
            ps.execute();
            return true;
        } catch (Exception e) {
            System.out.println("Error al modificar empresa: " + e.getMessage());
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
    public boolean deleteCompany(int idEmpresa) throws SQLException {
        Connection con = getDatabaseConnection();
        var sql = "DELETE FROM empresa WHERE id_empresa = ?";
        try {
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setInt(1, idEmpresa);
            ps.execute();
            return true;
        } catch (Exception e) {
            System.out.println("Error al eliminar empresa: " + e.getMessage());
        } finally {
            try {
                con.close();
            } catch (Exception e) {
                System.out.println("Error al cerrar conexion: " + e.getMessage());
            }
        }
        return false;
    }

    private Empresa buildCompany(ResultSet rs) throws SQLException {
        Direccion direccion = buildAddressIfPresent(rs);
        Empresa empresa = new Empresa(
                rs.getString("razon_social"),
                rs.getString("telefono"),
                direccion
        );
        empresa.setIdEmpresa(rs.getInt("id_empresa"));
        return empresa;
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
        var dao =  new EmpresaDAO();
        dao.companyList().forEach(System.out::println);
    }
}
