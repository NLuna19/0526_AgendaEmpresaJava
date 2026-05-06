package agenda.data;

import agenda.domain.Ciudad;
import static agenda.connection.DBConnection.getDatabaseConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class CiudadDAO implements ICiudadDAO{

    @Override
    public List<Ciudad> cityList() throws SQLException {
        List<Ciudad> cities = new ArrayList<>();
        var sql = "SELECT * FROM ciudad ORDER BY id_ciudad";
        try (
                Connection con = getDatabaseConnection();
                PreparedStatement ps = con.prepareStatement(sql);
                ResultSet rs = ps.executeQuery()
        ) {
            while(rs.next()){
                var city = new Ciudad(
                        rs.getInt("id_ciudad"),
                        rs.getString("nombre"),
                        rs.getString("provincia"),
                        rs.getString("pais")
                );
                cities.add(city);
            }
        } catch (Exception e){
            System.out.println("Error al listar ciudades: " + e.getMessage());
        }
        return cities;
    }

    @Override
    public boolean getCityById(Ciudad ciudad) throws SQLException {
        var sql = "SELECT * FROM ciudad WHERE id_ciudad = ?";
        try (
            Connection con = getDatabaseConnection();
            PreparedStatement ps = con.prepareStatement(sql);
            ResultSet rs = prepareAndExecute(ps, ciudad.getIdCiudad())
        ) {
            if(rs.next()) {
                ciudad.setNombre(rs.getString("nombre"));
                ciudad.setProvincia(rs.getString("provincia"));
                ciudad.setPais(rs.getString("pais"));
                return true;
            }

        } catch ( Exception e ){
            System.out.println("Error al recuperar ciudad por id: " + e.getMessage());
        }

        return false;
    }

    @Override
    public boolean addCity(Ciudad ciudad) throws SQLException {
        String sql = "INSERT INTO ciudad(nombre, provincia, pais) "
                + "VALUES(?, ?, ?)";
        try (
                Connection con = getDatabaseConnection();
                PreparedStatement ps = con.prepareStatement(sql)
        ) {
            ps.setString(1, ciudad.getNombre());
            ps.setString(2, ciudad.getProvincia());
            ps.setString(3, ciudad.getPais());
            ps.executeUpdate();
            return true;
        }catch (Exception e) {
            System.out.println("Error al agregar ciudad: "+ e.getMessage());
        }
        return false;
    }

    @Override
    public boolean modifyCity(Ciudad ciudad) throws SQLException{
        var sql = "UPDATE ciudad SET nombre=?, provincia=?, pais=? "+
                " WHERE id_ciudad=?";
        try (
                Connection con = getDatabaseConnection();
                PreparedStatement ps = con.prepareStatement(sql)
        ) {
            ps.setString(1, ciudad.getNombre());
            ps.setString(2, ciudad.getProvincia());
            ps.setString(3, ciudad.getPais());
            ps.setInt(4, ciudad.getIdCiudad());
            ps.executeUpdate();
            return true;
        }catch (Exception e) {
            System.out.println("Error al modificar ciudad: "+ e.getMessage());
        }
        return false;
    }

    @Override
    public boolean deleteCity(int idCiudad) throws SQLException{
        String sql = "DELETE FROM ciudad WHERE id_ciudad = ?";

        try (
                Connection con = getDatabaseConnection();
                PreparedStatement ps = con.prepareStatement(sql)
        ) {
            ps.setInt(1, idCiudad);
            ps.executeUpdate();
            return true;
        }catch (Exception e) {
            System.out.println("Error al eliminar ciudad: "+ e.getMessage());
        }
        return false;
    }

    private ResultSet prepareAndExecute(PreparedStatement ps, int idCiudad) throws SQLException {
        ps.setInt(1, idCiudad);
        return ps.executeQuery();
    }

    public static void main(String[] args) throws SQLException {
        System.out.println("TEST GET");
        var dao =  new CiudadDAO();
        dao.cityList().forEach(System.out::println);
    }
}
