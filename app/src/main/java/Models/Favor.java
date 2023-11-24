package Models;

public class Favor {

    private String id;
    private String fecha;
    private int status;
    private String titulo;
    private String descripcion;
    private String origen;
    private String destino;
    private User usuario;
    private User compi;
    private float calificacion;
    private float total;
    private String hora;
    private String img_producto;
    private String compi_nombre;

    public Favor(String id, String fecha, int status, String titulo, String descripcion, String origen, String destino, User usuario, User compi, float calificacion,
                 float total, String hora, String img_producto, String compi_nombre) {
        this.id = id;
        this.fecha = fecha;
        this.status = status;
        this.titulo = titulo;
        this.descripcion = descripcion;
        this.origen = origen;
        this.destino = destino;
        this.usuario = usuario;
        this.compi = compi;
        this.calificacion = calificacion;
        this.total = total;
        this.hora = hora;
        this.img_producto = img_producto;
        this.compi_nombre = compi_nombre;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getOrigen() {
        return origen;
    }

    public void setOrigen(String origen) {
        this.origen = origen;
    }

    public String getDestino() {
        return destino;
    }

    public void setDestino(String destino) {
        this.destino = destino;
    }

    public User getUsuario() {
        return usuario;
    }

    public void setUsuario(User usuario) {
        this.usuario = usuario;
    }

    public User getCompi() {
        return compi;
    }

    public void setCompi(User compi) {
        this.compi = compi;
    }

    public float getCalificacion() {
        return calificacion;
    }

    public void setCalificacion(float calificacion) {
        this.calificacion = calificacion;
    }

    public float getTotal() {
        return total;
    }

    public void setTotal(float total) {
        this.total = total;
    }

    public String getHora() {
        return hora;
    }

    public void setHora(String hora) {
        this.hora = hora;
    }

    public String getImg_producto() {
        return img_producto;
    }

    public void setImg_producto(String img_producto) {
        this.img_producto = img_producto;
    }

    public String getCompi_nombre() {
        return compi_nombre;
    }

    public void setCompi_nombre(String compi_nombre) {
        this.compi_nombre = compi_nombre;
    }
}
