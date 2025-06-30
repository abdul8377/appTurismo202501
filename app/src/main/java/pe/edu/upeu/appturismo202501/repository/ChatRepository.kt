package pe.edu.upeu.appturismo202501.repository

import pe.edu.upeu.appturismo202501.data.network.RestChat
import pe.edu.upeu.appturismo202501.modelo.ConversacionDto
import pe.edu.upeu.appturismo202501.modelo.MensajeDto
import pe.edu.upeu.appturismo202501.modelo.ConversacionRequest
import pe.edu.upeu.appturismo202501.modelo.MensajeRequest
import retrofit2.Response
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Define las operaciones que podemos hacer sobre el Chat.
 */
interface ChatRepository {

    /** Abre una nueva conversación */
    suspend fun abrirConversacion(conversacionRequest: ConversacionRequest): Response<ConversacionDto>

    /** Envía un mensaje a una conversación */
    suspend fun enviarMensaje(conversacionesId: Long, mensajeRequest: MensajeRequest): Response<MensajeDto>

    /** Obtiene los mensajes de una conversación */
    suspend fun listarMensajes(conversacionesId: Long): Response<List<MensajeDto>>
}

/**
 * Implementación concreta que usa Retrofit (RestChat) para
 * realizar las llamadas a la API de Chat.
 */
@Singleton
class ChatRepositoryImp @Inject constructor(
    private val restChat: RestChat
) : ChatRepository {

    override suspend fun abrirConversacion(conversacionRequest: ConversacionRequest): Response<ConversacionDto> =
        restChat.abrirConversacion(conversacionRequest)

    override suspend fun enviarMensaje(conversacionesId: Long, mensajeRequest: MensajeRequest): Response<MensajeDto> =
        restChat.enviarMensaje(conversacionesId, mensajeRequest)

    override suspend fun listarMensajes(conversacionesId: Long): Response<List<MensajeDto>> =
        restChat.listarMensajes(conversacionesId)
}