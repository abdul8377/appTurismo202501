package pe.edu.upeu.appturismo202501.modelo

data class Message(
    val statusCode: Long,
    val datetime: String,
    val message: String,
    val details: String
)
