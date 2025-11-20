package chakuy.com.vbo

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import java.util.Locale
import android.util.Log


class SearchResultAdapter(private val results: List<SimpleSearchResult>) :
    RecyclerView.Adapter<SearchResultAdapter.ViewHolder>() {

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val imgLogo: ImageView = itemView.findViewById(R.id.imgResultLogo)
        val tvName: TextView = itemView.findViewById(R.id.tvResultName)
        val tvCity: TextView = itemView.findViewById(R.id.tvResultCity)
        val tvPhone: TextView = itemView.findViewById(R.id.tvResultPhone)
        val btnCall: ImageView = itemView.findViewById(R.id.btnResultCall)
        val btnMap: ImageView = itemView.findViewById(R.id.btnResultMap)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_search_result, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = results[position]
        val context = holder.itemView.context

        holder.tvName.text = item.nombre
        holder.tvCity.text = item.ciudad
        holder.tvPhone.text = "Teléfono: ${item.telefono}"

        // Cargar imagen (Si no hay URL, usa un icono por defecto)
        if (!item.imageUrl.isNullOrEmpty()) {
            Glide.with(context)
                .load(item.imageUrl)
                .circleCrop()
                .placeholder(android.R.drawable.ic_menu_help)
                .error(android.R.drawable.ic_menu_help)
                .into(holder.imgLogo)
        } else {
            holder.imgLogo.setImageResource(android.R.drawable.ic_menu_help)
        }

        // Botón Llamar
        holder.btnCall.setOnClickListener {
            if (item.telefono.isNotEmpty()) {
                context.startActivity(Intent(Intent.ACTION_DIAL, Uri.parse("tel:${item.telefono}")))
            } else {
                Toast.makeText(context, "Teléfono no disponible", Toast.LENGTH_SHORT).show()
            }
        }

        // Botón Mapa
        holder.btnMap.setOnClickListener {
            val lat = item.lat
            val lon = item.lon

            // 1. Verificamos que las coordenadas sean válidas
            if (lat != null && lon != null && lat != 0.0 && lon != 0.0) {

                // IMPORTANTE: Imprimimos en Logcat para que puedas verificar
                Log.d("MAP_LINK", "Abriendo ${item.nombre}. Coordenadas enviadas: Lat=$lat, Lon=$lon")

                // 2. Usamos el formato HTTPS universal (https://maps.google.com/?q=lat,lon)
                val universalMapUrl = "https://maps.google.com/?q=$lat,$lon"

                val mapIntent = Intent(Intent.ACTION_VIEW, Uri.parse(universalMapUrl))

                // NO FORZAMOS EL PAQUETE, dejamos que el sistema resuelva
                try {
                    context.startActivity(mapIntent)
                } catch (e: Exception) {
                    Toast.makeText(context, "Error al abrir el mapa.", Toast.LENGTH_SHORT).show()
                }
            } else {
                Toast.makeText(context, "Ubicación GPS no disponible para ${item.nombre}.", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun getItemCount(): Int = results.size
}