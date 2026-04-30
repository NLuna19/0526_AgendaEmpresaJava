package agenda.domain;

import java.util.Objects;

public class Persona {
    private int idPersona;
    private String nombre;
    private String apellido;
    private String telefono;
    private String email;
    private Direccion direccion;

    public Persona() {}

    public Persona(int idPersona) {
        this.idPersona = idPersona;
    }

    public Persona(String nombre, String apellido, String telefono, String email, Direccion direccion) {
        this.nombre = nombre;
        this.apellido = apellido;
        this.telefono = telefono;
        this.email = email;
        this.direccion = direccion;
    }

    // *
    public int getIdPersona() {
        return idPersona;
    }

    public void setIdPersona(int idPersona) {
        this.idPersona = idPersona;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getApellido() {
        return apellido;
    }

    public void setApellido(String apellido) {
        this.apellido = apellido;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Direccion getDireccion() {
        return this.direccion;
    }

    public void setDireccion(Direccion Direccion) {
        this.direccion = Direccion;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Persona persona = (Persona) o;
        return idPersona == persona.idPersona && this.direccion == persona.direccion && Objects.equals(nombre, persona.nombre) && Objects.equals(apellido, persona.apellido) && Objects.equals(telefono, persona.telefono) && Objects.equals(email, persona.email);
    }

    @Override
    public int hashCode() {
        return Objects.hash(idPersona, nombre, apellido, telefono, email, direccion);
    }

    @Override
    public String toString() {
        return "Persona{" +
                "idPersona=" + idPersona +
                ", nombre='" + nombre + '\'' +
                ", apellido='" + apellido + '\'' +
                ", telefono='" + telefono + '\'' +
                ", email='" + email + '\'' +
                ", direccion=" + direccion +
                '}';
    }
}
