package chakuy.com.vbo

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide

class AmbulanciaAdapter(private var listaAmbulancias: ArrayList<AmbulanciaUnit>) :
    RecyclerView.Adapter<AmbulanciaAdapter.AmbulanciaViewHolder>() {

    fun actualizarLista(nuevaLista: ArrayList<AmbulanciaUnit>) {
        this.listaAmbulancias = nuevaLista
        notifyDataSetChanged()
    }

    class AmbulanciaViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val imgLogo: ImageView = itemView.findViewById(R.id.imgLogo)
        val tvNombre: TextView = itemView.findViewById(R.id.tvNombre)
        val tvCiudad: TextView = itemView.findViewById(R.id.tvCiudad)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AmbulanciaViewHolder {
        // Asegúrate de usar el layout correcto aquí
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_bombero_unit, parent, false) // Reutilizamos el layout visual
        return AmbulanciaViewHolder(view)
    }

    override fun onBindViewHolder(holder: AmbulanciaViewHolder, position: Int) {
        val item = listaAmbulancias[position]

        holder.tvNombre.text = item.nombre ?: "Sin nombre"
        holder.tvCiudad.text = item.ciudad ?: ""

        if (!item.imagen.isNullOrEmpty()) {
            Glide.with(holder.itemView.context)
                .load(item.imagen)
                .circleCrop()
                .placeholder(R.drawable.ic_launcher_background)
                .into(holder.imgLogo)
        }

        // CLICK PARA ABRIR DETALLE
        holder.itemView.setOnClickListener {
            val intent = Intent(holder.itemView.context, AmbulanciaDetailActivity::class.java)
            intent.putExtra("UNIT_DATA", item)
            holder.itemView.context.startActivity(intent)
        }
    }

    override fun getItemCount(): Int = listaAmbulancias.size
}