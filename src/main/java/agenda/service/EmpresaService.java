package agenda.service;

import agenda.domain.Empresa;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public interface EmpresaService {
    List<Empresa> listar() throws SQLException;

    Optional<Empresa> buscarPorId(int idEmpresa) throws SQLException;

    boolean crear(Empresa empresa) throws SQLException;

    boolean actualizar(Empresa empresa) throws SQLException;

    boolean eliminar(int idEmpresa) throws SQLException;
}
