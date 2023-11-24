package Models;

public class PropuestaServicio {

    private String key;
    private String favor;
    private String compi;
    private String costo;
    private String comentario;
    private String calif_compi;
    private String numero_compi;
    private String img;
    private String compi_id;

    public PropuestaServicio(){

    }

    public PropuestaServicio(String key, String favor, String compi, String costo, String comentario, String calif_compi, String numero_compi, String img, String compi_id) {
        this.key = key;
        this.favor = favor;
        this.compi = compi;
        this.costo = costo;
        this.comentario = comentario;
        this.calif_compi = calif_compi;
        this.numero_compi = numero_compi;
        this.img = img;
        this.compi_id = compi_id;
    }

    public String getComentario() {
        return comentario;
    }

    public void setComentario(String comentario) {
        this.comentario = comentario;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getFavor() {
        return favor;
    }

    public void setFavor(String favor) {
        this.favor = favor;
    }



    public String getCompi() {
        return compi;
    }

    public void setCompi(String compi) {
        this.compi = compi;
    }

    public String getCosto() {
        return costo;
    }

    public void setCosto(String costo) {
        this.costo = costo;
    }

    public String getCalif_compi() {
        return calif_compi;
    }

    public void setCalif_compi(String calif_compi) {
        this.calif_compi = calif_compi;
    }

    public String getNumero_compi() {
        return numero_compi;
    }

    public void setNumero_compi(String numero_compi) {
        this.numero_compi = numero_compi;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public String getCompi_id() {
        return compi_id;
    }

    public void setCompi_id(String compi_id) {
        this.compi_id = compi_id;
    }
}
