package chakuy.com.vbo

// Clases de datos usadas en varias Activities
data class NavItem(val title: String, val iconResId: Int, val actionId: String)
data class CategoryItem(val name: String, val imageResId: Int, val bgColor: String)
data class InfoItem(val title: String, val desc: String, val iconResId: Int)
data class GroupItem(val title: String, val iconResId: Int)

data class BomberoUnit(
    val nombre: String? = null,
    val ciudad: String? = null,
    val imagen: String? = null, // La URL de la imagen
    val telefono: Any?= null, // En tu imagen aparece como número, puede ser Long o String
    val whatsapp: Any? = null,
    val latitude: Double?= null,
    val longitude: Double? = null,
    val facebook: String? = null,
    val web: String? = null,
    val mapa: String? = null
)