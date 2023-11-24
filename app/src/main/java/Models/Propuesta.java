package Models;

public class Propuesta {

    private String key;
    private String favor;
    private String pago_centrega;
    private String compi;
    private String costo;
    private String tiempo_estimado;
    private String transporte_compi;
    private String calif_compi;
    private String numero_compi;
    private String img;
    private String compi_id;

    public Propuesta(){

    }

    public Propuesta(String key, String favor, String pago_centrega, String compi, String costo, String tiempo_estimado, String transporte_compi, String calif_compi, String numero_compi, String img, String compi_id) {
        this.key = key;
        this.favor = favor;
        this.pago_centrega = pago_centrega;
        this.compi = compi;
        this.costo = costo;
        this.tiempo_estimado = tiempo_estimado;
        this.transporte_compi = transporte_compi;
        this.calif_compi = calif_compi;
        this.numero_compi = numero_compi;
        this.img = img;
        this.compi_id = compi_id;
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

    public String getPago_centrega() {
        return pago_centrega;
    }

    public void setPago_centrega(String pago_centrega) {
        this.pago_centrega = pago_centrega;
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

    public String getTiempo_estimado() {
        return tiempo_estimado;
    }

    public void setTiempo_estimado(String tiempo_estimado) {
        this.tiempo_estimado = tiempo_estimado;
    }

    public String getTransporte_compi() {
        return transporte_compi;
    }

    public void setTransporte_compi(String transporte_compi) {
        this.transporte_compi = transporte_compi;
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
