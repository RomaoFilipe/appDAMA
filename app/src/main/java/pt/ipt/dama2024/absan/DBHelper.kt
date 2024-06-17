package pt.ipt.dama2024.absan

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.util.Log

// DBHelper é uma classe que ajuda a gerir um base de dados SQLite
class DBHelper(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {
        // Constantes para o nome do base de dados e versão
        private const val DATABASE_NAME = "users.db"
        private const val DATABASE_VERSION = 2

        // Constantes para o nome da tabela e colunas
        private const val TABLE_USERS = "Users"
        private const val COLUMN_ID = "id"
        private const val COLUMN_FULL_NAME = "fullName"
        private const val COLUMN_EMAIL = "email"
        private const val COLUMN_PHONE = "phone"
        private const val COLUMN_USERNAME = "username"
        private const val COLUMN_PASSWORD = "password"
        private const val COLUMN_PROFILE_IMAGE_PATH = "profile_image_path"
    }

    // Método chamado na criação do base de dados
    override fun onCreate(db: SQLiteDatabase?) {
        // Comando SQL para criar a tabela Users
        val createTable = ("CREATE TABLE $TABLE_USERS ("
                + "$COLUMN_ID INTEGER PRIMARY KEY AUTOINCREMENT,"
                + "$COLUMN_FULL_NAME TEXT,"
                + "$COLUMN_EMAIL TEXT,"
                + "$COLUMN_PHONE TEXT,"
                + "$COLUMN_USERNAME TEXT,"
                + "$COLUMN_PASSWORD TEXT,"
                + "$COLUMN_PROFILE_IMAGE_PATH TEXT)")
        db?.execSQL(createTable)
    }

    // Método chamado quando o base de dados precisa ser atualizado
    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        // Atualiza a tabela se a versão antiga for menor que a nova versão
        if (oldVersion < 2) {
            db?.execSQL("ALTER TABLE $TABLE_USERS ADD COLUMN $COLUMN_EMAIL TEXT")
            db?.execSQL("ALTER TABLE $TABLE_USERS ADD COLUMN $COLUMN_PHONE TEXT")
            db?.execSQL("ALTER TABLE $TABLE_USERS ADD COLUMN $COLUMN_FULL_NAME TEXT")
            db?.execSQL("ALTER TABLE $TABLE_USERS ADD COLUMN $COLUMN_PROFILE_IMAGE_PATH TEXT")
        }
    }

    // Método para adicionar um utilizador ao base de dados
    fun addUser(fullName: String, email: String, phone: String, username: String, password: String): Boolean {
        val db = this.writableDatabase
        val contentValues = ContentValues()
        contentValues.put(COLUMN_FULL_NAME, fullName)
        contentValues.put(COLUMN_EMAIL, email)
        contentValues.put(COLUMN_PHONE, phone)
        contentValues.put(COLUMN_USERNAME, username)
        contentValues.put(COLUMN_PASSWORD, password)
        contentValues.put(COLUMN_PROFILE_IMAGE_PATH, "")

        val result = db.insert(TABLE_USERS, null, contentValues)
        db.close()
        return result != (-1).toLong()
    }

    // Método para verificar se um utilizador existe no base de dados
    fun checkUserExists(username: String): Boolean {
        val db = this.readableDatabase
        val query = "SELECT * FROM $TABLE_USERS WHERE $COLUMN_USERNAME = ?"
        val cursor = db.rawQuery(query, arrayOf(username))
        val exists = cursor.count > 0
        cursor.close()
        db.close()
        return exists
    }

    // Método para obter o email de um utilizador pelo nome de utilizador
    fun getUserEmailByUsername(username: String): String? {
        val db = this.readableDatabase
        val query = "SELECT $COLUMN_EMAIL FROM $TABLE_USERS WHERE $COLUMN_USERNAME = ?"
        val cursor = db.rawQuery(query, arrayOf(username))
        var email: String? = null
        if (cursor.moveToFirst()) {
            email = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_EMAIL))
        }
        cursor.close()
        db.close()
        return email
    }

    // Método para obter um utilizador pelo nome de utilizador
    fun getUserByUsername(username: String): User? {
        val db = this.readableDatabase
        val query = "SELECT * FROM $TABLE_USERS WHERE $COLUMN_USERNAME = ?"
        val cursor = db.rawQuery(query, arrayOf(username))
        var user: User? = null
        if (cursor.moveToFirst()) {
            val id = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_ID))
            val fullName = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_FULL_NAME))
            val email = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_EMAIL))
            val phone = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_PHONE))
            val profileImagePath = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_PROFILE_IMAGE_PATH))
            user = User(id, fullName, email, phone, username, profileImagePath)
        }
        cursor.close()
        db.close()
        return user
    }

    // Método para atualizar o caminho da imagem de perfil de um utilizador
    fun updateUserProfileImage(username: String, profileImagePath: String): Boolean {
        val db = this.writableDatabase
        val contentValues = ContentValues()
        contentValues.put(COLUMN_PROFILE_IMAGE_PATH, profileImagePath)

        val result = db.update(TABLE_USERS, contentValues, "$COLUMN_USERNAME = ?", arrayOf(username))
        db.close()
        return result > 0
    }
}
