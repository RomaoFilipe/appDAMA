package pt.ipt.dama2024.absan

data class User(
    val id: Int,
    val fullName: String,
    val email: String,
    val phone: String,
    val username: String,
    val password: String,
    val profileImagePath: String? = null // Nova propriedade
)