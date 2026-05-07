package agenda.data;

import agenda.domain.Direccion;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public interface IDireccionDAO {
    List<Direccion> addressList() throws SQLException;
    boolean getAddressById(Direccion direccion) throws SQLException;
    Direccion addAddress(Direccion direccion) throws SQLException;
    boolean modifyAddress(Direccion direccion) throws SQLException;
    boolean deleteAddress(int idDireccion) throws SQLException;
    Optional<Direccion> findAddress(Direccion direccion) throws SQLException;
}
