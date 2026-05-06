package agenda.data;

import agenda.domain.Persona;

import java.sql.SQLException;
import java.util.List;

public interface IPersonaDAO {
    List<Persona> personList() throws SQLException;
    boolean getPersonById(Persona persona) throws SQLException;
    boolean addPerson(Persona persona) throws SQLException;
    boolean modifyPerson(Persona persona) throws SQLException;
    boolean deletePerson(int idPersona) throws SQLException;
}
