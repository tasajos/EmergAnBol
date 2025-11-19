package chakuy.com.vbo

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide // Importante

class BomberoAdapter(private var listaBomberos: ArrayList<BomberoUnit>) :
    RecyclerView.Adapter<BomberoAdapter.BomberoViewHolder>() {

    // --- NUEVA FUNCIÓN PARA EL BUSCADOR ---
    fun actualizarLista(nuevaLista: ArrayList<BomberoUnit>) {
        this.listaBomberos = nuevaLista
        notifyDataSetChanged() // Avisa a la vista que los datos cambiaron
    }
    // --------------------------------------

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
        holder.tvCiudad.text = item.ciudad ?: ""

        if (!item.imagen.isNullOrEmpty()) {
            Glide.with(holder.itemView.context)
                .load(item.imagen)
                .circleCrop()
                .placeholder(R.drawable.ic_launcher_background)
                .into(holder.imgLogo)
        }

        // Aquí iría tu listener para abrir detalle si lo necesitas
    }

    override fun getItemCount(): Int = listaBomberos.size
}