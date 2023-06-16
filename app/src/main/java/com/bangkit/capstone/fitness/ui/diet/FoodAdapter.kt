package com.bangkit.capstone.fitness.ui.diet

import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bangkit.capstone.fitness.databinding.ItemFoodBinding
import com.bangkit.capstone.fitness.network.apiresponse.Recipe

class FoodAdapter : RecyclerView.Adapter<FoodAdapter.FoodViewHolder>() {
    private val recipes: MutableList<Recipe> = mutableListOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FoodViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemFoodBinding.inflate(inflater, parent, false)
        return FoodViewHolder(binding)
    }

    override fun onBindViewHolder(holder: FoodViewHolder, position: Int) {
        val recipe = recipes[position]
        holder.bind(recipe)
    }
    class FoodViewHolder(private val binding: ItemFoodBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(recipe: Recipe) {
            binding.foodName.text = recipe.recipeName
            binding.cuisineType.text = recipe.cuisineType
            binding.dietType.text = recipe.dietType
            binding.nutritiousFact.text = "Protein(${recipe.protein} g), Carbs(${recipe.carbs} g), Fat(${recipe.fat} g), Calories(${recipe.calories} kcal)"

            val key = recipe.recipeName
            binding.searchButton.setOnClickListener {
                val searchQuery = "https://www.google.com/search?q=$key"
                val intent = Intent(Intent.ACTION_VIEW, Uri.parse(searchQuery))
                itemView.context.startActivity(intent)
            }
        }
    }

    override fun getItemCount(): Int {
        return recipes.size
    }

    fun setData(recipes: List<Recipe>) {
        this.recipes.clear()
        this.recipes.addAll(recipes)
        notifyDataSetChanged()
    }
}