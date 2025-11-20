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
            val lat = item.latitude
            val lon = item.longitude

            if (lat != null && lon != null && lat != 0.0) {
                // Abre Google Maps con coordenadas
                val gmmIntentUri = Uri.parse("geo:$lat,$lon?q=${Uri.encode(item.nombre)}")
                val mapIntent = Intent(Intent.ACTION_VIEW, gmmIntentUri)
                mapIntent.setPackage("com.google.android.apps.maps")

                if (mapIntent.resolveActivity(context.packageManager) != null) {
                    context.startActivity(mapIntent)
                } else {
                    Toast.makeText(context, "Abriendo mapa en navegador web...", Toast.LENGTH_SHORT).show()
                    // Si no tiene Google Maps, usa el navegador
                    val webMapIntent = Intent(Intent.ACTION_VIEW, Uri.parse("https://maps.google.com/?q=$lat,$lon"))
                    context.startActivity(webMapIntent)
                }
            } else if (!item.mapa.isNullOrEmpty()) {
                // Si solo hay un link de mapa (ej. Google Maps URL en 'mapa')
                val mapIntent = Intent(Intent.ACTION_VIEW, Uri.parse(item.mapa))
                context.startActivity(mapIntent)
            } else {
                Toast.makeText(context, "Ubicación GPS no disponible", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun getItemCount(): Int = lista.size
}