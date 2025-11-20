package chakuy.com.vbo

import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView

class HospitalAdapter(private var lista: ArrayList<HospitalUnit>) :
    RecyclerView.Adapter<HospitalAdapter.ViewHolder>() {

    fun actualizarLista(nuevaLista: ArrayList<HospitalUnit>) {
        this.lista = nuevaLista
        notifyDataSetChanged()
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val imgIcon: ImageView = itemView.findViewById(R.id.imgHospitalIcon)
        val tvNombre: TextView = itemView.findViewById(R.id.tvHospitalName)
        val tvCiudad: TextView = itemView.findViewById(R.id.tvHospitalCity)
        val btnCall: ImageView = itemView.findViewById(R.id.btnHospitalCall)
        val btnMap: ImageView = itemView.findViewById(R.id.btnHospitalMap)
        val btnShare: ImageView = itemView.findViewById(R.id.btnHospitalShare)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        // Usa el nuevo layout de fila
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_hospital_row, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = lista[position]
        val context = holder.itemView.context

        // Datos
        holder.tvNombre.text = item.nombre ?: "Hospital Desconocido"
        holder.tvCiudad.text = item.ciudad ?: "Sin Ciudad"
        holder.imgIcon.setImageResource(android.R.drawable.ic_menu_upload_you_tube) // Icono por defecto

        // 1. Botón LLAMAR
        holder.btnCall.setOnClickListener {
            val phone = item.getTelefonoString()
            if (phone.isNotEmpty()) {
                val intent = Intent(Intent.ACTION_DIAL)
                intent.data = Uri.parse("tel:$phone")
                context.startActivity(intent)
            } else {
                Toast.makeText(context, "Teléfono no disponible", Toast.LENGTH_SHORT).show()
            }
        }

        // 2. Botón UBICACIÓN (Abre Google Maps)
        holder.btnMap.setOnClickListener {
            // Conversión de coordenadas
            val lat = item.latitude.toString().toDoubleOrNull()
            val lon = item.longitude.toString().toDoubleOrNull()
            val context = holder.itemView.context

            if (lat != null && lon != null && lat != 0.0 && lon != 0.0) {

                // 1. Crear el nombre para el puntero (Encodeado para que no rompa la URL si tiene espacios)
                val label = Uri.encode(item.nombre ?: "Ubicación")

                // 2. CORRECCIÓN FINAL: Usamos el parámetro 'q' (Query)
                // geo:lat,lon?q=lat,lon(Nombre) -> Esto obliga a mostrar el PIN rojo.
                // &z=18 -> Esto fija el zoom cercano.
                val uriString = "geo:$lat,$lon?q=$lat,$lon($label)&z=18"

                val gmmIntentUri = Uri.parse(uriString)
                val mapIntent = Intent(Intent.ACTION_VIEW, gmmIntentUri)
                mapIntent.setPackage("com.google.android.apps.maps")

                if (mapIntent.resolveActivity(context.packageManager) != null) {
                    context.startActivity(mapIntent)
                } else {
                    // Fallback para navegador (usando las variables lat y lon correctas)
                    val webUrl = "https://www.google.com/maps/search/?api=1&query=$lat,$lon"
                    val webIntent = Intent(Intent.ACTION_VIEW, Uri.parse(webUrl))
                    context.startActivity(webIntent)
                }
            } else {
                Toast.makeText(context, "Ubicación GPS no disponible", Toast.LENGTH_SHORT).show()
            }
        }

// 3. Botón COMPARTIR (NUEVA LÓGICA)
        holder.btnShare.setOnClickListener {
            shareHospitalDetails(context, item)
        }

    }

    override fun getItemCount(): Int = lista.size
    // --- FUNCIÓN AUXILIAR PARA COMPARTIR DATOS ---
    private fun shareHospitalDetails(context: android.content.Context, hospital: HospitalUnit) {
        val phone = hospital.getTelefonoString()

        // CORRECCIÓN AQUÍ:
        // No usamos 'lat' ni 'lon' sueltos. Usamos 'hospital.latitude' y 'hospital.longitude'.
        val locationText = if (hospital.latitude != null && hospital.latitude != 0.0) {
            // Construimos el link usando las propiedades del objeto hospital
            "📍 Ubicación GPS: https://maps.google.com/?q=${hospital.latitude},${hospital.longitude}"
        } else {
            "Ubicación no registrada."
        }

        // 2. Construir el mensaje formateado
        val shareMessage = """
            🏥 *DETALLE DEL HOSPITAL* 🏥
            
            Nombre: ${hospital.nombre ?: "N/A"}
            Ciudad: ${hospital.ciudad ?: "N/A"}
            📞 Teléfono: ${phone.ifEmpty { "N/A" }}
            
            $locationText
            
            ¡Mantente seguro! #EmergenciaBolivia
        """.trimIndent()

        // 3. Crear Intent Universal
        val shareIntent = Intent(Intent.ACTION_SEND).apply {
            type = "text/plain"
            putExtra(Intent.EXTRA_TEXT, shareMessage)
        }

        // Usamos Intent.createChooser
        context.startActivity(Intent.createChooser(shareIntent, "Compartir detalles por..."))
    }
}