package agenda.data;

import agenda.domain.Empresa;

import java.sql.SQLException;
import java.util.List;

public interface IEmpresaDAO {
    List<Empresa> companyList() throws SQLException;
    boolean getCompanyById(Empresa empresa) throws SQLException;
    boolean addCompany(Empresa empresa) throws SQLException;
    boolean modifyCompany(Empresa empresa) throws SQLException;
    boolean deleteCompany(int idEmpresa) throws SQLException;
}
