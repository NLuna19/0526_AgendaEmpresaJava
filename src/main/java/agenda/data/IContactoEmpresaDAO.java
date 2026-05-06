package agenda.data;

import agenda.domain.ContactoEmpresa;

import java.sql.SQLException;
import java.util.List;

public interface IContactoEmpresaDAO {
    List<ContactoEmpresa> contactList() throws SQLException;
    boolean getContact(ContactoEmpresa contactoEmpresa) throws SQLException;
    boolean addContact(ContactoEmpresa contactoEmpresa) throws SQLException;
    boolean modifyContact(ContactoEmpresa contactoEmpresa) throws SQLException;
    boolean deleteContact(int idEmpresa, int idPersona) throws SQLException;
}
