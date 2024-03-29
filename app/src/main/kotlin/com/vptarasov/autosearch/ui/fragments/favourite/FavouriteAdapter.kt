package com.vptarasov.autosearch.ui.fragments.favourite

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import com.vptarasov.autosearch.R
import com.vptarasov.autosearch.model.Car
import com.vptarasov.autosearch.ui.fragments.favourite.FavouriteAdapter.ViewHolderItem
import kotlinx.android.synthetic.main.item_car.view.*
import java.util.*


class FavouriteAdapter(val cars: ArrayList<Car>) : RecyclerView.Adapter<ViewHolderItem>(),
    View.OnClickListener {
    private var listener: FavouriteContract.View? = null

    override fun getItemCount(): Int {
        return cars.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolderItem {
        val contactView = LayoutInflater.from(parent.context).inflate(R.layout.item_car, parent, false)
        return ViewHolderItem(contactView)
    }

    override fun onBindViewHolder(holder: ViewHolderItem, position: Int) {
        val car = cars[position]
        holder.nameCar?.text = car.name
        holder.priceCar?.text = car.price
        holder.yearCar?.text = car.year
        holder.engineCar?.text = car.engine
        holder.mileageCar?.text = car.mileage
        holder.colorCar?.text = car.color
        holder.gearboxCar?.text = car.gearbox
        holder.driveCar?.text = car.drive
        holder.bodyCar?.text = car.body
        holder.cityCar?.text = car.city
        holder.dateCar?.text = car.date

        Picasso.get()
            .load(car.photo)
            .placeholder(R.drawable.placeholder)
            .into(holder.photoCar)
        holder.favourite?.setBackgroundResource(R.drawable.favouritechecked )
        if (listener != null) {
            holder.itemView.tag = car
            holder.itemView.setOnClickListener(this@FavouriteAdapter)

            holder.favourite?.tag = car
            holder.favourite?.setOnClickListener(this@FavouriteAdapter)
        }
    }

    fun setListener(listener: FavouriteContract.View) {
        this.listener = listener
    }

    override fun onClick(view: View) {
        val car = view.tag as Car
        if (view.id == R.id.favourite) {
            listener?.onFavoriteClick(car)
            if (cars.size == 0) {
                listener?.onLastFavoriteRemoved()
            }
        } else {
            listener?.onItemClick(car)
        }
    }

    inner class ViewHolderItem(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var nameCar: TextView? = itemView.nameCar
        var priceCar: TextView? = itemView.priceCar
        var yearCar: TextView? = itemView.yearCar
        var engineCar: TextView? = itemView.engineCar
        var mileageCar: TextView? = itemView.mileageCar
        var colorCar: TextView? = itemView.colorCar
        var gearboxCar: TextView? = itemView.gearboxCar
        var driveCar: TextView? = itemView.driveCar
        var bodyCar: TextView? = itemView.bodyCar
        var cityCar: TextView? = itemView.cityCar
        var dateCar: TextView? = itemView.dateCar
        var photoCar: ImageView? = itemView.photoCar
        var favourite: ImageButton? = itemView.favourite

    }
}
