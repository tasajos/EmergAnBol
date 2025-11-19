package chakuy.com.vbo

// Clases de datos usadas en varias Activities
data class NavItem(val title: String, val iconResId: Int, val actionId: String)
data class CategoryItem(val name: String, val imageResId: Int, val bgColor: String)
data class InfoItem(val title: String, val desc: String, val iconResId: Int)
data class GroupItem(val title: String, val iconResId: Int)