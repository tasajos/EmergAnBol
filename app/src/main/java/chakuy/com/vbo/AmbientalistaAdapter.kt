package chakuy.com.vbo

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide

class AmbientalistasAdapter(private var lista: ArrayList<AmbientalistasUnit>) :
    RecyclerView.Adapter<AmbientalistasAdapter.ViewHolder>() {

    fun actualizarLista(nuevaLista: ArrayList<AmbientalistasUnit>) {
        this.lista = nuevaLista
        notifyDataSetChanged()
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val imgLogo: ImageView = itemView.findViewById(R.id.imgLogo)
        val tvNombre: TextView = itemView.findViewById(R.id.tvNombre)
        val tvCiudad: TextView = itemView.findViewById(R.id.tvCiudad)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        // Reutilizamos el diseño de item_bombero_unit
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_bombero_unit, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = lista[position]
        holder.tvNombre.text = item.nombre ?: "Sin nombre"
        holder.tvCiudad.text = item.ciudad ?: ""

        if (!item.imagen.isNullOrEmpty()) {
            Glide.with(holder.itemView.context)
                .load(item.imagen)
                .circleCrop()
                .placeholder(R.drawable.ic_launcher_background)
                .into(holder.imgLogo)
        }

        // Abrir Detalle
        holder.itemView.setOnClickListener {
            val intent = Intent(holder.itemView.context, AmbientalistasDetailActivity::class.java)
            intent.putExtra("UNIT_DATA", item)
            holder.itemView.context.startActivity(intent)
        }
    }

    override fun getItemCount(): Int = lista.size
}