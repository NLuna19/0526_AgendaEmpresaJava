package agenda.data;

import agenda.domain.Ciudad;

import java.sql.SQLException;
import java.util.List;

public interface ICiudadDAO {
    List<Ciudad> cityList() throws SQLException;
    boolean getCityById(Ciudad ciudad) throws SQLException;
    boolean addCity(Ciudad ciudad) throws SQLException;
    boolean modifyCity(Ciudad ciudad) throws SQLException;
    boolean deleteCity(int idCiudad) throws SQLException;
}
