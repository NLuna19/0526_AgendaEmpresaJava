package agenda.domain;

import java.util.Objects;

public class ContactoEmpresa {
    private Empresa empresa;
    private Persona persona;
    private String cargo;

    public ContactoEmpresa(
            Empresa empresa,
            Persona persona,
            String cargo) {

        validarEmpresa(empresa);
        validarPersona(persona);

        this.empresa = empresa;
        this.persona = persona;
        this.cargo = cargo;
    }

    private void validarEmpresa(Empresa empresa) {
        if (empresa == null) {
            throw new IllegalArgumentException("La empresa es obligatoria.");
        }
    }

    private void validarPersona(Persona persona) {
        if (persona == null) {
            throw new IllegalArgumentException("La persona es obligatoria.");
        }
    }

    public Empresa getEmpresa() {
        return empresa;
    }

    public void setEmpresa(Empresa empresa) {
        this.empresa = empresa;
    }

    public Persona getPersona() {
        return persona;
    }

    public void setPersona(Persona persona) {
        this.persona = persona;
    }

    public String getCargo() {
        return cargo;
    }

    public void setCargo(String cargo) {
        this.cargo = cargo;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        ContactoEmpresa that = (ContactoEmpresa) o;
        return Objects.equals(empresa, that.empresa) && Objects.equals(persona, that.persona) && Objects.equals(cargo, that.cargo);
    }

    @Override
    public int hashCode() {
        return Objects.hash(empresa, persona, cargo);
    }

    @Override
    public String toString() {
        return "ContactoEmpresa{" +
                "empresa=" + empresa +
                ", persona=" + persona +
                ", cargo='" + cargo + '\'' +
                '}';
    }
}
