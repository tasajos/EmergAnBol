package chakuy.com.vbo

import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast // Agregado para mostrar mensajes de error
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import android.widget.ProgressBar
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.target.Target
import android.graphics.drawable.Drawable

class EmergenciasFeedAdapter(private val lista: ArrayList<EmergenciaReporte>) :
    RecyclerView.Adapter<EmergenciasFeedAdapter.ViewHolder>() {

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvTitulo: TextView = itemView.findViewById(R.id.tvTitulo)
        val tvFecha: TextView = itemView.findViewById(R.id.tvFechaHora)
        val tvEstado: TextView = itemView.findViewById(R.id.tvEstadoBadge)
        val tvDesc: TextView = itemView.findViewById(R.id.tvDescripcion)
        val imgEmergencia: ImageView = itemView.findViewById(R.id.imgEmergencia)
        val tvAsignado: TextView = itemView.findViewById(R.id.tvAsignado)
        val btnMapa: Button = itemView.findViewById(R.id.btnVerMapa)
        val imgProgressBar: ProgressBar = itemView.findViewById(R.id.imgProgressBar)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_emergencia_feed, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = lista[position]
        val context = holder.itemView.context

        holder.tvTitulo.text = item.titulo ?: "Emergencia"
        holder.tvFecha.text = "${item.fecha} - ${item.hora}"
        holder.tvDesc.text = item.descripcion ?: "Sin detalles."

        // Estado
        holder.tvEstado.text = item.estado ?: "Desconocido"

        // Asignado A
        if (!item.asignadoA.isNullOrEmpty()) {
            holder.tvAsignado.text = "Atendido por: ${item.asignadoA}"
            holder.tvAsignado.setTextColor(android.graphics.Color.parseColor("#4CAF50")) // Verde
        } else {
            holder.tvAsignado.text = "Estado: Buscando unidad de respuesta..."
            holder.tvAsignado.setTextColor(android.graphics.Color.parseColor("#FF9800")) // Naranja
        }

        // Imagen
        if (!item.imagen.isNullOrEmpty() && item.imagen != "N/A") {
            holder.imgEmergencia.visibility = View.VISIBLE
            holder.imgProgressBar.visibility = View.VISIBLE

            Glide.with(context)
                .load(item.imagen)
                .centerCrop()
                .placeholder(android.R.drawable.ic_menu_gallery)
                .listener(object : RequestListener<Drawable> {
                    override fun onLoadFailed(e: GlideException?, model: Any?, target: Target<Drawable>, isFirstResource: Boolean): Boolean {
                        holder.imgProgressBar.visibility = View.GONE
                        return false
                    }

                    override fun onResourceReady(resource: Drawable, model: Any, target: Target<Drawable>, dataSource: DataSource, isFirstResource: Boolean): Boolean {
                        holder.imgProgressBar.visibility = View.GONE
                        return false
                    }
                })
                .into(holder.imgEmergencia)
        } else {
            holder.imgEmergencia.visibility = View.GONE
            holder.imgProgressBar.visibility = View.GONE
        }

        // --- AQUÍ ESTABA FALTANDO LA LÓGICA DEL BOTÓN MAPA ---
        holder.btnMapa.setOnClickListener {
            val ubicacionUrl = item.ubicacion

            if (!ubicacionUrl.isNullOrEmpty()) {
                try {
                    val intent = Intent(Intent.ACTION_VIEW, Uri.parse(ubicacionUrl))

                    // Intentamos forzar que abra con Google Maps
                    intent.setPackage("com.google.android.apps.maps")

                    if (intent.resolveActivity(context.packageManager) != null) {
                        context.startActivity(intent)
                    } else {
                        // Si no tiene la app, quitamos el paquete para que abra con el navegador
                        intent.setPackage(null)
                        context.startActivity(intent)
                    }
                } catch (e: Exception) {
                    Toast.makeText(context, "Error al abrir el mapa", Toast.LENGTH_SHORT).show()
                }
            } else {
                Toast.makeText(context, "Ubicación no disponible", Toast.LENGTH_SHORT).show()
            }
        }
        // -----------------------------------------------------
    }

    override fun getItemCount(): Int = lista.size
}