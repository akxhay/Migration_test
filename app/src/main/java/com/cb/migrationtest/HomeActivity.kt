package com.cb.migrationtest

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.cb.migrationtest.databinding.ActivityHomeBinding

class HomeActivity : AppCompatActivity() {
    private val TAG = "HomeActivity"

    private lateinit var userDatabase: UserDatabase
    private lateinit var users: ArrayList<UserEntity>
    private lateinit var adapter: UserListAdapter

    private lateinit var binding: ActivityHomeBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.recyclerView.setHasFixedSize(true)
        val mLayoutManager: RecyclerView.LayoutManager = GridLayoutManager(this, 1)
        binding.recyclerView.layoutManager = mLayoutManager
        binding.recyclerView.itemAnimator = DefaultItemAnimator()
        userDatabase = UserDatabase(this)

        fetchUser()
        binding.save.setOnClickListener {
            val firstName = binding.firstName.text.toString()
            val lastName = binding.lastName.text.toString()
            val user = UserEntity(firstName, lastName)
            saveUser(user)
            fetchUser()
        }
    }


    private fun saveUser(userEntity: UserEntity) {
        val id = userDatabase.insertUser(userEntity)
        Log.d(TAG, "saveUser: id $id")
        if (id < 0) {
            Toast.makeText(this, "User already exists", Toast.LENGTH_SHORT).show()
        }
    }

    private fun deleteUser(userEntity: UserEntity) {
        userDatabase.deleteUser(userEntity.id)
        Log.d(TAG, "deleteUser: id $userEntity.id")
    }

    private fun fetchUser() {
        users = userDatabase.getUsers()
        Log.d(TAG, "fetchUser: users ${users.size}")
        adapter = UserListAdapter(users, { onClickListener(it) }, { onDeleteClickListener(it) })
        binding.recyclerView.adapter = adapter
    }

    private fun onClickListener(userEntity: UserEntity) {
        Toast.makeText(this, "id :${userEntity.id}", Toast.LENGTH_SHORT).show()
    }

    private fun onDeleteClickListener(userEntity: UserEntity) {
        deleteUser(userEntity)
        fetchUser()
    }
}