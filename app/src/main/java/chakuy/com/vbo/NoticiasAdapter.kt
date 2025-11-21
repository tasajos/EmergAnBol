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
import com.bumptech.glide.Glide

class NoticiasAdapter(private var listaNoticias: ArrayList<Noticia>) :
    RecyclerView.Adapter<NoticiasAdapter.ViewHolder>() {

    fun actualizarLista(nuevaLista: ArrayList<Noticia>) {
        this.listaNoticias = nuevaLista
        notifyDataSetChanged()
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val imgNoticia: ImageView = itemView.findViewById(R.id.imgNoticia)
        val tvTitulo: TextView = itemView.findViewById(R.id.tvNoticiaTitulo)
        val tvContenido: TextView = itemView.findViewById(R.id.tvNoticiaContenido)
        val btnFuente: TextView = itemView.findViewById(R.id.btnAbrirFuente)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_noticia_card, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = listaNoticias[position]
        val context = holder.itemView.context

        holder.tvTitulo.text = item.titulo ?: "Título no disponible"
        holder.tvContenido.text = item.contenido ?: "Contenido no disponible."

        // Cargar Imagen con Glide
        if (!item.imagen.isNullOrEmpty()) {
            Glide.with(context)
                .load(item.imagen)
                .placeholder(android.R.drawable.ic_menu_gallery)
                .error(android.R.drawable.ic_menu_report_image)
                .into(holder.imgNoticia)
        } else {
            holder.imgNoticia.setImageResource(android.R.drawable.ic_menu_help)
        }

        // Clic para Abrir Enlace
        holder.btnFuente.setOnClickListener {
            if (!item.fuente.isNullOrEmpty()) {
                val intent = Intent(Intent.ACTION_VIEW, Uri.parse(item.fuente))
                context.startActivity(intent)
            } else {
                Toast.makeText(context, "Fuente no disponible.", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun getItemCount(): Int = listaNoticias.size
}