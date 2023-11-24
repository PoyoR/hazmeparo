package Models;

public class Calificacion {

    private int id;
    private float calificacion;
    private String comentario;
    private String fecha;
    private int usuario;

    public Calificacion(int id, float calificacion, String fecha, String comentario, int usuario) {
        this.id = id;
        this.calificacion = calificacion;
        this.fecha = fecha;
        this.comentario = comentario;
        this.usuario = usuario;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public float getCalificacion() {
        return calificacion;
    }

    public void setCalificacion(float calificacion) {
        this.calificacion = calificacion;
    }

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

    public String getComentario() {
        return comentario;
    }

    public void setComentario(String comentario) {
        this.comentario = comentario;
    }

    public int getUsuario() {
        return usuario;
    }

    public void setUsuario(int usuario) {
        this.usuario = usuario;
    }
}
