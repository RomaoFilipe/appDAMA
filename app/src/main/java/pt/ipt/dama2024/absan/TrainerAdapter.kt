package pt.ipt.dama2024.absan

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

/* Data class para representar um treinador */
data class Trainer(val name: String, val imageResId: Int)

/* Adapter para o RecyclerView de treinadores */
class TrainerAdapter(private val trainers: List<Trainer>, private val onClick: (Trainer) -> Unit) :
    RecyclerView.Adapter<TrainerAdapter.TrainerViewHolder>() {

    /* Variável para armazenar a posição selecionada */
    private var selectedPosition = RecyclerView.NO_POSITION

    /* ViewHolder para os itens do RecyclerView */
    class TrainerViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val imageViewTrainer: ImageView = view.findViewById(R.id.imageViewTrainer)
        val textViewTrainerName: TextView = view.findViewById(R.id.textViewTrainerName)
        val linearLayoutTrainerItem: View = view.findViewById(R.id.linearLayoutTrainerItem)
    }

    /* Método para criar um novo ViewHolder */
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TrainerViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_trainer, parent, false)
        return TrainerViewHolder(view)
    }

    /* Método para vincular dados ao ViewHolder */
    override fun onBindViewHolder(holder: TrainerViewHolder, position: Int) {
        val trainer = trainers[position]
        holder.imageViewTrainer.setImageResource(trainer.imageResId)
        holder.textViewTrainerName.text = trainer.name

        holder.linearLayoutTrainerItem.setBackgroundResource(
            if (selectedPosition == position) R.color.colorSelected else android.R.color.white
        )

        holder.itemView.setOnClickListener {
            val previousPosition = selectedPosition
            selectedPosition = holder.adapterPosition
            notifyItemChanged(previousPosition)
            notifyItemChanged(selectedPosition)
            onClick(trainer)
        }
    }

    /* Método para retornar o número de itens no RecyclerView */
    override fun getItemCount() = trainers.size
}