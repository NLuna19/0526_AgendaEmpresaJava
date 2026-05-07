package agenda.data;

import agenda.domain.Ciudad;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public interface ICiudadDAO {
    List<Ciudad> cityList() throws SQLException;
    boolean getCityById(Ciudad ciudad) throws SQLException;
    Ciudad addCity(Ciudad ciudad) throws SQLException;
    boolean modifyCity(Ciudad ciudad) throws SQLException;
    boolean deleteCity(int idCiudad) throws SQLException;
    Optional<Ciudad> findCity(Ciudad ciudad) throws SQLException;
}
