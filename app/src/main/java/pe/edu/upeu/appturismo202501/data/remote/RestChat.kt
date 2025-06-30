package pe.edu.upeu.appturismo202501.data.network

import pe.edu.upeu.appturismo202501.modelo.ConversacionDto
import pe.edu.upeu.appturismo202501.modelo.MensajeDto
import pe.edu.upeu.appturismo202501.modelo.ConversacionRequest
import pe.edu.upeu.appturismo202501.modelo.MensajeRequest
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface RestChat {

    // Crear o recuperar una conversación
    @POST("chat/abrir")
    suspend fun abrirConversacion(@Body request: ConversacionRequest): Response<ConversacionDto>

    // Enviar un mensaje
    @POST("chat/{conversaciones_id}/enviar")
    suspend fun enviarMensaje(@Path("conversaciones_id") conversacionesId: Long, @Body request: MensajeRequest): Response<MensajeDto>

    // Listar los mensajes de una conversación
    @GET("chat/{conversaciones_id}/mensajes")
    suspend fun listarMensajes(@Path("conversaciones_id") conversacionesId: Long): Response<List<MensajeDto>>
}
