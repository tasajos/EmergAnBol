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