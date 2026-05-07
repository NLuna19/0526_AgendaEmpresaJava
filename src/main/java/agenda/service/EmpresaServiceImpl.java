package agenda.service;

import agenda.data.ICiudadDAO;
import agenda.data.IDireccionDAO;
import agenda.data.IEmpresaDAO;
import agenda.domain.Ciudad;
import agenda.domain.Direccion;
import agenda.domain.Empresa;

import java.sql.SQLException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

public class EmpresaServiceImpl implements EmpresaService {
    private final IEmpresaDAO empresaDAO;
    private final IDireccionDAO direccionDAO;
    private final ICiudadDAO ciudadDAO;

    public EmpresaServiceImpl(
            IEmpresaDAO empresaDAO,
            IDireccionDAO direccionDAO,
            ICiudadDAO ciudadDAO
    ) {
        this.empresaDAO = Objects.requireNonNull(empresaDAO, "empresaDAO es obligatorio");
        this.direccionDAO = Objects.requireNonNull(direccionDAO, "direccionDAO es obligatorio");
        this.ciudadDAO = Objects.requireNonNull(ciudadDAO, "ciudadDAO es obligatorio");
    }

    @Override
    public List<Empresa> listar() throws SQLException {
        return empresaDAO.companyList();
    }

    @Override
    public Optional<Empresa> buscarPorId(int idEmpresa) throws SQLException {
        if (idEmpresa <= 0) {
            throw new IllegalArgumentException("El id de empresa debe ser mayor a cero.");
        }

        Empresa empresa = new Empresa();
        empresa.setIdEmpresa(idEmpresa);
        return empresaDAO.getCompanyById(empresa)
                ? Optional.of(empresa)
                : Optional.empty();
    }

    @Override
    public boolean crear(Empresa empresa) throws SQLException {
        validarEmpresa(empresa);
        Direccion direccionPersistida = obtenerOPersistirDireccion(empresa.getDireccion());
        empresa.setDireccion(direccionPersistida);
        return empresaDAO.addCompany(empresa);
    }

    @Override
    public boolean actualizar(Empresa empresa) throws SQLException {
        validarEmpresa(empresa);
        if (empresa.getIdEmpresa() <= 0) {
            throw new IllegalArgumentException("El id de empresa es obligatorio para actualizar.");
        }

        Direccion direccionPersistida = obtenerOPersistirDireccion(empresa.getDireccion());
        empresa.setDireccion(direccionPersistida);
        return empresaDAO.modifyCompany(empresa);
    }

    @Override
    public boolean eliminar(int idEmpresa) throws SQLException {
        if (idEmpresa <= 0) {
            throw new IllegalArgumentException("El id de empresa debe ser mayor a cero.");
        }
        return empresaDAO.deleteCompany(idEmpresa);
    }

    private Direccion obtenerOPersistirDireccion(Direccion direccion) throws SQLException {
        Ciudad ciudadPersistida = obtenerOPersistirCiudad(direccion.getCiudad());
        direccion.setCiudad(ciudadPersistida);
        Optional<Direccion> direccionExistente = direccionDAO.findAddress(direccion);
        if (direccionExistente.isPresent()) {
            return direccionExistente.get();
        }
        return direccionDAO.addAddress(direccion);
    }

    private Ciudad obtenerOPersistirCiudad(Ciudad ciudad) throws SQLException {
        validarCiudad(ciudad);
        Optional<Ciudad> ciudadExistente = ciudadDAO.findCity(ciudad);
        if (ciudadExistente.isPresent()) {
            return ciudadExistente.get();
        }
        return ciudadDAO.addCity(ciudad);
    }

    private void validarEmpresa(Empresa empresa) {
        if (empresa == null) {
            throw new IllegalArgumentException("La empresa es obligatoria.");
        }
        validarTexto(empresa.getRazonSocial(), "La razon social es obligatoria.");
        if (empresa.getDireccion() == null) {
            throw new IllegalArgumentException("La direccion es obligatoria.");
        }
    }

    private void validarCiudad(Ciudad ciudad) {
        if (ciudad == null) {
            throw new IllegalArgumentException("La ciudad es obligatoria.");
        }
        validarTexto(ciudad.getNombre(), "El nombre de ciudad es obligatorio.");
    }

    private void validarTexto(String valor, String mensaje) {
        if (valor == null || valor.trim().isEmpty()) {
            throw new IllegalArgumentException(mensaje);
        }
    }
}
