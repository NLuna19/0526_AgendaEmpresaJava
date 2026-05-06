package agenda.data;

import agenda.domain.Direccion;

import java.sql.SQLException;
import java.util.List;

public interface IDireccionDAO {
    List<Direccion> addressList() throws SQLException;
    boolean getAddressById(Direccion direccion) throws SQLException;
    boolean addAddress(Direccion direccion) throws SQLException;
    boolean modifyAddress(Direccion direccion) throws SQLException;
    boolean deleteAddress(int idDireccion) throws SQLException;
}
