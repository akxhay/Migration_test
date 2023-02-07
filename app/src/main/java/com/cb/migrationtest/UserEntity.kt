package com.cb.migrationtest

import java.io.Serializable

class UserEntity() : Serializable {
    var id = 0
    var firstName: String? = null
    var lastName: String? = null

    constructor(firstName: String?, lastName: String?) : this() {
        this.firstName = firstName
        this.lastName = lastName
    }

    override fun toString(): String {
        return "UserEntity(id=$id, firstName=$firstName, lastName=$lastName)"
    }
}