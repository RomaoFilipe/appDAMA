package pt.ipt.dama2024.absan

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.util.Log

class DBHelper(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {
        private const val DATABASE_VERSION = 2
        private const val DATABASE_NAME = "UserDB.db"
        private const val TABLE_NAME = "Users"
        private const val COLUMN_ID = "id"
        private const val COLUMN_FULLNAME = "fullName"
        private const val COLUMN_EMAIL = "email"
        private const val COLUMN_PHONE = "phone"
        private const val COLUMN_USERNAME = "username"
        private const val COLUMN_PASSWORD = "password"
        private const val COLUMN_PROFILE_IMAGE_PATH = "profile_image_path"
    }

    override fun onCreate(db: SQLiteDatabase?) {
        val createTable = ("CREATE TABLE $TABLE_NAME (" +
                "$COLUMN_ID INTEGER PRIMARY KEY AUTOINCREMENT," +
                "$COLUMN_FULLNAME TEXT," +
                "$COLUMN_EMAIL TEXT," +
                "$COLUMN_PHONE TEXT," +
                "$COLUMN_USERNAME TEXT," +
                "$COLUMN_PASSWORD TEXT," +
                "$COLUMN_PROFILE_IMAGE_PATH TEXT)")
        db?.execSQL(createTable)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        if (oldVersion < 2) {
            db?.execSQL("ALTER TABLE $TABLE_NAME ADD COLUMN $COLUMN_PROFILE_IMAGE_PATH TEXT")
        }
    }

    fun addUser(fullName: String, email: String, phone: String, username: String, password: String): Boolean {
        Log.d("DBHelper", "Attempting to add user: $username")
        Log.d("DBHelper", "Full name: $fullName, Email: $email, Phone: $phone, Username: $username, Password: $password")

        if (!checkUserExists(username)) {
            val db = this.writableDatabase
            val values = ContentValues().apply {
                put(COLUMN_FULLNAME, fullName)
                put(COLUMN_EMAIL, email)
                put(COLUMN_PHONE, phone)
                put(COLUMN_USERNAME, username)
                put(COLUMN_PASSWORD, password)
                put(COLUMN_PROFILE_IMAGE_PATH, "")
            }

            val result = db.insert(TABLE_NAME, null, values)
            db.close()

            Log.d("DBHelper", "Insert result: $result")

            return result != -1L
        } else {
            Log.d("DBHelper", "User already exists: $username")
        }
        return false
    }

    fun checkUserExists(username: String): Boolean {
        val db = this.readableDatabase
        val query = "SELECT * FROM $TABLE_NAME WHERE $COLUMN_USERNAME = ?"
        val cursor = db.rawQuery(query, arrayOf(username))
        val exists = cursor.count > 0
        cursor.close()
        db.close()
        return exists
    }

    fun isValidLogin(username: String, password: String): Boolean {
        val db = this.readableDatabase
        val query = "SELECT * FROM $TABLE_NAME WHERE $COLUMN_USERNAME = ? AND $COLUMN_PASSWORD = ?"
        val cursor = db.rawQuery(query, arrayOf(username, password))
        val isValid = cursor.count > 0
        cursor.close()
        db.close()
        return isValid
    }

    fun getUserByUsername(username: String): User? {
        val db = this.readableDatabase
        val query = "SELECT * FROM $TABLE_NAME WHERE $COLUMN_USERNAME = ?"
        val cursor = db.rawQuery(query, arrayOf(username))
        var user: User? = null

        if (cursor.moveToFirst()) {
            val id = cursor.getInt(cursor.getColumnIndex(COLUMN_ID))
            val fullName = cursor.getString(cursor.getColumnIndex(COLUMN_FULLNAME))
            val email = cursor.getString(cursor.getColumnIndex(COLUMN_EMAIL))
            val phone = cursor.getString(cursor.getColumnIndex(COLUMN_PHONE))
            val password = cursor.getString(cursor.getColumnIndex(COLUMN_PASSWORD))
            val profileImagePath = cursor.getString(cursor.getColumnIndex(COLUMN_PROFILE_IMAGE_PATH))

            user = User(id, fullName, email, phone, username, password, profileImagePath)
        }

        cursor.close()
        db.close()
        return user
    }

    fun updateUserProfileImagePath(username: String, imagePath: String) {
        val db = this.writableDatabase
        val contentValues = ContentValues().apply {
            put(COLUMN_PROFILE_IMAGE_PATH, imagePath)
        }
        db.update(TABLE_NAME, contentValues, "$COLUMN_USERNAME = ?", arrayOf(username))
        db.close()
    }
}
