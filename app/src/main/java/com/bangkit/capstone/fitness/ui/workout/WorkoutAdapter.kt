package com.bangkit.capstone.fitness.ui.workout

import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bangkit.capstone.fitness.R
import com.bangkit.capstone.fitness.databinding.ItemWorkoutBinding
import com.bangkit.capstone.fitness.network.model.Workout
import com.bangkit.capstone.fitness.ui.utils.SessionManager
import com.bangkit.capstone.fitness.ui.utils.bodyparts

class WorkoutAdapter(private val sessionManager: SessionManager) : RecyclerView.Adapter<WorkoutAdapter.WorkoutViewHolder>() {

    private val workouts: MutableList<Workout> = mutableListOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WorkoutViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemWorkoutBinding.inflate(inflater, parent, false)
        return WorkoutViewHolder(binding,sessionManager)
    }

    override fun onBindViewHolder(holder: WorkoutViewHolder, position: Int) {
        val workout = workouts[position]
        holder.bind(workout)
    }

    override fun getItemCount(): Int {
        return workouts.size
    }

    fun setWorkouts(newWorkouts: List<Workout>) {
        workouts.clear()
        workouts.addAll(newWorkouts)
        notifyDataSetChanged()
    }

    class WorkoutViewHolder(private val binding: ItemWorkoutBinding, private val sessionManager: SessionManager) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(workout: Workout) {
            binding.workoutName.text = workout.name
            val workoutName = workout.name

            val bodyPart = sessionManager.getUser()?.workout_preferences?.bodypart.toString().toInt()
            val bodyPartName = bodyparts[bodyPart - 1]

            val drawableResource = getDrawableResourceForWorkout(bodyPartName)
            if (drawableResource != 0) {
                binding.workoutImage.setImageResource(drawableResource)
            } else {
                // Fallback drawable resource in case the workout name doesn't match any specific drawable
                binding.workoutImage.setImageResource(R.drawable.ic_workout)
            }

            binding.searchButton.setOnClickListener {
                val searchQuery = "https://www.google.com/search?q=$workoutName"
                val intent = Intent(Intent.ACTION_VIEW, Uri.parse(searchQuery))
                itemView.context.startActivity(intent)
            }
        }

        private fun getDrawableResourceForWorkout(bodyPart: String): Int {
            // Mapping or convention to determine the drawable resource based on the workout name
            return when (bodyPart) {
                "Abdominals" -> R.drawable.bp_abdominals
                "Abductors" -> R.drawable.bp_abductors
                "Biceps" -> R.drawable.bp_biceps
                "Calves" -> R.drawable.bp_calves
                "Chest" -> R.drawable.bp_chest
                "Forearms" -> R.drawable.bp_forearms
                "Glutes" -> R.drawable.bp_glutes
                "Hamstrings" -> R.drawable.bp_hamstrings
                "Lats" -> R.drawable.bp_lats
                "Lower Back" -> R.drawable.bp_lower_back
                "Middle Back" -> R.drawable.bp_middle_back
                "Neck" -> R.drawable.bp_neck
                "Quadriceps" -> R.drawable.bp_quadriceps
                "Shoulders" -> R.drawable.bp_shoulders
                "Traps" -> R.drawable.bp_traps
                "Triceps" -> R.drawable.bp_triceps
                else -> 0
            }
        }
    }
}