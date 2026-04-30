package agenda.domain;

import java.util.Objects;

public class Direccion {
    private int idDireccion;
    private String calle;
    private int numero;
    private String piso;
    private String depto;
    private String cp;
    private Ciudad ciudad;

    public Direccion(
            String calle,
            int numero,
            String piso,
            String depto,
            String cp,
            Ciudad ciudad) {
        streetValidator(calle);
        streetNumberValidator(numero);
        cityValidator(ciudad);

        this.calle = calle.trim();
        this.numero = numero;
        this.piso = piso;
        this.depto = depto;
        this.cp = cp;
        this.ciudad = ciudad;
    }

    public Direccion(
            Integer idDireccion,
            String calle,
            Integer numero,
            String piso,
            String depto,
            String codigoPostal,
            Ciudad ciudad) {
        this(calle, numero, piso, depto, codigoPostal, ciudad);
        this.idDireccion = idDireccion;
    }

    private void streetValidator(String calle) {
        if (calle == null || calle.trim().isEmpty()) {
            throw new IllegalArgumentException("La calle es obligatoria.");
        }
    }

    private void streetNumberValidator(int numero) {
        if (numero <= 0) {
            throw new IllegalArgumentException("Número inválido.");
        }
    }

    private void cityValidator(Ciudad ciudad) {
        if (ciudad == null) {
            throw new IllegalArgumentException("La ciudad es obligatoria.");
        }
    }

    //*

    public int getIdDireccion() {
        return idDireccion;
    }

    public void setIdDireccion(int idDireccion) {
        this.idDireccion = idDireccion;
    }

    public String getCalle() {
        return calle;
    }

    public void setCalle(String calle) {
        this.calle = calle;
    }

    public int getNumero() {
        return numero;
    }

    public void setNumero(int numero) {
        this.numero = numero;
    }

    public String getPiso() {
        return piso;
    }

    public void setPiso(String piso) {
        this.piso = piso;
    }

    public String getDepto() {
        return depto;
    }

    public void setDepto(String depto) {
        this.depto = depto;
    }

    public String getCp() {
        return cp;
    }

    public void setCp(String cp) {
        this.cp = cp;
    }

    public Ciudad getCiudad() {
        return ciudad;
    }

    public void setCiudad(Ciudad ciudad) {
        this.ciudad = ciudad;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Direccion direccion = (Direccion) o;
        return idDireccion == direccion.idDireccion && numero == direccion.numero && Objects.equals(calle, direccion.calle) && Objects.equals(piso, direccion.piso) && Objects.equals(depto, direccion.depto) && Objects.equals(cp, direccion.cp) && Objects.equals(ciudad, direccion.ciudad);
    }

    @Override
    public int hashCode() {
        return Objects.hash(idDireccion, calle, numero, piso, depto, cp, ciudad);
    }

    @Override
    public String toString() {
        return "Direccion{" +
                "idDireccion=" + idDireccion +
                ", calle='" + calle + '\'' +
                ", numero=" + numero +
                ", piso='" + piso + '\'' +
                ", depto='" + depto + '\'' +
                ", cp='" + cp + '\'' +
                ", ciudad=" + ciudad +
                '}';
    }
}
