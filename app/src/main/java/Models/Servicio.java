package Models;

public class Servicio {

    private String id;
    private String fecha;
    private int status;
    private String titulo;
    private String descripcion;
    private String destino;
    private User usuario;
    private User profesionista;
    private float total;
    private String hora;
    private String img_producto;
    private String profesionista_nombre;

    public Servicio(String id, String fecha, int status, String titulo, String descripcion, String destino, User usuario, User profesionista,
                    float total, String hora, String img_producto, String profesionista_nombre) {

        this.id = id;
        this.fecha = fecha;
        this.status = status;
        this.titulo = titulo;
        this.descripcion = descripcion;
        this.destino = destino;
        this.usuario = usuario;
        this.profesionista = profesionista;
        this.total = total;
        this.hora = hora;
        this.img_producto = img_producto;
        this.profesionista_nombre = profesionista_nombre;
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

    public User getProfesionista() {
        return profesionista;
    }

    public void setProfesionista(User profesionista) {
        this.profesionista = profesionista;
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

    public String getProfesionista_nombre() {
        return profesionista_nombre;
    }

    public void setProfesionista_nombre(String profesionista_nombre) {
        this.profesionista_nombre = profesionista_nombre;
    }
}
