package agenda.domain;

import java.util.Objects;

public class Ciudad {
    private int idCiudad;
    private String nombre;
    private String provincia;
    private String pais;

    public Ciudad(int idCiudad){
        this.idCiudad = idCiudad;
    }

    public Ciudad(String nombre, String provincia, String pais){
        nameValidator(nombre);

        this.nombre = nombre.trim();
        this.provincia = provincia;
        this.pais = pais;
    }

    public Ciudad(int idCiudad, String nombre, String provincia, String pais){
        this(nombre, provincia, pais);
        this.idCiudad = idCiudad;
    }

    private void nameValidator(String nombre) {
        if (nombre == null || nombre.trim().isEmpty()) {
            throw new IllegalArgumentException("El nombre de la ciudad es obligatorio.");
        }
    }

    public String formatedCityName() {
        return nombre + ", " + provincia + ", " + pais;
    }

    // *
    public int getIdCiudad() {
        return idCiudad;
    }

    public void setIdCiudad(int idCiudad) {
        this.idCiudad = idCiudad;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getProvincia() {
        return provincia;
    }

    public void setProvincia(String provincia) {
        this.provincia = provincia;
    }

    public String getPais() {
        return pais;
    }

    public void setPais(String pais) {
        this.pais = pais;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Ciudad ciudad = (Ciudad) o;
        return idCiudad == ciudad.idCiudad && Objects.equals(nombre, ciudad.nombre) && Objects.equals(provincia, ciudad.provincia) && Objects.equals(pais, ciudad.pais);
    }

    @Override
    public int hashCode() {
        return Objects.hash(idCiudad, nombre, provincia, pais);
    }

    @Override
    public String toString() {
        return "Ciudad{" +
                "idCiudad=" + idCiudad +
                ", nombre='" + nombre + '\'' +
                ", provincia='" + provincia + '\'' +
                ", pais='" + pais + '\'' +
                '}';
    }
}
