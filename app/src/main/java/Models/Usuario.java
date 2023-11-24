package Models;

public class Usuario {

    private String estado;
    private String municipio;
    private String estado_nombre;
    private String municipio_nombre;
    private String foto;
    private String direccion;
    private String ubicacion;
    private String calificacion;
    private String calificacion_compi;
    private String calificacion_especialista;
    private String transporte;
    private String fcm_id;

    public Usuario(String estado, String municipio, String foto, String direccion, String ubicacion, String calificacion, String calificacion_compi, String transporte, String fcm_id) {
        this.estado = estado;
        this.municipio = municipio;
        this.foto = foto;
        this.direccion = direccion;
        this.ubicacion = ubicacion;
        this.calificacion = calificacion;
        this.calificacion = calificacion_compi;
        this.transporte = transporte;
        this.fcm_id = fcm_id;
    }

    public String getCalificacion_especialista() {
        return calificacion_especialista;
    }

    public void setCalificacion_especialista(String calificacion_especialista) {
        this.calificacion_especialista = calificacion_especialista;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public String getCiudad() {
        return municipio;
    }

    public void setCiudad(String ciudad) {
        this.municipio = ciudad;
    }

    public String getEstado_nombre() {
        return estado_nombre;
    }

    public void setEstado_nombre(String estado_nombre) {
        this.estado_nombre = estado_nombre;
    }

    public String getMunicipio_nombre() {
        return municipio_nombre;
    }

    public void setMunicipio_nombre(String municipio_nombre) {
        this.municipio_nombre = municipio_nombre;
    }

    public String getFoto() {
        return foto;
    }

    public void setFoto(String foto) {
        this.foto = foto;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public String getUbicacion() {
        return ubicacion;
    }

    public void setUbicacion(String ubicacion) {
        this.ubicacion = ubicacion;
    }

    public String getCalificacion() {
        return calificacion;
    }

    public void setCalificacion(String calificacion) {
        this.calificacion = calificacion;
    }

    public String getCalificacion_compi() {
        return calificacion_compi;
    }

    public void setCalificacion_compi(String calificacion_compi) {
        this.calificacion_compi = calificacion_compi;
    }

    public String getMunicipio() {
        return municipio;
    }

    public void setMunicipio(String municipio) {
        this.municipio = municipio;
    }

    public String getTransporte() {
        return transporte;
    }

    public void setTransporte(String transporte) {
        this.transporte = transporte;
    }
}