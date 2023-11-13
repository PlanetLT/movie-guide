import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.google.gson.Gson
import com.planet.movieguide.data.model.Movie
import com.planet.movieguide.data.model.Result

class DBHelper(context: Context, factory: SQLiteDatabase.CursorFactory?) :
    SQLiteOpenHelper(context, DATABASE_NAME, factory, DATABASE_VERSION) {

    override fun onCreate(db: SQLiteDatabase) {
        val query = ("CREATE TABLE " + UPCOMING + " ("
                + ID + " INTEGER PRIMARY KEY, " +
                RESULT + " TEXT" + ")")

        val queryTwo = ("CREATE TABLE " + POPULAR + " ("
                + ID + " INTEGER PRIMARY KEY, " +
                RESULT + " TEXT" + ")")

        val queryThree = ("CREATE TABLE " + FAVOURITE + " ("
                + ID + " INTEGER PRIMARY KEY, " +
                RESULT + " TEXT" + ")")

        db.execSQL(query)
        db.execSQL(queryTwo)
        db.execSQL(queryThree)
    }

    override fun onUpgrade(db: SQLiteDatabase, p1: Int, p2: Int) {
        db.execSQL("DROP TABLE IF EXISTS " + UPCOMING)
        db.execSQL("DROP TABLE IF EXISTS " + POPULAR)
        db.execSQL("DROP TABLE IF EXISTS " + FAVOURITE)
        onCreate(db)
    }

    fun clearDataIfOnline(){
        val db = this.writableDatabase
        db.execSQL("delete from "+ UPCOMING)
        db.execSQL("delete from "+ POPULAR)

    }


    fun addUpComing( result: Result) {
        val values = ContentValues()
        values.put(ID, result.page)
        values.put(RESULT, Gson().toJson(result))
        val db = this.writableDatabase

        db.insert(UPCOMING, null, values)
        db.close()
    }

    fun addPopular( result: Result) {
        val values = ContentValues()
        values.put(ID, result.page)
        values.put(RESULT, Gson().toJson(result))
        val db = this.writableDatabase
        db.insert(POPULAR, null, values)
        db.close()
    }

    fun addFavourite( result: Movie) {
        val values = ContentValues()
        values.put(ID, result.id)
        values.put(RESULT, Gson().toJson(result))
        val db = this.writableDatabase
        db.insert(FAVOURITE, null, values)
        db.close()
    }

    fun deleteFromFavourite(id: Int): Boolean {
        val db = this.writableDatabase
        return db.delete(FAVOURITE, ID + "=" + id, null) > 0
    }


    fun getAllPopularList(): ArrayList<Result> {
        var allPopularList: ArrayList<Result> = arrayListOf()

        val db = this.readableDatabase
        var cursor = db.rawQuery("SELECT * FROM " + POPULAR, null)
        if (cursor!!.moveToFirst()) {
            do {
                val resultString: String = cursor!!.getString(1)
                val result = gson.fromJson(resultString, Result::class.java)
                allPopularList.add(result)
            } while (cursor!!.moveToNext())
        }
        cursor!!.close()
        return allPopularList
    }

    fun getFavouriteList(): ArrayList<Movie> {
        var allPopularList: ArrayList<Movie> = arrayListOf()

        val db = this.readableDatabase
        var cursor = db.rawQuery("SELECT * FROM " + FAVOURITE, null)
        if (cursor!!.moveToFirst()) {
            do {
                val resultString: String = cursor!!.getString(1)
                val result = gson.fromJson(resultString, Movie::class.java)
                allPopularList.add(result)
            } while (cursor!!.moveToNext())
        }
        cursor!!.close()
        return allPopularList
    }


    fun getAllUpComingList(): ArrayList<Result> {
        var allUpComingList: ArrayList<Result> = arrayListOf()

        val db = this.readableDatabase
        var cursor = db.rawQuery("SELECT * FROM " + UPCOMING, null)
        if (cursor!!.moveToFirst()) {
            do {
                val type = cursor!!.getInt(1)
                if (type == 2) {
                    val resultString: String = cursor!!.getString(1)
                    val result = gson.fromJson(resultString, Result::class.java)
                    allUpComingList.add(result)
                }
            } while (cursor!!.moveToNext())
        }
        cursor!!.close()
        return allUpComingList
    }

    companion object {
        private val DATABASE_NAME = "Movie_database"
        private val DATABASE_VERSION = 1
        val UPCOMING = "upcoming_list"
        val POPULAR = "popular_list"
        val FAVOURITE = "favourite_list"
        val ID = "id"
        val RESULT = "result"
        val gson = Gson()
    }
}
