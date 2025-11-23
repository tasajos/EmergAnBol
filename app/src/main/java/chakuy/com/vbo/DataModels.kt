package chakuy.com.vbo
import java.io.Serializable

// Clases de datos usadas en varias Activities
data class NavItem(val title: String, val iconResId: Int, val actionId: String)
data class CategoryItem(val name: String, val imageResId: Int, val bgColor: String)
data class InfoItem(val title: String, val desc: String, val iconResId: Int)
data class GroupItem(val title: String, val iconResId: Int)

data class BomberoUnit(
    override val nombre: String? = null,
    override val ciudad: String? = null,
    override val imagen: String? = null, // La URL de la imagen
    override val telefono: Any?= null,
    val whatsapp: Any? = null,
    override val latitude: Double?= null,
    override val longitude: Double? = null,
    val facebook: String? = null,
    val web: String? = null,
    val mapa: String? = null
): Serializable,SearchableUnit
{
    // ... (tus funciones auxiliares siguen aquí) ...
    override fun getTelefonoString(): String = telefono?.toString() ?: ""
    fun getWhatsappString(): String = whatsapp?.toString() ?: ""

}

data class AmbulanciaUnit(
    override val nombre: String? = null,
    override val ciudad: String? = null,
    override val imagen: String? = null,
    override val telefono: Any? = null, // Usamos Any para evitar errores de String/Long
    val whatsapp: Any? = null,
    override val latitude: Double? = null,
    override val longitude: Double? = null,
    val facebook: String? = null,
    val web: String? = null,
    val mapa: String? = null
) : Serializable,SearchableUnit
{
    // ... (tus funciones auxiliares siguen aquí) ...
    override fun getTelefonoString(): String = telefono?.toString() ?: ""
    fun getWhatsappString(): String = whatsapp?.toString() ?: ""
}

data class AnimalistaUnit(
    override val nombre: String? = null,
    override val ciudad: String? = null,
    override val imagen: String? = null,
    override val telefono: Any? = null,
    val whatsapp: Any? = null,
    override val latitude: Double? = null,
    override val longitude: Double? = null,
    val facebook: String? = null,
    val web: String? = null,
    val mapa: String? = null
) : Serializable,SearchableUnit {
    override fun getTelefonoString(): String = telefono?.toString() ?: ""
    fun getWhatsappString(): String = whatsapp?.toString() ?: ""
}

data class AmbientalistasUnit(
    override val nombre: String? = null,
    override val ciudad: String? = null,
    override val imagen: String? = null,
    override  val telefono: Any? = null,
    val whatsapp: Any? = null,
    override val latitude: Double? = null,
    override val longitude: Double? = null,
    val facebook: String? = null,
    val web: String? = null,
    val mapa: String? = null
) : Serializable,SearchableUnit  {
    override fun getTelefonoString(): String = telefono?.toString() ?: ""
    fun getWhatsappString(): String = whatsapp?.toString() ?: ""
}

data class EducacionUnit(
    override val nombre: String? = null,
    override val ciudad: String? = null,
    override val imagen: String? = null,
    override val telefono: Any? = null,
    val whatsapp: Any? = null,
    override val latitude: Double? = null,
    override val longitude: Double? = null,
    val facebook: String? = null,
    val web: String? = null,
    val mapa: String? = null
)  :Serializable,SearchableUnit  {
    override fun getTelefonoString(): String = telefono?.toString() ?: ""
    fun getWhatsappString(): String = whatsapp?.toString() ?: ""
}
data class HospitalUnit(
    override val nombre: String? = null,
    override val ciudad: String? = null,
    override val imagen: String? = null, // No se usará, pero mantenemos la estructura
    override val telefono: Any? = null,
    val whatsapp: Any? = null,
    override val latitude: Double? = null,
    override val longitude: Double? = null,
    val facebook: String? = null,
    val web: String? = null,
    val mapa: String? = null
) :Serializable,SearchableUnit {
    override fun getTelefonoString(): String = telefono?.toString() ?: ""
    fun getWhatsappString(): String = whatsapp?.toString() ?: ""
}
interface SearchableUnit : Serializable {
    val nombre: String?
    val ciudad: String?
    val imagen: String? // Necesario para mostrar la imagen en el modal
    val telefono: Any?
    val latitude: Double?
    val longitude: Double?
    fun getTelefonoString(): String // Asegúrate de que esta función exista en todos los modelos
}
data class SimpleSearchResult(
    val nombre: String,
    val telefono: String,
    val ciudad: String,
    val imageUrl: String?,
    val lat: Double?,
    val lon: Double?
)

data class Noticia(
    val titulo: String? = null,
    val contenido: String? = null,
    val imagen: String? = null, // URL de la imagen principal
    val fuente: String? = null  // URL de la fuente original
) :Serializable

data class EmergenciaReporte(
    val titulo: String? = null,
    val descripcion: String? = null,
    val ciudad: String? = null,
    val estado: String? = null, // Ej: "En proceso", "Pendiente", "Finalizado"
    val fecha: String? = null,
    val hora: String? = null,
    val imagen: String? = null, // URL de la foto
    val ubicacion: String? = null, // Link de Google Maps
    val tipo: String? = null, // Ej: "Rescate", "Incendio"
    val asignadoA: String? = null // Ej: "Yunka Atoq"
) : Serializable