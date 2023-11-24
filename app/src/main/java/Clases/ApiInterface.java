 package Clases;

import java.util.List;

import Models.Calificacion;
import Models.Favor;
import Models.Lugar;
import Models.Propuesta;
import Models.Servicio;
import Models.User;
import Models.Usuario;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Part;
import retrofit2.http.Path;
import retrofit2.http.Query;
import retrofit2.http.Url;

public interface ApiInterface {

    @POST("/login/")
    @FormUrlEncoded
    Call<User> login(
            @Field("username") String usuario,
            @Field("password") String pass
    );

    @POST("/a/usuario_create/")
    Call<User> registrarse(
            @Body User user
    );

    @GET("/a/estados/")
    Call<List<Lugar>> getEstados();

    @GET("/a/municipios/")
    Call<List<Lugar>> getMunicipios(
            @Query("estado") String idEstado
    );

    @GET
    Call<User> getUserInfo(
            @Url String url,
            @Header(("Authorization")) String token
    );


    @POST("/a/favor_create/")
    @Multipart
    Call<Favor> postFavor(
            @Header(("Authorization")) String token,
            @Part("titulo") RequestBody titulo,
            @Part("descripcion") RequestBody descripcion,
            @Part("destino") RequestBody destino,
            @Part("hora") RequestBody hora,
            @Part MultipartBody.Part img_producto
    );

    @POST("/a/servicio_create/")
    @Multipart
    Call<Servicio> postServicio(
            @Header(("Authorization")) String token,
            @Part("titulo") RequestBody titulo,
            @Part("descripcion") RequestBody descripcion,
            @Part("destino") RequestBody destino,
            @Part("hora") RequestBody hora,
            @Part MultipartBody.Part img_producto
    );

    @POST("/a/propuesta/")
    @FormUrlEncoded
    Call<Propuesta> guardarPropuesta(
            @Header(("Authorization")) String token,
            @Field("compi") String compi,
            @Field("favor") String favor,
            @Field("tiempo_estimado") String tiempo,
            @Field("pago_centrega") boolean pago,
            @Field("costo") String costo
    );

    @POST("/a/propuesta_servicio/")
    @FormUrlEncoded
    Call<Propuesta> guardarPropuestaServicio(
            @Header(("Authorization")) String token,
            @Field("profesionista") String profesionista,
            @Field("servicio") String servicio,
            @Field("costo") String costo
    );

    @PUT
    @FormUrlEncoded
    Call<Favor> updateStatusFavor(
            @Url String url,
            @Header(("Authorization")) String token,
            @Field("status") String status,
            @Field("motivo_cancelacion") String motivo,
            @Field("total") String total
    );

    @PUT
    @FormUrlEncoded
    Call<Servicio> updateStatusServicio(
            @Url String url,
            @Header(("Authorization")) String token,
            @Field("status") String status,
            @Field("motivo_cancelacion") String motivo,
            @Field("total") String total
    );

    @POST("/a/calif_compi_create/")
    @FormUrlEncoded
    Call<Calificacion> createCalifCompi(
            @Header(("Authorization")) String token,
            @Field("calificacion") float calif,
            @Field("comentario") String comentario,
            @Field("usuario") String usuario
    );

    @POST("/a/calif_esp_create/")
    @FormUrlEncoded
    Call<Calificacion> createCalifEsp(
            @Header(("Authorization")) String token,
            @Field("calificacion") float calif,
            @Field("comentario") String comentario,
            @Field("usuario") String usuario
    );

    @GET("/a/favor_list_usuario/")
    Call<List<Favor>> getFavores(
            @Header(("Authorization")) String token
    );

    @PUT
    @Multipart
    Call<Usuario> updateFotoPerfil(
            @Url String url,
            @Header(("Authorization")) String token,
            @Part MultipartBody.Part img_producto
    );

    @PUT
    @FormUrlEncoded
    Call<Usuario> updateFcm(
            @Url String url,
            @Header(("Authorization")) String token,
            @Field("fcm_id") String fcm_token
    );

    @PUT
    Call<User> updateDatos(
            @Url String url,
            @Header(("Authorization")) String token,
            @Body User user
    );

    @PUT
    @FormUrlEncoded
    Call<User> updatePass(
            @Url String url,
            @Header(("Authorization")) String token,
            @Field("password") String usuario
    );

    @GET
    Call<List<Calificacion>> getOpinionesUsuario(
            @Url String url,
            @Header(("Authorization")) String token
    );

}
