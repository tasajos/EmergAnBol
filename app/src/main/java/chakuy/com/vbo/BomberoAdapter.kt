package chakuy.com.vbo

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide // Importante

class BomberoAdapter(private val listaBomberos: List<BomberoUnit>) :
    RecyclerView.Adapter<BomberoAdapter.BomberoViewHolder>() {

    class BomberoViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val imgLogo: ImageView = itemView.findViewById(R.id.imgLogo)
        val tvNombre: TextView = itemView.findViewById(R.id.tvNombre)
        val tvCiudad: TextView = itemView.findViewById(R.id.tvCiudad)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BomberoViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_bombero_unit, parent, false)
        return BomberoViewHolder(view)
    }

    override fun onBindViewHolder(holder: BomberoViewHolder, position: Int) {
        val item = listaBomberos[position]

        holder.tvNombre.text = item.nombre ?: "Sin nombre"
        holder.tvCiudad.text = item.ciudad ?: "Bolivia"

        // Usar GLIDE para cargar la imagen desde la URL de Firebase
        if (!item.imagen.isNullOrEmpty()) {
            Glide.with(holder.itemView.context)
                .load(item.imagen)
                .circleCrop() // Para que se vea redonda
                .placeholder(R.drawable.ic_launcher_background) // Imagen mientras carga
                .error(android.R.drawable.ic_menu_report_image) // Imagen si falla
                .into(holder.imgLogo)
        }

        // Aquí puedes agregar el clickListener para ir al detalle más adelante
        holder.itemView.setOnClickListener {
            // Lógica para abrir detalles
        }
    }

    override fun getItemCount(): Int = listaBomberos.size
}