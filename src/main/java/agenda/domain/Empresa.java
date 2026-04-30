package agenda.domain;

import java.util.Objects;

public class Empresa {
    private int idEmpresa;
    private String razonSocial;
    private String telefono;
    private Direccion direccion;

    public Empresa() {}

    public Empresa(String razonSocial, String telefono, Direccion direccion) {
        this.razonSocial = razonSocial;
        this.telefono = telefono;
        this.direccion = direccion;
    }

    public Empresa(int idEmpresa, String razonSocial, String telefono, Direccion direccion) {
        this(razonSocial, telefono, direccion);
        this.idEmpresa = idEmpresa;
    }

    //*

    public int getIdEmpresa() {
        return idEmpresa;
    }

    public void setIdEmpresa(int idEmpresa) {
        this.idEmpresa = idEmpresa;
    }

    public String getRazonSocial() {
        return razonSocial;
    }

    public void setRazonSocial(String razonSocial) {
        this.razonSocial = razonSocial;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public Direccion getDireccion() {
        return direccion;
    }

    public void setDireccion(Direccion direccion) {
        this.direccion = direccion;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Empresa empresa = (Empresa) o;
        return idEmpresa == empresa.idEmpresa && Objects.equals(razonSocial, empresa.razonSocial) && Objects.equals(telefono, empresa.telefono) && Objects.equals(direccion, empresa.direccion);
    }

    @Override
    public int hashCode() {
        return Objects.hash(idEmpresa, razonSocial, telefono, direccion);
    }

    @Override
    public String toString() {
        return "Empresa{" +
                "idEmpresa=" + idEmpresa +
                ", razonSocial='" + razonSocial + '\'' +
                ", telefono='" + telefono + '\'' +
                ", direccion=" + direccion +
                '}';
    }
}
